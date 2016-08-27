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
import com.charlatano.model.EntityType
import com.charlatano.netvars.m_iTeamNum
import com.charlatano.offsets.ClientOffsets
import com.charlatano.offsets.ClientOffsets.glowObject
import com.charlatano.offsets.ClientOffsets.localPlayer
import com.charlatano.util.uint

fun esp() = every(2) {
	val myAddress = clientDLL.uint(localPlayer)
	val myTeam = csgoEXE.uint(myAddress + m_iTeamNum)

	val glowObject = clientDLL.uint(glowObject)
	val glowObjectCount = clientDLL.uint(ClientOffsets.glowObject + 4)

	for (glowIndex in 0..glowObjectCount) {
		val glowAddress = glowObject + (glowIndex * GLOW_OBJECT_SIZE)
		val entityAddress = csgoEXE.uint(glowAddress)

		if (EntityType.CCSPlayer != EntityType.byEntityAddress(entityAddress)) continue

		var red = 255F
		var green = 0F
		var blue = 0F
		var alpha = 0.6F

		val entityTeam = csgoEXE.uint(entityAddress + m_iTeamNum)
		if (myTeam == entityTeam) {
			red = 0F
			blue = 255F
		}

		csgoEXE[glowAddress + 0x4] = red
		csgoEXE[glowAddress + 0x8] = green
		csgoEXE[glowAddress + 0xC] = blue
		csgoEXE[glowAddress + 0x10] = alpha
		csgoEXE[glowAddress + 0x24] = true
	}
}