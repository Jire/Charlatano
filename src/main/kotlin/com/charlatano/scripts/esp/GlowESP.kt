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

package com.charlatano.scripts.esp

import com.charlatano.*
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.entities
import com.charlatano.game.entity.*
import com.charlatano.game.me
import com.charlatano.utils.every

fun glowEsp() = every(4) {
	entities() {
		val entity = it.entity
		if (entity <= 0 || me == entity) return@entities

		val glowAddress = it.glowAddress
		if (glowAddress <= 0) return@entities

		val type = it.type
		if (EntityType.CCSPlayer == type) {
			if (entity.dead() || (!SHOW_DORMANT && entity.dormant())) return@entities

			val team = me.team() == entity.team()
			if (!team) {
				glowAddress.glow(ENEMY_COLOR_RED, ENEMY_COLOR_GREEN, ENEMY_COLOR_BLUE, ENEMY_COLOR_ALPHA)
				entity.chams(ENEMY_COLOR_RED, ENEMY_COLOR_GREEN, ENEMY_COLOR_BLUE)
			} else if (SHOW_TEAM && team) {
				glowAddress.glow(TEAM_COLOR_RED, TEAM_COLOR_GREEN, TEAM_COLOR_BLUE, TEAM_COLOR_ALPHA)
				entity.chams(TEAM_COLOR_RED, TEAM_COLOR_GREEN, TEAM_COLOR_BLUE)
			}
		} else if (type.weapon || type.grenade) {
			if (SHOW_EQUIPMENT) {
				glowAddress.glow(EQUIPMENT_COLOR_RED, EQUIPMENT_COLOR_GREEN, EQUIPMENT_COLOR_BLUE,
						if (type.grenade) 1.0 else EQUIPMENT_COLOR_ALPHA)
				//entity.chams(EQUIPMENT_COLOR_RED, EQUIPMENT_COLOR_GREEN, EQUIPMENT_COLOR_BLUE)
			}
		} else if (EntityType.CPlantedC4 == type || EntityType.CC4 == type) {
			if (SHOW_BOMB) {
				glowAddress.glow(BOMB_COLOR_RED, BOMB_COLOR_GREEN, BOMB_COLOR_BLUE, BOMB_COLOR_ALPHA)
				entity.chams(BOMB_COLOR_RED, BOMB_COLOR_GREEN, BOMB_COLOR_BLUE)
			}
		}
	}
}

fun Entity.glow(red: Int, green: Int, blue: Int, alpha: Double) {
	csgoEXE[this + 0x4] = red / 255F
	csgoEXE[this + 0x8] = green / 255F
	csgoEXE[this + 0xC] = blue / 255F
	csgoEXE[this + 0x10] = alpha.toFloat()
	csgoEXE[this + 0x24] = true
}

fun Entity.chams(red: Int, green: Int, blue: Int, alpha: Int = 255) {
	csgoEXE[this + 0x70] = red.toByte()
	csgoEXE[this + 0x71] = green.toByte()
	csgoEXE[this + 0x72] = blue.toByte()
	csgoEXE[this + 0x73] = alpha.toByte()
}