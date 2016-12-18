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

import com.sun.jna.Memory
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap

/**
 * Fast memory caching using a fast array map.
 *
 * Do not use results after they have been reused.
 */
object MemoryCache {
	
	/**
	 * The maximum size of a memory in bytes to cache.
	 */
	const val CACHE_BYTE_MAX = 128
	
	/**
	 * The resource map cache, mapping size in bytes to memory.
	 */
	private val map = ThreadLocal.withInitial { Int2ObjectArrayMap<Memory>(64) }
	
	/**
	 * Returns a zeroed-out memory of the specified size in bytes.
	 *
	 * If the size meets the cached size limit, it will be reused.
	 *
	 * @param size The desired amount of bytes of the memory.
	 * @param clear Whether or not to clear (zero-out) the returned memory. (By default this is `false`.)
	 */
	operator fun get(size: Int, clear: Boolean = false): Memory {
		val map = map.get()
		
		var memory = map.get(size)
		if (memory == null) {
			memory = Memory(size.toLong())
			if (size <= CACHE_BYTE_MAX)
				map.put(size, memory)
		} else if (clear) memory.clear()
		return memory
	}
	
}