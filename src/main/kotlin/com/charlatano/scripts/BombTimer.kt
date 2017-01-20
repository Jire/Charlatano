package com.charlatano.scripts

import com.badlogic.gdx.graphics.Color
import com.charlatano.game.entity.EntityType
import com.charlatano.game.entity.location
import com.charlatano.game.entity.timeLeft
import com.charlatano.game.entityByType
import com.charlatano.game.hooks.bombPlanted
import com.charlatano.game.hooks.location
import com.charlatano.overlay.CharlatanoOverlay

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
		if (location.isEmpty()) return@CharlatanoOverlay
		
		val bomb = entityByType(EntityType.CPlantedC4)?.entity
		if (bomb == null) {
			location = ""
			return@CharlatanoOverlay
		}
		
		val tr = textRenderer.get()
		val batch = batch.get()
		batch.begin()
		tr.color = Color.ORANGE
		tr.draw(batch, "Location: $location, ${bomb.timeLeft()} seconds, can defuse? $canDefuse", 20f, 400f)
		batch.end()
	}
}