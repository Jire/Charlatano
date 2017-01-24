package com.charlatano.overlay

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.CSGO.gameX
import com.charlatano.game.CSGO.gameY
import com.charlatano.settings.OPENGL_FPS
import com.charlatano.settings.OPENGL_MSAA_SAMPLES
import com.charlatano.settings.OPENGL_VSYNC
import com.charlatano.utils.randLong
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef

object Overlay {
	
	var hwnd: WinDef.HWND? = null
	
	fun open() {
		val cfg = LwjglApplicationConfiguration()
		cfg.width = gameWidth
		cfg.height = gameHeight
		cfg.title = randLong(Long.MAX_VALUE).toString()
		cfg.x = gameX
		cfg.y = gameY
		cfg.resizable = false
		cfg.fullscreen = false
		cfg.vSyncEnabled = OPENGL_VSYNC
		if (OPENGL_MSAA_SAMPLES > 0)
			cfg.samples = OPENGL_MSAA_SAMPLES
		
		cfg.foregroundFPS = OPENGL_FPS
		cfg.backgroundFPS = OPENGL_FPS
		
		LwjglApplication(CharlatanoOverlay, cfg)
		
		do {
			hwnd = User32.INSTANCE.FindWindow(null, cfg.title)
			Thread.sleep(512)
		} while (hwnd == null)
		WindowTools.transparentWindow(hwnd!!)
	}
	
}