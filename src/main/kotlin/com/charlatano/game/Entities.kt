/*
 * Charlatano is a premium CS:GO cheat ran on the JVM.
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

package com.charlatano.game

import com.charlatano.game.entity.Entity
import com.charlatano.game.entity.EntityType
import com.charlatano.game.entity.Player
import com.charlatano.utils.collections.CacheableList
import com.charlatano.utils.collections.EntityContainer
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import java.util.*

var me: Player = 0
var clientState: ClientState = 0

fun entitiesByType(vararg types: EntityType = arrayOf(EntityType.NULL)): CacheableList<EntityContext> {
	var types = types
	if (types.first() == EntityType.NULL) types = EntityType.cachedValues
	
	val hashcode = Arrays.hashCode(types)
	val container = cachedResults.get(hashcode) ?: EntityContainer()
	if (!container.needsUpdate()) return container
	
	container.clear() //TODO bug if we clean CCSPlayer it might not call collect for the other cached results
	
	for (type in types) container.addList(entities[type.hashCode()]!!)
	
	cachedResults.put(hashcode, container.collect())
	return container
}

fun entityByType(type: EntityType): EntityContext? = entitiesByType(type)[0]

val entities = Int2ObjectArrayMap<CacheableList<EntityContext>>(EntityType.size).apply {
	for (type in EntityType.cachedValues) put(type.hashCode(), CacheableList<EntityContext>(0, 256))
}

private val cachedResults = Int2ObjectArrayMap<EntityContainer>(EntityType.size)

val contexts = Array(1024) { EntityContext() }

class EntityContext {
	
	var entity: Entity = -1
	var glowAddress: Entity = -1
	var glowIndex: Int = -1
	var type: EntityType = EntityType.NULL
	
	fun set(entity: Entity, glowAddress: Entity, glowIndex: Int, type: EntityType) = apply {
		this.entity = entity
		this.glowAddress = glowAddress
		this.glowIndex = glowIndex
		this.type = type
	}
	
}