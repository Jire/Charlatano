/*
 * Charlatano is a premium CS:GO cheat ran on the JVM.
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
import com.charlatano.AIM_BONE
import com.charlatano.RCS_DURATION
import com.charlatano.RCS_SMOOTHING
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.scaleFormDLL
import com.charlatano.game.angle
import com.charlatano.game.clientState
import com.charlatano.game.entity.Player
import com.charlatano.game.entity.weapon
import com.charlatano.game.me
import com.charlatano.game.netvars.NetVarOffsets.iShotsFired
import com.charlatano.game.netvars.NetVarOffsets.vecPunch
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.game.offsets.ScaleFormOffsets.bCursorEnabled
import com.charlatano.utils.*
import com.charlatano.utils.extensions.uint

private var prevFired = 0
private val lastPunch = DoubleArray(2)

@Suspendable private fun work() {
	val myAddress: Player = clientDLL.uint(dwLocalPlayer)
	if (myAddress <= 0) return

	val shotsFired = csgoEXE.int(myAddress + iShotsFired)
	if (shotsFired <= 2 || shotsFired < prevFired || scaleFormDLL.boolean(bCursorEnabled)) {
		reset()
		return
	}
	val weapon = me.weapon()
	if (!weapon.pistol && !weapon.automatic && !weapon.shotgun) {
		reset()
		return
	}

	val punch = Vector(csgoEXE.float(myAddress + vecPunch).toDouble(),
			csgoEXE.float(myAddress + vecPunch + 4).toDouble(), 0.0)
	punch.x *= 2.0
	punch.y *= 2.0
	punch.z = 0.0
	punch.normalize()

	val newView: Angle = Vector(punch.x, punch.y, punch.z)
	newView.x -= lastPunch[0]
	newView.y -= lastPunch[1]
	newView.z = 0.0
	newView.normalize()

	val view = clientState.angle()
	view.x -= newView.x
	view.y -= newView.y
	view.z = 0.0
	view.normalize()

	aim(clientState.angle(), view, RCS_SMOOTHING)

	lastPunch[0] = punch.x
	lastPunch[1] = punch.y
	prevFired = shotsFired

	if (shotsFired >= 3) {
		bone.set(if (shotsFired == 3) 7 else 6)
		perfect.set(false)
	}
}

private fun reset() {
	prevFired = 0
	lastPunch[0] = 0.0
	lastPunch[1] = 0.0
	bone.set(AIM_BONE)
}

fun rcs() = every(RCS_DURATION) { work() }