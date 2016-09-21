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

internal fun Angle.normalize() {
	if (x != x) x = 0F
	if (y != y) y = 0F

	if (x > 89) x = 89F
	if (x < -89) x = -89F

	while (y > 180) y -= 360
	while (y <= -180) y += 360

	if (y > 180) y = 180F
	if (y < -180F) y = -180F

	z = 0F
}

internal fun Angle.distanceTo(target: Angle) = abs(x - target.x) + abs(y - target.y) + abs(z - target.z)

internal fun Angle.finalize(orig: Angle, smoothing: Float) {
	x -= orig.x
	y -= orig.y
	z = 0F
	normalize()

	x = orig.x + x / 100F * smoothing
	y = orig.y + y / 100F * smoothing
	normalize()
}