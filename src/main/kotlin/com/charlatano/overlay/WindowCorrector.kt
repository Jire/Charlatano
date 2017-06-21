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

import com.charlatano.game.CSGO
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser

object WindowCorrector {
	
	const val SWP_NOSIZE = 0x0001
	const val SWP_NOMOVE = 0x0002
	
	const val WS_EX_TOOLWINDOW = 0x00000080
	const val WS_EX_APPWINDOW = 0x00040000
	
	fun setupWindow(hwnd: WinDef.HWND) = with(User32.INSTANCE) {
		var wl = GetWindowLong(hwnd, WinUser.GWL_EXSTYLE)
		wl = wl or WinUser.WS_EX_LAYERED or WinUser.WS_EX_TRANSPARENT
		SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl)
		
		wl = wl and WinUser.WS_VISIBLE.inv()
		
		wl = wl or WS_EX_TOOLWINDOW   // flags don't work - windows remains in taskbar
		wl = wl and WS_EX_APPWINDOW.inv()
		
		ShowWindow(hwnd, WinUser.SW_HIDE) // hide the window
		SetWindowLong(hwnd, WinUser.GWL_STYLE, wl) // set the style
		ShowWindow(hwnd, WinUser.SW_SHOW) // show the window for the new style to come into effect
		SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl)
		
		val HWND_TOPPOS = WinDef.HWND(Pointer(-1))
		SetWindowPos(hwnd, HWND_TOPPOS,
				CSGO.gameX, CSGO.gameY,
				CSGO.gameWidth, CSGO.gameHeight,
				SWP_NOMOVE or SWP_NOSIZE)
	}
	
}