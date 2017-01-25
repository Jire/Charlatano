package com.charlatano.game.hooks

import com.charlatano.settings.CLEANUP_TIME
import com.charlatano.settings.MAX_ENTITIES
import com.charlatano.game.CSGO.GLOW_OBJECT_SIZE
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.EntityContext
import com.charlatano.game.clientState
import com.charlatano.game.entities
import com.charlatano.game.entity.EntityType
import com.charlatano.game.me
import com.charlatano.game.offsets.ClientOffsets.dwGlowObject
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.game.offsets.EngineOffsets.dwClientState
import com.charlatano.utils.every
import com.charlatano.utils.extensions.uint
import java.util.concurrent.atomic.AtomicLong

private val lastCleanup = AtomicLong(0L)

private val contexts = Array(MAX_ENTITIES) { EntityContext() }

private fun shouldReset() = (System.currentTimeMillis() - lastCleanup.get()) >= CLEANUP_TIME

private fun reset() {
	entities.forEach { _, cacheableList ->
		cacheableList.clear()
		lastCleanup.set(System.currentTimeMillis())
	}
}

fun constructEntities() = every(512) {
	me = clientDLL.uint(dwLocalPlayer)
	if (me <= 0) return@every
	
	clientState = engineDLL.uint(dwClientState)
	
	val glowObject = clientDLL.uint(dwGlowObject)
	val glowObjectCount = clientDLL.int(dwGlowObject + 4)
	
	if (shouldReset()) reset()
	
	for (glowIndex in 0..glowObjectCount) {
		val glowAddress = glowObject + (glowIndex * GLOW_OBJECT_SIZE)
		val entity = csgoEXE.uint(glowAddress)
		val type = EntityType.byEntityAddress(entity)
		
		val context = contexts[glowIndex].set(entity, glowAddress, glowIndex, type)
		
		with(entities[type]!!) {
			if (!contains(context)) add(context)
		}
	}
}