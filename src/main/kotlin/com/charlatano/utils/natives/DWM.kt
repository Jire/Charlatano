package com.charlatano.utils.natives

import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import org.jire.arrowhead.Struct

object DWM {
	
	init {
		Native.register("Dwmapi")
	}
	
	@JvmStatic
	external fun DwmEnableBlurBehindWindow(hWnd: WinDef.HWND, pBlurBehind: DWM_BLURBEHIND): WinNT.HRESULT
	
}

class DWM_BLURBEHIND : Struct() {
	
	@JvmField var dwFlags: WinDef.DWORD? = null
	@JvmField var fEnable = false
	@JvmField var hRgnBlur: WinDef.HRGN? = null
	@JvmField var fTransitionOnMaximized = false
	
}