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
import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.CSGO.gameX
import com.charlatano.game.CSGO.gameY
import com.charlatano.game.hooks.constructEntities
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.scripts.*

fun main(args: Array<String>) {
	System.setProperty("co.paralleluniverse.fibers.detectRunawayFibers", "false")
	System.setProperty("co.paralleluniverse.fibers.DefaultFiberPool.parallelism", "1")
	//System.setProperty("co.paralleluniverse.fibers.verifyInstrumentation", "true")

	CSGO.initalize()

	constructEntities()//DO NOT DELETE

	// -- START OF SCRIPTS -- //
	bunnyHop()
	esp()
	rcs()
	noFlash()
	bombTimer()
	aim()
	// -- END OF SCRIPTS -- //
	
	println("$gameWidth, $gameHeight, $gameX, $gameY")
	CharlatanoOverlay.open(gameWidth, gameHeight, gameX, gameY)

	Strand.sleep(5000) // wait a bit to catch everything
	System.gc() // then cleanup
	Strand.sleep(Long.MAX_VALUE) // prevent exit
}