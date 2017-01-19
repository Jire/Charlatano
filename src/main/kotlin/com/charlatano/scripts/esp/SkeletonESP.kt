package com.charlatano.scripts.esp

import com.badlogic.gdx.graphics.Color
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.entities
import com.charlatano.game.entity.*
import com.charlatano.game.me
import com.charlatano.game.offsets.EngineOffsets.studioModel
import com.charlatano.game.worldToScreen
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.utils.Vector
import com.charlatano.utils.collections.CacheableList
import com.charlatano.utils.extensions.uint
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap

private val bones = Array(2048) { Line() }
private val entityBones = Long2ObjectArrayMap<CacheableList<Pair<Int, Int>>>()
private var currentIdx = 0

fun skeletonEsp() {
	CharlatanoOverlay {
		entities(EntityType.CCSPlayer) {
			val entity = it.entity
			if (entity <= 0 || entity == me || entity.dead() || entity.dormant()) return@entities
			
			val list = entityBones.get(entity) ?: CacheableList<Pair<Int, Int>>(20)
			
			if (list.isEmpty()) {
				var offset = 0
				
				val studioModel = findStudioModel(entity.model())
				val numbones = csgoEXE.int(studioModel + 0x9C)
				val boneIndex = csgoEXE.int(studioModel + 0xA0)
				
				for (idx in 0..numbones - 1) {
					val parent = csgoEXE.int(studioModel + boneIndex + 0x4 + offset)
					val flags = csgoEXE.int(studioModel + boneIndex + 0xA0 + offset) and 0x100
					
					if (flags != 0 && parent != -1) {
						list.add(Pair(parent, idx))
					}
					
					offset += 216
				}
				entityBones.put(entity, list)
			}
			list.forEach { drawBone(entity, it.first, it.second) }
		}
		
		val sr = shapeRenderer.get()
		sr.begin()
		for (i in 0..currentIdx - 1) {
			val bone = bones[i]
			sr.color = bone.color
			sr.line(bone.sX.toFloat(), bone.sY.toFloat(), bone.eX.toFloat(), bone.eY.toFloat())
		}
		sr.end()
		
		currentIdx = 0
	}
}

private fun findStudioModel(pModel: Long): Long {
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

private fun drawBone(target: Player, start: Int, end: Int) {
	val startBone = startBone.get()
	val endBone = endBone.get()
	
	val boneMatrix = target.boneMatrix()
	startBone.set(
			target.bone(0xC, start, boneMatrix),
			target.bone(0x1C, start, boneMatrix),
			target.bone(0x2C, start, boneMatrix))
	endBone.set(
			target.bone(0xC, end, boneMatrix),
			target.bone(0x1C, end, boneMatrix),
			target.bone(0x2C, end, boneMatrix))
	
	val startDraw = startDraw.get()
	val endDraw = endDraw.get()
	
	if (!worldToScreen(startBone, startDraw) || !worldToScreen(endBone, endDraw)) return
	
	bones[currentIdx].sX = startDraw.x.toInt()
	bones[currentIdx].sY = startDraw.y.toInt()
	bones[currentIdx].eX = endDraw.x.toInt()
	bones[currentIdx].eY = endDraw.y.toInt()
	bones[currentIdx++].color = colors[target.health()]
}

private data class Line(var sX: Int = -1, var sY: Int = -1,
                        var eX: Int = -1, var eY: Int = -1,
                        var color: Color = Color.WHITE)