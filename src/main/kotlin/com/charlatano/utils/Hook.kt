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

package com.charlatano.utils

import com.charlatano.utils.every
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

class Hook(val clauseDefault: Boolean, val durationDefault: Long,
           val durationUnitDefault: TimeUnit,
           val predicate: () -> Boolean) {

	operator inline fun invoke(clause: Boolean = clauseDefault,
	                           duration: Long = durationDefault,
	                           durationUnit: TimeUnit = durationUnitDefault,
	                           crossinline body: () -> Unit) {
		if (!clause) every(duration, durationUnit) {
			if (predicate()) body()
		} else if (predicate()) body()
	}

}

fun hook(durationDefault: Long = 8, durationUnitDefault: TimeUnit = MILLISECONDS,
         clauseDefault: Boolean = false, predicate: () -> Boolean)
		= Hook(clauseDefault, durationDefault, durationUnitDefault, predicate)