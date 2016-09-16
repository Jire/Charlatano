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
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.ClientState
import com.charlatano.game.EntityType
import com.charlatano.game.entity.Entity
import com.charlatano.game.entity.Player
import com.charlatano.game.offsets.ClientOffsets.dwGlowObject
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.game.offsets.EngineOffsets.dwClientState
import com.charlatano.utils.every
import com.charlatano.utils.uint
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.*

private val entites = Object2ObjectArrayMap<EntityType, ObjectArrayList<Pair<Entity, Entity>>>(EntityType.size).apply {
	for (type in EntityType.cachedValues) put(type, ObjectArrayList<Pair<Entity, Entity>>())
}

class EntityContext(var entity: Entity, var glowAddress: Entity, var type: EntityType)

private val result = ThreadLocal.withInitial { ArrayList<EntityContext>() }

fun entitiesByType(vararg types: EntityType = arrayOf(EntityType.NULL)): List<EntityContext> {
	var types = types
	if (types.first() == EntityType.NULL) types = EntityType.cachedValues
	
	val result = result.get()
	result.clear()
	for (type in types) {
		for (list in entites[type]!!)
			result.add(EntityContext(list.first, list.second, type))
	}
	
	return result
}

fun entityByType(type: EntityType): EntityContext? = entitiesByType(type).firstOrNull()

var me: Player = 0
var clientState: ClientState = 0

fun constructEntities() = every(128) {
	me = clientDLL.uint(dwLocalPlayer)
	if (me < 0x200) return@every
	
	clientState = engineDLL.uint(dwClientState)
	
	val glowObject = clientDLL.uint(dwGlowObject)
	val glowObjectCount = clientDLL.uint(dwGlowObject + 4)
	
	for (glowIndex in 0..glowObjectCount) {
		val glowAddress = glowObject + (glowIndex * GLOW_OBJECT_SIZE)
		val entity = csgoEXE.uint(glowAddress)
		val type = EntityType.byEntityAddress(entity)
		
		val pair = Pair(entity, glowAddress)
		val list = entites[type]!!
		
		if (!list.contains(pair)) list.add(pair)
	}
}
