/*
 * Charlatano is a premium CS:GO cheat ran on the JVM.
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
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.offsets.ClientOffsets.dwSensitivity
import com.charlatano.overlay.Overlay
import com.charlatano.scripts.bunnyHop
import com.charlatano.scripts.esp
import com.charlatano.scripts.fovAim
import com.charlatano.scripts.rcs
import java.util.*

fun main(args: Array<String>) {
	
	System.setProperty("co.paralleluniverse.fibers.detectRunawayFibers", "false")
	System.setProperty("co.paralleluniverse.fibers.verifyInstrumentation", "false")
	System.setProperty("co.paralleluniverse.fibers.DefaultFiberPool.parallelism", "1")
	
	CSGO.initalize()
	
	// -- START OF SCRIPTS -- //
	bunnyHop()
	esp()
	rcs()
	//noFlash()
	//bombTimer()
	fovAim()
	// -- END OF SCRIPTS -- //
	
	println(dwSensitivity)
	val sens_ptr = clientDLL.float(0xAAB004)
	val sens_value_xored = clientDLL.float(0xAAAFD8)
	val Value = (sens_ptr.toInt() xor sens_value_xored.toInt()).toFloat()
	
	println(sens_ptr)
	println(sens_value_xored)
	println(Value)
	if (OPENGL_GUI)
		Overlay.open()
	
	Strand.sleep(5000) // wait a bit to catch everything
	System.gc() // then cleanup
	
	val scanner = Scanner(System.`in`)
	while (!Thread.interrupted()) {
		if (scanner.nextLine().equals("exit", true))
			System.exit(0)
	}
}