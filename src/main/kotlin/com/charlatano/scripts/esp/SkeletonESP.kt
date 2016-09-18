/*
 * Charlatano is a premium CS:GO cheat ran on the JVM.
 * Copyright (C) 2016 - Thomas Nappo, Jonathan Beaudoin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.charlatano.scripts.esp

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.entitiesByType
import com.charlatano.game.entity.*
import com.charlatano.game.me
import com.charlatano.game.offsets.EngineOffsets.studioModel
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.utils.Vector
import com.charlatano.utils.uint
import com.charlatano.worldToScreen

const val MAXSTUDIOBONES = 128

private val skeletons = Array(2048) { Line() }

private var currentIdx = 0

fun skeletonEsp() {
	CharlatanoOverlay {
		for (e in entitiesByType(EntityType.CCSPlayer)) {
			val entity = e.entity
			if (entity == me || entity.dead() || entity.dormant()) continue
			
			val studioModel = findStudioModel(entity.model())
			val numbones = csgoEXE.int(studioModel + 0x9C)
			val boneIndex = csgoEXE.int(studioModel + 0xA0)
			
			var offset = 0
			for (idx in 0..numbones - 1) {
				val parent = csgoEXE.int(studioModel + boneIndex + 0x4 + offset)
				val flags = csgoEXE.int(studioModel + boneIndex + 0xA0 + offset) and 0x100
				
				if (flags != 0 && parent != -1) drawBone(entity, parent, idx)
				
				offset += 216
			}
		}
		
		val shapeRenderer = shapeRenderer.get() ?: return@CharlatanoOverlay
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
		for (i in 0..currentIdx - 1) {
			val it = skeletons[i]
			shapeRenderer.color = it.color
			shapeRenderer.line(it.sX.toFloat(), it.sY.toFloat(), it.eX.toFloat(), it.eY.toFloat())
		}
		currentIdx = 0
		shapeRenderer.end()
	}
}

fun findStudioModel(pModel: Long): Long {
	val type = csgoEXE.uint(pModel + 0x0110)
	if (type != 3L) return 0 //Type is not Studiomodel
	
	var handle = csgoEXE.uint(pModel + 0x0138) and 0xFFFF
	if (handle == 0xFFFFL) return 0 //Handle is not valid
	
	handle = handle shl 4
	
	var studioModel = engineDLL.uint(studioModel)
	studioModel = csgoEXE.uint(studioModel + 0x028)
	studioModel = csgoEXE.uint(studioModel + handle + 0x0C)
	
	return csgoEXE.uint(studioModel + 0x0074)
}

private val colors: Array<Color> = Array(101) {
	val red = 1 - (it / 100f)
	val green = (it / 100f)
	
	Color(red, green, 0f, 1f)
}

private val startBone = ThreadLocal.withInitial { Vector() }
private val endBone = ThreadLocal.withInitial { Vector() }

private val startDraw = ThreadLocal.withInitial { Vector() }
private val endDraw = ThreadLocal.withInitial { Vector() }

fun drawBone(target: Player, start: Int, end: Int) {
	val startBone = startBone.get()
	val endBone = endBone.get()
	
	val boneMatrix = target.boneMatrix()
	startBone.set(target.bone(0xC, start, boneMatrix), target.bone(0x1C, start, boneMatrix), target.bone(0x2C, start, boneMatrix))
	endBone.set(target.bone(0xC, end, boneMatrix), target.bone(0x1C, end, boneMatrix), target.bone(0x2C, end, boneMatrix))
	
	val startDraw = startDraw.get()
	val endDraw = endDraw.get()
	
	if (!worldToScreen(startBone, startDraw) || !worldToScreen(endBone, endDraw))
		return
	
	skeletons[currentIdx].sX = startDraw.x.toInt()
	skeletons[currentIdx].sY = startDraw.y.toInt()
	skeletons[currentIdx].eX = endDraw.x.toInt()
	skeletons[currentIdx].eY = endDraw.y.toInt()
	skeletons[currentIdx++].color = colors[target.health()]
}

class Line() {
	var sX = -1
	var sY = -1
	var eX = -1
	var eY = -1
	var color: Color = Color.WHITE
}