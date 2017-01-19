@file:JvmName("Charlatano")

package com.charlatano

import com.charlatano.game.CSGO
import com.charlatano.overlay.Overlay
import com.charlatano.settings.BOX_ESP
import com.charlatano.settings.SKELETON_ESP
import java.util.*

fun main(args: Array<String>) {
	System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")
	
	CSGO.initalize()
	
	scripts()
	
	if (SKELETON_ESP or BOX_ESP) Overlay.open()
	
	Thread.sleep(10_000) // wait a bit to catch everything
	System.gc() // then cleanup
	
	val scanner = Scanner(System.`in`)
	while (!Thread.interrupted()) if (scanner.nextLine().equals("exit", true)) System.exit(0)
}