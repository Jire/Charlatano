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
import com.charlatano.game.netvars.NetVarOffsets
import com.charlatano.game.netvars.NetVarOffsets.lifeState
import com.charlatano.game.offsets.ClientOffsets
import org.jire.arrowhead.get

typealias Entity = Long

fun Entity.dead(): Boolean = csgoEXE.byte(this + lifeState) != 0.toByte()

fun Entity.spotted(): Boolean = csgoEXE.int(this + NetVarOffsets.bSpotted) != 0

fun Entity.dormant(): Boolean = csgoEXE[this + ClientOffsets.bDormant]

fun Entity.team(): Int = csgoEXE[this + NetVarOffsets.iTeamNum]

fun Entity.type(): EntityType = EntityType.byEntityAddress(this)