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

import java.lang.Math.abs

typealias Angle = Vector

fun Vector.normalize() = apply {
	if (x != x) x = 0.0
	if (y != y) y = 0.0

	if (x > 89) x = 89.0
	if (x < -89) x = -89.0

	while (y > 180) y -= 360
	while (y <= -180) y += 360

	if (y > 180) y = 180.0
	if (y < -180F) y = -180.0

	z = 0.0
}

internal fun Angle.distanceTo(target: Angle) = abs(x - target.x) + abs(y - target.y) + abs(z - target.z)

internal fun Angle.finalize(orig: Angle, strictness: Double) {
	x -= orig.x
	y -= orig.y
	z = 0.0
	normalize()

	x = orig.x + x * strictness
	y = orig.y + y * strictness
	normalize()
}