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

import org.jire.arrowhead.Addressed
import com.charlatano.csgoEXE
import com.charlatano.util.uint

internal class Class(override val address: Long) : Addressed {

	val id by lazy { csgoEXE.uint(address + 20) }

	val next by lazy { csgoEXE.uint(address + 16) }

	val table by lazy { csgoEXE.uint(address + 12) }

	fun readable(): Boolean = try {
		csgoEXE.read(address, 40, false)
		true
	} catch (t: Throwable) {
		false
	}

}