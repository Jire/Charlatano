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

import com.charlatano.game.CSGO.GLOW_OBJECT_SIZE
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.entity.Player
import com.charlatano.game.offsets.ClientOffsets.dwGlowObject
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.utils.every
import com.charlatano.utils.uint
import it.unimi.dsi.fastutil.objects.ObjectArrayList

class GlowIterationContext {
	
	var me: Player = 0L
	var glowObject = 0L
	var glowObjectCount = 0L
	var glowIndex = 0L
	var glowAddress = 0L
	var entity: Player = 0L
	
}

object GlowIteration {
	
	private val glowIteration = ThreadLocal.withInitial { GlowIterationContext() }
	private val bodies = ObjectArrayList<GlowIterationContext.() -> Unit>()
	
	private fun GlowIterationContext.load() = every(2) {
		me = clientDLL.uint(dwLocalPlayer)
		
		glowObject = clientDLL.uint(dwGlowObject)
		glowObjectCount = clientDLL.uint(dwGlowObject + 4)
		
		for (i in 0..glowObjectCount) {
			glowIndex = i
			glowAddress = glowObject + (i * GLOW_OBJECT_SIZE)
			entity = csgoEXE.uint(glowAddress)
			
			for (x in 0..bodies.size - 1) bodies[x]()
		}
	}
	
	fun load() = glowIteration.get().load()
	
	operator fun invoke(body: GlowIterationContext.() -> Unit) {
		bodies.add(body)
	}
	
}