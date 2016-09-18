/*
 * Charlatan is a premium CS:GO cheat ran on the JVM.
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

import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import java.lang.Math.sqrt

fun WinDef.POINT.set(x: Int, y: Int) = apply {
	this.x = x
	this.y = y
}

fun WinDef.POINT.refresh() = apply { User32.INSTANCE.GetCursorPos(this) }

fun WinDef.POINT.distance(b: WinDef.POINT): Double {
	val px = (b.x - this.x).toDouble()
	val py = (b.y - this.y).toDouble()
	return sqrt(px * px + py * py)
}