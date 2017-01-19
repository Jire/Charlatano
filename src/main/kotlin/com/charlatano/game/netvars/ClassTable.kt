package com.charlatano.game.netvars

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.utils.extensions.readable
import com.charlatano.utils.extensions.toNetVarString
import com.charlatano.utils.extensions.uint
import org.jire.arrowhead.Addressed
import kotlin.LazyThreadSafetyMode.NONE

internal class ClassTable(override val address: Long, val offset: Long = 16) : Addressed {

	val name by lazy(NONE) {
		val bytes = ByteArray(64)

		val memoryAddress = csgoEXE.uint(address + 12)
		val memory = csgoEXE.read(memoryAddress, bytes.size)!!
		memory.read(0, bytes, 0, bytes.size)

		bytes.toNetVarString()
	}

	val propCount by lazy(NONE) { csgoEXE.int(address + 4) }

	fun propForID(id: Int) = csgoEXE.uint(address) + id * 60

	fun readable() = csgoEXE.read(address, offset.toInt()).readable()

}