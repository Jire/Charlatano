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

package com.charlatano.utils.collections

import kotlin.system.exitProcess

@Suppress("UNCHECKED_CAST")
class CacheableList<out E>(val capacity: Int, val minIndex: Int = 0) {
	
	private val arr = arrayOfNulls<Any>(capacity)
	
	var size = 0
		private set
	
	var nextIndex = 0
		private set
	
	operator fun get(index: Int) = arr[index] as E
	
	fun add(element: @UnsafeVariance E): Int {
		if (nextIndex >= capacity) {
			Thread.dumpStack()
			exitProcess(5)
		}
		arr[nextIndex] = element
		size++
		return nextIndex++
	}
	
	operator fun contains(element: @UnsafeVariance E): Boolean {
		for (e in iterator()) {
			if (element === e) {
				return true
			}
		}
		return false
	}
	
	inline fun forEach(crossinline action: (E) -> Boolean): Boolean {
		for (e in iterator()) {
			if (e !== null) {
				if (action(e)) {
					return true
				}
			}
		}
		return false
	}
	
	fun clear() {
		size = 0
		nextIndex = 0
	}
	
	fun size() = size
	
	operator fun iterator(): Iterator<E> {
		iterator.pointer = minIndex
		return iterator
	}
	
	private val iterator = IndexerIterator()
	
	private inner class IndexerIterator : Iterator<E> {
		
		var pointer: Int = 0
		
		override fun hasNext() = size > 0 && pointer < nextIndex
		
		override fun next(): E {
			val o = arr[pointer++]
			if (o === null && hasNext()) {
				return next()
			}
			return o as E
		}
		
	}
	
	fun isEmpty() = size <= 0
	
	fun firstOrNull(): E? = if (isEmpty()) null else get(0)
	
}