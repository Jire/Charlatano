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

/**
 * Enables the bunny hop script.
 *
 * !!! IMPORTANT NOTE !!!
 *
 * You need to unbind the bunnyhop key, and bind mwheelup and mwheeldown to jump.
 * To do this, type the following commands into the in-game developer console:
 * unbind "space"
 * bind "mwheeldown" "+jump"
 */
ENABLE_BUNNY_HOP = false

/**
 * Enables the recoil control system (RCS) script.
 */
ENABLE_RCS = true

/**
 * Enables the extra sensory perception (ESP) script.
 */
ENABLE_ESP = true

/**
 * Enables the flat aim script.
 *
 * This script uses traditional flat linear-regression smoothing.
 */
ENABLE_FLAT_AIM = true

/**
 * Enables the path aim script.
 *
 * This script uses an advanced path generation smoothing.
 */
ENABLE_PATH_AIM = false

/**
 * Enables the bone trigger bot script.
 */
ENABLE_BONE_TRIGGER = false

/**
 * Enables the reduced flash script.
 */
ENABLE_REDUCED_FLASH = false

/**
 * Enables the bomb timer script.
 */
ENABLE_BOMB_TIMER = false