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

package com.charlatano.scripts

import com.badlogic.gdx.graphics.Color
import com.charlatano.game.entity.EntityType
import com.charlatano.game.entity.location
import com.charlatano.game.entity.timeLeft
import com.charlatano.game.entityByType
import com.charlatano.game.hooks.bombPlanted
import com.charlatano.game.hooks.location
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.settings.ENABLE_BOMB_TIMER

@Volatile var canDefuse = false

fun bombTimer() {
	bombPlanted {
		val hasKit = false
		val bomb = entityByType(EntityType.CPlantedC4)?.entity
		if (bomb == null) {
			location = ""
			return@bombPlanted
		}
		canDefuse = bomb.timeLeft() >= if (hasKit) 5 else 10
		
		if (location.isEmpty()) location = bomb.location()
	}
	
	CharlatanoOverlay {
		if (ENABLE_BOMB_TIMER) {
			if (location.isEmpty()) return@CharlatanoOverlay
			
			val bomb = entityByType(EntityType.CPlantedC4)?.entity
			if (bomb == null) {
				location = ""
				return@CharlatanoOverlay
			}
			
			batch.begin()
			textRenderer.color = Color.ORANGE
			textRenderer.draw(batch, "Location: $location," +
					"${bomb.timeLeft()} seconds, can defuse? $canDefuse", 20F, 400F)
			batch.end()
		}
	}
}