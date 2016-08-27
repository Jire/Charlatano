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

package com.charlatano.script.scripts

import co.paralleluniverse.strands.Strand.sleep
import com.charlatano.clientDLL
import com.charlatano.hook.onGround
import com.charlatano.offsets.ClientOffsets.forceJump
import org.jire.arrowhead.keyPressed
import java.awt.event.KeyEvent

fun bunnyHop() = onGround {
	if (keyPressed(KeyEvent.VK_SPACE)) {
		clientDLL[forceJump] = 5.toByte()
		sleep(20)
		clientDLL[forceJump] = 4.toByte()
	}
}