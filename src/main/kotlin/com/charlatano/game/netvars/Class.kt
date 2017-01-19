package com.charlatano.game.netvars

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.utils.extensions.readable
import com.charlatano.utils.extensions.uint
import org.jire.arrowhead.Addressed
import kotlin.LazyThreadSafetyMode.NONE

internal class Class(override val address: Long) : Addressed {

	val id by lazy(NONE) { csgoEXE.uint(address + 20) }

	val next by lazy(NONE) { csgoEXE.uint(address + 16) }

	val table by lazy(NONE) { csgoEXE.uint(address + 12) }

	fun readable() = csgoEXE.read(address, 40).readable()

}