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

import co.paralleluniverse.strands.Strand
import com.charlatano.game.hooks.onGround
import com.charlatano.utils.nextBoolean
import com.charlatano.utils.nextInt
import com.charlatano.utils.nextLong
import org.jire.arrowhead.keyPressed
import java.awt.Robot
import java.awt.event.KeyEvent

val robot = Robot()

// onGround + key(VK_SPACE) {
//     press(VK_SPACE)
//     sleep(20)
//     release(VK_SPACE)

//    pressAndRelease(VK_SPACE, 20)
// }
//

fun bunnyHop() = onGround {
	if (keyPressed(KeyEvent.VK_SPACE)) {
		randScroll()
		//clientDLL[dwForceJump] = 5.toByte()
		/*robot.keyPress(KeyEvent.VK_MINUS)*/
		Strand.sleep(8 + nextLong(10))
		randScroll()
		//robot.keyRelease(KeyEvent.VK_MINUS)
		//Strand.sleep(2)
		//clientDLL[dwForceJump] = 4.toByte()
	}
}

private fun randScroll() {
	Strand.sleep(nextLong(2, 5))
	val amount = nextInt(60) + 10
	robot.mouseWheel(if (nextBoolean()) amount else -amount)
}