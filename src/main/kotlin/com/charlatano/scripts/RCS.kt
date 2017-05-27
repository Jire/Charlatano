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

package com.charlatano.scripts

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
import com.charlatano.scripts.aim.bone
import com.charlatano.scripts.aim.perfect
import com.charlatano.settings.*
import com.charlatano.utils.*
import com.charlatano.utils.extensions.uint

private @Volatile var prevFired = 0
private val lastPunch = DoubleArray(2)

fun rcs() = every(RCS_DURATION) {
	if (!ENABLE_RCS) return@every
	
	val myAddress: Player = clientDLL.uint(dwLocalPlayer)
	if (myAddress <= 0) return@every
	
	val shotsFired = csgoEXE.int(myAddress + iShotsFired)
	if (shotsFired <= 2 || shotsFired < prevFired || scaleFormDLL.boolean(bCursorEnabled)) {
		reset()
		return@every
	}
	
	if (!CLASSIC_OFFENSIVE) {
		val weapon = me.weapon()
		if (!weapon.automatic) {
			reset()
			return@every
		}
	}
	
	val punch = Vector(csgoEXE.float(myAddress + vecPunch).toDouble(),
			csgoEXE.float(myAddress + vecPunch + 4).toDouble(), 0.0).apply {
		x *= if (RCS_MAX > RCS_MIN) randDouble(RCS_MIN, RCS_MAX) else RCS_MIN
		y *= if (RCS_MAX > RCS_MIN) randDouble(RCS_MIN, RCS_MAX) else RCS_MIN
		z = 0.0
		normalize()
	}
	
	val newView = Vector(punch.x, punch.y, punch.z).apply {
		x -= lastPunch[0]
		y -= lastPunch[1]
		z = 0.0
		normalize()
	}
	
	val view = clientState.angle().apply {
		x -= newView.x
		y -= newView.y
		z = 0.0
		normalize()
	}
	
	// maybe swap with flat aim for better accuracy
	// but really you'd only need it in LEM+
	pathAim(clientState.angle(), view, RCS_SMOOTHING)
	
	lastPunch[0] = punch.x
	lastPunch[1] = punch.y
	prevFired = shotsFired
	
	if (shotsFired >= SHIFT_TO_SHOULDER_SHOTS) {
		bone.set(if (shotsFired < SHIFT_TO_BODY_SHOTS) SHOULDER_BONE else BODY_BONE)
		perfect.set(false)
	}
}

private fun reset() {
	prevFired = 0
	lastPunch[0] = 0.0
	lastPunch[1] = 0.0
	bone.set(HEAD_BONE)
}