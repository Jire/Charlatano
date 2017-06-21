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

package com.charlatano.overlay

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.CSGO.gameX
import com.charlatano.game.CSGO.gameY
import com.charlatano.overlay.transparency.TransparencyApplier
import com.charlatano.overlay.transparency.win10.Win10TransparencyApplier
import com.charlatano.overlay.transparency.win7.Win7TransparencyApplier
import com.charlatano.settings.OPENGL_FPS
import com.charlatano.settings.OPENGL_MSAA_SAMPLES
import com.charlatano.settings.OPENGL_VSYNC
import com.charlatano.utils.randLong
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef

object Overlay {
	
	@Volatile var opened = false
	
	lateinit var hwnd: WinDef.HWND
	
	fun open() = LwjglApplicationConfiguration().apply {
		width = gameWidth
		height = gameHeight
		title = randLong(Long.MAX_VALUE).toString()
		x = gameX
		y = gameY
		resizable = false
		fullscreen = false
		vSyncEnabled = OPENGL_VSYNC
		if (OPENGL_MSAA_SAMPLES > 0)
			samples = OPENGL_MSAA_SAMPLES
		
		foregroundFPS = OPENGL_FPS
		backgroundFPS = OPENGL_FPS
		
		LwjglApplication(CharlatanoOverlay, this)
		
		do {
			val hwnd = User32.INSTANCE.FindWindow(null, title)
			if (hwnd != null) {
				Overlay.hwnd = hwnd
				break
			}
			Thread.sleep(64) // decreased so it won't go black as long
		} while (!Thread.interrupted())
		
		// sets up window to be fullscreen, click-through, etc.
		WindowCorrector.setupWindow(hwnd)
		
		
		// sets up the full transparency of the Window (only Windows 7 and 10 can do this)
		val transparencyApplier: TransparencyApplier =
				if (System.getProperty("os.name").contains("windows 10", ignoreCase = true))
					Win10TransparencyApplier
				else
					Win7TransparencyApplier // will only work on Windows 7 or early Windows 10 builds
		transparencyApplier.applyTransparency(hwnd)
		
		opened = true
	}
	
	init {
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")
	}
	
}