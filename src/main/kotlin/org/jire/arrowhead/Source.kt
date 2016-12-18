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

import com.sun.jna.Memory
import com.sun.jna.Pointer

/**
 * A native source which can be read from and written to with native addresses.
 */
interface Source {
	
	/**
	 * Reads at the specified native address into the specified data.
	 *
	 * @param address The native address to read from.
	 * @param data A pointer to the data to read into.
	 * @param bytesToRead The amount of bytes to read.
	 * @return Whether or not the read was successful. (`true` indicates success, `false` indicates failure.)
	 */
	fun read(address: Pointer, data: Pointer, bytesToRead: Int): Boolean
	
	/**
	 * Reads at the specified native address into the specified data.
	 *
	 * @param address The native address to read from.
	 * @param data A pointer to the data to read into.
	 * @param bytesToRead The amount of bytes to read.
	 */
	fun read(address: Long, data: Pointer, bytesToRead: Int)
			= read(PointerCache[address], data, bytesToRead)
	
	/**
	 * Reads at the specified native address into the specified data.
	 *
	 * @param address The native address to read from.
	 * @param data A pointer to the data to read into.
	 * @param bytesToRead The amount of bytes to read.
	 */
	fun read(address: Int, data: Pointer, bytesToRead: Int)
			= read(address.toLong(), data, bytesToRead)
	
	/**
	 * Reads at the specified native address into the specified memory.
	 *
	 * @param address The native address to read from.
	 * @param memory The memory to read into.
	 * @param bytesToRead The amount of bytes to read. (By default this is the size of the memory.)
	 */
	fun read(address: Long, memory: Memory, bytesToRead: Int = memory.size().toInt())
			= read(address, memory as Pointer, bytesToRead)
	
	/**
	 * Reads at the specified native address into the specified memory.
	 *
	 * @param address The native address to read from.
	 * @param memory The memory to read into.
	 * @param bytesToRead The amount of bytes to read. (By default this is the size of the memory.)
	 */
	fun read(address: Int, memory: Memory, bytesToRead: Int = memory.size().toInt())
			= read(address.toLong(), memory, bytesToRead)
	
	/**
	 * Reads at the specified native address into the specified struct.
	 *
	 * @param address The native address to read from.
	 * @param struct The struct to read into.
	 * @param bytesToRead The amount of bytes to read. (By default this is the size of the struct.)
	 */
	fun read(address: Long, struct: Struct, bytesToRead: Int = struct.size())
			= read(address, struct.pointer, bytesToRead)
	
	/**
	 * Reads at the specified native address into the specified struct.
	 *
	 * @param address The native address to read from.
	 * @param struct The struct to read into.
	 * @param bytesToRead The amount of bytes to read. (By default this is the size of the struct.)
	 */
	fun read(address: Int, struct: Struct, bytesToRead: Int = struct.size())
			= read(address.toLong(), struct, bytesToRead)
	
	/**
	 * Reads at the specified native address into a memory.
	 *
	 * @param address The native address to read from.
	 * @param bytesToRead The amount of bytes to read.
	 * @param fromCache Whether or not to use the memory cache for the supplied memory. (By default this is `true`.)
	 */
	fun read(address: Long, bytesToRead: Int, fromCache: Boolean = true): Memory? {
		val memory = if (fromCache) MemoryCache[bytesToRead] else Memory(bytesToRead.toLong())
		if (read(address, memory, bytesToRead)) return memory // read to the memory using the implementation
		return null // invalid result
	}
	
	/**
	 * Reads at the specified native address into a memory.
	 *
	 * @param address The native address to read from.
	 * @param bytesToRead The amount of bytes to read.
	 * @param fromCache Whether or not to use the memory cache for the supplied memory. (By default this is `true`.)
	 */
	fun read(address: Int, bytesToRead: Int, fromCache: Boolean = true)
			= read(address.toLong(), bytesToRead, fromCache)
	
	/**
	 * Reads a byte at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun byte(address: Long, offset: Long = 0) = read(address, 1)!!.getByte(offset)
	
	/**
	 * Reads a byte at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun byte(address: Int, offset: Long = 0) = byte(address.toLong(), offset)
	
	/**
	 * Reads a short at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun short(address: Long, offset: Long = 0) = read(address, 2)!!.getShort(offset)
	
	/**
	 * Reads a short at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun short(address: Int, offset: Long = 0) = short(address.toLong(), offset)
	
	/**
	 * Reads a char at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun char(address: Long, offset: Long = 0) = read(address, 2)!!.getChar(offset)
	
	/**
	 * Reads a char at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun char(address: Int, offset: Long = 0) = char(address.toLong(), offset)
	
	/**
	 * Reads an int at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun int(address: Long, offset: Long = 0) = read(address, 4)!!.getInt(offset)
	
	/**
	 * Reads an int at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun int(address: Int, offset: Long = 0) = int(address.toLong(), offset)
	
	/**
	 * Reads a long at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun long(address: Long, offset: Long = 0) = read(address, 8)!!.getLong(offset)
	
	/**
	 * Reads a long at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun long(address: Int, offset: Long = 0) = long(address.toLong(), offset)
	
	/**
	 * Reads a float at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun float(address: Long, offset: Long = 0) = read(address, 4)!!.getFloat(offset)
	
	/**
	 * Reads a float at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun float(address: Int, offset: Long = 0) = float(address.toLong(), offset)
	
	/**
	 * Reads a double at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun double(address: Long, offset: Long = 0) = read(address, 8)!!.getDouble(offset)
	
	/**
	 * Reads a double at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun double(address: Int, offset: Long = 0) = double(address.toLong(), offset)
	
	/**
	 * Reads a boolean at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun boolean(address: Long, offset: Long = 0) = byte(address, offset).unsign() > 0
	
	/**
	 * Reads a boolean at the specified native address, offset by the specified offset.
	 *
	 * @param address The native address to read from.
	 * @param offset The offset in bytes off the native address.
	 */
	fun boolean(address: Int, offset: Long = 0) = boolean(address.toLong(), offset)
	
	/**
	 * Writes the specified memory to the specified native address.
	 *
	 * @param address The native address to write to.
	 * @param data A pointer to the data to write to.
	 */
	fun write(address: Pointer, data: Pointer, bytesToWrite: Int): Boolean
	
	/**
	 * Writes the specified memory to the specified native address.
	 *
	 * @param address The native address to write to.
	 * @param data A pointer to the data to write to.
	 */
	fun write(address: Long, data: Pointer, bytesToWrite: Int)
			= write(PointerCache[address], data, bytesToWrite)
	
	/**
	 * Writes the specified memory to the specified native address.
	 *
	 * @param address The native address to write to.
	 * @param data A pointer to the data to write to.
	 */
	fun write(address: Int, data: Pointer, bytesToWrite: Int) = write(address.toLong(), data, bytesToWrite)
	
	/**
	 * Writes the specified memory to the specified native address.
	 *
	 * @param address The native address to write to.
	 * @param memory The memory to write.
	 * @param bytesToWrite The amount of bytes to write of the memory. (By default this is the size of the memory.)
	 */
	fun write(address: Long, memory: Memory, bytesToWrite: Int = memory.size().toInt())
			= write(address, memory as Pointer, bytesToWrite)
	
	/**
	 * Writes the specified memory to the specified native address.
	 *
	 * @param address The native address to write to.
	 * @param memory The memory to write.
	 * @param bytesToWrite The amount of bytes to write of the memory. (By default this is the size of the memory.)
	 */
	fun write(address: Int, memory: Memory, bytesToWrite: Int = memory.size().toInt())
			= write(address.toLong(), memory, bytesToWrite)
	
	/**
	 * Writes the specified struct to the specified native address.
	 *
	 * @param address The native address to write to.
	 * @param struct The struct to write.
	 * @param bytesToWrite The amount of bytes to write of the struct. (By default this is the size of the struct.)
	 */
	fun write(address: Long, struct: Struct, bytesToWrite: Int = struct.size())
			= write(address, struct.pointer, bytesToWrite)
	
	/**
	 * Writes the specified struct to the specified native address.
	 *
	 * @param address The native address to write to.
	 * @param struct The struct to write.
	 * @param bytesToWrite The amount of bytes to write of the struct. (By default this is the size of the struct.)
	 */
	fun write(address: Int, struct: Struct, bytesToWrite: Int = struct.size())
			= write(address.toLong(), struct, bytesToWrite)
	
	/**
	 * Writes at the specified native address to the specified byte value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the byte to write.
	 */
	operator fun set(address: Long, value: Byte) = write(address, 1) {
		setByte(0, value)
	}
	
	/**
	 * Writes at the specified native address to the specified byte value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the byte to write.
	 */
	operator fun set(address: Int, value: Byte) = set(address.toLong(), value)
	
	/**
	 * Writes at the specified native address to the specified short value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the short to write.
	 */
	operator fun set(address: Long, value: Short) = write(address, 2) {
		setShort(0, value)
	}
	
	/**
	 * Writes at the specified native address to the specified short value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the short to write.
	 */
	operator fun set(address: Int, value: Short) = set(address.toLong(), value)
	
	/**
	 * Writes at the specified native address to the specified char value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the char to write.
	 */
	operator fun set(address: Long, value: Char) = write(address, 2) {
		setChar(0, value)
	}
	
	/**
	 * Writes at the specified native address to the specified char value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the char to write.
	 */
	operator fun set(address: Int, value: Char) = set(address.toLong(), value)
	
	/**
	 * Writes at the specified native address to the specified int value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the int to write.
	 */
	operator fun set(address: Long, value: Int) = write(address, 4) {
		setInt(0, value)
	}
	
	/**
	 * Writes at the specified native address to the specified int value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the int to write.
	 */
	operator fun set(address: Int, value: Int) = set(address.toLong(), value)
	
	/**
	 * Writes at the specified native address to the specified long value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the long to write.
	 */
	operator fun set(address: Long, value: Long) = write(address, 8) {
		setLong(0, value)
	}
	
	/**
	 * Writes at the specified native address to the specified long value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the long to write.
	 */
	operator fun set(address: Int, value: Long) = set(address.toLong(), value)
	
	/**
	 * Writes at the specified native address to the specified float value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the float to write.
	 */
	operator fun set(address: Long, value: Float) = write(address, 4) {
		setFloat(0, value)
	}
	
	/**
	 * Writes at the specified native address to the specified float value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the float to write.
	 */
	operator fun set(address: Int, value: Float) = set(address.toLong(), value)
	
	/**
	 * Writes at the specified native address to the specified double value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the double to write.
	 */
	operator fun set(address: Long, value: Double) = write(address, 8) {
		setDouble(0, value)
	}
	
	/**
	 * Writes at the specified native address to the specified double value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the double to write.
	 */
	operator fun set(address: Int, value: Double) = set(address.toLong(), value)
	
	/**
	 * Writes at the specified native address to the specified boolean value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the boolean to write.
	 */
	operator fun set(address: Long, value: Boolean) = set(address, (if (value) 1 else 0).toByte())
	
	/**
	 * Writes at the specified native address to the specified boolean value.
	 *
	 * @param address The native address to write to.
	 * @param value The value of the boolean to write.
	 */
	operator fun set(address: Int, value: Boolean) = set(address.toLong(), value)
	
}