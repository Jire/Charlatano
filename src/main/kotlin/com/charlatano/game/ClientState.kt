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
import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.CSGO.gameX
import com.charlatano.game.CSGO.gameY
import com.charlatano.game.offsets.EngineOffsets.dwViewAngles
import com.charlatano.utils.*
import com.sun.jna.platform.win32.WinDef
import org.jire.arrowhead.get
import java.util.concurrent.ThreadLocalRandom.current

typealias ClientState = Long

fun ClientState.angle(): Angle
		= Vector(csgoEXE[this + dwViewAngles], csgoEXE[this + dwViewAngles + 4], csgoEXE[this + dwViewAngles + 8])


private val mousePos = WinDef.POINT()
private val target = WinDef.POINT()

private val delta = Vector()

@Suspendable
fun aim(currentAngle: Angle, dest: Angle, smoothing: Int = 100) {
	if (dest.z != 0F || dest.x < -89 || dest.x > 180 || dest.y < -180 || dest.y > 180
			|| dest.x.isNaN() || dest.y.isNaN() || dest.z.isNaN()) return
	
	delta.set(currentAngle.y - dest.y, currentAngle.x - dest.x, 0F)
	
	val dx = Math.round(delta.x / (InGameSensitivity * InGamePitch))
	val dy = Math.round(-delta.y / (InGameSensitivity * InGameYaw))
	
	mousePos.refresh()
	
	target.set(mousePos.x + (dx / 2), mousePos.y + (dy / 2))
	
	if (target.x <= 0) return
	else if (target.x >= gameX + gameWidth) return
	if (target.y <= 0) return
	else if (target.y >= gameY + gameHeight) return
	
	val points = ZetaMouseGenerator.generate(mousePos, target)
	for (i in 1..points.lastIndex) @Suspendable {
		val point = points[points.lastIndex]
		mousePos.refresh()
		
		val tx = point.x - mousePos.x
		val ty = point.y - mousePos.y
		
		var halfIndex = points.lastIndex / 2
		if (halfIndex == 0) halfIndex = 1
		mouseMove(tx / halfIndex, ty / halfIndex)
		
		Strand.sleep(Math.ceil((2 + current().nextInt(6) + current().nextInt(i)) * (smoothing / 100.0)).toLong())
	}
}