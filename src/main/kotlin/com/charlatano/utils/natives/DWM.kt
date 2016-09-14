/*
 * Charlatan is a premium CS:GO cheat ran on the JVM.
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
import com.sun.jna.Structure
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import java.util.*

object DWM {

    init {
        Native.register("Dwmapi")
    }

    external fun DwmEnableBlurBehindWindow(hWnd: WinDef.HWND, pBlurBehind: DWM_BLURBEHIND): WinNT.HRESULT

}

class DWM_BLURBEHIND : Structure() {

    @JvmField var dwFlags: WinDef.DWORD? = null
    @JvmField var fEnable: Boolean = false
    @JvmField var hRgnBlur: WinDef.HRGN? = null
    @JvmField var fTransitionOnMaximized: Boolean = false

    override fun getFieldOrder() = Arrays.asList("dwFlags", "fEnable", "hRgnBlur", "fTransitionOnMaximized")

}
