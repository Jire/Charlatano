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

package com.charlatano.game

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.offsets.ClientOffsets.dwViewMatrix
import com.charlatano.utils.Vector

private val viewMatrix = Array(4) { DoubleArray(4) }

fun worldToScreen(from: Vector, vOut: Vector) = try {
	val buffer = clientDLL.readPointer(dwViewMatrix, 4 * 4 * 4).ensureReadable()
	var offset = 0
	for (row in 0..3) for (col in 0..3) {
		val value = buffer.getFloat(offset.toLong())
		viewMatrix[row][col] = value.toDouble()
		offset += 4
	}
	
	vOut.x = viewMatrix[0][0] * from.x + viewMatrix[0][1] * from.y + viewMatrix[0][2] * from.z + viewMatrix[0][3]
	vOut.y = viewMatrix[1][0] * from.x + viewMatrix[1][1] * from.y + viewMatrix[1][2] * from.z + viewMatrix[1][3]
	
	val w = viewMatrix[3][0] * from.x + viewMatrix[3][1] * from.y + viewMatrix[3][2] * from.z + viewMatrix[3][3]
	
	if (!w.isNaN() && w >= 0.01F) {
		val invw = 1.0 / w
		vOut.x *= invw
		vOut.y *= invw
		
		val width = gameWidth
		val height = gameHeight
		
		var x = width / 2.0
		var y = height / 2.0
		
		x += 0.5 * vOut.x * width + 0.5
		y -= 0.5 * vOut.y * height + 0.5
		
		vOut.x = x
		vOut.y = y
		
		true
	} else false
} catch (e: Exception) {
	e.printStackTrace()
	false
}