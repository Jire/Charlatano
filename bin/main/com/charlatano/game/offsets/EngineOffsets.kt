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

package com.charlatano.game.offsets

import com.charlatano.game.CSGO.engineDLL
import com.charlatano.utils.extensions.invoke
import com.charlatano.utils.get

object EngineOffsets {

    val dwClientState by engineDLL(1)(0xA1, 0[4], 0x33, 0xD2, 0x6A, 0, 0x6A, 0, 0x33, 0xC9, 0x89, 0xB0)
    val dwInGame by engineDLL(2, subtract = false)(0x83, 0xB8, 0[5], 0x0F, 0x94, 0xC0, 0xC3)
    val dwGlobalVars by engineDLL(1)(0x68, 0[4], 0x68, 0[4], 0xFF, 0x50, 0x08, 0x85, 0xC0)
    val dwViewAngles by engineDLL(4, subtract = false)(0xF3, 0x0F, 0x11, 0x80, 0[4], 0xD9, 0x46, 0x04, 0xD9, 0x05)

    val dwSignOnState by engineDLL(2, subtract = false)(0x83, 0xB8, 0[5], 0x0F, 0x94, 0xC0, 0xC3)

    val pStudioModel by engineDLL(0x2)(0x8B, 0x0D, 0x00, 0x00, 0x00, 0x00, 0x0F, 0xB7, 0x80,
            0x00, 0x00, 0x00, 0x00, 0x8B, 0x11, 0x89, 0x45, 0x08, 0x5D, 0xFF, 0x62, 0x38, 0x33, 0xC0)

}
