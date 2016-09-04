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

package com.charlatano.game.hooks

import com.charlatano.game.CSGO
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.netvars.NetVarOffsets
import com.charlatano.game.offsets.ClientOffsets
import com.charlatano.scripts.bombAddress
import com.charlatano.utils.hook
import com.charlatano.utils.uint

/**
 * Created by Jonathan on 8/31/2016.
 */

var location = ""

val bombPlanted = hook(8) {
	val bombPlanted = bombAddress != -1L && !csgoEXE.boolean(bombAddress + NetVarOffsets.bBombDefused)
	if (bombPlanted && location.isEmpty()) {
		val carrierIndex = (csgoEXE.int(bombAddress + 0x148) and 0xFFF) - 1
		val plantedBy = clientDLL.uint(ClientOffsets.dwEntityList + (carrierIndex * CSGO.ENTITY_SIZE))
		location = csgoEXE.read(plantedBy + NetVarOffsets.szLastPlaceName, 32, true)!!.getString(0)
	} else if (!bombPlanted && !location.isEmpty()) location = ""
	
	bombPlanted
}