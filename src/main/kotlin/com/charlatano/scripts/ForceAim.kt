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

package com.charlatano.scripts

import com.charlatano.FORCE_AIM_KEY
import com.charlatano.FORCE_AIM_SMOOTHING
import com.charlatano.calculateAngle
import com.charlatano.compensateVelocity
import com.charlatano.game.EntityType
import com.charlatano.game.aim
import com.charlatano.game.angle
import com.charlatano.game.entity.*
import com.charlatano.game.hooks.clientState
import com.charlatano.game.hooks.entitiesByType
import com.charlatano.game.hooks.me
import com.charlatano.utils.*
import org.jire.arrowhead.keyPressed
import java.util.concurrent.ThreadLocalRandom.current as tlr

private var target: Player = -1L

private const val LOCK_FOV = 60

private const val SMOOTHING_MIN = 30F
private const val SMOOTHING_MAX = 40F

private const val SMOOTHING = 1

fun forceAim() = every(FORCE_AIM_SMOOTHING) {
	val pressed = keyPressed(FORCE_AIM_KEY) {
		val currentAngle = clientState.angle()

		var currentTarget = target
		if (currentTarget == -1L) {
			currentTarget = findTarget(me.position(), currentAngle)
			if (currentTarget == -1L) {
				return@keyPressed
			}
			target = currentTarget
			return@keyPressed
		}

		if (me.dead() || target.dead() || target.dormant() || !target.spotted() || target.team() == me.team()) {
			target = -1L
			return@keyPressed
		}

		val bonePosition = Vector(target.bone(0xC), target.bone(0x1C), target.bone(0x2C))
		compensateVelocity(me, target, bonePosition, SMOOTHING_MAX)

		val dest = calculateAngle(me, bonePosition)
		dest.normalize()
		//dest.finalize(currentAngle, SMOOTHING_MAX)

		aim(currentAngle, dest, SMOOTHING)
	}
	if (!pressed) target = -1L
}

private fun findTarget(position: Angle, angle: Angle, lockFOV: Int = LOCK_FOV): Player {
	var closestDelta = Int.MAX_VALUE
	var closetPlayer: Player? = null
	for (e in entitiesByType(EntityType.CCSPlayer)) {
		val entity = e.entity
		if (entity <= 0) continue
		if (entity == me || entity.team() == me.team()) continue

		if (me.dead() || entity.dead() || !entity.spotted() || entity.dormant()) continue

		val ePos: Angle = Vector(entity.bone(0xC), entity.bone(0x1C), entity.bone(0x2C))
		val distance = position.distanceTo(ePos)

		val dest = calculateAngle(me, ePos)
		dest.normalize()

		val yawDiff = Math.abs(angle.y - dest.y)
		val delta = Math.abs(Math.sin(Math.toRadians(yawDiff.toDouble())) * distance)

		if (delta <= lockFOV && delta < closestDelta) {
			closestDelta = delta.toInt()
			closetPlayer = entity
		}
	}
	return closetPlayer ?: -1
}