/*
 * Charlatano is a premium CS:GO cheat ran on the JVM.
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

import com.charlatano.*
import com.charlatano.game.*
import com.charlatano.game.entity.*
import com.charlatano.utils.*
import org.jire.arrowhead.keyPressed
import java.lang.Math.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.ThreadLocalRandom.current as tlr

private val target = AtomicLong(-1)

fun aim() = every(AIM_DURATION) {
	val pressed = keyPressed(1) or keyPressed(FORCE_AIM_KEY)
	if (!pressed) {
		target.set(-1L)
		return@every
	}

	val currentAngle = clientState.angle()

	var currentTarget = target.get()
	val position = me.position()
	if (currentTarget == -1L) {
		currentTarget = findTarget(position, currentAngle)
		if (currentTarget == -1L) return@every
		target.set(currentTarget)
		return@every
	}

	if (me.dead() || currentTarget.dead() || currentTarget.dormant()
			|| !currentTarget.spotted() || currentTarget.team() == me.team()) {
		target.set(-1L)
		return@every
	}

	val bonePosition = Vector(currentTarget.bone(0xC), currentTarget.bone(0x1C), currentTarget.bone(0x2C))
	compensateVelocity(me, currentTarget, bonePosition, AIM_VELOCITY_STRICTNESS)

	val dest = calculateAngle(me, bonePosition)
	if (AIM_ASSIST_MODE) dest.finalize(currentAngle, AIM_ASSIST_STRICTNESS / 100.0)

	val ePos: Angle = Vector(currentTarget.bone(0xC), currentTarget.bone(0x1C), currentTarget.bone(0x2C))
	val distance = position.distanceTo(ePos)
	var sensMultiplier = AIM_STRICTNESS

	if (distance > AIM_STRICTNESS_BASELINE_DISTANCE) {
		val amountOver = AIM_STRICTNESS_BASELINE_DISTANCE / distance
		sensMultiplier *= (amountOver * AIM_STRICTNESS_BASELINE_MODIFIER)
	}

	aim(currentAngle, dest, AIM_SPEED, sensMultiplier = sensMultiplier)
}

private fun findTarget(position: Angle, angle: Angle, lockFOV: Int = AIM_FOV): Player {
	var closestDelta = Double.MAX_VALUE
	var closetPlayer: Player? = null

	entities(EntityType.CCSPlayer) {
		val entity = it.entity
		if (entity <= 0) return@entities
		if (entity == me || entity.team() == me.team()) return@entities

		if (me.dead() || entity.dead() || !entity.spotted() || entity.dormant()) return@entities

		val ePos: Angle = Vector(entity.bone(0xC), entity.bone(0x1C), entity.bone(0x2C))
		val distance = position.distanceTo(ePos)

		val dest = calculateAngle(me, ePos)

		val yawDiff = abs(angle.y - dest.y)
		val delta = abs(sin(toRadians(yawDiff.toDouble())) * distance)

		if (delta <= lockFOV && delta < closestDelta) {
			closestDelta = delta
			closetPlayer = entity
		}
	}

	if (closestDelta == Double.MAX_VALUE) return -1
	return closetPlayer ?: -1
}