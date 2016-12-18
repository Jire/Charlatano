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

package org.jire.arrowhead

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import kotlin.reflect.KClass

/**
 * Caching and more for [Struct]s.
 */
object StructCache {
	
	/**
	 * The caching map which maps types to their struct pool.
	 */
	val map: MutableMap<Class<*>, Struct> = Object2ObjectArrayMap<Class<*>, Struct>()
	
	/**
	 * Gets (and constructs, if necessary) a struct of the specified type using the provided arguments.
	 *
	 * @param type The desired type of the struct.
	 * @param args The arguments to pass to the constructor of the struct.
	 */
	operator inline fun <reified T : Struct> get(type: Class<*>, vararg args: Any): T {
		var struct = map[type]
		if (struct == null) {
			struct = (if (args.size > 0) {
				val types = arrayOfNulls<Class<*>>(args.size)
				type.declaredFields.forEachIndexed { i, field -> types[i] = field.type }
				val constructor = type.getDeclaredConstructor(*types)
				constructor.newInstance(*args)
			} else type.newInstance()) as T
			map[type] = struct
		}
		return struct as T
	}
	
	/**
	 * Gets (and constructs, if necessary) a struct of the specified type using the provided arguments.
	 *
	 * @param type The desired type of the struct.
	 * @param args The arguments to pass to the constructor of the struct.
	 */
	operator inline fun <reified T : Struct> get(type: KClass<T>, vararg args: Any): T = get(type.java, *args)
	
}

/**
 * Gets (and constructs, if necessary) a struct of the type using the provided arguments.
 *
 * @param args The arguments to pass to the constructor of the struct.
 */
inline operator fun <reified T : Struct> KClass<T>.get(vararg args: Any)
		= StructCache.get(this, *args) // Explosions can't be passed to get operator... a bug perhaps?