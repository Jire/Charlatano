/*
 * Charlatan is a premium CS:GO cheat ran on the JVM.
 * Copyright (C) 2016 Thomas Nappo, Jonathan Beaudoin
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
import com.charlatano.game.entity.*
import com.charlatano.game.hooks.bomb
import com.charlatano.game.hooks.entity
import com.charlatano.game.hooks.me
import com.charlatano.game.hooks.players
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.utils.Vector
import com.charlatano.utils.every
import com.charlatano.worldToScreen


private val vHead = ThreadLocal.withInitial { Vector() }
private val vFeet = ThreadLocal.withInitial { Vector() }

private val vTop = ThreadLocal.withInitial { Vector(0F, 0F, 0F) }
private val vBot = ThreadLocal.withInitial { Vector(0F, 0F, 0F) }

private val boxes = Array(128) { Box() }

private var currentIdx = 0

fun boxEsp() {
	every(1) {
		for (i in 0..players.size - 1) {//TODO clean this up alot
			val entity = players.entity(i)
			if (entity == me || entity.dead() || entity.dormant()) continue

			val vHead = vHead.get()
			val vFeet = vFeet.get()
			vHead.set(entity.bone(0xC), entity.bone(0x1C), entity.bone(0x2C) + 9)
			vFeet.set(vHead.x, vHead.y, vHead.z - 75)

			val vTop = vTop.get()
			val vBot = vBot.get()
			if (!worldToScreen(vHead, vTop) || !worldToScreen(vFeet, vBot)) continue

			val h = vBot.y - vTop.y
			val w = h / 5F

			val c = if (bomb > -1 && entity == bomb.carrier()) Color.GREEN else if (me.team() == entity.team()) Color.BLUE else Color.RED

			val sx = (vTop.x - w).toInt()
			val sy = vTop.y.toInt()

			boxes[currentIdx].x = sx
			boxes[currentIdx].y = sy
			boxes[currentIdx].w = (w * 2).toInt()
			boxes[currentIdx].h = h.toInt()
			boxes[currentIdx++].color = c
		}
		currentIdx = 0
	}
	CharlatanoOverlay {
		val shapeRenderer = shapeRenderer.get() ?: return@CharlatanoOverlay
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
		boxes.forEach {
			if (it.color != Color.BLACK && it.x > 0 && it.y > 0 && it.w > 0 && it.h > 0) {
				shapeRenderer.color = it.color
				shapeRenderer.rect(it.x.toFloat(), it.y.toFloat(), it.w.toFloat(), it.h.toFloat())
			}
			it.reset()
		}
		shapeRenderer.end()
	}
}

class Box() {
	var x = -1
	var y = -1
	var w = -1
	var h = -1
	var color: Color = Color.BLACK

	fun reset() {
		x = -1
		y = -1
		w = -1
		h = -1
		color = Color.BLACK
	}
}