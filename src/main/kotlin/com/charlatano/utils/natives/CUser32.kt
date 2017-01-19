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
	external fun GetCursorPos(p: WinDef.POINT): Boolean
	
	@JvmStatic
	external fun FindWindowA(lpClassName: String?, lpWindowName: String): WinDef.HWND
	
	@JvmStatic
	external fun GetForegroundWindow(): WinDef.HWND
	
	@JvmStatic
	external fun GetWindowRect(hWnd: WinDef.HWND, rect: WinDef.RECT): Boolean
	
	@JvmStatic
	external fun mouse_event(dwFlags: Int, dx: Int, dy: Int, dwData: Int, dwExtraInfo: Long)
	
}