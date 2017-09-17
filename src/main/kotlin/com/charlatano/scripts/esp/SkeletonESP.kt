/*
 * Charlatano: Free and open-source (FOSS) cheat for CS:GO/CS:CO
 * Copyright (C) 2017 - Thomas G. P. Nappo, Jonathan Beaudoin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.charlatano.scripts.esp

import com.badlogic.gdx.graphics.Color
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.entity.*
import com.charlatano.game.entity.EntityType.Companion.ccsPlayer
import com.charlatano.game.forEntities
import com.charlatano.game.me
import com.charlatano.game.offsets.EngineOffsets.pStudioModel
import com.charlatano.game.worldToScreen
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.settings.ENABLE_ESP
import com.charlatano.settings.SKELETON_ESP
import com.charlatano.utils.Vector
import com.charlatano.utils.collections.CacheableList
import com.charlatano.utils.extensions.uint
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap

private val bones = Array(2048) { Line() }
private val entityBones = Long2ObjectArrayMap<CacheableList<Pair<Int, Int>>>()
private var currentIdx = 0

internal fun skeletonEsp() {
	CharlatanoOverlay {
		if (!SKELETON_ESP || !ENABLE_ESP) return@CharlatanoOverlay
		
		forEntities(ccsPlayer) {
			val entity = it.entity
			if (entity > 0 && entity != me && !entity.dead() && !entity.dormant()) {
				(entityBones.get(entity) ?: CacheableList<Pair<Int, Int>>(20)).apply {
					if (isEmpty()) {
						val entityModel = entity.model()
						val studioModel = findStudioModel(entityModel)
						val numbones = csgoEXE.uint(studioModel + 0x9C).toInt()
						val boneIndex = csgoEXE.uint(studioModel + 0xA0)
						
						var offset = 0
						for (idx in 0..numbones - 1) {
							val parent = csgoEXE.int(studioModel + boneIndex + 0x4 + offset)
							if (parent != -1) {
								val flags = csgoEXE.uint(studioModel + boneIndex + 0xA0 + offset) and 0x100
								if (flags != 0L) add(parent to idx)
							}
							
							offset += 216
						}
						
						entityBones.put(entity, this)
					}
					
					forEach { drawBone(entity, it.first, it.second) }
				}
			}
		}
		
		shapeRenderer.apply {
			begin()
			for (i in 0..currentIdx - 1) {
				val bone = bones[i]
				color = bone.color
				line(bone.sX.toFloat(), bone.sY.toFloat(), bone.eX.toFloat(), bone.eY.toFloat())
			}
			end()
		}
		
		currentIdx = 0
	}
}

private fun findStudioModel(pModel: Long): Long {
	val type = csgoEXE.uint(pModel + 0x0110)
	if (type != 3L) return 0 // Type is not Studiomodel
	
	var handle = csgoEXE.uint(pModel + 0x0138) and 0xFFFF
	if (handle == 0xFFFFL) return 0 // Handle is not valid
	handle = handle shl 4
	
	var studioModel = engineDLL.uint(pStudioModel)
	studioModel = csgoEXE.uint(studioModel + 0x28)
	studioModel = csgoEXE.uint(studioModel + handle + 0xC)
	
	return csgoEXE.uint(studioModel + 0x74)
}

private val colors: Array<Color> = Array(101) {
	val red = 1 - (it / 100f)
	val green = (it / 100f)
	
	Color(red, green, 0f, 1f)
}

private val startBone = Vector()
private val endBone = Vector()

private val startDraw = Vector()
private val endDraw = Vector()

private fun drawBone(target: Player, start: Int, end: Int) {
	val boneMatrix = target.boneMatrix()
	startBone.set(
			target.bone(0xC, start, boneMatrix),
			target.bone(0x1C, start, boneMatrix),
			target.bone(0x2C, start, boneMatrix))
	endBone.set(
			target.bone(0xC, end, boneMatrix),
			target.bone(0x1C, end, boneMatrix),
			target.bone(0x2C, end, boneMatrix))
	
	if (worldToScreen(startBone, startDraw) && worldToScreen(endBone, endDraw)) {
		bones[currentIdx].apply {
			sX = startDraw.x.toInt()
			sY = startDraw.y.toInt()
			eX = endDraw.x.toInt()
			eY = endDraw.y.toInt()
			color = colors[target.health()]
		}
		currentIdx++
	}
}

private data class Line(var sX: Int = -1, var sY: Int = -1,
                        var eX: Int = -1, var eY: Int = -1,
                        var color: Color = Color.WHITE)