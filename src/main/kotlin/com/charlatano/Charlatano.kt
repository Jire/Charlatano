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
import com.charlatano.netvars.NetVars
import com.charlatano.script.scripts.bunnyHop
import com.charlatano.script.scripts.esp
import com.charlatano.util.retry
import org.jire.arrowhead.processByName

fun main(args: Array<String>) {
	retry { csgoEXE = processByName("csgo.exe")!! }
	retry {
		csgoEXE.loadModules()

		clientDLL = csgoEXE.modules["client.dll"]!!
		engineDLL = csgoEXE.modules["engine.dll"]!!
	}

	NetVars.load()

	// enable plugins

	bunnyHop()
	esp()

	Strand.sleep(Long.MAX_VALUE) // prevent process exit
}