/*
 * Charlatano is a premium CS:GO cheat ran on the JVM.
 * Copyright (C) 2016 - Thomas Nappo, Jonathan Beaudoin
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

import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser

private const val MOUSEEVENTF_MOVE = 0x0001L
private const val MOUSEEVENTF_ABSOLUTE = 0x8000L

private val DWORD_ONE = WinDef.DWORD(1)

private val input = object : WinUser.INPUT() {

	init {
		type = WinDef.DWORD(WinUser.INPUT.INPUT_MOUSE.toLong())

		input.mi.dx = WinDef.LONG(0)
		input.mi.dy = WinDef.LONG(0)
		input.mi.mouseData = WinDef.DWORD(0)
		input.mi.dwFlags = WinDef.DWORD(MOUSEEVENTF_MOVE)
		input.setType("mi")

	}
}.toArray(1) as Array<WinUser.INPUT>

private val inputSize by lazy(LazyThreadSafetyMode.NONE) { input[0].size() }

fun mouseMove(dx: Int, dy: Int) {
	input[0].input.mi.dx.setValue(dx.toLong())
	input[0].input.mi.dy.setValue(dy.toLong())

	User32.INSTANCE.SendInput(DWORD_ONE, input, inputSize)
}