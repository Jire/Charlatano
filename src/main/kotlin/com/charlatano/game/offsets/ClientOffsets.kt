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

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.utils.extensions.invoke
import com.charlatano.utils.extensions.uint
import com.charlatano.utils.get

object ClientOffsets {
	
	const val dwIndex = 0x64
	
	val bDormant by clientDLL(0xC, subtract = false)(0x55, 0x8B, 0xEC, 0x53, 0x8B, 0x5D, 0x08,
			0x56, 0x8B, 0xF1, 0x88, 0x9E, 0[4], 0xE8)
	
	val decalname by clientDLL(read = false, subtract = false)(0x64, 0x65, 0x63, 0x61, 0x6C, 0x6E, 0x61, 0x6D, 0x65, 0x00)
	
	val dwFirstClass by lazy(LazyThreadSafetyMode.NONE) { findFirstClass() }
	
	val dwForceJump by clientDLL(2)(0x89, 0x15, 0[4], 0x8B, 0x15, 0[4], 0xF6,
			0xC2, 0x03, 0x74, 0x03, 0x83, 0xCE, 0x08)
	
	var dwLocalPlayer by clientDLL(1, 0x10)(0xA3, 0[4], 0xC7, 0x05, 0[8], 0xE8, 0[4], 0x59, 0xC3, 0x6A, 0)
	val dwGlowObject by clientDLL(1, 4)(0xA1, 0[4], 0xA8, 0x01, 0x75, 0x4B)
	val dwEntityList by clientDLL(1)(0xBB, 0[4], 0x83, 0xFF, 0x01, 0x0F, 0x8C, 0[4], 0x3B, 0xF8)
	val dwViewMatrix by clientDLL(3, 158)(0x0F, 0x10, 0x05, 0[4], 0x8D, 0x85, 0[4], 0xB9)
	
	val dwSensitivity by clientDLL(2, 44)(0x81, 0xF9, 0[4], 0x75, 0x1D, 0xF3, 0x0F, 0x10, 0x05, 0[4],
			0xF3, 0x0F, 0x11, 0x44, 0x24, 0, 0x8B, 0x44, 0x24, 0x18, 0x35, 0[4], 0x89, 0x44, 0x24, 0x0C, 0xEB, 0x0B)
	
	val dwSensitivityPtr by clientDLL(2)(0x81, 0xF9, 0[4], 0x75, 0x1D, 0xF3, 0x0F, 0x10, 0x05, 0[4], 0xF3, 0x0F, 0x11,
			0x44, 0x24, 0, 0x8B, 0x44, 0x24, 0x18, 0x35, 0[4], 0x89, 0x44, 0x24, 0x0C, 0xEB, 0x0B)
	
}