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

package com.charlatano.offsets

import com.sun.jna.Memory
import org.jire.arrowhead.Addressed
import org.jire.arrowhead.Module
import com.charlatano.util.uint
import java.nio.ByteBuffer
import java.nio.ByteOrder.LITTLE_ENDIAN
import kotlin.reflect.KProperty
import kotlin.text.Charsets.UTF_8

class Offset(val module: Module, val patternOffset: Long, val addressOffset: Long,
             val read: Boolean, val subtract: Boolean, val mask: ByteArray) : Addressed {

	val memory by lazy { module.read(0, module.size.toInt(), fromCache = false) }

	override val address by lazy {
		val offset = module.size - mask.size

		var currentAddress = 0L
		while (currentAddress < offset) {
			if (mask(memory, currentAddress, mask)) {
				currentAddress += module.address + patternOffset
				if (read) currentAddress = module.process.uint(currentAddress)
				if (subtract) currentAddress -= module.address
				return@lazy currentAddress + addressOffset
			}
			currentAddress++
		}

		throw IllegalStateException("Failed to resolve offset")
	}

	operator fun getValue(thisRef: Any?, property: KProperty<*>) = address

	private fun mask(memory: Memory, offset: Long, mask: ByteArray): Boolean {
		for (i in mask.indices) {
			val value = mask[i]
			if (0.toByte() != value && value != memory.getByte(offset + i))
				return false
		}
		return true
	}

}

class ModuleScan(private val module: Module, private val mask: ByteArray) {
	fun scan(patternOffset: Long = 0, addressOffset: Long = 0,
	         read: Boolean = true, subtract: Boolean = true)
			= Offset(module, patternOffset, addressOffset, read, subtract, mask)
}

operator fun Module.invoke(vararg mask: Int) = ModuleScan(this, mask.map { it.toByte() }.toByteArray())

fun Module.offset(patternOffset: Long = 0, addressOffset: Long = 0,
                  read: Boolean = true, subtract: Boolean = true, className: String)
		= Offset(this, patternOffset, addressOffset, read, subtract, className.toByteArray(UTF_8))

fun Module.offset(patternOffset: Long = 0, addressOffset: Long = 0,
                  read: Boolean = true, subtract: Boolean = true, offset: Long)
		= Offset(this, patternOffset, addressOffset, read, subtract,
		ByteBuffer.allocate(4).order(LITTLE_ENDIAN).putInt(offset.toInt()).array())