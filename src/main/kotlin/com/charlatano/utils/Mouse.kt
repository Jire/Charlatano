/*
 * Charlatano: Free and open-source (FOSS) cheat for CS:GO/CS:CO
 * Copyright (C) 2017 - Thomas G. P. Nappo, Jonathan Beaudoin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.charlatano.utils

import com.charlatano.utils.natives.CUser32.mouse_event

const val MOUSEEVENTF_ABSOLUTE = 0x8000
const val MOUSEEVENTF_MOVE = 0x0001
const val MOUSEEVENTF_WHEEL = 0x0800

const val MOUSEEVENTF_LEFTDOWN = 0x0002
const val MOUSEEVENTF_LEFTUP = 0x0004

fun mouseMove(dx: Int, dy: Int, flags: Int = MOUSEEVENTF_MOVE) = mouse_event(flags, dx, dy, 0, 0)

fun mouseWheel(amount: Int) = mouse_event(MOUSEEVENTF_WHEEL, 0, 0, amount, 0)

fun mouse(button: Int) = mouse_event(button, 0, 0, 0, 0)