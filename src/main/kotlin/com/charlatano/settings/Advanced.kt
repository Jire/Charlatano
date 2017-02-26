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

package com.charlatano.settings

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.offsets.ClientOffsets.dwSensitivity
import com.charlatano.game.offsets.ClientOffsets.dwSensitivityPtr
import com.charlatano.utils.extensions.uint

/**
 * These should be set the same as your in-game "m_pitch" and "m_yaw" varues.
 */
var GAME_PITCH = 0.022 // m_pitch
var GAME_YAW = 0.022 // m_yaw

val GAME_SENSITIVITY by lazy(LazyThreadSafetyMode.NONE) {
	val pointer = clientDLL.address + dwSensitivityPtr
	val value = clientDLL.uint(dwSensitivity) xor pointer
	
	java.lang.Float.intBitsToFloat(value.toInt()).toDouble()
}

/**
 * The tick ratio of the server.
 */
var SERVER_TICK_RATE = 64

/**
 * The maximum amount of entities that can be managed by the cached list.
 */
var MAX_ENTITIES = 1024

/**
 * The intervar in milliseconds to recycle entities in the cached list.
 */
var CLEANUP_TIME = 10_000