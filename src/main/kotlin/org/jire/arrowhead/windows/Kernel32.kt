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
import com.sun.jna.NativeLibrary
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinNT
import org.jire.arrowhead.windows.Kernel32.ReadProcessMemory
import org.jire.arrowhead.windows.Kernel32.WriteProcessMemory

/**
 * Provides zero-garbage [WriteProcessMemory] and [ReadProcessMemory] access.
 *
 * The "secret sauce" is avoiding JNA object allocations from translation-necessary types.
 */
object Kernel32 {
	
	/**
	 * Writes memory to an area of memory in a specified process.
	 * The entire area to be written to must be accessible or the operation fails.
	 *
	 * @param hProcess A handle to the process memory to be modified.
	 * @param lpBaseAddress The base address in the specified process to which memory is written.
	 * @param lpBuffer The buffer that contains memory to be written in the
	 * address space of the specified process.
	 * @param nSize The number of bytes to be written to the specified process.
	 * @param lpNumberOfBytesWritten A variable that receives the number of bytes
	 * transferred into the specified process.  If `null` the parameter is ignored.
	 * @return `1` if successful, `0` otherwise.
	 * To get extended error information, call [Native.getLastError()][com.sun.jna.Native#getLastError].
	 */
	@JvmStatic
	external fun WriteProcessMemory(hProcess: Pointer, lpBaseAddress: Pointer, lpBuffer: Pointer,
	                                nSize: Int, lpNumberOfBytesWritten: Int): Long
	
	@JvmStatic
	external fun Module32NextW(hSnapshot: WinNT.HANDLE, lpme: MODULEENTRY32W): Boolean
	
	/**
	 * Reads memory from an area of memory in a specified process. The entire area
	 * to be read must be accessible or the operation fails.
	 *
	 * @param hProcess
	 *            A handle to the process with memory that is being read. The
	 *            handle must have PROCESS_VM_READ access to the process.
	 * @param lpBaseAddress
	 *            A data to the base address in the specified process from
	 *            which to read.
	 *            Before any memory transfer occurs, the system verifies that all
	 *            memory in the base address and memory of the specified size is
	 *            accessible for read access, and if it is not accessible the
	 *            function fails.
	 * @param lpBuffer
	 *            A data to a buffer that receives the contents from the
	 *            address space of the specified process.
	 * @param nSize
	 *            The number of bytes to be read from the specified process.
	 * @param lpNumberOfBytesRead
	 *            A data to a variable that receives the number of bytes
	 *            transferred into the specified buffer. If `lpNumberOfBytesRead`
	 *            is `NULL`, the parameter is ignored.
	 * @return If the function succeeds, the return value is nonzero.
	 *         If the function fails, the return value is `0` (zero). To get
	 *         extended error information, call [Native.getLastError()][com.sun.jna.Native#getLastError].
	 *         The function fails if the requested read operation crosses into
	 *         an area of the process that is inaccessible.
	 */
	@JvmStatic
	external fun ReadProcessMemory(hProcess: Pointer, lpBaseAddress: Pointer, lpBuffer: Pointer,
	                               nSize: Int, lpNumberOfBytesRead: Int): Long
	
	init {
		Native.register(NativeLibrary.getInstance("Kernel32"))
	}
	
}