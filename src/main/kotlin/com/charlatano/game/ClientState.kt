/*
 * Charlatan is a premium CS:GO cheat ran on the JVM.
 * Copyright (C) 2016 Thomas Nappo, Jonathan Beaudoin
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

import co.paralleluniverse.fibers.Suspendable
import co.paralleluniverse.strands.Strand
import com.charlatano.InGamePitch
import com.charlatano.InGameSensitivity
import com.charlatano.InGameYaw
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.offsets.EngineOffsets.dwViewAngles
import com.charlatano.utils.Angle
import com.charlatano.utils.Vector
import com.charlatano.utils.ZetaMouseGenerator
import com.charlatano.utils.mouseMove
import org.jire.arrowhead.get
import java.awt.MouseInfo
import java.awt.Point
import java.util.concurrent.ThreadLocalRandom.current

typealias ClientState = Long

fun ClientState.angle(): Angle
		= Vector(csgoEXE[this + dwViewAngles], csgoEXE[this + dwViewAngles + 4], csgoEXE[this + dwViewAngles + 8])

@Suspendable
fun aim(currentAngle: Angle, dest: Angle, smoothing: Int = 100) {
	if (dest.z != 0F || dest.x < -89 || dest.x > 180 || dest.y < -180 || dest.y > 180
			|| dest.x.isNaN() || dest.y.isNaN() || dest.z.isNaN()) return

	val delta = Vector(currentAngle.y - dest.y, currentAngle.x - dest.x, 0F)

	val dx = Math.round(delta.x / (InGameSensitivity * InGamePitch))
	val dy = Math.round(-delta.y / (InGameSensitivity * InGameYaw))

	val current = MouseInfo.getPointerInfo().location!!
	val target = Point(current.x + (dx / 2), current.y + (dy / 2))

	if (target.x <= 0) return
	else if (target.x >= CSGO.gameX + CSGO.gameWidth) return
	if (target.y <= 0) return
	else if (target.y >= CSGO.gameY + CSGO.gameHeight) return

	val points = ZetaMouseGenerator.generate(current, target)
	for (i in 1..points.lastIndex) @Suspendable {
		val point = points[points.lastIndex]
		val mouse = MouseInfo.getPointerInfo().location!!

		val tx = point.x - mouse.x
		val ty = point.y - mouse.y

		var halfIndex = points.lastIndex / 2
		if (halfIndex == 0) halfIndex = 1
		mouseMove(tx / halfIndex, ty / halfIndex)

		Strand.sleep(Math.ceil((2 + current().nextInt(6) + current().nextInt(i)) * (smoothing / 100.0)).toLong())
	}
}