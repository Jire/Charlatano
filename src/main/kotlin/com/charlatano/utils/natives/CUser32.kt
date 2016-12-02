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

package com.charlatano.utils.natives

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef

object CUser32 {

	init {
		Native.register("user32")
	}

	@JvmStatic
	external fun GetClientRect(hWnd: WinDef.HWND, rect: WinDef.RECT): Boolean

	@JvmStatic
	external fun GetCursorPos(p: WinDef.POINT): Boolean

	@JvmStatic
	external fun FindWindowA(lpClassName: String?, lpWindowName: String): WinDef.HWND

	@JvmStatic
	external fun GetForegroundWindow(): WinDef.HWND

	@JvmStatic
	external fun GetWindowRect(hWnd: WinDef.HWND, rect: WinDef.RECT): Boolean

	@JvmStatic
	external fun mouse_event(dwFlags: Int, dx: Int, dy: Int, dwData: Int, dwExtraInfo: Long)

	@JvmStatic
	external fun SetWindowLongPtrA(hWnd: WinDef.HWND, nIndex: Int, dwNewLongPtr: Pointer): Long

	@JvmStatic
	external fun GetWindowLongPtrA(hWnd: WinDef.HWND, nIndex: Int): Long

	@JvmStatic
	external fun ShowWindow(hWnd: WinDef.HWND, nCmdShow: Int): Boolean

	@JvmStatic
	external fun SetWindowPos(hWnd: WinDef.HWND, hWndInsertAfter: WinDef.HWND, X: Int, Y: Int, cx: Int,
	                          cy: Int, uFlags: Int): Boolean

}