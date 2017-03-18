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
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.game.offsets.EngineOffsets.dwClientState
import com.charlatano.game.offsets.EngineOffsets.dwInGame
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.overlay.CharlatanoOverlay.camera
import com.charlatano.overlay.Overlay
import com.charlatano.settings.CLASSIC_OFFENSIVE
import com.charlatano.utils.every
import com.charlatano.utils.extensions.uint
import com.charlatano.utils.natives.CUser32
import com.charlatano.utils.paused
import com.charlatano.utils.retry
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import org.jire.arrowhead.Module
import org.jire.arrowhead.Process
import org.jire.arrowhead.processByName

object CSGO {
	
	const val ENTITY_SIZE = 16
	const val GLOW_OBJECT_SIZE = 56
	
	lateinit var csgoEXE: Process
		private set
	
	lateinit var clientDLL: Module
		private set
	lateinit var engineDLL: Module
		private set
	lateinit var scaleFormDLL: Module
		private set
	
	var gameHeight: Int = 0
		private set
	
	var gameX: Int = 0
		private set
	
	var gameWidth: Int = 0
		private set
	
	var gameY: Int = 0
		private set
	
	fun initalize() {
		retry(128) { csgoEXE = processByName("csgo.exe")!! }
		retry(128) {
			csgoEXE.loadModules()
			
			clientDLL = csgoEXE.modules["client.dll"]!!
			engineDLL = csgoEXE.modules["engine.dll"]!!
			scaleFormDLL = csgoEXE.modules["scaleformui.dll"]!!
		}
		
		val rect = WinDef.RECT()
		val hwd = CUser32.FindWindowA(null, "Counter-Strike: "
				+ (if (CLASSIC_OFFENSIVE) "Classic" else "Global") + " Offensive")
		
		var lastWidth = 0
		var lastHeight = 0
		var lastX = 0
		var lastY = 0
		
		every(1000) {
			if (!CUser32.GetClientRect(hwd, rect)) System.exit(2)
			gameWidth = rect.right - rect.left
			gameHeight = rect.bottom - rect.top
			
			if (!CUser32.GetWindowRect(hwd, rect)) System.exit(3)
			gameX = rect.left + (((rect.right - rect.left) - gameWidth) / 2)
			gameY = rect.top + ((rect.bottom - rect.top) - gameHeight)
			
			if ((lastX != gameX || lastY != gameY) && Overlay.hwnd != null)
				User32.INSTANCE.MoveWindow(Overlay.hwnd, gameX, gameY, gameWidth, gameHeight, false)
			
			if (CharlatanoOverlay.created && (lastWidth != gameWidth || lastHeight != gameHeight))
				camera.setToOrtho(true, gameWidth.toFloat(), gameHeight.toFloat())
			
			lastWidth = gameWidth
			lastHeight = gameHeight
			lastX = gameX
			lastY = gameY
		}
		
		every(1024, continuous = true) {
			paused = CUser32.GetForegroundWindow() != hwd
			if (paused) return@every
		}
		
		NetVars.load()
		
		retry(16) {
			var myAddress = clientDLL.uint(dwLocalPlayer)
			if (myAddress <= 0) {
				dwLocalPlayer = dwLocalPlayer + 0x1C // can't do dwLocalPlayer += 0x1C because of compiler bug...
				myAddress = clientDLL.uint(dwLocalPlayer)
			}
			
			val enginePointer = engineDLL.uint(dwClientState)
			val inGame = csgoEXE.int(enginePointer + dwInGame) == 6
			paused = !inGame || myAddress <= 0
		}
		
		constructEntities()
	}
	
}