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

package com.charlatano.script.scripts

import com.charlatano.GLOW_OBJECT_SIZE
import com.charlatano.clientDLL
import com.charlatano.csgoEXE
import com.charlatano.every.every
import com.charlatano.offsets.m_dwGlowObject
import com.charlatano.util.uint

fun esp() = every(32) {
	val glowObject = clientDLL.uint(m_dwGlowObject)
	val glowObjectCount = clientDLL.uint(m_dwGlowObject + 4)

	for (glowIndex in 0..glowObjectCount) {
		val glowAddress = glowObject + (glowIndex * GLOW_OBJECT_SIZE)

		//val entityAddress = csgoEXE.uint(glowAddress)

		csgoEXE[glowAddress + 0x4] = 0F
		csgoEXE[glowAddress + 0x8] = 1F
		csgoEXE[glowAddress + 0xC] = 0F
		csgoEXE[glowAddress + 0x10] = 0.7F
		csgoEXE[glowAddress + 0x24] = true
	}
}