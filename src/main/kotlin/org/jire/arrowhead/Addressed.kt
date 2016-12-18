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

/**
 * Something that has an address and whose address can be used as a base to offset another.
 */
interface Addressed {
	
	/**
	 * The address.
	 */
	val address: Long
	
	/**
	 * Offsets the base address by the specified offset.
	 *
	 * @param offset The offset in bytes off the base address.
	 */
	fun offset(offset: Long) = address + offset
	
	/**
	 * Offsets the base address by the specified offset.
	 *
	 * @param offset The offset in bytes off the base address.
	 */
	fun offset(offset: Int) = offset(offset.toLong())
	
}