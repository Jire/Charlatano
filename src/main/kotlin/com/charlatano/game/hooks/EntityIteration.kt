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

package com.charlatano.game.hooks

import com.charlatano.game.*
import com.charlatano.game.CSGO.GLOW_OBJECT_SIZE
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.entity.EntityType
import com.charlatano.game.offsets.ClientOffsets
import com.charlatano.game.offsets.ClientOffsets.dwGlowObject
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.game.offsets.EngineOffsets
import com.charlatano.game.offsets.EngineOffsets.dwClientState
import com.charlatano.settings.*
import com.charlatano.utils.every
import com.charlatano.utils.extensions.uint
import com.charlatano.utils.extensions.writeForced
import com.charlatano.utils.notInGame
import com.sun.jna.platform.win32.WinNT
import org.jire.kna.Pointer
import org.jire.kna.int
import java.util.concurrent.atomic.AtomicLong
import kotlin.properties.Delegates

private val lastCleanup = AtomicLong(0L)

private val contexts = Array(MAX_ENTITIES) { EntityContext() }

private fun shouldReset() = System.currentTimeMillis() - lastCleanup.get() >= CLEANUP_TIME

private fun reset() {
	for (cacheableList in entitiesValues)
		cacheableList?.clear()
	lastCleanup.set(System.currentTimeMillis())
}

private val writeGlowMemory = ThreadLocal.withInitial { Pointer.alloc(1).apply { setByte(0, 0xEB.toByte()) } }

// Credits to Mr.Noad
private var state by Delegates.observable(SignOnState.MAIN_MENU) { _, old, new ->
	if (old != new) {
		notInGame = if (new == SignOnState.IN_GAME) {
			if (GLOW_ESP && FLICKER_FREE_GLOW && PROCESS_ACCESS_FLAGS and WinNT.PROCESS_VM_OPERATION > 0) {
				clientDLL.writeForced(ClientOffsets.dwGlowUpdate, writeGlowMemory.get(), 1)
			}
			if (GARBAGE_COLLECT_ON_MAP_START) {
				System.gc()
			}
			false
		} else {
			true
		}
	}
}

@Volatile
var cursorEnable = false

private val cursorEnableAddress by lazy(LazyThreadSafetyMode.NONE) { clientDLL.address + ClientOffsets.dwMouseEnable }
private val cursorEnablePtr by lazy(LazyThreadSafetyMode.NONE) { clientDLL.address + ClientOffsets.dwMouseEnablePtr }

fun constructEntities() = every(512) {
	state = SignOnState[csgoEXE.int(clientState + EngineOffsets.dwSignOnState)]
	cursorEnable = csgoEXE.int(cursorEnableAddress) xor cursorEnablePtr.toInt() != 1
	
	me = clientDLL.uint(dwLocalPlayer)
	if (me <= 0) return@every
	
	clientState = engineDLL.uint(dwClientState)
	
	var dzMode = false
	
	val glowObject = clientDLL.uint(dwGlowObject)
	val glowObjectCount = clientDLL.int(dwGlowObject + 4)
	
	if (shouldReset()) reset()
	
	for (glowIndex in 0..glowObjectCount) {
		val glowAddress = glowObject + (glowIndex * GLOW_OBJECT_SIZE)
		val entity = csgoEXE.uint(glowAddress)
		if (entity == 0L) continue
		val type = EntityType.byEntityAddress(entity)
		
		if (type == EntityType.CFists) {
			// sometimes it takes a while for game to initialize gameRulesProxy
			// so our dz mode detection wasn't working perfectly.
			dzMode = true
		}
		
		val context = contexts[glowIndex].set(entity, glowAddress, glowIndex, type)
		
		with(entities[type]!!) {
			if (!contains(context)) add(context)
		}
	}
	DANGER_ZONE = dzMode
}