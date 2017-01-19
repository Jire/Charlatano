package com.charlatano.utils.extensions

import com.charlatano.game.offsets.ModuleScan
import com.charlatano.game.offsets.Offset
import org.jire.arrowhead.Module
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal operator fun Module.invoke(patternOffset: Long = 0, addressOffset: Long = 0,
                                    read: Boolean = true, subtract: Boolean = true)
		= ModuleScan(this, patternOffset, addressOffset, read, subtract)

internal operator fun Module.invoke(patternOffset: Long = 0, addressOffset: Long = 0,
                                    read: Boolean = true, subtract: Boolean = true, className: String)
		= Offset(this, patternOffset, addressOffset, read, subtract, className.toByteArray(Charsets.UTF_8))

internal operator fun Module.invoke(patternOffset: Long = 0, addressOffset: Long = 0,
                                    read: Boolean = true, subtract: Boolean = true, offset: Long)
		= Offset(this, patternOffset, addressOffset, read, subtract,
		ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(offset.toInt()).array())