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

import com.charlatano.game.*
import com.charlatano.scripts.*
import com.charlatano.settings.*
import com.charlatano.utils.*
import org.jire.arrowhead.keyPressed

// Start in "ON" position
public var toggleAIM = true
public var toggleRCS = true
public var toggleESP = true
public var toggleBunnyHop = true
public var toggleTrigger = true
public var toggleFlash = true

// Start in "OFF" position
public var toggleRage = false

fun Toggles_AIM() = every(10) {
	if (keyPressed(0x12) && keyPressed(TOGGLE_KEY_AIM)) {
		toggleAIM = !toggleAIM
		do {
			Thread.sleep(25)
		} while (keyPressed(TOGGLE_KEY_AIM))
	}
}

fun Toggles_BUNNYHOP() = every(10) {
	if (keyPressed(0x12) && keyPressed(TOGGLE_KEY_BUNNYHOP)) {
		toggleBunnyHop = !toggleBunnyHop
		do {
			Thread.sleep(25)
		} while (keyPressed(TOGGLE_KEY_BUNNYHOP))
	}
}

fun Toggles_ESP() = every(10) {
	if (keyPressed(0x12) && keyPressed(TOGGLE_KEY_ESP)) {
		toggleESP = !toggleESP
		do {
			Thread.sleep(25)
		} while (keyPressed(TOGGLE_KEY_ESP))
	}
}

fun Toggles_RAGE() = every(10) {
	if (keyPressed(0x12) && keyPressed(TOGGLE_KEY_RAGE)) {
		toggleRage = !toggleRage
		do {
			Thread.sleep(25)
		} while (keyPressed(TOGGLE_KEY_RAGE))
	}
}

fun Toggles_RCS() = every(10) {
	if (keyPressed(0x12) && keyPressed(TOGGLE_KEY_RCS)) {
		toggleRCS = !toggleRCS
		do {
			Thread.sleep(25)
		} while (keyPressed(TOGGLE_KEY_RCS))
	}
}

fun Toggles_TRIGGER() = every(10) {
	if (keyPressed(0x12) && keyPressed(TOGGLE_KEY_TRIGGER)) {
		toggleTrigger = !toggleTrigger
		do {
			Thread.sleep(25)
		} while (keyPressed(TOGGLE_KEY_TRIGGER))
	}
}