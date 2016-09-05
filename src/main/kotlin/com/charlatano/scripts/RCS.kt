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

package com.charlatano.scripts

import co.paralleluniverse.strands.Strand
import com.charlatano.SCREEN_SIZE
import com.charlatano.User32
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.netvars.NetVarOffsets.iShotsFired
import com.charlatano.game.netvars.NetVarOffsets.vecPunch
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.utils.Vector
import com.charlatano.utils.every
import com.charlatano.utils.uint

var prevFired = 0

val width = SCREEN_SIZE.width
val height = SCREEN_SIZE.height

val dxN = width / 90
val dyN = height / 90

fun rcs() = every(16) {
	val myAddress = clientDLL.uint(dwLocalPlayer)
	if (myAddress <= 0) return@every
	
	val shotsFired = csgoEXE.int(myAddress + iShotsFired)
	if (shotsFired > 1 && shotsFired > prevFired) {
		val lastPunch = Vector(csgoEXE.float(myAddress + vecPunch), csgoEXE.float(myAddress + vecPunch + 4), 0F)
		
		Strand.sleep(32)
		
		val currentPunch = Vector(csgoEXE.float(myAddress + vecPunch), csgoEXE.float(myAddress + vecPunch + 4), 0F)
		
		val toScreen = toScreen(currentPunch, lastPunch)
		
		User32.mouse_event(User32.MOUSEEVENTF_MOVE, toScreen.x.toInt(), toScreen.y.toInt(), null, null)
		
		prevFired = shotsFired
	} else {
		prevFired = 0
		
	}
}

fun toScreen(current: Vector<Float>, previous: Vector<Float>): Vector<Float> {
	val previous = angleToScreen(previous)
	val current = angleToScreen(current)
	return Vector(-(current.x - previous.x) * 4, -(current.y - previous.y) * 6)
}

fun angleToScreen(vector: Vector<Float>) = Vector(((width / 2.0) - (dxN * (vector.y / 2.0f))).toFloat(), ((height / 2.0) - (dyN * (-vector.x / 2.0f))).toFloat())