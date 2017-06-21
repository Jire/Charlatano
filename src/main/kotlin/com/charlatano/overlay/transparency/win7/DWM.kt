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

package com.charlatano.overlay.transparency.win7

import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT

object DWM {
	
	@JvmStatic
	external fun DwmEnableBlurBehindWindow(hWnd: WinDef.HWND, pBlurBehind: DWM_BLURBEHIND): WinNT.HRESULT
	
	init {
		Native.register("Dwmapi")
	}
	
	const val DWM_BB_ENABLE = 0x00000001L
	const val DWM_BB_BLURREGION = 0x00000002L
	const val DWM_BB_TRANSITIONONMAXIMIZED = 0x00000004L
	
}