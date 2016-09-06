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

package com.charlatano.scripts

import com.charlatano.*
import com.charlatano.game.CSGO.ENTITY_SIZE
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.EntityType.*
import com.charlatano.game.entity.Player
import com.charlatano.game.entity.team
import com.charlatano.game.entity.type
import com.charlatano.game.hooks.GlowIteration
import com.charlatano.game.netvars.NetVarOffsets
import com.charlatano.game.netvars.NetVarOffsets.hOwnerEntity
import com.charlatano.game.offsets.ClientOffsets.bDormant
import com.charlatano.game.offsets.ClientOffsets.dwEntityList
import com.charlatano.utils.uint


var bombCarrier = -1L
var bombAddress = -1L

fun esp() = GlowIteration {
	if (entity == me) return@GlowIteration
	
	if (entity.type() == CPlantedC4 || entity.type() == CC4) {
		val carrierIndex = (csgoEXE.int(entity + hOwnerEntity) and 0xFFF) - 1
		bombCarrier = if (carrierIndex == 4094 || entity.type() == CPlantedC4) -1
		else clientDLL.uint(dwEntityList + (carrierIndex * ENTITY_SIZE)) // TODO find a way to get glowIndex from entityIndex?
		bombAddress = if (entity.type() == CPlantedC4) entity else -1L
		glowAddress.glow()
	} else if (entity.type() == CCSPlayer) {
		if (csgoEXE.boolean(entity + bDormant) || csgoEXE.uint(entity + NetVarOffsets.lifeState) > 0)
			return@GlowIteration
		
		if (entity == bombCarrier) {
			glowAddress.glow(BOMB_COLOR_RED, BOMB_COLOR_GREEN, BOMB_COLOR_BLUE, BOMB_COLOR_ALPHA)
			entity.chams(BOMB_COLOR_RED, BOMB_COLOR_GREEN, BOMB_COLOR_BLUE)
		} else if (me.team() == entity.team()) {
			glowAddress.glow(TEAM_COLOR_RED, TEAM_COLOR_GREEN, TEAM_COLOR_BLUE, TEAM_COLOR_ALPHA)
			entity.chams(TEAM_COLOR_RED, TEAM_COLOR_GREEN, TEAM_COLOR_BLUE)
		} else {
			glowAddress.glow()
			entity.chams()
		}
		
		/*val vHead = Vector(entityAddress.bone(0xC), entityAddress.bone(0x1C), entityAddress.bone(0x2C) + 9)
		val vFeet = Vector(vHead.x, vHead.y, vHead.z - 75)
		
		val vTop = Vector(0F, 0F, 0F)
		worldToScreen(vHead, vTop)
		
		val vBot = Vector(0F, 0F, 0F)
		worldToScreen(vFeet, vBot)
		
		val h = vBot.y - vTop.y
		val w = h / 5F
		
		val carryingBomb = entityAddress == bombCarrier
		val health = csgoEXE.uint(entityAddress + iHealth)
		
		Overlay {
			color = Color(ENEMY_COLOR_RED, ENEMY_COLOR_GREEN, ENEMY_COLOR_GREEN)
			val sx = (vTop.x - w).toInt()
			val sy = vTop.y.toInt()
			draw3DRect(sx, sy, (w * 2).toInt(), h.toInt(), true)
			color = Color.CYAN
			font = Font("Arial", Font.ITALIC, 24)
			drawString("Health: $health", sx, sy + 50)
			if (carryingBomb) {
				font = Font("Arial", Font.PLAIN, 20)
				color = Color.RED
				drawString("Carrying bomb", sx, sy)
			}
		}*/
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