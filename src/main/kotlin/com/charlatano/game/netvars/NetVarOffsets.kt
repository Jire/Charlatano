package com.charlatano.game.netvars

object NetVarOffsets {

	val dwModel = 0x6C
	val iTeamNum by netVar("DT_BaseEntity")
	val bSpotted by netVar("DT_BaseEntity")
	val bSpottedByMask by netVar("DT_BaseEntity")
	val vecOrigin by netVar("DT_BaseEntity")

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
	
	val flC4Blow by netVar("DT_PlantedC4")
	val bBombDefused by netVar("DT_PlantedC4")
	val hOwnerEntity by netVar("DT_PlantedC4")

	val dwBoneMatrix by netVar("DT_BaseAnimating", "m_nForceBone", 0x1C)
	
	val flNextPrimaryAttack by netVar("DT_BaseCombatWeapon")
	val iClip1 by netVar("DT_BaseCombatWeapon")
	val iClip2 by netVar("DT_BaseCombatWeapon")
	
	val iWeaponID by netVar("DT_WeaponCSBase", "m_fAccuracyPenalty", 0x30)
}