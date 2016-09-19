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

import com.charlatano.game.EntityContext


@Suppress("UNCHECKED_CAST")
class EntityContainer : CacheableList<EntityContext>(0, 256) {
	
	private val lists = CacheableList<CacheableList<EntityContext>>(0, 10)
	
	fun addList(list: CacheableList<EntityContext>) = lists.add(list)
	
	override fun clear() {
		super.clear()
		lists.clear()
	}
	
	fun collect() = apply {
		lists.forEach {
			it.forEach { add(it) }
			it.clean()
		}
	}
	
	fun needsUpdate(): Boolean {
		if (lists.size() == 0) return true
		lists.forEach {
			if (it.isDirty()) return true
		}
		return false
	}
	
}