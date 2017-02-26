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

package com.charlatano.utils

class Hook(val clauseDefault: Boolean, val durationDefault: Int,
           val predicate: () -> Boolean) {
	
	operator inline fun invoke(clause: Boolean = clauseDefault,
	                           duration: Int = durationDefault,
	                           crossinline body: () -> Unit) {
		if (!clause) every(duration) {
			if (predicate()) body()
		} else if (predicate()) body()
	}
	
}

fun hook(durationDefault: Int = 8, clauseDefault: Boolean = false, predicate: () -> Boolean)
		= Hook(clauseDefault, durationDefault, predicate)