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

import com.sun.jna.Structure

/**
 * Represents a native struct, with the ability to be reused via [StructCache].
 *
 * All fields which are to be used as native members must be annotated with [@JvmField][kotlin.jvm.JvmField].
 */
abstract class Struct : Structure() {
	
	/**
	 * Whether or not the struct has been released.
	 */
	@Volatile var released = false
		private set
	
	/**
	 * Releases this struct back into the caching pool.
	 *
	 * After the struct is released you should no longer use it.
	 *
	 * You should not hold a reference to structs you have released.
	 */
	fun release() = apply {
		if (released) throw IllegalStateException("You must renew the struct before releasing it!")
		
		StructCache.map.put(javaClass, this)
		released = true
	}
	
	/**
	 * Reads at the specified native address into this struct.
	 *
	 * @param source The source to read from.
	 * @param address The native address to read at.
	 */
	fun read(source: Source, address: Long) = apply {
		if (!released) throw IllegalStateException("You must release the struct before renewing it!")
		
		source.read(address, this)
		released = false
	}
	
	/**
	 * Reads at the specified native address into this struct.
	 *
	 * @param source The source to read from.
	 * @param address The native address to read at.
	 */
	fun read(source: Source, address: Int) = read(source, address.toLong())
	
	/**
	 * Writes this struct to the specified native address.
	 *
	 * @param source The source to read from.
	 * @param address The native address to write at.
	 */
	fun write(source: Source, address: Long) = apply {
		source.write(address, this)
	}
	
	/**
	 * Writes this struct to the specified native address.
	 *
	 * @param source The source to read from.
	 * @param address The native address to write at.
	 */
	fun write(source: Source, address: Int) = write(source, address.toLong())
	
	override fun getFieldOrder(): List<String> = javaClass.declaredFields.map { it.name }
	
}