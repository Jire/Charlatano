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

package com.charlatano.game.netvars

object NetVarOffsets {

	val iTeamNum by netVar("DT_BaseEntity")
	val bSpotted by netVar("DT_BaseEntity")

	val fFlags by netVar("DT_BasePlayer")
	val lifeState by netVar("DT_BasePlayer")
	val vecPunch by netVar("DT_BasePlayer", "m_aimPunchAngle")

	val flFlashMaxAlpha by netVar("DT_CSPlayer")
	val iCrossHairID by netVar("DT_CSPlayer", "m_bHasDefuser", 0x5C)
	val iShotsFired by netVar("DT_CSPlayer")

	val dwBoneMatrix by netVar("DT_BaseAnimating", "m_nForceBone", 0x1C)

}