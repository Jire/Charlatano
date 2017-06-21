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

package com.charlatano.overlay.transparency.win10

import com.charlatano.overlay.transparency.TransparencyApplier
import com.sun.jna.NativeLibrary
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT.HRESULT

object Win10TransparencyApplier : TransparencyApplier {
	
	// CREDITS: https://gist.github.com/Guerra24/429de6cadda9318b030a7d12d0ad58d4
	
	override fun applyTransparency(hwnd: WinDef.HWND): Boolean {
		val user32 = NativeLibrary.getInstance("user32")
		
		val accent = AccentPolicy()
		accent.AccentState = AccentState.ACCENT_ENABLE_TRANSPARENTGRADIENT
		accent.AccentFlags = 2 // must be 2 for transparency
		accent.GradientColor = 0 // ARGB color code for gradient
		val accentStructSize = accent.size()
		accent.write()
		val accentPtr = accent.pointer
		
		val data = WindowCompositionAttributeData()
		data.Attribute = WindowCompositionAttribute.WCA_ACCENT_POLICY
		data.SizeOfData = accentStructSize
		data.Data = accentPtr
		
		val setWindowCompositionAttribute = user32.getFunction("SetWindowCompositionAttribute")
		val result = setWindowCompositionAttribute(HRESULT::class.java, arrayOf(hwnd, data)) as HRESULT
		return 1 == result.toInt()
	}
	
}