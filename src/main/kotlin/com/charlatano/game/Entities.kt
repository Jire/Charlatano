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

package com.charlatano.game

import com.charlatano.settings.MAX_ENTITIES
import com.charlatano.game.entity.Entity
import com.charlatano.game.entity.EntityType
import com.charlatano.game.entity.Player
import com.charlatano.utils.collections.CacheableList
import com.charlatano.utils.collections.ListContainer
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps
import java.util.*

var me: Player = 0
var clientState: ClientState = 0

typealias EntityContainer = ListContainer<EntityContext>
typealias EntityList = Object2ObjectArrayMap<EntityType, CacheableList<EntityContext>>

private val cachedResults = Int2ObjectMaps.synchronize(Int2ObjectArrayMap<EntityContainer>(EntityType.size))

val entities: Object2ObjectMap<EntityType, CacheableList<EntityContext>>
		= Object2ObjectMaps.synchronize(EntityList(EntityType.size)).apply {
	for (type in EntityType.cachedValues) put(type, CacheableList<EntityContext>(MAX_ENTITIES))
}

fun entityByType(type: EntityType): EntityContext? = entities[type]?.firstOrNull()

internal inline fun forEntities(vararg types: EntityType = EntityType.cachedValues, body: (EntityContext) -> Unit) {
	val hashcode = Arrays.hashCode(types)
	val container = cachedResults.get(hashcode) ?: EntityContainer(EntityType.size)
	
	if (container.empty()) {
		for (type in types) if (type != EntityType.NULL) {
			container.addList(entities[type]!!)
			cachedResults.put(hashcode, container)
		}
	}
	
	container.forEach(body)
}

data class EntityContext(var entity: Entity = -1, var glowAddress: Entity = -1,
                         var glowIndex: Int = -1, var type: EntityType = EntityType.NULL) {
	
	fun set(entity: Entity, glowAddress: Entity, glowIndex: Int, type: EntityType) = apply {
		this.entity = entity
		this.glowAddress = glowAddress
		this.glowIndex = glowIndex
		this.type = type
	}
	
}