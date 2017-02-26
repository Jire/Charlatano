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

import com.charlatano.utils.extensions.distance
import com.sun.jna.platform.win32.WinDef
import java.lang.Math.*
import java.util.concurrent.ThreadLocalRandom.current as tlr

object HumanMouse {
	
	inline fun fastSteps(a: WinDef.POINT, b: WinDef.POINT, action: (Int, Int) -> Unit) {
		val a2b = a.distance(b)
		val sq_a2b = sqrt(a2b)
		
		val steps = sq_a2b * 3
		
		val totalSteps = steps.toInt() + 2
		val lastIndex = totalSteps - 1
		for (i in 1..totalSteps) action(lastIndex, i)
		action(lastIndex, lastIndex) // final step
	}
	
	inline fun steps(a: WinDef.POINT, b: WinDef.POINT, action: (Int, Int, Int, Int) -> Unit) {
		val a2b = a.distance(b)
		val sq_a2b = sqrt(a2b)
		
		val steps = sq_a2b * 3
		val radSteps = toRadians(180 / steps)
		
		val xOffset = (b.x - a.x) / steps
		val yOffset = (b.y - a.y) / steps
		
		var x = radSteps
		var y = radSteps
		
		var waviness = 2.8
		if (a2b < 120) // less than 120px
			waviness = 0.5
		
		var multiplier = 1
		
		if (tlr().nextBoolean()) x *= floor(tlr().nextDouble() * waviness + 1)
		if (tlr().nextBoolean()) y *= floor(tlr().nextDouble() * waviness + 1)
		if (tlr().nextBoolean()) multiplier *= -1
		
		val offset = tlr().nextDouble() * (1.6 + sqrt(steps)) + 6 + 2
		
		val totalSteps = steps.toInt() + 2
		val lastIndex = totalSteps - 1
		for (i in 1..totalSteps) {
			val stepX = a.x + ((xOffset * i).toInt() + multiplier * (offset * Math.sin(x * i)).toInt())
			val stepY = a.y + ((yOffset * i).toInt() + multiplier * (offset * Math.sin(y * i)).toInt())
			action(lastIndex, stepX, stepY, i)
		}
		action(lastIndex, b.x, b.y, lastIndex) // final step
	}
	
}