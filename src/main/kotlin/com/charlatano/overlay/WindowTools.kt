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

import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.CSGO.gameX
import com.charlatano.game.CSGO.gameY
import com.charlatano.utils.natives.DWM
import com.charlatano.utils.natives.DWM_BLURBEHIND
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32.INSTANCE as User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinUser.*

internal object WindowTools {
	
	private val DWM_BB_ENABLE = WinDef.DWORD(0x00000001)
	private val DWM_BB_BLURREGION = WinDef.DWORD(0x00000002)
	private val DWM_BB_TRANSITIONONMAXIMIZED = WinDef.DWORD(0x00000004)
	
	val HWND_TOPPOS = WinDef.HWND(Pointer(-1))
	
	val SWP_NOSIZE = 0x0001
	val SWP_NOMOVE = 0x0002
	
	private val WS_EX_TOOLWINDOW = 0x00000080
	private val WS_EX_APPWINDOW = 0x00040000
	
	fun transparentWindow(hwnd: WinDef.HWND): Boolean {
		DWM_BLURBEHIND().apply {
			dwFlags = DWM_BB_ENABLE
			fEnable = true
			hRgnBlur = null
			DWM.DwmEnableBlurBehindWindow(hwnd, this)
		}
		
		return with(User32) {
			var wl = GetWindowLong(hwnd, GWL_EXSTYLE)
			wl = wl or WS_EX_LAYERED or WS_EX_TRANSPARENT
			SetWindowLong(hwnd, GWL_EXSTYLE, wl)
			
			wl = wl and WS_VISIBLE.inv()
			
			wl = wl or WS_EX_TOOLWINDOW   // flags don't work - windows remains in taskbar
			wl = wl and WS_EX_APPWINDOW.inv()
			
			ShowWindow(hwnd, SW_HIDE) // hide the window
			SetWindowLong(hwnd, GWL_STYLE, wl) // set the style
			ShowWindow(hwnd, SW_SHOW) // show the window for the new style to come into effect
			SetWindowLong(hwnd, GWL_EXSTYLE, wl)
			
			SetWindowPos(hwnd, HWND_TOPPOS, gameX, gameY, gameWidth, gameHeight, SWP_NOMOVE or SWP_NOSIZE)
		}
	}
	
}