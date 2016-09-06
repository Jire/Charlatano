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

package com.charlatano.game.entity

import com.charlatano.FORCE_AIM_TARGET_BONE
import com.charlatano.game.CSGO
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.netvars.NetVarOffsets
import com.charlatano.game.netvars.NetVarOffsets.fFlags
import com.charlatano.game.offsets.ClientOffsets
import com.charlatano.utils.uint
import org.jire.arrowhead.get

typealias Player = Long

internal fun Player.flags(): Int = csgoEXE[this + fFlags]

internal fun Player.onGround(): Boolean = flags() and 1 == 1

private fun Player.cross(): Int = csgoEXE[this + NetVarOffsets.iCrossHairID]/* - 1L*/ //Bug wont let me compile -1?

internal fun Player.crosshair(): Int = cross() - 1

internal fun Player.target(): Player {
	val crosshair = crosshair()
	if (crosshair <= 0)
		return -1
	return clientDLL.uint(ClientOffsets.dwEntityList + (crosshair * CSGO.ENTITY_SIZE))
}

private fun Player.boneMatrix() = csgoEXE.uint(this + NetVarOffsets.dwBoneMatrix)

internal fun Player.bone(offset: Int, boneID: Int = FORCE_AIM_TARGET_BONE): Float =
		csgoEXE[boneMatrix() + ((0x30 * boneID) + offset)]
