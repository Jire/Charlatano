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

import com.badlogic.gdx.graphics.Color
import com.charlatano.game.entity.EntityType
import com.charlatano.game.entity.location
import com.charlatano.game.entity.timeLeft
import com.charlatano.game.entityByType
import com.charlatano.game.hooks.bombPlanted
import com.charlatano.game.hooks.location
import com.charlatano.overlay.CharlatanoOverlay

var canDefuse = false

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
		if (location.isEmpty()) return@CharlatanoOverlay

		val textRenderer = textRenderer.get() ?: return@CharlatanoOverlay
		val batch = batch.get() ?: return@CharlatanoOverlay
		batch.begin()

		val bomb = entityByType(EntityType.CPlantedC4)?.entity
		if (bomb == null) {
			location = ""
			return@CharlatanoOverlay
		}
		textRenderer.color = Color.ORANGE
		textRenderer.draw(batch, "Location: $location, ${bomb.timeLeft()} seconds, can defuse? $canDefuse", 20f, 400f)
		batch.end()
	}
}