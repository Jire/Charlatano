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

import com.charlatano.settings.*
import java.awt.event.KeyEvent

/*
 * Key codes can be found @ https://msdn.microsoft.com/en-us/library/windows/desktop/dd375731(v=vs.85).aspx
 * In order to toggle something you need to hold ALT + the Toggle Key.
 * Alternatively, if you have HOLD_TOGGLE set to true for that variable you only need to hold the key.
 */

// Aim toggle key.
TOGGLE_KEY_AIM = KeyEvent.VK_NUMPAD1
HOLD_TOGGLE_AIM = false

// RCS toggle key
TOGGLE_KEY_RCS = KeyEvent.VK_NUMPAD2
HOLD_TOGGLE_RCS = false

// ESP toggle key
TOGGLE_KEY_ESP = KeyEvent.VK_NUMPAD0
HOLD_TOGGLE_ESP = false

// Bunnyhop toggle key.
TOGGLE_KEY_BUNNYHOP = KeyEvent.VK_NUMPAD3
HOLD_TOGGLE_BUNNYHOP = false

// BoneTrigger toggle key.
TOGGLE_KEY_BONETRIGGER = KeyEvent.VK_NUMPAD4
HOLD_TOGGLE_BONETRIGGER = false

// Rage toggle key.
TOGGLE_KEY_RAGE = KeyEvent.VK_NUMPAD9
HOLD_TOGGLE_RAGE = false
