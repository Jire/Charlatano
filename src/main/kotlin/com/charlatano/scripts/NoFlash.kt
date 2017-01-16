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

package com.charlatano.scripts

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.entity.Player
import com.charlatano.game.entity.dead
import com.charlatano.game.hooks.onFlash
import com.charlatano.game.netvars.NetVarOffsets.flFlashMaxAlpha
import com.charlatano.game.offsets.ClientOffsets.localPlayer
import com.charlatano.utils.extensions.uint

fun noFlash() = onFlash {
	val me: Player = clientDLL.uint(localPlayer())
	if (me <= 0x200 || me.dead()) return@onFlash
	
	csgoEXE[me + flFlashMaxAlpha] = 0f
}