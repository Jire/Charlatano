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

package com.charlatano.game.netvars

import com.charlatano.game.netvars.NetVars.hashClassAndVar
import com.charlatano.game.netvars.NetVars.map
import kotlin.reflect.KProperty

class NetVar(val className: String, var varName: String?, val offset: Int, val index: Int) {
	
	private var value = -1L
	
	operator fun getValue(thisRef: Any?, property: KProperty<*>): Long {
		if (varName == null) varName = "m_${property.name}" + if (index < 0) "" else "[$index]"
		if (value == -1L) value = map[hashClassAndVar(className, varName!!)]!!.offset + offset
		return value
	}
	
	operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
		this.value = value
	}
	
}

fun netVar(className: String, varName: String? = null, offset: Int = 0, index: Int = -1)
		= NetVar(className, if (varName != null && index >= 0) "$varName[$index]" else varName, offset, index)