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

fun main(args: Array<String>) {
	System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")
	
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
			return@main
		}
	}
	
	CSGO.initalize()
	
	if (Dojo["ENABLE_BUNNY_HOP"]) bunnyHop()
	if (Dojo["ENABLE_RCS"]) rcs()
	val esp: Boolean = Dojo["ENABLE_ESP"]
	if (esp) esp()
	if (Dojo["ENABLE_AIM"]) fovAim()
	if (Dojo["ENABLE_TRIGGER"]) fovTrigger()
	if (Dojo["ENABLE_NO_FLASH"]) noFlash()
	val bombTimer: Boolean = Dojo["ENABLE_BOMB_TIMER"]
	if (bombTimer) bombTimer()
	
	if (bombTimer or ((SKELETON_ESP or BOX_ESP) and esp)) Overlay.open()
	
	Thread.sleep(10_000) // wait a bit to catch everything
	System.gc() // then cleanup
	
	val scanner = Scanner(System.`in`)
	while (!Thread.interrupted()) if (scanner.nextLine().equals("exit", true)) System.exit(0)
}