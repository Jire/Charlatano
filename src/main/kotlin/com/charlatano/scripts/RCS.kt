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

fun rcs() = every(1) {
	val myAddress = clientDLL.uint(dwLocalPlayer)
	if (myAddress <= 0) return@every
	
	val shotsFired = csgoEXE.int(myAddress + iShotsFired)
	if (shotsFired > 1 && shotsFired > prevFired) {
		var punchVector = Vector(csgoEXE.float(myAddress + vecPunch), csgoEXE.float(myAddress + vecPunch + 4), 0F)
		
		val oldx = punchVector.x
		val oldy = punchVector.y
		
		Strand.sleep(20)
		
		punchVector = Vector(csgoEXE.float(myAddress + vecPunch), csgoEXE.float(myAddress + vecPunch + 4), 0F)
		
		val newx = punchVector.x
		val newy = punchVector.y
		
		val mouseX = AngletoScreenX(newx.toDouble(), oldx.toDouble()).toInt()
		val mouseY = AngletoScreenY(newy.toDouble(), oldy.toDouble()).toInt()
		
		println("$mouseX, $mouseY")
		User32.mouse_event(User32.MOUSEEVENTF_MOVE, mouseX, mouseY, null, null)
		prevFired = shotsFired
	} else {
		prevFired = 0
		
	}
}

//https://www.unknowncheats.me/forum/counterstrike-global-offensive/186394-convert-angle-position.html#post1528951
fun AngletoScreenX(angle: Double, previous: Double): Double {
	val A = 74 * (Math.PI / 180)
	val theta = (angle - previous) * (Math.PI / 180)
	val X = (height * Math.tan(theta)) / (2 * Math.tan(A))
	return X
}

fun AngletoScreenY(angle: Double, previous: Double): Double {
	var B = 100
	if (width / height == 4 / 3) {
		B = 90
	} else if (width / height == 16 / 9) {
		B = 106
	}
	val A = (B / 2) * (Math.PI / 180)
	val theta = (angle - previous) * (Math.PI / 180)
	val Y = (height * Math.tan(theta)) / (2 * Math.tan(A))
	return Y
}