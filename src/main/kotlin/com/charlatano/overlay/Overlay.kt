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

package com.charlatano.overlay

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.CSGO.gameX
import com.charlatano.game.CSGO.gameY
import com.charlatano.utils.nextLong
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import java.awt.GraphicsEnvironment

object Overlay {
	
	var hwnd: WinDef.HWND? = null
	
	fun open() {
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")
		val cfg = LwjglApplicationConfiguration()
		cfg.width = gameWidth
		cfg.height = gameHeight
		cfg.title = nextLong(Long.MAX_VALUE).toString()
		cfg.x = gameX
		cfg.y = gameY
		cfg.resizable = false
		cfg.fullscreen = false
		cfg.vSyncEnabled = true
		cfg.samples = 8

		var fps = 60
		for (screen in GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices) {
			val screenFPS = screen.displayMode.refreshRate
			if (screenFPS > fps) fps = screenFPS
		}
		cfg.foregroundFPS = fps
		cfg.backgroundFPS = fps

		LwjglApplication(CharlatanoOverlay, cfg)

		do {
			hwnd = User32.INSTANCE.FindWindow(null, cfg.title)
			Thread.sleep(512)
		} while (hwnd == null)
		WindowTools.transparentWindow(hwnd!!)
	}

}