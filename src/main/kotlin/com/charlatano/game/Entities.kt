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
import com.charlatano.game.entity.bone
import com.charlatano.utils.Angle
import com.charlatano.utils.collections.CacheableList
import com.charlatano.utils.collections.ListContainer
import com.charlatano.utils.readCached
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import java.util.*

@Volatile
var me: Player = 0
@Volatile
var clientState: ClientState = 0

typealias EntityContainer = ListContainer<EntityContext>
typealias EntityList = Object2ObjectArrayMap<EntityType, CacheableList<EntityContext>>

private val cachedResults = Int2ObjectArrayMap<EntityContainer>(EntityType.size)

val entitiesValues = arrayOfNulls<CacheableList<EntityContext>>(MAX_ENTITIES)
var entitiesValuesCounter = 0

val entities: Object2ObjectMap<EntityType, CacheableList<EntityContext>>
		= EntityList(EntityType.size).apply {
	for (type in EntityType.cachedValues) {
		val cacheableList = CacheableList<EntityContext>(MAX_ENTITIES)
		put(type, cacheableList)
		entitiesValues[entitiesValuesCounter++] = cacheableList
	}
}

fun entityByType(type: EntityType): EntityContext? = entities[type]?.firstOrNull()

internal inline fun forEntities(types: Array<EntityType> = EntityType.cachedValues,
                                crossinline body: (EntityContext) -> Boolean): Boolean {
	val hash = Arrays.hashCode(types)
	val container = cachedResults.get(hash) ?: EntityContainer(EntityType.size)
	
	if (container.empty()) {
		for (type in types) if (type != EntityType.NULL) {
			val cacheableList = entities[type]!!
			container.addList(cacheableList)
			cachedResults.put(hash, container)
		}
	}
	
	return container.forEach(body)
}

val entityToBones: Long2ObjectMap<Angle> = Long2ObjectOpenHashMap()

fun Entity.bones(boneID: Int) = readCached(entityToBones) {
	x = bone(0xC, boneID)
	y = bone(0x1C, boneID)
	z = bone(0x2C, boneID)
}