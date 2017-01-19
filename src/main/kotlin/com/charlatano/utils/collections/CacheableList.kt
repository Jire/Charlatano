package com.charlatano.utils.collections

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
			println("Overflow $nextIndex, $capacity")
			Thread.dumpStack()
			System.exit(5)
			return -1
		}
		arr[nextIndex] = element
		size++
		return nextIndex++
	}
	
	operator fun contains(element: @UnsafeVariance E): Boolean {
		for (e in iterator()) {
			if (element == e) {
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
			if (o == null && hasNext()) {
				return next()
			}
			return o as E
		}
		
	}
	
	fun isEmpty() = size <= 0
	
	fun firstOrNull(): E? = if (isEmpty()) null else get(0)
	
}