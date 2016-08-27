/*
 * Charlatan is a premium CS:GO cheat ran on the JVM.
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

package com.charlatano.netvars

import com.charlatano.offsets.m_dwFirstClass
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import kotlin.LazyThreadSafetyMode.NONE

object NetVars {

	fun load() {
		map
	}

	internal val map: Map<Int, ClassOffset> by lazy(NONE) {
		val map = Int2ObjectArrayMap<ClassOffset>(20000) // Cover us for a while with 20K

		var clientClass = Class(m_dwFirstClass)
		while (clientClass.readable()) {
			val table = ClassTable(clientClass.table)
			if (table.readable()) scanTable(map, table, 0, table.name)
			clientClass = Class(clientClass.next)
		}

		map
	}

	private fun scanTable(netVars: MutableMap<Int, ClassOffset>, table: ClassTable, offset: Long, name: String) {
		for (i in 0..table.propCount - 1) {
			val prop = ClassVariable(table.propForID(i), offset)
			if (!Character.isDigit(prop.name[0])) {
				if (!prop.name.contains("baseclass")) {
					val netVar = ClassOffset(name, prop.name, prop.offset)
					netVars.put(hashClassAndVar(netVar.className, netVar.variableName), netVar)
				}

				val child = prop.table
				if (0L != child) scanTable(netVars, ClassTable(child), prop.offset, name)
			}
		}
	}

}

internal fun hashClassAndVar(className: String, varName: String) = className.hashCode() xor varName.hashCode()