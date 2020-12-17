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

package com.charlatano.game

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.entity.Player
import com.charlatano.game.entity.position
import com.charlatano.game.entity.punch
import com.charlatano.game.netvars.NetVarOffsets.vecViewOffset
import com.charlatano.utils.Angle
import com.charlatano.utils.Vector
import com.charlatano.utils.normalize
import org.jire.kna.float
import java.lang.Math.toDegrees
import kotlin.math.atan
import kotlin.math.sqrt

private val angles: ThreadLocal<Angle> = ThreadLocal.withInitial { Vector() }

fun calculateAngle(player: Player, dst: Vector): Angle = angles.get().apply {
	val myPunch = player.punch()
	val myPosition = player.position()
	
	val dX = myPosition.x - dst.x
	val dY = myPosition.y - dst.y
	val dZ = myPosition.z + csgoEXE.float(player + vecViewOffset) - dst.z
	
	val hyp = sqrt((dX * dX) + (dY * dY))
	
	x = toDegrees(atan(dZ / hyp)) - myPunch.x * 2.0
	y = toDegrees(atan(dY / dX)) - myPunch.y * 2.0
	z = 0.0
	if (dX >= 0.0) y += 180
	
	normalize()
}