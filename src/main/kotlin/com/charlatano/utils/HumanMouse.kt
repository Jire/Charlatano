package com.charlatano.utils

import com.charlatano.utils.extensions.distance
import com.sun.jna.platform.win32.WinDef
import java.lang.Math.*
import java.util.concurrent.ThreadLocalRandom.current as tlr

object HumanMouse {
	
	operator inline fun invoke(a: WinDef.POINT, b: WinDef.POINT, action: (Int, Int, Int, Int) -> Unit) {
		val a2b = a.distance(b)
		val sq_a2b = sqrt(a2b)
		
		val steps = sq_a2b * 3
		
		var multiplier = 1
		
		val xOffset = (b.x - a.x) / steps
		val yOffset = (b.y - a.y) / steps
		
		var x = toRadians(180 / steps)
		var y = toRadians(180 / steps)
		
		var waviness = 2.8
		if (a2b < 120) // less than 120px
			waviness = 0.5
		
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