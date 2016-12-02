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

import com.charlatano.calculateAngle
import com.charlatano.game.entity.Player
import java.lang.Math.*

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

fun GetFov(angle: Angle, src: Angle, dst: Angle, player: Player): Double {
	val ang = calculateAngle(player, dst)
	val aim = Angle()

	MakeVector(angle, aim)
	MakeVector(ang, ang)

	val mag = sqrt(pow(aim.x, 2.0) + pow(aim.y, 2.0) + pow(aim.z, 2.0))
	val u_dot_v = aim.Dot(ang)

	return Math.toDegrees(acos(u_dot_v / pow(mag, 2.0)))
}

fun MakeVector(angle: Angle, vector: Angle) {
	val pitch = angle.x * Math.PI / 180
	val yaw = angle.y * Math.PI / 180
	val tmp = cos(pitch)
	vector.x = -tmp * -cos(yaw)
	vector.y = sin(yaw) * tmp
	vector.z = -sin(pitch)
}

fun Angle.Dot(v: Angle) = x * v.x + y * v.y + z * v.z

internal fun Angle.distanceTo(target: Angle)
		/*= sqrt((x * x - target.x * target.x) + (y * y - target.y * target.y) + (z * z - target.z * target.z))*/= abs(x - target.x) + abs(y - target.y) + abs(z - target.z)

internal fun Angle.finalize(orig: Angle, strictness: Double) {
	x -= orig.x
	y -= orig.y
	z = 0.0
	normalize()

	x = orig.x + x * strictness
	y = orig.y + y * strictness
	normalize()
}