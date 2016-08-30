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