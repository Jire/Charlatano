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

import co.paralleluniverse.fibers.Suspendable
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.aim
import com.charlatano.game.angle
import com.charlatano.game.clientState
import com.charlatano.game.entity.Player
import com.charlatano.game.netvars.NetVarOffsets.iShotsFired
import com.charlatano.game.netvars.NetVarOffsets.vecPunch
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.utils.*
import org.jire.arrowhead.keyReleased

var prevFired = 0
val lastPunch = FloatArray(2)

@Suspendable private fun work() {
	val myAddress: Player = clientDLL.uint(dwLocalPlayer)
	if (myAddress <= 0) {
		return
	}

	val shotsFired = csgoEXE.int(myAddress + iShotsFired)
	if (shotsFired <= 2 || shotsFired < prevFired) {
		if (keyReleased(1)) {
			reset()
			return
		}
	}

	val punch = Vector(csgoEXE.float(myAddress + vecPunch), csgoEXE.float(myAddress + vecPunch + 4), 0F)
	punch.x *= 2F
	punch.y *= 2F
	punch.z = 0F
	punch.normalize()

	var view = clientState.angle()
	if (view.x == 0F || view.y == 0F || view.z == 0F) view = clientState.angle()

	val newView: Angle = Vector(punch.x, punch.y, punch.z)
	newView.x -= lastPunch[0]
	newView.y -= lastPunch[1]
	newView.z = 0F
	newView.normalize()

	view.x -= newView.x
	view.y -= newView.y
	view.z = 0F
	view.normalize()

	aim(clientState.angle(), view, 65)

	lastPunch[0] = punch.x
	lastPunch[1] = punch.y
	prevFired = shotsFired
}

private fun reset() {
	prevFired = 0
	lastPunch[0] = 0F
	lastPunch[1] = 0F
}

fun rcs() = every(1) { work() }