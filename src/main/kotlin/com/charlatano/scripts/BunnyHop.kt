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

import com.charlatano.game.hooks.onGround
import com.charlatano.settings.BUNNY_HOP_KEY
import com.charlatano.settings.ENABLE_BUNNY_HOP
import com.charlatano.utils.*
import org.jire.arrowhead.keyPressed

fun bunnyHop() = onGround {
	if (ENABLE_BUNNY_HOP && keyPressed(BUNNY_HOP_KEY)) {
		randScroll()
		Thread.sleep(8 + randLong(10))
		randScroll()
	}
}

private fun randScroll() {
	Thread.sleep(randLong(1, 4))
	val amount = randInt(60) + 10
	mouseWheel(-amount)
}