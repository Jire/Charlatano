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

internal fun Angle.distanceTo(target: Angle) = abs(x - target.x) +
		abs(y - target.y) + abs(z - target.z)

internal fun Angle.isValid() = !(z != 0.0 || x < -89 || x > 180 || y < -180 || y > 180
		|| x.isNaN() || y.isNaN() || z.isNaN())

internal fun Angle.finalize(orig: Angle, strictness: Double) {
	x -= orig.x
	y -= orig.y
	z = 0.0
	normalize()
	
	x = orig.x + x * strictness
	y = orig.y + y * strictness
	normalize()
}