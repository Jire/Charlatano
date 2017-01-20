package com.charlatano.scripts.esp

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.entities
import com.charlatano.game.entity.*
import com.charlatano.game.me
import com.charlatano.settings.*
import com.charlatano.utils.every

internal fun glowEsp() = every(4) {
	entities {
		val entity = it.entity
		if (entity <= 0 || me == entity) return@entities
		
		val glowAddress = it.glowAddress
		if (glowAddress <= 0) return@entities
		
		when (it.type) {
			EntityType.CCSPlayer -> {
				if (entity.dead() || (!SHOW_DORMANT && entity.dormant())) return@entities
				
				val team = me.team() == entity.team()
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
	}
}

private fun Entity.glow(color: GameColor) {
	csgoEXE[this + 0x4] = color.red / 255F
	csgoEXE[this + 0x8] = color.green / 255F
	csgoEXE[this + 0xC] = color.blue / 255F
	csgoEXE[this + 0x10] = color.alpha.toFloat()
	csgoEXE[this + 0x24] = true
}

private fun Entity.chams(color: GameColor) {
/*	csgoEXE[this + 0x70] = color.red.toByte()
	csgoEXE[this + 0x71] = color.green.toByte()
	csgoEXE[this + 0x72] = color.blue.toByte()
	csgoEXE[this + 0x73] = color.alpha.toByte()*/
}