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
import com.charlatano.game.offsets.EngineOffsets.dwViewAngles
import com.charlatano.utils.Angle
import com.charlatano.utils.readCached
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import org.jire.kna.float
import org.jire.kna.set

typealias ClientState = Long

private val clientState2Angle: Long2ObjectMap<Angle> = Long2ObjectOpenHashMap()

fun ClientState.angle(): Angle = readCached(clientState2Angle) {
	x = csgoEXE.float(it + dwViewAngles).toDouble()
	y = csgoEXE.float(it + dwViewAngles + 4).toDouble()
	z = csgoEXE.float(it + dwViewAngles + 8).toDouble()
}

fun ClientState.setAngle(angle: Angle) {
	if (angle.z != 0.0 || angle.x < -89 || angle.x > 180 || angle.y < -180 || angle.y > 180
			|| angle.x.isNaN() || angle.y.isNaN() || angle.z.isNaN()) return
	
	csgoEXE[this + dwViewAngles] = angle.x.toFloat() // pitch (up and down)
	csgoEXE[this + dwViewAngles + 4] = angle.y.toFloat() // yaw (side to side)
	// csgo[address + m_dwViewAngles + 8] = angle.z.toFloat() // roll (twist)
}