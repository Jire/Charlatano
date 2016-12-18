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

package org.jire.arrowhead.windows

import com.sun.jna.Native
import org.jire.arrowhead.windows.User32.GetKeyState

/**
 * Provides zero-garbage fast access to [GetKeyState].
 */
object User32 {
	
	/**
	 * Retrieves the status of the specified virtual key.
	 *
	 * The status specifies whether the key is up, down, or toggled
	 * (on, off—alternating each time the key is pressed).
	 *
	 * @param nVirtKey A virtual key. If the desired virtual key is a letter or digit
	 * (_A_ through _Z_, _a_ through _z_, or _0_ through _9_), `nVirtKey` must be set to the ASCII value
	 * of that character. For other keys, it must be a virtual-key code.
	 *
	 * If a non-English keyboard layout is used, virtual keys with values in the range ASCII
	 * _A_ through _Z_ and _0_ through _9_ are used to specify most of the character keys. For example,
	 * for the German keyboard layout, the virtual key of value ASCII _O_ (_0x4F_) refers to the "o" key,
	 * whereas _VK_OEM_1_ refers to the "o with umlaut" key.
	 */
	@JvmStatic
	external fun GetKeyState(nVirtKey: Int): Short
	
	init {
		Native.register("user32")
	}
	
}