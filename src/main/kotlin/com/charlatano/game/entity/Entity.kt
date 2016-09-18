/*
 * Charlatano is a premium CS:GO cheat ran on the JVM.
 * Copyright (C) 2016 - Thomas Nappo, Jonathan Beaudoin
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

package com.charlatano.game.entity

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.netvars.NetVarOffsets.bSpotted
import com.charlatano.game.netvars.NetVarOffsets.dwModel
import com.charlatano.game.netvars.NetVarOffsets.iTeamNum
import com.charlatano.game.netvars.NetVarOffsets.vecOrigin
import com.charlatano.game.netvars.NetVarOffsets.vecViewOffset
import com.charlatano.game.offsets.ClientOffsets.bDormant
import com.charlatano.utils.Angle
import com.charlatano.utils.Vector
import com.charlatano.utils.uint
import org.jire.arrowhead.get

typealias Entity = Long

internal fun Entity.spotted(): Boolean = csgoEXE.int(this + bSpotted) != 0

internal fun Entity.dormant(): Boolean = csgoEXE[this + bDormant]

internal fun Entity.team(): Int = csgoEXE[this + iTeamNum]

internal fun Entity.model(): Long = csgoEXE.uint(this + dwModel)

internal fun Entity.position(): Angle = Vector(csgoEXE[this + vecOrigin], csgoEXE[this + vecOrigin + 4],
		csgoEXE.float(this + vecOrigin + 8) + csgoEXE.float(this + vecViewOffset + 8))