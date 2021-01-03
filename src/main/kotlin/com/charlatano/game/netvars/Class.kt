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

package com.charlatano.game.netvars

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.utils.extensions.uint
import org.jire.kna.Addressed
import kotlin.LazyThreadSafetyMode.NONE

internal class Class(override val address: Long) : Addressed {
	
	val id by lazy(NONE) { csgoEXE.uint(address + 20) }
	
	val next by lazy(NONE) { csgoEXE.uint(address + 16) }
	
	val table by lazy(NONE) { csgoEXE.uint(address + 12) }
	
	fun readable() = address != 0L && csgoEXE.readPointer(address, 40).readable()
	
}