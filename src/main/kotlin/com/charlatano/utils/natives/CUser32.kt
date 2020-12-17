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

package com.charlatano.utils.natives

import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef

object CUser32 {
	
	init {
		Native.register("user32")
	}
	
	@JvmStatic
	external fun GetClientRect(hWnd: WinDef.HWND, rect: WinDef.RECT): Boolean
	
	@JvmStatic
	external fun GetCursorPos(p: WinDef.POINT): Int
	
	@JvmStatic
	external fun FindWindowA(lpClassName: String?, lpWindowName: String): WinDef.HWND
	
	@JvmStatic
	external fun GetForegroundWindow(): Long
	
	@JvmStatic
	external fun GetWindowRect(hWnd: WinDef.HWND, rect: WinDef.RECT): Boolean
	
	@JvmStatic
	external fun mouse_event(dwFlags: Int, dx: Int, dy: Int, dwData: Int, dwExtraInfo: Long)
	
}