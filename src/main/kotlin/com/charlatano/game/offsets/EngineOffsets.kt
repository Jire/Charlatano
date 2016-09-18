/*
 * Charlatan is a premium CS:GO cheat ran on the JVM.
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

package com.charlatano.game.offsets

import com.charlatano.game.CSGO.engineDLL
import com.charlatano.utils.get

object EngineOffsets {

	val dwClientState by engineDLL(1)(161, 0[4], 243, 15, 17, 128, 0[4], 217, 70, 4, 217, 5, 0[4])
	val dwInGame by engineDLL(2, subtract = false)(131, 185, 0[4], 6, 15, 148, 192, 195)
	val dwGlobalVars by engineDLL(12)(0x8B, 0x0D, 0x0[4], 0x83, 0xC4, 0x04,
			0x8B, 0x01, 0x68, 0x0[4], 0xFF, 0x35)
	val dwViewAngles by engineDLL(4, subtract = false)(0xF3, 0x0F, 0x11, 0x80, 0x00[4],
			0xD9, 0x46, 0x04, 0xD9, 0x05, 0x00[4])

	val studioModel by engineDLL(0x2)(0x8B, 0x0D, 0x00, 0x00, 0x00, 0x00, 0x0F, 0xB7, 0x80,
			0x00, 0x00, 0x00, 0x00, 0x8B, 0x11, 0x89, 0x45, 0x08, 0x5D, 0xFF, 0x62, 0x38, 0x33, 0xC0)

}