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
import com.charlatano.game.CSGO.ENTITY_SIZE
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.netvars.NetVarOffsets.dwBoneMatrix
import com.charlatano.game.netvars.NetVarOffsets.fFlags
import com.charlatano.game.netvars.NetVarOffsets.iCrossHairID
import com.charlatano.game.netvars.NetVarOffsets.iHealth
import com.charlatano.game.netvars.NetVarOffsets.lifeState
import com.charlatano.game.netvars.NetVarOffsets.vecPunch
import com.charlatano.game.netvars.NetVarOffsets.vecVelocity
import com.charlatano.game.netvars.NetVarOffsets.vecViewOffset
import com.charlatano.game.offsets.ClientOffsets.dwEntityList
import com.charlatano.utils.Angle
import com.charlatano.utils.Vector
import com.charlatano.utils.uint
import org.jire.arrowhead.get

typealias Player = Long

internal fun Player.flags(): Int = csgoEXE[this + fFlags]

internal fun Player.onGround(): Boolean = flags() and 1 == 1

internal fun Player.crosshair(): Int = csgoEXE.int(this + iCrossHairID) - 1

internal fun Player.health(): Int = csgoEXE[this + iHealth]

internal fun Player.dead(): Boolean = csgoEXE.byte(this + lifeState) != 0.toByte()

internal fun Player.punch(): Angle = Vector<Float>(csgoEXE[this + vecPunch], csgoEXE[this + vecPunch + 4], 0f)

internal fun Player.viewOffset(): Angle = Vector<Float>(csgoEXE[this + vecViewOffset], csgoEXE[this + vecViewOffset + 4], csgoEXE[this + vecViewOffset + 8])

internal fun Player.velocity(): Angle = Vector<Float>(csgoEXE[this + vecVelocity], csgoEXE[this + vecVelocity + 4], csgoEXE[this + vecVelocity + 8])

internal fun Player.target(): Player {
	val crosshair = crosshair()
	if (crosshair <= 0)
		return -1
	return clientDLL.uint(dwEntityList + (crosshair * ENTITY_SIZE))
}

internal fun Player.boneMatrix() = csgoEXE.uint(this + dwBoneMatrix)

internal fun Player.bone(offset: Int, boneID: Int = FORCE_AIM_TARGET_BONE): Float =
		csgoEXE[boneMatrix() + ((0x30 * boneID) + offset)]
