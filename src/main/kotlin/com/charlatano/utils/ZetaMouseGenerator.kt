package com.charlatano.utils

import com.charlatano.utils.extensions.distance
import com.sun.jna.platform.win32.WinDef

/**
 * Date: 01/05/13
 * Time: 16:55
 
 * @author A_C/Cov
 * *
 * @author Speljohan / ZeroFreeze
 */
object ZetaMouseGenerator {
	
	fun generate(a: WinDef.POINT, b: WinDef.POINT): Array<WinDef.POINT> {
		//DEF VARS
		val steps = (Math.sqrt(a.distance(b)) * 3).toInt()
		
		var multiplier = 1
		
		val xOffset = (b.x - a.x) / (Math.sqrt(a.distance(b)) * 3)
		val yOffset = (b.y - a.y) / (Math.sqrt(a.distance(b)) * 3)
		
		var x = Math.toRadians(180 / steps.toDouble())
		var y = Math.toRadians(180 / steps.toDouble())
		
		//RANDOMISE PATH VARS
		var waviness = 2.8/*2.8*/
		if (a.distance(b) < 120) { // less than 120px
			waviness = 0.5
		}
		
		if (Math.random() >= 0.5) {
			x *= Math.floor(Math.random() * waviness + 1)
		}
		if (Math.random() >= 0.5) {
			y *= Math.floor(Math.random() * waviness + 1)
		}
		if (Math.random() >= 0.5) {
			multiplier *= -1
		}
		
		val offset = Math.random() * (1.6 + Math.sqrt(steps.toDouble())) + 6 + 2
		
		// GEN PATH
		val path = Array(steps + 2) {
			WinDef.POINT(a.x + ((xOffset * it).toInt() + multiplier * (offset * Math.sin(x * it)).toInt()),
					a.y + ((yOffset * it).toInt() + multiplier * (offset * Math.sin(y * it)).toInt()))
		}
		
		// RETURN PATH
		path[0] = a
		path[path.size - 1] = b
		return path
	}
	
}