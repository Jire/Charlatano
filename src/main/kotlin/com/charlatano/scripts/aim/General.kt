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

package com.charlatano.scripts.aim

import com.charlatano.game.*
import com.charlatano.game.entity.*
import com.charlatano.game.entity.EntityType.Companion.ccsPlayer
import com.charlatano.game.offsets.ScaleFormOffsets
import com.charlatano.scripts.*
import com.charlatano.settings.*
import com.charlatano.utils.*
import org.jire.arrowhead.keyPressed
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

val target = AtomicLong(-1)
val bone = AtomicInteger(HEAD_BONE)
val perfect = AtomicBoolean() // only applicable for safe aim

internal fun reset() {
	target.set(-1L)
	bone.set(if (AIM_AT_HEAD) HEAD_BONE else BODY_BONE)
	perfect.set(false)
}

internal fun findTarget(position: Angle, angle: Angle, allowPerfect: Boolean,
                        lockFOV: Int = AIM_FOV, boneID: Int = HEAD_BONE): Player {
	var closestDelta = Double.MAX_VALUE
	var closestPlayer = -1L
	var FOV = lockFOV
	if (keyPressed(FORCE_AIM_KEY) && FOV != BONE_TRIGGER_FOV)
		FOV = FORCE_AIM_FOV
	
	var closestFOV = Double.MAX_VALUE
	
	forEntities(ccsPlayer) {
		val entity = it.entity
		if (entity <= 0) return@forEntities
		if (ENABLE_RAGE && keyPressed(FORCE_AIM_KEY) && entity.canShootWall()) FOV = 360
		else if (!entity.canShoot() && !IGNORE_WALLS) return@forEntities
		else if (!entity.canShootWall()) return@forEntities
		
		val ePos: Angle = entity.bones(boneID)
		val distance = position.distanceTo(ePos)
		
		val dest = calculateAngle(me, ePos)
		
		val pitchDiff = Math.abs(angle.x - dest.x)
		val yawDiff = Math.abs(angle.y - dest.y)
		val delta = Math.abs(Math.sin(Math.toRadians(yawDiff)) * distance)
		val fovDelta = Math.abs((Math.sin(Math.toRadians(pitchDiff)) + Math.sin(Math.toRadians(yawDiff))) * distance)
		
		if (fovDelta <= FOV || ENABLE_RAGE) {
			if (delta < closestDelta)
			{
				closestDelta = delta
				closestPlayer = entity
				closestFOV = fovDelta
			}
		}
	}
	
	if (closestDelta == Double.MAX_VALUE || closestDelta < 0 || closestPlayer < 0) return -1
	
	if (PERFECT_AIM && allowPerfect && closestFOV <= PERFECT_AIM_FOV && randInt(100 + 1) <= PERFECT_AIM_CHANCE)
		perfect.set(true)
	
	return closestPlayer
}

internal fun Entity.canShoot()
		= spotted()
		&& !dormant()
		&& !dead()
		&& me.team() != team()
		&& !me.dead()
		
internal fun Entity.canShootWall()
		= !dormant()
		&& !dead()
		&& me.team() != team()
		&& !me.dead()

internal inline fun <R> aimScript(duration: Int, crossinline precheck: () -> Boolean,
                                  crossinline doAim: (destinationAngle: Angle,
                                                      currentAngle: Angle, aimSpeed: Int) -> R)
		= every(duration) {
	if (!precheck()) return@every
	
	val aim = ACTIVATE_FROM_FIRE_KEY && keyPressed(FIRE_KEY)
	val forceAim = keyPressed(FORCE_AIM_KEY)
	val pressed = aim or forceAim or ENABLE_RAGE
	var currentTarget = target.get()
	
	if (!pressed || CSGO.scaleFormDLL.boolean(ScaleFormOffsets.bCursorEnabled)) {
		reset()
		return@every
	}
	
	if (!CLASSIC_OFFENSIVE) {
		val weapon = me.weapon()
		if (!weapon.pistol && !weapon.automatic && !weapon.shotgun && !weapon.sniper) {
			reset()
			return@every
		}
	}
	
	val currentAngle = clientState.angle()
	
	val position = me.position()
	if (currentTarget < 0) {
		currentTarget = findTarget(position, currentAngle, aim)
		if (currentTarget < 0) {
			Thread.sleep(16 + randLong(16))
			return@every
		}
		target.set(currentTarget)
	}
	
	if (!currentTarget.canShootWall()) {
		reset()
		Thread.sleep(16 + randLong(16))
	} else if (ENABLE_AIM) {
		val weapon = me.weapon()
		val boneID = bone.get()
		val bonePosition = currentTarget.bones(boneID)
		
		val destinationAngle = calculateAngle(me, bonePosition)
		if (AIM_ASSIST_MODE) destinationAngle.finalize(currentAngle, AIM_ASSIST_STRICTNESS / 100.0)
		
		val aimSpeed = AIM_SPEED_MIN + randInt(AIM_SPEED_MAX - AIM_SPEED_MIN)
		if (!ENABLE_RAGE)
			doAim(destinationAngle, currentAngle, aimSpeed)
		else{
			if (weapon.sniper && !me.isScoped())
				return@every
			doAim(destinationAngle, currentAngle, 1)
			if (currentTarget.canShoot())
				click()
			else 
				reset()
		}
	}
}
