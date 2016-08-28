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

package com.charlatano

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.offsets.ClientOffsets
import com.charlatano.utils.Vector
import java.awt.Toolkit

val SCREEN_SIZE = Toolkit.getDefaultToolkit().screenSize!!

fun worldToScreen(from: Vector<Float>, vOut: Vector<Float>): Boolean {
	try {
		val m_vMatrix = Array(4) { FloatArray(4) }
		val buffer = clientDLL.read(ClientOffsets.dwViewMatrix, 4 * 4 * 4)!!.getByteBuffer(0, 4 * 4 * 4)
		for (row in 0..3) {
			for (col in 0..3) {
				val value = buffer.float
				m_vMatrix[row][col] = value
			}
		}
		
		vOut.x = m_vMatrix[0][0] * from.x + m_vMatrix[0][1] * from.y + m_vMatrix[0][2] * from.z + m_vMatrix[0][3]
		vOut.y = m_vMatrix[1][0] * from.x + m_vMatrix[1][1] * from.y + m_vMatrix[1][2] * from.z + m_vMatrix[1][3]
		
		val w = m_vMatrix[3][0] * from.x + m_vMatrix[3][1] * from.y + m_vMatrix[3][2] * from.z + m_vMatrix[3][3]
		
		if (w.isNaN() || w < 0.01f) {
			return false
		}
		
		val invw = 1.0f / w
		vOut.x *= invw
		vOut.y *= invw
		
		val width = SCREEN_SIZE.width
		val height = SCREEN_SIZE.height
		
		var x = (width / 2).toFloat()
		var y = (height / 2).toFloat()
		
		x += (0.5 * vOut.x.toDouble() * width.toDouble() + 0.5).toFloat()
		y -= (0.5 * vOut.y.toDouble() * height.toDouble() + 0.5).toFloat()
		
		vOut.x = x + 0
		vOut.y = y + 0
	} catch (t: Throwable) {
		t.printStackTrace()
		return false
	}
	
	return true
}

const val AIM_SPEED = 5F

fun moveTo(position: Vector<Float>) {
	val ScreenCenterX = SCREEN_SIZE.width / 2F
	val ScreenCenterY = SCREEN_SIZE.height / 2F
	
	val screenPosition = Vector<Float>()
	if (!worldToScreen(position, screenPosition)) {
		println("Can't find screen position")
		return
	}
	
	val x = screenPosition.x
	val y = screenPosition.y
	
	var mouseX = 0f
	var mouseY = 0f
	
	if (x !== 0F) {
		if (x > ScreenCenterX) {
			mouseX = -(ScreenCenterX - x)
			mouseX /= AIM_SPEED
			if (mouseX + ScreenCenterX > ScreenCenterX * 2) mouseX = 0f
		}
		
		if (x < ScreenCenterX) {
			mouseX = x - ScreenCenterX
			mouseX /= AIM_SPEED
			if (mouseX + ScreenCenterX < 0) mouseX = 0f
		}
	}
	
	if (y !== 0F) {
		if (y > ScreenCenterY) {
			mouseY = -(ScreenCenterY - y)
			mouseY /= AIM_SPEED
			if (mouseY + ScreenCenterY > ScreenCenterY * 2) mouseY = 0f
		}
		
		if (y < ScreenCenterY) {
			mouseY = y - ScreenCenterY
			mouseY /= AIM_SPEED
			if (mouseY + ScreenCenterY < 0) mouseY = 0f
		}
	}
	
	User32.mouse_event(User32.MOUSEEVENTF_MOVE, mouseX.toInt(), mouseY.toInt(), null, null)
}