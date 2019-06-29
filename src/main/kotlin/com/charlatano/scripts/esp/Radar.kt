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

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.entity.Entity
import com.charlatano.game.entity.EntityType.Companion.ccsPlayer
import com.charlatano.game.entity.dead
import com.charlatano.game.entity.dormant
import com.charlatano.game.forEntities
import com.charlatano.game.netvars.NetVarOffsets.bSpotted
import com.charlatano.game.offsets.ClientOffsets.bDormant
import com.charlatano.settings.RADAR
import com.charlatano.utils.every

internal fun radar() = every(1) {
	if (!RADAR) return@every
	
	forEntities(ccsPlayer) {
		val entity = it.entity
		if (entity.dead() || entity.dormant()) return@forEntities false
		entity.show()
		
		false
	}
}

private fun Entity.show() {
	csgoEXE[this + bSpotted] = true
	csgoEXE[this + bDormant] = false
}