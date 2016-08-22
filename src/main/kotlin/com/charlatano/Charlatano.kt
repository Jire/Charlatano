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

import co.paralleluniverse.strands.Strand.sleep
import com.charlatano.netvars.NetVars
import com.charlatano.script.scripts.bunnyHop
import com.charlatano.script.scripts.esp
import org.jire.arrowhead.processByName

fun main(args: Array<String>) {
	while (!Thread.interrupted()) {
		val process = processByName("csgo.exe")
		if (process != null) {
			csgoEXE = process
			break
		}
		sleep(3000) // wait 3 seconds before reattempt
	}

	// initialize the modules
	clientDLL = csgoEXE.modules["client.dll"]!!
	engineDLL = csgoEXE.modules["engine.dll"]!!

	NetVars.map

	bunnyHop()
	esp()

	sleep(Long.MAX_VALUE)
}