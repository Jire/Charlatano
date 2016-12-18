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

package org.jire.arrowhead

import com.sun.jna.Pointer

/**
 * Lock-free caching for [Pointer][com.sun.jna.Pointer]s.
 */
object PointerCache {
	
	/**
	 * Thread-local reusable pointer.
	 */
	private val pointer = ThreadLocal.withInitial { Pointer(0) }
	
	/**
	 * Returns a pointer of the specified address.
	 *
	 * The pointer should be used immediately as it will be reused.
	 *
	 * @param address The native address for the returned pointer.
	 */
	operator fun get(address: Long): Pointer {
		val pointer = pointer.get()
		Pointer.nativeValue(pointer, address)
		return pointer
	}
	
}