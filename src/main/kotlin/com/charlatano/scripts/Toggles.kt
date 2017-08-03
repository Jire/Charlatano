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

fun Toggles_AIM() = every(10) {
	if ((keyPressed(0x12) && keyPressed(TOGGLE_KEY_AIM)) 
		|| (HOLD_TOGGLE_AIM && keyPressed(TOGGLE_KEY_AIM))) {
		ENABLE_AIM = !ENABLE_AIM
		
		do { 
			Thread.sleep(25) 
		} while (keyPressed(TOGGLE_KEY_AIM))
			
		if (HOLD_TOGGLE_AIM) 
			ENABLE_AIM = false
	}
}

fun Toggles_BUNNYHOP() = every(10) {
	if ((keyPressed(0x12) && keyPressed(TOGGLE_KEY_BUNNYHOP)) 
		|| (HOLD_TOGGLE_BUNNYHOP && keyPressed(TOGGLE_KEY_BUNNYHOP))) {
		ENABLE_BUNNY_HOP = !ENABLE_BUNNY_HOP
		
		do { 
			Thread.sleep(25) 
		} while (keyPressed(TOGGLE_KEY_BUNNYHOP))
			
		if (HOLD_TOGGLE_AIM) 
			ENABLE_BUNNY_HOP = false
	}
}

fun Toggles_ESP() = every(10) {
	if ((keyPressed(0x12) && keyPressed(TOGGLE_KEY_ESP)) 
		|| (HOLD_TOGGLE_ESP && keyPressed(TOGGLE_KEY_ESP))) {
		ENABLE_ESP = !ENABLE_ESP
		
		do { 
			Thread.sleep(25) 
		} while (keyPressed(TOGGLE_KEY_ESP))
			
		if (HOLD_TOGGLE_ESP) 
			ENABLE_ESP = false
	}
}

fun Toggles_RAGE() = every(10) {
	if ((keyPressed(0x12) && keyPressed(TOGGLE_KEY_RAGE)) 
		|| (HOLD_TOGGLE_RAGE && keyPressed(TOGGLE_KEY_RAGE))) {
		ENABLE_RAGE = !ENABLE_RAGE
		
		do { 
			Thread.sleep(25) 
		} while (keyPressed(TOGGLE_KEY_RAGE))
			
		if (HOLD_TOGGLE_RAGE) 
			ENABLE_RAGE = false
	}
}

fun Toggles_RCS() = every(10) {
	if ((keyPressed(0x12) && keyPressed(TOGGLE_KEY_RCS)) 
		|| (HOLD_TOGGLE_RCS && keyPressed(TOGGLE_KEY_RCS))) {
		ENABLE_RCS = !ENABLE_RCS
		
		do { 
			Thread.sleep(25) 
		} while (keyPressed(TOGGLE_KEY_RCS))
			
		if (HOLD_TOGGLE_RCS) 
			ENABLE_RCS = false
	}
}

fun Toggles_BONETRIGGER() = every(10) {
	if ((keyPressed(0x12) && keyPressed(TOGGLE_KEY_BONETRIGGER)) 
		|| (HOLD_TOGGLE_BONETRIGGER && keyPressed(TOGGLE_KEY_BONETRIGGER))) {
		ENABLE_BONE_TRIGGER = !ENABLE_BONE_TRIGGER
		
		do { 
			Thread.sleep(25) 
		} while (keyPressed(TOGGLE_KEY_BONETRIGGER))
			
		if (HOLD_TOGGLE_RCS) 
			ENABLE_BONE_TRIGGER = false
	}
}
