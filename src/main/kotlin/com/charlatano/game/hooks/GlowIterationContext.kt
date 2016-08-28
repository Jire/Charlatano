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
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.netvars.NetVarOffsets
import com.charlatano.game.offsets.ClientOffsets
import com.charlatano.utils.every
import com.charlatano.utils.uint
import it.unimi.dsi.fastutil.objects.ObjectArrayList

class GlowIterationContext {

	var myAddress = 0L
	var myTeam = 0L
	var glowObject = 0L
	var glowObjectCount = 0L
	var glowIndex = 0L
	var glowAddress = 0L
	var entityAddress = 0L

}

object GlowIteration {

	private val glowIteration = ThreadLocal.withInitial { GlowIterationContext() }
	private val bodies = ObjectArrayList<GlowIterationContext.() -> Unit>()

	private fun GlowIterationContext.load() = every(2) {
		myAddress = CSGO.clientDLL.uint(ClientOffsets.dwLocalPlayer)
		myTeam = csgoEXE.uint(myAddress + NetVarOffsets.iTeamNum)

		glowObject = CSGO.clientDLL.uint(ClientOffsets.dwGlowObject)
		glowObjectCount = CSGO.clientDLL.uint(ClientOffsets.dwGlowObject + 4)

		for (glowIndex in 0..glowObjectCount) {
			glowAddress = glowObject + (glowIndex * CSGO.GLOW_OBJECT_SIZE)
			entityAddress = csgoEXE.uint(glowAddress)

			for (body in bodies) body()
		}
	}

	fun load() = glowIteration.get().load()

	operator fun invoke(body: GlowIterationContext.() -> Unit) {
		bodies.add(body)
	}

}