package com.charlatano.game

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.offsets.ClientOffsets.dwViewMatrix
import com.charlatano.utils.Vector

private val viewMatrix = Array(4) { DoubleArray(4) }

fun worldToScreen(from: Vector, vOut: Vector): Boolean {
	try {
		val buffer = clientDLL.read(dwViewMatrix, 4 * 4 * 4)!!
		var offset = 0
		for (row in 0..3) for (col in 0..3) {
			val value = buffer.getFloat(offset.toLong())
			viewMatrix[row][col] = value.toDouble()
			offset += 4
		}
		
		vOut.x = viewMatrix[0][0] * from.x + viewMatrix[0][1] * from.y + viewMatrix[0][2] * from.z + viewMatrix[0][3]
		vOut.y = viewMatrix[1][0] * from.x + viewMatrix[1][1] * from.y + viewMatrix[1][2] * from.z + viewMatrix[1][3]
		
		val w = viewMatrix[3][0] * from.x + viewMatrix[3][1] * from.y + viewMatrix[3][2] * from.z + viewMatrix[3][3]
		
		if (w.isNaN() || w < 0.01f) return false
		
		val invw = 1.0 / w
		vOut.x *= invw
		vOut.y *= invw
		
		val width = gameWidth
		val height = gameHeight
		
		var x = width / 2.0
		var y = height / 2.0
		
		x += 0.5 * vOut.x * width + 0.5
		y -= 0.5 * vOut.y * height + 0.5
		
		vOut.x = x + 0
		vOut.y = y + 0
	} catch (t: Throwable) {
		t.printStackTrace()
		return false
	}
	return true
}