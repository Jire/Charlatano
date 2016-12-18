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

import com.sun.jna.Native
import com.sun.jna.platform.win32.Tlhelp32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.Kernel32.INSTANCE as JNAKernel32

/**
 * Utility functions for dealing with native processes on Windows.
 */
object Windows {
	
	val PROCESS_ALL_ACCESS = WinNT.PROCESS_CREATE_PROCESS or WinNT.PROCESS_CREATE_THREAD or WinNT.PROCESS_DUP_HANDLE or WinNT.PROCESS_QUERY_INFORMATION or WinNT.PROCESS_QUERY_LIMITED_INFORMATION or WinNT.PROCESS_SET_INFORMATION or WinNT.PROCESS_SET_QUOTA or WinNT.PROCESS_SUSPEND_RESUME or WinNT.PROCESS_SYNCHRONIZE or WinNT.PROCESS_TERMINATE or WinNT.PROCESS_VM_OPERATION or WinNT.PROCESS_VM_READ or WinNT.PROCESS_VM_WRITE or WinNT.DELETE or WinNT.READ_CONTROL or WinNT.WRITE_DAC or WinNT.WRITE_OWNER or WinNT.SYNCHRONIZE
	/**
	 * Reusable DWORD of value zero; not intended to be mutated.
	 */
	val DWORD_ZERO = WinDef.DWORD(0)
	
	/**
	 * Opens a native process on Windows by the specified process ID, given the specified access flags.
	 *
	 * @param processID The process ID of the process to open.
	 * @param accessFlags The access permission flags given to the process.
	 */
	fun openProcess(processID: Int, accessFlags: Int = PROCESS_ALL_ACCESS): WindowsProcess {
		val handle = JNAKernel32.OpenProcess(accessFlags, true, processID)
		return WindowsProcess(processID, handle)
	}
	
	/**
	 * Opens a native process on Windows of the specified process name.
	 *
	 * @param processName The process name of the process to open.
	 */
	fun openProcess(processName: String): WindowsProcess? {
		val snapshot = JNAKernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPALL, DWORD_ZERO)
		val entry = Tlhelp32.PROCESSENTRY32.ByReference() // we reuse the same entry during iteration
		try {
			while (JNAKernel32.Process32Next(snapshot, entry)) {
				val fileName = Native.toString(entry.szExeFile)
				if (processName.equals(fileName)) return openProcess(entry.th32ProcessID.toInt())
			}
		} finally {
			JNAKernel32.CloseHandle(snapshot)
		}
		return null
	}
	
}