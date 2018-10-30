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

package com.charlatano.scripts

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.charlatano.game.*
import com.charlatano.game.entity.*
import com.charlatano.scripts.aim.bone
import com.charlatano.settings.*
import com.charlatano.utils.every
import com.charlatano.utils.normalize
import com.charlatano.utils.randDouble

/**
 * Affects the shakiness of the screen,
 * higher value = low shake, high inaccuracy
 */
private const val rcsDelay = 75
private val lastPunch = Vector2()
private val newPunch = Vector2()
private val playerPunch = Vector3()
private var lastApplied = System.currentTimeMillis()
private var time = lastApplied

fun rcs() = every(RCS_DURATION) {
	if (me <= 0 || !ENABLE_RCS) return@every
	val weaponEntity = me.weaponEntity()
	val weapon = me.weapon(weaponEntity)
	if (weapon.boltAction || weapon == Weapons.DESERT_EAGLE) return@every
	val shotsFired = me.shotsFired()
	time = System.currentTimeMillis()
	if ((time - lastApplied > rcsDelay && shotsFired > 0) || (shotsFired < 1 && !lastPunch.isZero) || weaponEntity.bullets() < 1) {
		lastApplied = time
		val p = me.punch()
		playerPunch.set(p.x.toFloat(), p.y.toFloat(), p.z.toFloat())
		newPunch.set(playerPunch.x - lastPunch.x, playerPunch.y - lastPunch.y)
		newPunch.scl((if (RCS_MAX > RCS_MIN) randDouble(RCS_MIN, RCS_MAX) else RCS_MIN).toFloat(),
				(if (RCS_MAX > RCS_MIN) randDouble(RCS_MIN, RCS_MAX) else RCS_MIN).toFloat())

		val angle = clientState.angle()
		angle.apply {
			x -= newPunch.x
			y -= newPunch.y
			normalize()
		}
		clientState.setAngle(angle)
		lastPunch.x = playerPunch.x
		lastPunch.y = playerPunch.y
	}

	bone.set(when {
		shotsFired >= SHIFT_TO_BODY_SHOTS -> BODY_BONE
		shotsFired >= SHIFT_TO_SHOULDER_SHOTS -> SHOULDER_BONE
		else -> HEAD_BONE
	})
}