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

package org.jire.arrowhead

import com.sun.jna.Platform
import org.jire.arrowhead.linux.LinuxProcess
import org.jire.arrowhead.windows.Windows
import java.util.*

/**
 * Attempts to open a process of the specified process ID.
 *
 * @param processID The ID of the process to open.
 */
fun processByID(processID: Int): Process? = when {
	Platform.isWindows() || Platform.isWindowsCE() -> Windows.openProcess(processID)
	Platform.isLinux() -> LinuxProcess(processID)
	else -> null
}

/**
 * Attempts to open a process of the specified process name.
 *
 * @param processName The name of the process to open.
 */
fun processByName(processName: String): Process? = when {
	Platform.isWindows() || Platform.isWindowsCE() -> Windows.openProcess(processName)
	Platform.isLinux() -> {
		val search = Runtime.getRuntime().exec(arrayOf("bash", "-c",
				"ps -A | grep -m1 \"$processName\" | awk '{print $1}'"))
		val scanner = Scanner(search.inputStream)
		val processID = scanner.nextInt()
		scanner.close()
		processByID(processID)
	}
	else -> null
}