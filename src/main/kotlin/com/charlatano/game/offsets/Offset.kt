package com.charlatano.game.offsets

import com.charlatano.utils.extensions.uint
import com.sun.jna.Memory
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import org.jire.arrowhead.Addressed
import org.jire.arrowhead.Module
import kotlin.LazyThreadSafetyMode.NONE
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
	
	override val address by lazy(NONE) {
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