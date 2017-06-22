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

@file:JvmName("Charlatano")

package com.charlatano

import com.charlatano.game.CSGO
import com.charlatano.overlay.Overlay
import com.charlatano.scripts.*
import com.charlatano.scripts.aim.flatAim
import com.charlatano.scripts.aim.pathAim
import com.charlatano.scripts.esp.esp
import com.charlatano.settings.BOX_ESP
import com.charlatano.settings.ENABLE_BOMB_TIMER
import com.charlatano.settings.ENABLE_ESP
import com.charlatano.settings.SKELETON_ESP
import com.charlatano.utils.Dojo
import java.io.File
import java.io.FileReader
import java.util.*

const val SETTINGS_DIRECTORY = "settings"

fun main(args: Array<String>) {
	System.setProperty("kotlin.compiler.jar", "kotlin-compiler.jar")
	
	loadSettings()
	
	CSGO.initialize()
	toggesp()
	bunnyHop()
	rcs()
	esp()
	flatAim()
	pathAim()
	boneTrigger()
	reducedFlash()
	bombTimer()
	
	Thread.sleep(10_000) // wait a bit to catch everything
	System.gc() // then cleanup
	
	val scanner = Scanner(System.`in`)
	while (!Thread.interrupted()) {
		when (scanner.nextLine()) {
			"exit", "quit" -> System.exit(0)
			"reload" -> loadSettings()
		}
	}
}

private fun loadSettings() {
	File(SETTINGS_DIRECTORY).listFiles().forEach {
		FileReader(it).use {
			Dojo.script(it
					.readLines()
					.joinToString("\n"))
		}
	}
	
	val needsOverlay = ENABLE_BOMB_TIMER or (ENABLE_ESP and (SKELETON_ESP or BOX_ESP))
	if (!Overlay.opened && needsOverlay) Overlay.open()
}