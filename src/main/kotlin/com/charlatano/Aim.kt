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

package com.charlatano

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.entity.*
import com.charlatano.game.netvars.NetVarOffsets.vecViewOffset
import com.charlatano.game.offsets.ClientOffsets.dwViewMatrix
import com.charlatano.utils.Angle
import com.charlatano.utils.Vector
import com.charlatano.utils.normalize
import java.lang.Math.atan
import java.lang.Math.toDegrees

const val InGameSensitivity = 2.0
const val InGamePitch = 0.022
const val InGameYaw = 0.022

const val PITCH_MIN_PUNCH = 1.96
const val PITCH_MAX_PUNCH = 2.07

const val YAW_MIN_PUNCH = 1.97
const val YAW_MAX_PUNCH = 2.02

val m_vMatrix = Array(4) { DoubleArray(4) }

fun worldToScreen(from: Vector, vOut: Vector): Boolean {
	try {
		val buffer = clientDLL.read(dwViewMatrix, 4 * 4 * 4)!!
		var offset = 0
		for (row in 0..3) {
			for (col in 0..3) {
				val value = buffer.getFloat(offset.toLong())
				m_vMatrix[row][col] = value.toDouble()
				offset += 4
			}
		}

		vOut.x = m_vMatrix[0][0] * from.x + m_vMatrix[0][1] * from.y + m_vMatrix[0][2] * from.z + m_vMatrix[0][3]
		vOut.y = m_vMatrix[1][0] * from.x + m_vMatrix[1][1] * from.y + m_vMatrix[1][2] * from.z + m_vMatrix[1][3]

		val w = m_vMatrix[3][0] * from.x + m_vMatrix[3][1] * from.y + m_vMatrix[3][2] * from.z + m_vMatrix[3][3]

		if (w.isNaN() || w < 0.01f) {
			return false
		}

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

fun compensateVelocity(source: Player, target: Player, enemyPos: Vector, smoothing: Double): Vector {
	val myVelocity = source.velocity()
	val enemyVelocity = target.velocity()

	val smoothingFactor = 0.15F
	enemyPos.x += enemyVelocity.x * smoothingFactor
	enemyPos.y += enemyVelocity.y * smoothingFactor
	enemyPos.z += enemyVelocity.z * smoothingFactor

	enemyPos.x -= myVelocity.x * smoothingFactor
	enemyPos.y -= myVelocity.y * smoothingFactor
	enemyPos.z -= myVelocity.z * smoothingFactor

/*	val smoothingFactor = 40.0 / smoothing
	enemyPos.x += (enemyVelocity.x / 100) * smoothingFactor
	enemyPos.y += (enemyVelocity.y / 100) * smoothingFactor
	enemyPos.z += (enemyVelocity.z / 100) * smoothingFactor
	enemyPos.x -= (myVelocity.x / 100) * smoothingFactor
	enemyPos.y -= (myVelocity.y / 100) * smoothingFactor
	enemyPos.z -= (myVelocity.z / 100) * smoothingFactor*/

	return enemyPos
}

val angles: ThreadLocal<Angle> = ThreadLocal.withInitial { Vector() }

fun calculateAngle(player: Player, dst: Vector): Angle {
	val angles = angles.get()
	angles.reset()

	val myPunch = player.punch()
	val myPosition = player.position()

	val dX = myPosition.x - dst.x
	val dY = myPosition.y - dst.y
	val dZ = myPosition.z + csgoEXE.float(player + vecViewOffset) - dst.z

	val hyp = Math.sqrt((dX * dX) + (dY * dY))

	angles.x = toDegrees(atan(dZ / hyp)) - myPunch.x * 2.0
	angles.y = toDegrees(atan(dY / dX)) - myPunch.y * 2.0
	angles.z = 0.0
	if (dX >= 0.0) angles.y += 180

	return angles.normalize()
}