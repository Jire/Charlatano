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

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.hooks.bombPlanted
import com.charlatano.game.hooks.location
import com.charlatano.game.netvars.NetVarOffsets.flC4Blow
import com.charlatano.game.offsets.EngineOffsets.dwGlobalVars
import com.charlatano.overlay.Overlay
import java.awt.Color

fun bombTimer() = bombPlanted {
	val timeLeft = -(engineDLL.float(dwGlobalVars + 16) - csgoEXE.float(bombAddress + flC4Blow))
	if (timeLeft < 0)
		return@bombPlanted
	
	val hasKit = false
	val canDefuse = timeLeft >= if (hasKit) 5 else 10
	
	Overlay {
		color = Color.ORANGE
		drawString("Location: $location, $timeLeft seconds, can defuse? $canDefuse", 200, 200)
	}
}