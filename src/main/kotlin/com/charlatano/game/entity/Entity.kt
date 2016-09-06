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

package com.charlatano.game.entity

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.EntityType
import com.charlatano.game.EntityType.Companion.byEntityAddress
import com.charlatano.game.netvars.NetVarOffsets.bSpotted
import com.charlatano.game.netvars.NetVarOffsets.iHealth
import com.charlatano.game.netvars.NetVarOffsets.iTeamNum
import com.charlatano.game.netvars.NetVarOffsets.lifeState
import com.charlatano.game.offsets.ClientOffsets.bDormant
import org.jire.arrowhead.get

typealias Entity = Long

fun Entity.dead(): Boolean = csgoEXE.byte(this + lifeState) != 0.toByte()

fun Entity.spotted(): Boolean = csgoEXE.int(this + bSpotted) != 0

fun Entity.dormant(): Boolean = csgoEXE[this + bDormant]

fun Entity.team(): Int = csgoEXE[this + iTeamNum]

fun Entity.health(): Int = csgoEXE[this + iHealth]

fun Entity.type(): EntityType = byEntityAddress(this)