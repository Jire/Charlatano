/*
 *     Charlatano: Free and open-source (FOSS) cheat for CS:GO/CS:CO
 *     Copyright (C) 2017 - Thomas G. P. Nappo, Jonathan Beaudoin
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.charlatano.scripts

import com.charlatano.game.angle
import com.charlatano.game.clientState
import com.charlatano.game.entity.position
import com.charlatano.game.me
import com.charlatano.settings.ENABLE_TRIGGER
import com.charlatano.settings.FIRE_KEY
import com.charlatano.settings.TRIGGER_BONE
import com.charlatano.settings.TRIGGER_FOV
import com.charlatano.utils.hook
import com.charlatano.utils.randLong
import com.charlatano.utils.robot
import org.jire.arrowhead.keyReleased
import java.awt.event.InputEvent

private val onTriggerTarget = hook(1) {
	if (ENABLE_TRIGGER) findTarget(me.position(), clientState.angle(), false, TRIGGER_FOV, TRIGGER_BONE) >= 0
	else false
}

fun fovTrigger() = onTriggerTarget {
	if (keyReleased(FIRE_KEY)) {
		robot.mousePress(InputEvent.BUTTON1_MASK)
		Thread.sleep(8 + randLong(16))
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
	}
}