package com.charlatano.game.netvars

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.utils.extensions.toNetVarString
import com.charlatano.utils.extensions.uint
import org.jire.arrowhead.Addressed
import kotlin.LazyThreadSafetyMode.NONE

internal class ClassVariable(override val address: Long, val addressOffset: Long) : Addressed {

	val resolvedAddress by lazy(NONE) { csgoEXE.uint(address) }

	val name by lazy(NONE) {
		val bytes = ByteArray(32)

		val memory = csgoEXE.read(resolvedAddress, bytes.size)!!
		memory.read(0, bytes, 0, bytes.size)

		bytes.toNetVarString()
	}

	val table by lazy(NONE) { csgoEXE.uint(address + 0x28) }

	val offset by lazy(NONE) { addressOffset + csgoEXE.uint(address + 0x2C) }

	val type by lazy(NONE) { csgoEXE.uint(address + 0x4) }

	val elements by lazy(NONE) { csgoEXE.uint(address + 0x34) }

	val stringBufferCount by lazy(NONE) { csgoEXE.uint(address + 0xC) }

}