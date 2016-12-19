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

package com.charlatano.game

import co.paralleluniverse.fibers.SuspendExecution
import co.paralleluniverse.strands.Strand
import com.charlatano.InGamePitch
import com.charlatano.InGameSensitivity
import com.charlatano.InGameYaw
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.CSGO.gameX
import com.charlatano.game.CSGO.gameY
import com.charlatano.game.offsets.EngineOffsets.dwViewAngles
import com.charlatano.utils.*
import com.sun.jna.platform.win32.WinDef
import org.jire.arrowhead.get
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.ThreadLocalRandom.current as tlr

typealias ClientState = Long

fun ClientState.angle(): Angle
		= Vector(csgoEXE.float(this + dwViewAngles).toDouble(),
		csgoEXE.float(this + dwViewAngles + 4).toDouble(),
		csgoEXE.float(this + dwViewAngles + 8).toDouble())

private val mousePos = ThreadLocal.withInitial { WinDef.POINT() }
private val target = ThreadLocal.withInitial { WinDef.POINT() }

private val delta = ThreadLocal.withInitial { Vector() }

@Throws(SuspendExecution::class) fun aim(currentAngle: Angle, dest: Angle, smoothing: Int = 100,
                                         randomSleepMax: Int = 10, staticSleep: Int = 2,
                                         sensMultiplier: Double = 1.0, perfect: Boolean = false) {
	if (dest.z != 0.0 || dest.x < -89 || dest.x > 180 || dest.y < -180 || dest.y > 180
			|| dest.x.isNaN() || dest.y.isNaN() || dest.z.isNaN()) return

	val delta = delta.get()
	delta.set(currentAngle.y - dest.y, currentAngle.x - dest.x, 0.0)

	var sens = InGameSensitivity * sensMultiplier
	if (sens < InGameSensitivity || perfect) sens = InGameSensitivity

	val dx = Math.round(delta.x / (sens * InGamePitch))
	val dy = Math.round(-delta.y / (sens * InGameYaw))

	val mousePos = mousePos.get()
	mousePos.refresh()

	val target = target.get()
	target.set((mousePos.x + (dx / 2)).toInt(), (mousePos.y + (dy / 2)).toInt())

	if (target.x <= 0) return
	else if (target.x >= gameX + gameWidth) return
	if (target.y <= 0) return
	else if (target.y >= gameY + gameHeight) return

	if (perfect) {
		mouseMove(dx.toInt(), dy.toInt())
		Strand.sleep(20)
		return
	} else {
		mouseMove((dx / 10).toInt(), (dy / 10).toInt())
		Strand.sleep(20)
		return
	}

	
	/*val points = ZetaMouseGenerator.generate(mousePos, target)
	for (i in 1..points.lastIndex) {
		val point = points[points.lastIndex]
		mousePos.refresh()

		val tx = point.x - mousePos.x
		val ty = point.y - mousePos.y

		var halfIndex = points.lastIndex / 2
		if (halfIndex == 0) halfIndex = 1
		mouseMove(tx / halfIndex, ty / halfIndex)

		val sleepingFactor = smoothing / 100.0
		val sleepTime = Math.floor(staticSleep.toDouble()
				+ tlr().nextInt(randomSleepMax)
				+ tlr().nextInt(i)) * sleepingFactor
		if (sleepTime > 0) Strand.sleep(sleepTime.toLong())
	}*/
}