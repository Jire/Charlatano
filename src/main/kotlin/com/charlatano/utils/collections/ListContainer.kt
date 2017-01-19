package com.charlatano.utils.collections


@Suppress("UNCHECKED_CAST")
class ListContainer<E>(capacity: Int) {
	
	private val lists = CacheableList<CacheableList<E>>(capacity)
	
	fun addList(list: CacheableList<E>) = lists.add(list)
	
	fun clear() = lists.clear()
	
	fun empty() = lists.size() == 0
	
	internal inline fun forEach(action: (E) -> Unit): Unit {
		lists.forEach {
			it.forEach {
				action(it)
			}
		}
	}
	
	fun <E> firstOrNull() = lists[0][0] as E
	
}

internal inline operator fun <E> ListContainer<E>.invoke(action: (E) -> Unit) = this.forEach { action(it) }