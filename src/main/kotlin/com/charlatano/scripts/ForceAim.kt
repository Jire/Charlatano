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
import com.charlatano.game.netvars.NetVarOffsets.bSpotted
import com.charlatano.game.netvars.NetVarOffsets.dwBoneMatrix
import com.charlatano.game.netvars.NetVarOffsets.iCrossHairID
import com.charlatano.game.netvars.NetVarOffsets.iTeamNum
import com.charlatano.game.netvars.NetVarOffsets.lifeState
import com.charlatano.game.offsets.ClientOffsets.bDormant
import com.charlatano.game.offsets.ClientOffsets.dwEntityList
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.moveTo
import com.charlatano.utils.every
import com.charlatano.utils.uint
import org.jire.arrowhead.keyPressed
import java.util.concurrent.atomic.AtomicLong

private fun Long.boneMatrix() = csgoEXE.uint(this + dwBoneMatrix)
private fun Long.bone(offset: Int, boneID: Int = 6) = csgoEXE.float(boneMatrix() + ((0x30 * boneID) + offset))

private val targetAddressA = AtomicLong()

fun forceAim() = every(16) {
	val pressed = keyPressed(5) {
		val myAddress = clientDLL.uint(dwLocalPlayer)
		if (myAddress <= 0) return@keyPressed

		if (csgoEXE.uint(myAddress + lifeState) > 0) return@keyPressed

		val myTeam = csgoEXE.uint(myAddress + iTeamNum)

		var targetAddress = targetAddressA.get()
		if (targetAddress == 0L) {
			val crosshairID = csgoEXE.uint(myAddress + iCrossHairID) - 1
			if (crosshairID <= 0) return@keyPressed
			targetAddress = clientDLL.uint(dwEntityList + (crosshairID * ENTITY_SIZE))
			if (targetAddress == 0L) return@keyPressed
			targetAddressA.set(targetAddress)
		}

		if (csgoEXE.uint(targetAddress + lifeState) > 0
				|| csgoEXE.boolean(targetAddress + bDormant)
				|| !csgoEXE.boolean(targetAddress + bSpotted)
				|| csgoEXE.uint(targetAddress + iTeamNum) == myTeam) {
			targetAddressA.set(0L)
			return@keyPressed
		}

		val x = targetAddress.bone(0xC)
		val y = targetAddress.bone(0x1C)
		val z = targetAddress.bone(0x2C)

		moveTo(x, y, z)
	}
	if (!pressed) targetAddressA.set(0L)
}