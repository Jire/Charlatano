@file:JvmName("Charlatano")

package com.charlatano

import com.charlatano.game.CSGO
import com.charlatano.overlay.Overlay
import com.charlatano.scripts.bunnyHop
import com.charlatano.scripts.esp.esp
import com.charlatano.scripts.fovAim
import com.charlatano.scripts.rcs
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
	
	//Dojo("scripts")
	scripts()
	if (SKELETON_ESP or BOX_ESP) Overlay.open()
	
	Thread.sleep(10_000) // wait a bit to catch everything
	System.gc() // then cleanup
	
	val scanner = Scanner(System.`in`)
	while (!Thread.interrupted()) if (scanner.nextLine().equals("exit", true)) System.exit(0)
}

/**
 * Invoke the scripts you'd like to use below.
 *
 * If a script is commented (has a "//" before it), it is not enabled.
 */
fun scripts() {
	bunnyHop()
	rcs()
	esp()
	fovAim()
	//fovTrigger()
	//noFlash()
	//bombTimer()
}