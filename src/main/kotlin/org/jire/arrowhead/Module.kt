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
 * Remember reading `Process`' documentation? No..? Well go read it first.
 *
 * Anyway, a module is a native library linked (usually dynamically like with a DLL) to a process.
 *
 * This type doubles as an `Addressed` which handles the offsetting for its `Source` operations.
 * All of the `Source` operations are done using its parent process.
 */
interface Module : Source, Addressed {
	
	/**
	 * The process of which this module belongs to.
	 */
	val process: Process
	
	/**
	 * The name of the module.
	 */
	val name: String
	
	/**
	 * The size of the module in bytes.
	 */
	val size: Long
	
	override fun read(address: Pointer, data: Pointer, bytesToRead: Int)
			= process.read(address, data, bytesToRead)
	
	override fun write(address: Pointer, data: Pointer, bytesToWrite: Int)
			= process.write(address, data, bytesToWrite)
	
	override fun read(address: Long, data: Pointer, bytesToRead: Int)
			= process.read(offset(address), data, bytesToRead)
	
	override fun write(address: Long, data: Pointer, bytesToWrite: Int)
			= process.write(offset(address), data, bytesToWrite)
	
}