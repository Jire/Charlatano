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

import com.sun.jna.platform.win32.WinDef
import org.jire.arrowhead.Struct

class DWM_BLURBEHIND : Struct() {
	
	@JvmField var dwFlags: WinDef.DWORD? = null
	@JvmField var fEnable = false
	@JvmField var hRgnBlur: WinDef.HRGN? = null
	@JvmField var fTransitionOnMaximized = false
	
}