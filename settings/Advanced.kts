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
import com.sun.jna.platform.win32.WinNT

/**
 * These should be set the same as your in-game "m_pitch" and "m_yaw" values.
 */
GAME_PITCH = 0.022 // m_pitch
GAME_YAW = 0.022 // m_yaw

/**
 * The tick ratio of the server.
 *
 * This isn't that important to set, but is recommended.
 */
SERVER_TICK_RATE = 64

/**
 * The maximum amount of entities that can be managed by the cached list.
 */
MAX_ENTITIES = 4096

/**
 * The interin milliseconds to recycle entities in the cached list.
 */
CLEANUP_TIME = 10_000


/**
 * Whether or not garbage collect
 * after every map load startup.
 */
GARBAGE_COLLECT_ON_MAP_START = true

/**
 * The process name of CS:GO
 */
PROCESS_NAME = "csgo.exe"

/**
 * The process flags to open the handle to CS:GO with.
 */
PROCESS_ACCESS_FLAGS = WinNT.PROCESS_QUERY_INFORMATION or
		WinNT.PROCESS_VM_READ or
		WinNT.PROCESS_VM_WRITE or
		WinNT.PROCESS_VM_OPERATION

/**
 * The module name of the client module.
 */
CLIENT_MODULE_NAME = "client.dll"

/**
 * The module name of the engine module.
 */
ENGINE_MODULE_NAME = "engine.dll"
