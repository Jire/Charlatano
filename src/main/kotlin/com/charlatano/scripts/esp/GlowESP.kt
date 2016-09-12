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

package com.charlatano.scripts.esp

import com.charlatano.*
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.EntityType
import com.charlatano.game.entity.*
import com.charlatano.game.hooks.entites
import com.charlatano.game.hooks.me
import com.charlatano.game.hooks.players
import com.charlatano.utils.every

var bomb: Bomb = -1L

fun glowEsp() = every(32) {
    for (i in 0..entites.size - 1) {//TODO clean this up alot
        val entity = players.getLong(i)
        if (entity == me) continue

        if (entity.type() == EntityType.CPlantedC4 || entity.type() == EntityType.CC4) {
            bomb = if (entity.type() == EntityType.CPlantedC4 || (entity.type() == EntityType.CC4 && !bomb.planted())) entity else -1L
            // glowAddress.glow()
        } else if (entity.type() == EntityType.CCSPlayer) {
            if (entity.dead() || entity.dormant()) continue

            if (bomb > -1 && entity == bomb.planter()) {
                //  glowAddress.glow(BOMB_COLOR_RED, BOMB_COLOR_GREEN, BOMB_COLOR_BLUE, BOMB_COLOR_ALPHA)
                entity.chams(BOMB_COLOR_RED, BOMB_COLOR_GREEN, BOMB_COLOR_BLUE)
            } else if (me.team() == entity.team()) {
                // glowAddress.glow(TEAM_COLOR_RED, TEAM_COLOR_GREEN, TEAM_COLOR_BLUE, TEAM_COLOR_ALPHA)
                entity.chams(TEAM_COLOR_RED, TEAM_COLOR_GREEN, TEAM_COLOR_BLUE)
            } else {
                // glowAddress.glow()
                entity.chams()
            }
        }
    }
}

fun Player.glow(red: Int = ENEMY_COLOR_RED, green: Int = ENEMY_COLOR_GREEN, blue: Int = ENEMY_COLOR_BLUE, alpha: Float = ENEMY_COLOR_ALPHA) {
    csgoEXE[this + 0x4] = red / 255F
    csgoEXE[this + 0x8] = green / 255F
    csgoEXE[this + 0xC] = blue / 255F
    csgoEXE[this + 0x10] = alpha
    csgoEXE[this + 0x24] = true
}

fun Player.chams(red: Int = ENEMY_COLOR_RED, green: Int = ENEMY_COLOR_GREEN, blue: Int = ENEMY_COLOR_BLUE) {
    csgoEXE[this + 0x70] = red.toByte()
    csgoEXE[this + 0x71] = green.toByte()
    csgoEXE[this + 0x72] = blue.toByte()
    csgoEXE[this + 0x73] = 255.toByte()
}