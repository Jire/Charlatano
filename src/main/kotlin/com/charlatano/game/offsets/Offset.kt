/*
 * Charlatano: Free and open-source (FOSS) cheat for CS:GO/CS:CO
 * Copyright (C) 2017 - Thomas G. P. Nappo, Jonathan Beaudoin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.charlatano.game.offsets

import com.charlatano.utils.extensions.uint
import com.sun.jna.Memory
import com.sun.jna.Pointer
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import org.jire.kna.Addressed
import org.jire.kna.attach.AttachedModule
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.reflect.KProperty

class Offset(
	val module: AttachedModule, val patternOffset: Long, val addressOffset: Long,
	val read: Boolean, val subtract: Boolean, val mask: ByteArray
) : Addressed {
	
	companion object {
		val memoryByModule = Object2ObjectArrayMap<AttachedModule, Memory>()
	}
	
	val memory = memoryByModule[module]
		?: module.read(0, module.size)!!.apply {
			memoryByModule[module] = this
		}
	
	override val address by lazy(NONE) {
		val offset = module.size - mask.size
		
		var currentAddress = 0L
		while (currentAddress < offset) {
			if (memory.mask(currentAddress, mask)) {
				currentAddress += module.address + patternOffset
				if (read) currentAddress = module.process.uint(currentAddress)
				if (subtract) currentAddress -= module.address
				return@lazy currentAddress + addressOffset
			}
			currentAddress++
		}
		
		throw IllegalStateException("Failed to resolve offset")
	}
	
	private var value = -1L
	
	operator fun getValue(thisRef: Any?, property: KProperty<*>): Long {
		if (value == -1L)
			value = address
		return value
	}
	
	operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
		this.value = value
	}
	
}

fun Pointer.mask(offset: Long, mask: ByteArray, skipZero: Boolean = true): Boolean {
	for (i in 0..mask.lastIndex) {
		val value = mask[i]
		if (skipZero && 0 == value.toInt()) continue
		if (value != getByte(offset + i))
			return false
	}
	return true
}