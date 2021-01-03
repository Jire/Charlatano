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

package com.charlatano.scripts.esp

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.Color
import com.charlatano.game.entity.*
import com.charlatano.game.forEntities
import com.charlatano.game.me
import com.charlatano.game.netvars.NetVarOffsets.m_hViewModel
import com.charlatano.game.offsets.ClientOffsets.dwEntityList
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.game.offsets.EngineOffsets.dwModelAmbientMin
import com.charlatano.settings.*
import com.charlatano.utils.every
import com.charlatano.utils.extensions.uint
import org.jire.kna.set

internal fun glowEsp() = every(if (FLICKER_FREE_GLOW) 1024 else 4) {
	if (!GLOW_ESP) return@every
	
	val myTeam = me.team()
	if (myTeam == 0L) return@every
	
	forEntities body@{
		val entity = it.entity
		if (entity <= 0 || me == entity) return@body false
		
		val glowAddress = it.glowAddress
		if (glowAddress <= 0) return@body false
		
		when (it.type) {
			EntityType.CCSPlayer -> {
				if (entity.dead() || (!SHOW_DORMANT && entity.dormant())) return@body false
				
				val entityTeam = entity.team()
				if (entityTeam == 0L) return@body false
				val team = !DANGER_ZONE && myTeam == entityTeam
				if (SHOW_ENEMIES && !team) {
					glowAddress.glow(ENEMY_COLOR)
					entity.chams(ENEMY_COLOR)
				} else if (SHOW_TEAM && team) {
					glowAddress.glow(TEAM_COLOR)
					entity.chams(TEAM_COLOR)
				}
			}
			EntityType.CPlantedC4, EntityType.CC4 -> if (SHOW_BOMB) {
				glowAddress.glow(BOMB_COLOR)
				entity.chams(BOMB_COLOR)
			}
			else ->
				if (SHOW_WEAPONS && it.type.weapon) glowAddress.glow(WEAPON_COLOR)
				else if (SHOW_GRENADES && it.type.grenade) glowAddress.glow(GRENADE_COLOR)
		}
		
		return@body false
	}
}

private fun Entity.glow(color: Color) {
	csgoEXE[this + 0x4] = color.red / 255F
	csgoEXE[this + 0x8] = color.green / 255F
	csgoEXE[this + 0xC] = color.blue / 255F
	csgoEXE[this + 0x10] = color.alpha.toFloat()
	csgoEXE[this + 0x2C] = MODEL_ESP
	csgoEXE[this + 0x24] = true
}

private fun Entity.chams(color: Color) {
	if (CHAMS) {
		csgoEXE[this + 0x70] = color.red.toByte()
		csgoEXE[this + 0x71] = color.green.toByte()
		csgoEXE[this + 0x72] = color.blue.toByte()
		csgoEXE[this + 0x73] = color.alpha.toInt().toByte()
		
		engineDLL[dwModelAmbientMin] =
			CHAMS_BRIGHTNESS.toFloat().hashCode() xor (engineDLL.address + dwModelAmbientMin - 0x2C).toInt()
		
		// Counter weapon brightness
		val ClientVModEnt =
			csgoEXE.uint(clientDLL.address + dwEntityList + (((csgoEXE.uint(csgoEXE.uint(clientDLL.address + dwLocalPlayer) + m_hViewModel)) and 0xFFF) - 1) * 16) //Probably easier way to do this shit
		csgoEXE[ClientVModEnt + 0x70] = Color(
			(255F / (CHAMS_BRIGHTNESS / 10F)).toInt(),
			0,
			0,
			1.0
		).red.toByte() //Probably easier way to do this shit
		csgoEXE[ClientVModEnt + 0x71] = Color(0, (255F / (CHAMS_BRIGHTNESS / 10F)).toInt(), 0, 1.0).green.toByte()
		csgoEXE[ClientVModEnt + 0x72] = Color(0, 0, (255F / (CHAMS_BRIGHTNESS / 10F)).toInt(), 1.0).blue.toByte()
	}
}
