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

package com.charlatano.scripts

import com.charlatano.game.CSGO
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.EntityType.*
import com.charlatano.game.EntityType.Companion.byEntityAddress
import com.charlatano.game.hooks.GlowIteration
import com.charlatano.game.netvars.NetVarOffsets.iTeamNum
import com.charlatano.game.offsets.ClientOffsets.dwEntityList
import com.charlatano.utils.uint

var bombCarrier = -1L

fun esp() = GlowIteration {
	val type = byEntityAddress(entityAddress)
	if (type == CPlantedC4 || type == CC4) {
		val carrierIndex = (csgoEXE.int(entityAddress + 0x148) and 0xFFF) - 1
		bombCarrier = if (carrierIndex == 4094) -1 else clientDLL.uint(dwEntityList + (carrierIndex * CSGO.ENTITY_SIZE))
		if (type == CPlantedC4) {
			bombCarrier = -1L
		}
		glow(glowAddress)
	} else if (type == CCSPlayer ) {
		val entityTeam = csgoEXE.uint(entityAddress + iTeamNum)
		if (entityAddress == bombCarrier) {
			glow(glowAddress, red = 0f, green = 255f)
		} else if (myTeam == entityTeam) {
			glow(glowAddress, red = 0f, blue = 255f)
		} else {
			glow(glowAddress)
		}
	}
}

fun glow(glowAddress: Long, red: Float = 255f, green: Float = 0f, blue: Float = 0f, alpha: Float = 0.6f) {
	csgoEXE[glowAddress + 0x4] = red
	csgoEXE[glowAddress + 0x8] = green
	csgoEXE[glowAddress + 0xC] = blue
	csgoEXE[glowAddress + 0x10] = alpha
	csgoEXE[glowAddress + 0x24] = true
}