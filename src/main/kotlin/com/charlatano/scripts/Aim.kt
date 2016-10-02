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
import java.util.concurrent.ThreadLocalRandom.current as tlr

private var target: Player = -1L

fun aim() = every(AIM_DURATION) {
	val pressed = keyPressed(1) or keyPressed(FORCE_AIM_KEY)
	if (!pressed) {
		target = -1L
		return@every
	}
	
	val currentAngle = clientState.angle()
	
	var currentTarget = target
	if (currentTarget == -1L) {
		currentTarget = findTarget(me.position(), currentAngle)
		if (currentTarget == -1L) return@every
		target = currentTarget
		return@every
	}
	
	if (me.dead() || target.dead() || target.dormant() || !target.spotted() || target.team() == me.team()) {
		target = -1L
		return@every
	}
	
	val bonePosition = Vector(target.bone(0xC), target.bone(0x1C), target.bone(0x2C))
	compensateVelocity(me, target, bonePosition, AIM_CALCULATION_SMOOTHING)
	
	val dest = calculateAngle(me, bonePosition)
	if (AIM_ASSIST_MODE) dest.finalize(currentAngle, AIM_CALCULATION_SMOOTHING) else dest.normalize()
	
	aim(currentAngle, dest, AIM_SMOOTHING)
}

private fun findTarget(position: Angle, angle: Angle, lockFOV: Int = AIM_FOV): Player {
	var closestDelta = Int.MAX_VALUE
	var closetPlayer: Player? = null
	
	entities(EntityType.CCSPlayer) {
		val entity = it.entity
		if (entity <= 0) return@entities
		if (entity == me || entity.team() == me.team()) return@entities
		
		if (me.dead() || entity.dead() /*|| !entity.spotted() */ || entity.dormant()) return@entities
		
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