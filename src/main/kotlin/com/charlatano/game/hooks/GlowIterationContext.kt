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

		for (i in 0..glowObjectCount) {
			glowIndex = i
			glowAddress = glowObject + (i * CSGO.GLOW_OBJECT_SIZE)
			entityAddress = csgoEXE.uint(glowAddress)

			for (body in bodies) body()
		}
	}

	fun load() = glowIteration.get().load()

	operator fun invoke(body: GlowIterationContext.() -> Unit) {
		bodies.add(body)
	}

}