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
import com.charlatano.game.me
import com.charlatano.game.entity.EntityType
import com.charlatano.game.entity.hasDefuser
import com.charlatano.game.entity.location
import com.charlatano.game.entity.timeLeft
import com.charlatano.game.entity.planted
import com.charlatano.game.entity.defused
import com.charlatano.game.entityByType
import com.charlatano.game.hooks.bombPlanted
import com.charlatano.game.hooks.location
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.settings.ENABLE_BOMB_TIMER

@Volatile var canDefuse = false

fun bombTimer() {
	if (!ENABLE_BOMB_TIMER) return

	bombPlanted {
		val hasKit = me.hasDefuser()
		val entityByType = entityByType(EntityType.CPlantedC4)
		if (entityByType == null) {
			location = ""
			return@bombPlanted
		}

		val bomb = entityByType.entity
		canDefuse = bomb.timeLeft() >= if (hasKit) 5 else 10

		if (location.isEmpty()) location = bomb.location()
	}
	
	CharlatanoOverlay {
		if (ENABLE_BOMB_TIMER) {
			// TODO: Bomb timer sometimes carries over into next round.
			// TODO: Location can be "unknown." Not sure why.
			
			val bomb = entityByType(EntityType.CPlantedC4)?.entity
			if (bomb == null) {
				location = ""
				return@CharlatanoOverlay
			}

			if (!bomb.planted() || bomb.defused()) return@CharlatanoOverlay

			if (location.isEmpty()) location = "unknown"
			
			batch.begin()
			textRenderer.color = Color.ORANGE
			textRenderer.draw(batch, "Location: $location\n" +
					"${"%.3f".format(bomb.timeLeft())} seconds\ncan defuse? $canDefuse",
					20F, 500F)
			batch.end()
		}
	}
}