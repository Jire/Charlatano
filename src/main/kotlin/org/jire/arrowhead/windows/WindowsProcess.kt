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

/*
 * "Windows" is a trademark of Microsoft Corporation.
 *
 * The trademark holders are not affiliated with the maker
 * of this product and do not endorse this product.
 */

package org.jire.arrowhead.windows

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Tlhelp32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import org.jire.arrowhead.Process
import java.util.*
import com.sun.jna.platform.win32.Kernel32.INSTANCE as JNAKernel32

/**
 * Represents a process on Windows.
 */
class WindowsProcess(override val id: Int, val handle: WinNT.HANDLE) : Process {
	
	private val modulesMap = Collections.synchronizedMap(Object2ObjectArrayMap<String, WindowsModule>())
	
	override fun loadModules() {
		modulesMap.clear()
		
		val snapshot = JNAKernel32.CreateToolhelp32Snapshot(WinDef.DWORD((Tlhelp32.TH32CS_SNAPMODULE32.toInt() or Tlhelp32.TH32CS_SNAPMODULE.toInt()).toLong()), WinDef.DWORD(id.toLong()))
		val entry = MODULEENTRY32W.ByReference()
		try {
			while (Kernel32.Module32NextW(snapshot, entry)) {
				val name = entry.szModule()
				val address = Pointer.nativeValue(entry.hModule.pointer)
				val module = WindowsModule(address, this, name, entry.modBaseSize.toLong())
				modulesMap.putIfAbsent(module.name, module)
				
			}
		} finally {
			JNAKernel32.CloseHandle(snapshot)
		}
	}
	
	override val modules: Map<String, WindowsModule> = modulesMap
	
	override fun read(address: Pointer, data: Pointer, bytesToRead: Int)
			= Kernel32.ReadProcessMemory(handle.pointer, address, data, bytesToRead, 0) > 0
	
	override fun write(address: Pointer, data: Pointer, bytesToWrite: Int)
			= Kernel32.WriteProcessMemory(handle.pointer, address, data, bytesToWrite, 0) > 0
	
}