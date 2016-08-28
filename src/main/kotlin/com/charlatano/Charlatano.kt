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

@file:JvmName("Charlatano")

package com.charlatano

import co.paralleluniverse.strands.Strand
import com.charlatano.game.CSGO
import com.charlatano.game.hooks.GlowIteration
import com.charlatano.scripts.bunnyHop
import com.charlatano.scripts.esp

fun main(args: Array<String>) {
	CSGO.initalize()

	// -- START OF SCRIPTS -- //
	bunnyHop()
	esp()
	// -- END OF SCRIPTS -- //

	// -- START OF HOOKS -- //
	GlowIteration.load()
	// -- END OF HOOKS -- //

	Strand.sleep(3000) // wait a bit to catch everything
	System.gc() // then cleanup
	Strand.sleep(Long.MAX_VALUE) // prevent exit
}