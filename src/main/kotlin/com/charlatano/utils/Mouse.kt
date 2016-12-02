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

package com.charlatano.utils

import com.charlatano.utils.natives.CUser32
import com.sun.jna.platform.win32.WinDef
import org.jire.arrowhead.windows.User32

private const val MOUSEEVENTF_MOVE = 0x0001
private const val MOUSEEVENTF_ABSOLUTE = 0x8000

private val DWORD_ONE = WinDef.DWORD(1)

fun mouseMove(dx: Int, dy: Int) = CUser32.mouse_event(MOUSEEVENTF_MOVE, dx, dy, 0, 0)