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
	
	bunnyHop()
	rcs()
	esp()
	flatAim()
	pathAim()
	boneTrigger()
	reducedFlash()
	bombTimer()
	
	Toggles_AIM()
	Toggles_BUNNYHOP()
	Toggles_ESP()
	Toggles_RAGE()
	Toggles_RCS()
	Toggles_TRIGGER()
	
	Thread.sleep(10_000) // wait a bit to catch everything
	System.gc() // then cleanup
	
	clearScreen()
	
	val scanner = Scanner(System.`in`)
	while (!Thread.interrupted()) {
		System.out.println()
		System.out.print("> ")
		when (scanner.nextLine()) {
			"exit", "quit", "e", "q" -> System.exit(0)
			"reload", "r" -> loadSettings()
			"reset" -> resetToggles()
			"toggles", "t" -> printToggles()
			"cls", "clear", "c" -> clearScreen()
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
	
	System.out.println("Loaded settings.")
	
	val needsOverlay = ENABLE_BOMB_TIMER or (ENABLE_ESP and (SKELETON_ESP or BOX_ESP))
	if (!Overlay.opened && needsOverlay) Overlay.open()
}

private fun resetToggles() {
	toggleAIM = true
	toggleRCS = true
	toggleESP = true
	toggleBunnyHop = true
	toggleTrigger = true
	
	toggleRage = false
	System.out.println("All togglables enabled, Rage disabled.")
}

private fun printToggles(){
	System.out.println("AIM      = " + toggleAIM)
	System.out.println("BunnyHop = " + toggleBunnyHop)
	System.out.println("ESP      = " + toggleESP)
	System.out.println("Rage     = " + toggleRage)
	System.out.println("RCS      = " + toggleRCS)
	System.out.println("Trigger  = " + toggleTrigger)
}

private fun clearScreen() {
	repeat(512) { _ ->
		System.out.print("\n")
	}
	System.out.println("   Command     | Alias  | Function");
	System.out.println("  =============+========+=========================")
	System.out.println(" | clear       | cls, c | clears console screen   |")
	System.out.println(" | exit / quit | e, q   | stop CS:PRO             |")
	System.out.println(" | reload      | r      | reloads /settings       |")
	System.out.println(" | reset       |        | sets toggles to default |")
	System.out.println(" | toggles     | t      | show what is toggled    |")
	System.out.println("  =============+========+=========================")
	System.out.println()
}