package com.charlatano.game.offsets

import com.charlatano.utils.RepeatedInt
import it.unimi.dsi.fastutil.bytes.ByteArrayList
import org.jire.arrowhead.Module

internal class ModuleScan(private val module: Module, private val patternOffset: Long,
                          private val addressOffset: Long, private val read: Boolean,
                          private val subtract: Boolean) {
	
	operator fun invoke(vararg mask: Any): Offset {
		val bytes = ByteArrayList()
		
		for (flag in mask) when (flag) {
			is Number -> bytes.add(flag.toByte())
			is RepeatedInt -> repeat(flag.repeats) { bytes.add(flag.value.toByte()) }
		}
		
		return Offset(module, patternOffset, addressOffset, read, subtract, bytes.toByteArray())
	}
	
}