@file:JvmName("Charlatano")

package com.charlatano

import com.charlatano.game.CSGO
import com.charlatano.overlay.Overlay
import com.charlatano.scripts.*
import com.charlatano.scripts.esp.esp
import com.charlatano.settings.BOX_ESP
import com.charlatano.settings.SKELETON_ESP
import com.charlatano.utils.Dojo
import java.io.File
import java.io.FileReader
import java.util.*

const val SETTINGS_DIRECTORY = "settings"

private fun loadSettings() {
	File(SETTINGS_DIRECTORY).listFiles().forEach {
		try {
			FileReader(it).use {
				Dojo.script(it
						.readLines()
						.filter { !it.startsWith("/") && !it.startsWith(" *//**//*") && !it.startsWith(" *") }
						.joinToString("\n"))
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}

fun main(args: Array<String>) {
	System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")
	
	loadSettings()
	
	CSGO.initalize()
	
	val esp: Boolean = Dojo["ENABLE_ESP"]
	val bombTimer: Boolean = Dojo["ENABLE_BOMB_TIMER"]
	
	if (bombTimer or (esp and (SKELETON_ESP or BOX_ESP))) Overlay.open()
	
	if (Dojo["ENABLE_BUNNY_HOP"]) bunnyHop()
	if (Dojo["ENABLE_RCS"]) rcs()
	if (esp) esp()
	if (Dojo["ENABLE_AIM"]) fovAim()
	if (Dojo["ENABLE_TRIGGER"]) fovTrigger()
	if (Dojo["ENABLE_REDUCED_FLASH"]) reducedFlash()
	if (bombTimer) bombTimer()
	
	Thread.sleep(10_000) // wait a bit to catch everything
	System.gc() // then cleanup
	
	val scanner = Scanner(System.`in`)
	while (!Thread.interrupted()) {
		when (scanner.nextLine()) {
			"exit", "quit" -> System.exit(0)
			"reload" -> {
				// TODO
			}
		}
	}
}