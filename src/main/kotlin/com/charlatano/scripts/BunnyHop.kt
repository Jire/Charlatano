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

import com.charlatano.BUNNY_HOP_KEY
import com.charlatano.game.hooks.onGround
import com.charlatano.utils.randBoolean
import com.charlatano.utils.randInt
import com.charlatano.utils.randLong
import org.jire.arrowhead.keyPressed

fun bunnyHop() = onGround {
	if (keyPressed(BUNNY_HOP_KEY)) {
		randScroll()
		Thread.sleep(8 + randLong(10))
		randScroll()
	}
}

private fun randScroll() {
	Thread.sleep(randLong(1, 4))
	val amount = randInt(60) + 10
	robot.mouseWheel(if (randBoolean()) amount else -amount)
}