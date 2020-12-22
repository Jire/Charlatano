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
import com.sun.jna.platform.win32.WinNT

/**
 * These should be set the same as your in-game "m_pitch" and "m_yaw" values.
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
var MAX_ENTITIES = 4096

/**
 * The interval in milliseconds to recycle entities in the cached list.
 */
var CLEANUP_TIME = 10_000

/**
 * Whether or not garbage collect
 * after every map load startup.
 */
var GARBAGE_COLLECT_ON_MAP_START = true

/**
 * The process name of CS:GO
 */
var PROCESS_NAME = "csgo.exe"

/**
 * The process flags to open the handle to CS:GO with.
 */
var PROCESS_ACCESS_FLAGS = WinNT.PROCESS_QUERY_INFORMATION or
		WinNT.PROCESS_VM_READ or
		WinNT.PROCESS_VM_WRITE or
		WinNT.PROCESS_VM_OPERATION

/**
 * The module name of the client module.
 */
var CLIENT_MODULE_NAME = "client.dll"

/**
 * The module name of the engine module.
 */
var ENGINE_MODULE_NAME = "engine.dll"