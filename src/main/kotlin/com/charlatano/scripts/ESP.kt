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

import com.charlatano.game.CSGO.ENTITY_SIZE
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.EntityType.*
import com.charlatano.game.EntityType.Companion.byEntityAddress
import com.charlatano.game.hooks.GlowIteration
import com.charlatano.game.netvars.NetVarOffsets
import com.charlatano.game.netvars.NetVarOffsets.hOwnerEntity
import com.charlatano.game.netvars.NetVarOffsets.iHealth
import com.charlatano.game.netvars.NetVarOffsets.iTeamNum
import com.charlatano.game.offsets.ClientOffsets.bDormant
import com.charlatano.game.offsets.ClientOffsets.dwEntityList
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.overlay.Overlay
import com.charlatano.utils.Vector
import com.charlatano.utils.uint
import com.charlatano.worldToScreen
import java.awt.Color
import java.awt.Font

var bombCarrier = -1L
var bombAddress = -1L

fun esp() = GlowIteration {
	if (entityAddress == clientDLL.uint(dwLocalPlayer)) return@GlowIteration
	val type = byEntityAddress(entityAddress)
	if (type == CPlantedC4 || type == CC4) {
		val carrierIndex = (csgoEXE.int(entityAddress + hOwnerEntity) and 0xFFF) - 1
		bombCarrier = if (carrierIndex == 4094 || type == CPlantedC4) -1
		else clientDLL.uint(dwEntityList + (carrierIndex * ENTITY_SIZE)) // TODO find a way to get glowIndex from entityIndex?
		bombAddress = if (type == CPlantedC4) entityAddress else -1L
		glowAddress.glow()
	} else if (type == CCSPlayer) {
		if (csgoEXE.boolean(entityAddress + bDormant) || csgoEXE.uint(entityAddress + NetVarOffsets.lifeState) > 0)
			return@GlowIteration

		var red = 255
		var green = 0
		var blue = 0

		val entityTeam = csgoEXE.uint(entityAddress + iTeamNum)
		if (entityAddress == bombCarrier) {
			red = 0
			green = 255
		} else if (myTeam == entityTeam) {
			red = 0
			blue = 255
		}
		glowAddress.glow(red, green, blue)

		val vHead = Vector(entityAddress.bone(0xC), entityAddress.bone(0x1C), entityAddress.bone(0x2C) + 9)
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
			color = Color(red, green, blue)
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
		}
	}
}

fun Long.glow(red: Int = 255, green: Int = 0, blue: Int = 0, alpha: Float = 0.6F) {
	csgoEXE[this + 0x4] = red / 255F
	csgoEXE[this + 0x8] = green / 255F
	csgoEXE[this + 0xC] = blue / 255F
	csgoEXE[this + 0x10] = alpha
	csgoEXE[this + 0x24] = true
}