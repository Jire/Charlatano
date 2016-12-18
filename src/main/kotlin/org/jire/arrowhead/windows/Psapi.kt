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
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.ptr.IntByReference
import com.sun.jna.win32.StdCallLibrary

/**
 * Provides a standard calling library interface for `Psapi`.
 */
interface Psapi : StdCallLibrary {
	
	/**
	 * Retrieves a handle for each module in the specified process that meets the specified filter criteria.
	 * @param hProcess A handle to the process.
	 * @param lphModule An array that receives the list of module handles.
	 * @param lpcbNeeded The number of bytes to store all module handles in the _lphModule_ array.
	 * @param dwFilterFlag The filter criteria.
	 */
	fun EnumProcessModulesEx(hProcess: WinNT.HANDLE, lphModule: Array<WinDef.HMODULE?>, cb: Int,
	                         lpcbNeeded: IntByReference, dwFilterFlag: Int = FilterFlags.LIST_MODULES_ALL): Boolean
	
	/**
	 * Retrieves the base name of the specified module.
	 */
	fun GetModuleBaseNameA(hProcess: WinNT.HANDLE, hModule: WinDef.HMODULE,
	                       lpBaseName: ByteArray, nSize: Int): Int
	
	companion object {
		
		/**
		 * The loaded instance of our `Psapi` standard call library.
		 *
		 * Use this to actually call the native functions.
		 */
		val INSTANCE = Native.loadLibrary("Psapi", Psapi::class.java)!!
		
	}
	
}