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

package com.charlatano

import com.charlatano.game.CSGO
import com.charlatano.overlay.Overlay
import com.charlatano.scripts.aim.flatAim
import com.charlatano.scripts.aim.pathAim
import com.charlatano.scripts.bombTimer
import com.charlatano.scripts.boneTrigger
import com.charlatano.scripts.bunnyHop
import com.charlatano.scripts.esp.esp
import com.charlatano.scripts.rcs
import com.charlatano.settings.*
import com.sun.jna.platform.win32.WinNT
import java.io.File
import java.util.*
import kotlin.script.experimental.jsr223.KotlinJsr223DefaultScriptEngineFactory
import kotlin.system.exitProcess

object Charlatano {
	
	const val SETTINGS_DIRECTORY = "settings"
	
	@JvmStatic
	fun main(args: Array<String>) {
		System.setProperty("jna.nosys", "true")
		System.setProperty("idea.io.use.fallback", "true")
		System.setProperty("idea.use.native.fs.for.win", "false")
		
		loadSettings()
		
		if (FLICKER_FREE_GLOW) {
			PROCESS_ACCESS_FLAGS = PROCESS_ACCESS_FLAGS or
					//required by FLICKER_FREE_GLOW
					WinNT.PROCESS_VM_OPERATION
		}
		
		if (LEAGUE_MODE) {
			GLOW_ESP = false
			BOX_ESP = false
			SKELETON_ESP = false
			ENABLE_ESP = false
			
			ENABLE_BOMB_TIMER = false
			ENABLE_REDUCED_FLASH = false
			ENABLE_FLAT_AIM = false
			
			SERVER_TICK_RATE = 128 // most leagues are 128-tick
			CACHE_EXPIRE_MILLIS = 4
			
			PROCESS_ACCESS_FLAGS = WinNT.PROCESS_QUERY_INFORMATION or WinNT.PROCESS_VM_READ // all we need
			GARBAGE_COLLECT_ON_MAP_START = true // get rid of traces
		}
		
		CSGO.initialize()
		
		bunnyHop()
		rcs()
		esp()
		flatAim()
		pathAim()
		boneTrigger()
		bombTimer()
		
		val scanner = Scanner(System.`in`)
		while (!Thread.interrupted()) {
			when (scanner.nextLine()) {
				"exit", "quit" -> exitProcess(0)
				"reload" -> loadSettings()
			}
		}
	}
	
	private fun loadSettings() {
		val ef = KotlinJsr223DefaultScriptEngineFactory()
		val se = ef.scriptEngine
		
		val strings = File(SETTINGS_DIRECTORY).listFiles()!!.map { file -> file.readText() }
		for (string in strings) se.eval(string)
		
		val needsOverlay = ENABLE_BOMB_TIMER or (ENABLE_ESP and (SKELETON_ESP or BOX_ESP))
		if (!Overlay.opened && needsOverlay) Overlay.open()
	}
	
}