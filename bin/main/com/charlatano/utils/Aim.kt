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

import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.CSGO.gameX
import com.charlatano.game.CSGO.gameY
import com.charlatano.game.clientState
import com.charlatano.game.setAngle
import com.charlatano.settings.GAME_PITCH
import com.charlatano.settings.GAME_SENSITIVITY
import com.charlatano.settings.GAME_YAW
import com.charlatano.utils.extensions.refresh
import com.charlatano.utils.extensions.set
import com.sun.jna.platform.win32.WinDef.POINT

private val mousePos = ThreadLocal.withInitial { POINT() }
private val target = ThreadLocal.withInitial { POINT() }

private val delta = ThreadLocal.withInitial { Vector() }

fun applyFlatSmoothing(currentAngle: Angle, destinationAngle: Angle, smoothing: Double) = destinationAngle.apply {
	x -= currentAngle.x
	y -= currentAngle.y
	z = 0.0
	normalize()
	
	x = currentAngle.x + x / 100 * (100 / smoothing)
	y = currentAngle.y + y / 100 * (100 / smoothing)
	
	normalize()
}

fun writeAim(currentAngle: Angle, destinationAngle: Angle, smoothing: Double)
		= clientState.setAngle(applyFlatSmoothing(currentAngle, destinationAngle, smoothing))

fun flatAim(currentAngle: Angle, destinationAngle: Angle, smoothing: Double, sensMultiplier: Double = 1.0) {
	applyFlatSmoothing(currentAngle, destinationAngle, smoothing)
	if (!destinationAngle.isValid()) return
	
	val delta = delta.get()
	delta.set(currentAngle.y - destinationAngle.y, currentAngle.x - destinationAngle.x, 0.0)
	
	var sens = GAME_SENSITIVITY * sensMultiplier
	if (sens < GAME_SENSITIVITY) sens = GAME_SENSITIVITY
	
	val dx = Math.round(delta.x / (sens * GAME_PITCH))
	val dy = Math.round(-delta.y / (sens * GAME_YAW))
	
	mouseMove((dx / 2).toInt(), (dy / 2).toInt())
}

fun pathAim(currentAngle: Angle, destinationAngle: Angle, smoothing: Int,
            randomSleepMax: Int = 10, staticSleep: Int = 2,
            sensMultiplier: Double = 1.0, perfect: Boolean = false) {
	if (!destinationAngle.isValid()) return
	
	val delta = delta.get()
	delta.set(currentAngle.y - destinationAngle.y, currentAngle.x - destinationAngle.x, 0.0)
	
	var sens = GAME_SENSITIVITY * sensMultiplier
	if (sens < GAME_SENSITIVITY) sens = GAME_SENSITIVITY
	if (perfect) sens = 1.0
	
	val dx = Math.round(delta.x / (sens * GAME_PITCH))
	val dy = Math.round(-delta.y / (sens * GAME_YAW))
	
	val mousePos = mousePos.get().refresh()
	
	val target = target.get()
	target.set((mousePos.x + (dx / 2)).toInt(), (mousePos.y + (dy / 2)).toInt())
	
	if (target.x <= 0 || target.x >= gameX + gameWidth
			|| target.y <= 0 || target.y >= gameY + gameHeight) return
	
	if (perfect) {
		mouseMove((dx / 2).toInt(), (dy / 2).toInt())
		Thread.sleep(20)
	} else HumanMouse.fastSteps(mousePos, target) { steps, i ->
		mousePos.refresh()
		
		val tx = target.x - mousePos.x
		val ty = target.y - mousePos.y
		
		var halfIndex = steps / 2
		if (halfIndex == 0) halfIndex = 1
		mouseMove(tx / halfIndex, ty / halfIndex)
		
		val sleepingFactor = smoothing / 100.0
		val sleepTime = Math.floor(staticSleep.toDouble()
				+ randInt(randomSleepMax)
				+ randInt(i)) * sleepingFactor
		if (sleepTime > 0) Thread.sleep(sleepTime.toLong())
	}
}