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

package com.charlatano.game

import com.charlatano.game.hooks.constructEntities
import com.charlatano.game.netvars.NetVars
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.overlay.CharlatanoOverlay.camera
import com.charlatano.overlay.Overlay
import com.charlatano.settings.*
import com.charlatano.utils.every
import com.charlatano.utils.inBackground
import com.charlatano.utils.natives.CUser32
import com.charlatano.utils.retry
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import org.jire.kna.attach.Attach
import org.jire.kna.attach.windows.WindowsAttachAccess
import org.jire.kna.attach.windows.WindowsAttachedModule
import org.jire.kna.attach.windows.WindowsAttachedProcess
import org.jire.kna.cached.CachedReadableSource
import kotlin.system.exitProcess

object CSGO {
	
	const val ENTITY_SIZE = 16
	const val GLOW_OBJECT_SIZE = 56
	
	lateinit var csgoEXE: WindowsAttachedProcess
		private set
	
	lateinit var clientDLL: WindowsAttachedModule
		private set
	lateinit var engineDLL: WindowsAttachedModule
		private set
	
	var gameHeight: Int = 0
		private set
	
	var gameX: Int = 0
		private set
	
	var gameWidth: Int = 0
		private set
	
	var gameY: Int = 0
		private set
	
	fun initialize() {
		retry(128) {
			csgoEXE = Attach.byName(PROCESS_NAME, WindowsAttachAccess(PROCESS_ACCESS_FLAGS)) {
				set(CachedReadableSource.CACHE_EXPIRATION_MILLIS, 512L / SERVER_TICK_RATE)
			} as WindowsAttachedProcess
		}
		retry(128) {
			val modules = csgoEXE.modules
			modules.attach(csgoEXE)
			clientDLL = modules.byName(CLIENT_MODULE_NAME) as WindowsAttachedModule
			engineDLL = modules.byName(ENGINE_MODULE_NAME) as WindowsAttachedModule
		}
		val rect = WinDef.RECT()
		val hwd = CUser32.FindWindowA(
			null, "Counter-Strike: "
					+ (if (CLASSIC_OFFENSIVE) "Classic" else "Global") + " Offensive - Direct3D 9"
		)
		
		var lastWidth = 0
		var lastHeight = 0
		var lastX = 0
		var lastY = 0
		
		every(1000) {
			if (!CUser32.GetClientRect(hwd, rect)) exitProcess(2)
			gameWidth = rect.right - rect.left
			gameHeight = rect.bottom - rect.top
			
			if (!CUser32.GetWindowRect(hwd, rect)) exitProcess(3)
			gameX = rect.left + (((rect.right - rect.left) - gameWidth) / 2)
			gameY = rect.top + ((rect.bottom - rect.top) - gameHeight)
			
			if (Overlay.opened && (lastX != gameX || lastY != gameY))
				User32.INSTANCE.MoveWindow(Overlay.hwnd, gameX, gameY, gameWidth, gameHeight, false)
			
			if (Overlay.opened && CharlatanoOverlay.created && (lastWidth != gameWidth || lastHeight != gameHeight))
				camera.setToOrtho(true, gameWidth.toFloat(), gameHeight.toFloat())
			
			lastWidth = gameWidth
			lastHeight = gameHeight
			lastX = gameX
			lastY = gameY
		}
		
		every(1024, continuous = true) {
			inBackground = Pointer.nativeValue(hwd.pointer) != CUser32.GetForegroundWindow()
		}
		
		NetVars.load()
		constructEntities()
	}
	
}