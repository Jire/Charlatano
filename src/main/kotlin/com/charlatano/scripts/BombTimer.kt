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

import com.charlatano.game.CSGO.ENTITY_SIZE
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.EntityType
import com.charlatano.game.hooks.GlowIteration
import com.charlatano.game.netvars.NetVarOffsets.flC4Blow
import com.charlatano.game.netvars.NetVarOffsets.szLastPlaceName
import com.charlatano.game.offsets.ClientOffsets.dwEntityList
import com.charlatano.game.offsets.EngineOffsets.dwGlobalVars
import com.charlatano.utils.uint

var location = ""

fun bombTimer() = GlowIteration {
	if (EntityType.CPlantedC4 == EntityType.byEntityAddress(entityAddress)) {
		if (location.isEmpty()) {
			val carrierIndex = (csgoEXE.int(entityAddress + 0x148) and 0xFFF) - 1
			val plantedBy = if (carrierIndex == 4094) -1 else clientDLL.uint(dwEntityList + (carrierIndex * ENTITY_SIZE))
			if (plantedBy != -1L) {
				location = csgoEXE.read(plantedBy + szLastPlaceName, 32, true)!!.getString(0)
			}
		}
		val flags = csgoEXE.float(entityAddress + flC4Blow)
		val timeLeft = -(engineDLL.float(dwGlobalVars + 16) - flags)
		if (timeLeft > 0) {
			val hasKit = false
			val canDefuse = if (hasKit) timeLeft >= 5 else timeLeft >= 10
			println("Location: $location, $timeLeft seconds, can defuse? $canDefuse")
		} else {
			location = ""
		}
		return@GlowIteration
	}
}