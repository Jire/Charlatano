/*
 * Charlatano is a premium CS:GO cheat ran on the JVM.
 * Copyright (C) 2016 - Thomas Nappo, Jonathan Beaudoin
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
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.*

var me: Player = 0
var clientState: ClientState = 0

fun entitiesByType(vararg types: EntityType = arrayOf(EntityType.NULL)): CacheableList<EntityContext> {
	var types = types
	if (types.first() == EntityType.NULL) types = EntityType.cachedValues

	val hashcode = Arrays.hashCode(types)
	if (cachedResults.containsKey(hashcode)) {
		val list = cachedResults.get(hashcode)
		if ((System.currentTimeMillis() - list.lastUpdate) <= 10000) {
			return list
		}
	}
	val result = CacheableList<EntityContext>(10)

	for (type in types)
		for (list in entities[type.hashCode()]!!)
			result.add(contexts[list.glowIndex].set(list.entity, list.glowAddress, type))


	cachedResults.put(hashcode, result.update())

	return result
}

fun entityByType(type: EntityType): EntityContext? = entitiesByType(type).firstOrNull()

val entities = Int2ObjectArrayMap<ObjectArrayList<Triple>>(EntityType.size).apply {
	for (type in EntityType.cachedValues) put(type.hashCode(), ObjectArrayList<Triple>())
}

private val cachedResults = Int2ObjectArrayMap<CacheableList<EntityContext>>(EntityType.size)
private val contexts = Array(1024) { EntityContext() }

class EntityContext {

	var entity: Entity = -1
	var glowAddress: Entity = -1
	var type: EntityType = EntityType.NULL

	fun set(entity: Entity, glowAddress: Entity, type: EntityType) = apply {
		this.entity = entity
		this.glowAddress = glowAddress
		this.type = type
	}

}

class Triple {

	var entity: Entity = -1
	var glowAddress: Entity = -1
	var glowIndex: Int = -1

	fun set(entity: Entity, glowAddress: Entity, glowIndex: Int) = apply {
		this.entity = entity
		this.glowAddress = glowAddress
		this.glowIndex = glowIndex
	}

}