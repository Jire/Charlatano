package com.charlatano.scripts.esp

import com.badlogic.gdx.graphics.Color
import com.charlatano.game.entities
import com.charlatano.game.entity.*
import com.charlatano.game.entityByType
import com.charlatano.game.me
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.utils.Vector
import com.charlatano.game.worldToScreen

private val vHead = Vector()
private val vFeet = Vector()

private val vTop = Vector(0.0, 0.0, 0.0)
private val vBot = Vector(0.0, 0.0, 0.0)

private val boxes = Array(128) { Box() }

private var currentIdx = 0

internal fun boxEsp() = CharlatanoOverlay {
	entities(EntityType.CCSPlayer) {
		val entity = it.entity
		if (entity == me || entity.dead() || entity.dormant()) return@entities
		
		vHead.set(entity.bone(0xC), entity.bone(0x1C), entity.bone(0x2C) + 9)
		vFeet.set(vHead.x, vHead.y, vHead.z - 75)
		
		if (!worldToScreen(vHead, vTop) || !worldToScreen(vFeet, vBot)) return@entities
		
		val h = vBot.y - vTop.y
		val w = h / 5F
		
		val bomb: Entity = entityByType(EntityType.CC4)?.entity ?: -1
		val c = if (bomb > -1 && entity == bomb.carrier()) Color.GREEN
		else if (me.team() == entity.team()) Color.BLUE else Color.RED
		
		val sx = (vTop.x - w).toInt()
		val sy = vTop.y.toInt()
		
		boxes[currentIdx].x = sx
		boxes[currentIdx].y = sy
		boxes[currentIdx].w = (w * 2).toInt()
		boxes[currentIdx].h = h.toInt()
		boxes[currentIdx++].color = c
	}
	
	shapeRenderer.begin()
	for (i in 0..currentIdx - 1) boxes[i].apply {
		shapeRenderer.color = color
		shapeRenderer.rect(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat())
	}
	shapeRenderer.end()
	
	currentIdx = 0
}

private data class Box(var x: Int = -1, var y: Int = -1,
                       var w: Int = -1, var h: Int = -1,
                       var color: Color = Color.WHITE)