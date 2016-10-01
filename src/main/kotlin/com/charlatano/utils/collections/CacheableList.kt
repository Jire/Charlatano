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

package com.charlatano.utils.collections

@Suppress("UNCHECKED_CAST")
open class CacheableList<out E>(val minIndex: Int, val capacity: Int) {
	
	private var arr = arrayOfNulls<Any>(capacity)
	
	private var size = 0
	private var highest: Int = 0
	private var dirty = false
	
	constructor(capacity: Int) : this(0, capacity)
	
	operator fun get(index: Int) = arr[index] as E
	
	operator fun set(index: Int, element: @UnsafeVariance E?): E {
		val previous = arr[index]
		if (previous == element) return previous as E
		
		arr[index] = element
		if (previous == null && element != null) {
			size++
			if (highest < index) {
				highest = index
			}
		} else if (previous != null && element == null) {
			size--
			if (highest == index) {
				highest--
			}
		}
		dirty = true
		return previous as E
	}
	
	fun add(element: @UnsafeVariance E): Int {
		val index = nextIndex()
		set(index, element)
		return index
	}
	
	fun remove(element: @UnsafeVariance E) {
		for (i in minIndex..highest) {
			if (element!!.equals(arr[i])) {
				set(i, null)
				return
			}
		}
	}
	
	operator fun contains(element: @UnsafeVariance E): Boolean {
		for (e in iterator()) {
			if (element!!.equals(e)) {
				return true
			}
		}
		
		return false
	}
	
	inline fun forEach(action: (E) -> Unit): Unit {
		for (e in iterator()) {
			if (e != null)
				action(e)
		}
	}
	
	open fun clear() {
		for (i in minIndex..arr.size - 1)
			arr[i] = null
		size = 0
		dirty = true
	}
	
	fun size() = size
	
	fun isDirty() = dirty
	
	fun clean() = apply { dirty = false }
	
	fun nextIndex(): Int {
		for (i in minIndex..arr.size - 1) {
			if (null == arr[i]) {
				return i
			}
		}
		throw IllegalStateException("Out of indices!")
	}
	
	operator fun iterator(): Iterator<E> {
		iterator.pointer = minIndex
		return iterator
	}
	
	private val iterator = IndexerIterator()
	
	private inner class IndexerIterator : Iterator<E> {
		
		var pointer: Int = 0
		
		override fun hasNext() = size > 0 && pointer <= highest
		
		override fun next(): E {
			val o = arr[pointer++]
			if (o == null && hasNext()) {
				return next()
			}
			return o as E
		}
		
		fun remove() = set(pointer, null)
		
	}
	
	fun isEmpty() = size <= 0
	
}