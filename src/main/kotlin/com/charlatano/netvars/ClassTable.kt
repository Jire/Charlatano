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

import com.charlatano.csgoEXE
import com.charlatano.util.readable
import com.charlatano.util.uint
import org.jire.arrowhead.Addressed
import kotlin.LazyThreadSafetyMode.NONE

internal class ClassTable(override val address: Long, val offset: Long = 16) : Addressed {

	val name by lazy(NONE) {
		val bytes = ByteArray(64)

		val memoryAddress = csgoEXE.uint(address + 12)
		val memory = csgoEXE.read(memoryAddress, bytes.size)!!
		memory.read(0, bytes, 0, bytes.size)

		bytes.toNetVarString()
	}

	val propCount by lazy(NONE) { csgoEXE.int(address + 4) }

	fun propForID(id: Int) = csgoEXE.uint(address) + id * 60

	fun readable() = csgoEXE.read(address, offset.toInt()).readable()

}