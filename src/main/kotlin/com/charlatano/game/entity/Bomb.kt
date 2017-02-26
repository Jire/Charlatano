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

package com.charlatano.game.entity

import com.charlatano.game.CSGO.ENTITY_SIZE
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.netvars.NetVarOffsets.bBombDefused
import com.charlatano.game.netvars.NetVarOffsets.flC4Blow
import com.charlatano.game.netvars.NetVarOffsets.hOwnerEntity
import com.charlatano.game.netvars.NetVarOffsets.szLastPlaceName
import com.charlatano.game.offsets.ClientOffsets.dwEntityList
import com.charlatano.game.offsets.EngineOffsets.dwGlobalVars
import com.charlatano.utils.extensions.uint
import org.jire.arrowhead.get

typealias Bomb = Long

internal fun Bomb.defused(): Boolean = csgoEXE[this + bBombDefused]

internal fun Bomb.timeLeft(): Int = (-(engineDLL.float(dwGlobalVars + 16) - csgoEXE.float(this + flC4Blow))).toInt()

internal fun Bomb.planted() = this != -1L && !defused() && timeLeft() > 0

internal fun Bomb.owner() = csgoEXE.uint(this + hOwnerEntity)

internal fun Bomb.carrier(): Player {
	val owner = owner()
	return if (owner > 0) (owner and 0xFFF) - 1L else 0
}

internal fun Bomb.planter(): Player = clientDLL.uint(dwEntityList + (carrier() * ENTITY_SIZE))

internal fun Bomb.location(): String = csgoEXE.read(planter() + szLastPlaceName, 32, true)!!.getString(0)