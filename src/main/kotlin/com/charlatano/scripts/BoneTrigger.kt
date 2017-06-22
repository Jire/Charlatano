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

import com.charlatano.game.angle
import com.charlatano.game.clientState
import com.charlatano.game.entity.position
import com.charlatano.game.me
import com.charlatano.scripts.aim.findTarget
import com.charlatano.settings.BONE_TRIGGER_BONE
import com.charlatano.settings.BONE_TRIGGER_FOV
import com.charlatano.settings.ENABLE_BONE_TRIGGER
import com.charlatano.settings.FIRE_KEY
import com.charlatano.settings.FORCE_AIM_KEY
import com.charlatano.utils.*
import org.jire.arrowhead.keyReleased
import org.jire.arrowhead.keyPressed
import com.charlatano.scripts.*

private val onBoneTriggerTarget = hook(1) {
	if ((ENABLE_BONE_TRIGGER && toggleTrigger) && !toggleRage) findTarget(me.position(), clientState.angle(), false,
			BONE_TRIGGER_FOV, BONE_TRIGGER_BONE) >= 0
	else false
}

fun boneTrigger() = onBoneTriggerTarget {
	if (keyReleased(FIRE_KEY) && keyPressed(FORCE_AIM_KEY))
		doClick()
}

fun doClick() {
	mouse(MOUSEEVENTF_LEFTDOWN)
	Thread.sleep(12 + randLong(4))
	mouse(MOUSEEVENTF_LEFTUP)
	Thread.sleep(4 + randLong(4))
}