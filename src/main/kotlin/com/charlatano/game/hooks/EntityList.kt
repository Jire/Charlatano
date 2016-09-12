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

package com.charlatano.game.hooks

import com.charlatano.game.CSGO.GLOW_OBJECT_SIZE
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.EntityType
import com.charlatano.game.entity.Player
import com.charlatano.game.offsets.ClientOffsets.dwGlowObject
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.utils.every
import com.charlatano.utils.uint
import it.unimi.dsi.fastutil.longs.LongArrayList


var me: Player = 0
val entites = LongArrayList(512)
val players = LongArrayList(128)

fun entities() = every(128) {
    me = clientDLL.uint(dwLocalPlayer)
    if (me < 0x200) return@every

    val glowObject = clientDLL.uint(dwGlowObject)
    val glowObjectCount = clientDLL.uint(dwGlowObject + 4)

    for (glowIndex in 0..glowObjectCount) {
        val glowAddress = glowObject + (glowIndex * GLOW_OBJECT_SIZE)
        val entity = csgoEXE.uint(glowAddress)
        val type = EntityType.byEntityAddress(entity)
        if (!players.contains(entity))
            if (type == EntityType.CCSPlayer) players.add(entity)
        if (!entites.contains(entity))
            entites.add(entity)
    }
}