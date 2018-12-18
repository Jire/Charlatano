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
import com.charlatano.game.angle
import com.charlatano.game.clientState
import com.charlatano.game.entity.position
import com.charlatano.game.me
import com.charlatano.game.offsets.ClientOffsets.dwForceAttack
import com.charlatano.scripts.aim.findTarget
import com.charlatano.settings.*
import com.charlatano.utils.*
import org.jire.arrowhead.keyReleased

private val onBoneTriggerTarget = hook(1) {
	if (ENABLE_BONE_TRIGGER) findTarget(me.position(), clientState.angle(), false,
			BONE_TRIGGER_FOV, BONE_TRIGGER_BONE, false) >= 0
	else false
}

fun boneTrigger() = onBoneTriggerTarget {
	if (keyReleased(FIRE_KEY)) {
		if (LEAGUE_MODE) mouse(MOUSEEVENTF_LEFTDOWN) else clientDLL[dwForceAttack] = 5.toByte()
		Thread.sleep(8 + randLong(16))
		if (LEAGUE_MODE) mouse(MOUSEEVENTF_LEFTUP) else clientDLL[dwForceAttack] = 4.toByte()
	}
}