package com.charlatano.game.hooks

import com.charlatano.game.entity.EntityType
import com.charlatano.game.entity.planted
import com.charlatano.game.entityByType
import com.charlatano.utils.hook

var location = ""

val bombPlanted = hook(8) {
	val bomb = entityByType(EntityType.CPlantedC4)?.entity ?: return@hook false
	
	val planted = bomb.planted()
	if (!planted) location = ""
	
	planted
}