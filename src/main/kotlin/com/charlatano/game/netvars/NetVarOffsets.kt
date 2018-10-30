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

package com.charlatano.game.netvars

object NetVarOffsets {
	
	val dwModel = 0x6C
	val iTeamNum by netVar("DT_BaseEntity")
	val bSpotted by netVar("DT_BaseEntity")
	val bSpottedByMask by netVar("DT_BaseEntity")
	val vecOrigin by netVar("DT_BaseEntity")
	
	val iCompetitiveRanking by netVar("DT_CSPlayerResource")
	
	val fFlags by netVar("DT_BasePlayer")
	val lifeState by netVar("DT_BasePlayer")
	val vecPunch by netVar("DT_BasePlayer", "m_aimPunchAngle")
	val szLastPlaceName by netVar("DT_BasePlayer")
	val iHealth by netVar("DT_BasePlayer")
	val vecViewOffset by netVar("DT_BasePlayer", "m_vecViewOffset[0]")
	val vecVelocity by netVar("DT_BasePlayer", "m_vecVelocity[0]")
	val hActiveWeapon by netVar("DT_BasePlayer", "m_hActiveWeapon")
	val nTickBase by netVar("DT_BasePlayer")
	
	val flFlashMaxAlpha by netVar("DT_CSPlayer")
	val iCrossHairID by netVar("DT_CSPlayer", "m_bHasDefuser", 0x5C)
	val iShotsFired by netVar("DT_CSPlayer")
	val bIsScoped by netVar("DT_CSPlayer")
	val bHasDefuser by netVar("DT_CSPlayer", "m_bHasDefuser")
	
	val flC4Blow by netVar("DT_PlantedC4")
	val bBombDefused by netVar("DT_PlantedC4")
	val hBombDefuser by netVar("DT_PlantedC4")
	val flDefuseCountDown by netVar("DT_PlantedC4")

	val hOwnerEntity by netVar("DT_BaseEntity")

	val dwBoneMatrix by netVar("DT_BaseAnimating", "m_nForceBone", 0x1C)
	
	val flNextPrimaryAttack by netVar("DT_BaseCombatWeapon")
	val iClip1 by netVar("DT_BaseCombatWeapon")
	val iClip2 by netVar("DT_BaseCombatWeapon")

	val iItemDefinitionIndex by netVar("DT_BaseCombatWeapon")
}
