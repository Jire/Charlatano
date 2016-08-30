/*
 *    Copyright 2016 Jonathan Beaudoin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.charlatano.scripts

import com.charlatano.game.CSGO.ENTITY_SIZE
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
		bombCarrier = if (carrierIndex == 4094) -1 else clientDLL.uint(dwEntityList + (carrierIndex * ENTITY_SIZE))//TODO find a way to get glowIndex from entityIndex?
		if (type == CPlantedC4) bombCarrier = -1L
		glow(glowAddress)
	} else if (type == CCSPlayer) {
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