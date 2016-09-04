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

package com.charlatano.game.offsets

import com.charlatano.utils.uint
import com.sun.jna.Memory
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import org.jire.arrowhead.Addressed
import org.jire.arrowhead.Module
import kotlin.reflect.KProperty

class Offset(val module: Module, val patternOffset: Long, val addressOffset: Long,
             val read: Boolean, val subtract: Boolean, val mask: ByteArray) : Addressed {
	
	companion object {
		val memoryByModule = Object2ObjectArrayMap<Module, Memory>()
		
		private fun Offset.cachedMemory(): Memory {
			var memory = memoryByModule[module]
			if (memory == null) {
				memory = module.read(0, module.size.toInt(), fromCache = false)!!
				memoryByModule.put(module, memory)
			}
			return memory
		}
	}
	
	val memory = cachedMemory()
	
	override val address by lazy(LazyThreadSafetyMode.NONE) {
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