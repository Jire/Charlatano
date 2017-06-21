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

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.me
import com.charlatano.game.netvars.NetVarOffsets.bSpottedByMask
import com.charlatano.game.netvars.NetVarOffsets.dwModel
import com.charlatano.game.netvars.NetVarOffsets.iTeamNum
import com.charlatano.game.netvars.NetVarOffsets.vecOrigin
import com.charlatano.game.netvars.NetVarOffsets.vecViewOffset
import com.charlatano.game.offsets.ClientOffsets.bDormant
import com.charlatano.game.offsets.ClientOffsets.dwIndex
import com.charlatano.utils.Angle
import com.charlatano.utils.extensions.uint
import com.charlatano.utils.readCached
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap

typealias Entity = Long

internal fun Entity.spotted(): Boolean {
	val meID = csgoEXE.int(me + dwIndex) - 1
	val spottedByMask = csgoEXE.uint(this + bSpottedByMask)
	val result = spottedByMask and (1 shl meID).toLong()
	return result != 0L
}

internal fun Entity.dormant(): Boolean = try {
	csgoEXE.boolean(this + bDormant)
} catch (t: Throwable) {
	false
}

internal fun Entity.team() = csgoEXE.uint(this + iTeamNum)

internal fun Entity.model(): Long = csgoEXE.uint(this + dwModel)

private val entity2Angle: Long2ObjectMap<Angle> = Long2ObjectOpenHashMap(255)

internal fun Entity.position(): Angle = readCached(entity2Angle) {
	x = csgoEXE.float(it + vecOrigin).toDouble()
	y = csgoEXE.float(it + vecOrigin + 4).toDouble()
	z = csgoEXE.float(it + vecOrigin + 8).toDouble() + csgoEXE.float(it + vecViewOffset + 8)
}