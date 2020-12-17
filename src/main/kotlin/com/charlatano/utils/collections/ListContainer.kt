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

@Suppress("UNCHECKED_CAST")
class ListContainer<E>(capacity: Int) {
	
	private val lists = CacheableList<CacheableList<E>>(capacity)
	
	fun addList(list: CacheableList<E>) = lists.add(list)
	
	fun clear() = lists.clear()
	
	fun empty() = lists.size() == 0
	
	internal inline fun forEach(crossinline action: (E) -> Boolean): Boolean {
		return lists.forEach {
			it.forEach {
				action(it)
			}
		}
	}
	
	fun <E> firstOrNull() = lists[0][0] as E
	
}

internal inline operator fun <E> ListContainer<E>.invoke(
	crossinline action: (E) -> Boolean
) = forEach { action(it) }