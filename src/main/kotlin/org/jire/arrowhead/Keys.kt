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
import org.jire.arrowhead.windows.User32

/**
 * Returns the key state of the specified virtual key code.
 *
 * @param virtualKeyCode The key code of which to check the state.
 * @throws UnsupportedOperationException If the platform is not supported.
 */
fun keyState(virtualKeyCode: Int): Int = when {
	Platform.isWindows() || Platform.isWindowsCE() -> User32.GetKeyState(virtualKeyCode).toInt()
	else -> throw UnsupportedOperationException("Unsupported platform (osType=${Platform.getOSType()}")
}

/**
 * Checks whether or not the key state of the specified virtual key code is pressed.
 *
 * @param virtualKeyCode The key code of which to check the state.
 * @return `true` if the key is pressed, `false` otherwise.
 * @throws UnsupportedOperationException If the platform is not supported.
 */
fun keyPressed(virtualKeyCode: Int) = keyState(virtualKeyCode) < 0

/**
 * Checks whether or not the key state of the specified virtual key code is pressed,
 * then runs the specified action code block.
 *
 * @param virtualKeyCode The key code of which to check the state.
 * @param action The code to run if the key is pressed.
 * @return `true` if the key is pressed, `false` otherwise.
 * @throws UnsupportedOperationException If the platform is not supported.
 */
inline fun keyPressed(virtualKeyCode: Int, action: () -> Unit) = if (keyPressed(virtualKeyCode)) {
	action()
	true
} else false

/**
 * Checks whether or not the key state of the specified virtual key code is released (not pressed).
 *
 * @param virtualKeyCode The key code of which to check the state.
 * @return `false` if the key is pressed, `true` otherwise.
 * @throws UnsupportedOperationException If the platform is not supported.
 */
fun keyReleased(virtualKeyCode: Int) = !keyPressed(virtualKeyCode)

/**
 * Checks whether or not the key state of the specified virtual key code is released (not pressed),
 * then runs the specified action code block.
 *
 * @param virtualKeyCode The key code of which to check the state.
 * @param action The code to run if the key is released (not pressed).
 * @return `false` if the key is pressed, `true` otherwise.
 * @throws UnsupportedOperationException If the platform is not supported.
 */
inline fun keyReleased(virtualKeyCode: Int, action: () -> Unit) = if (keyReleased(virtualKeyCode)) {
	action()
	true
} else false