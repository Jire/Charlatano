package com.charlatano.game.hooks

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.scaleFormDLL
import com.charlatano.game.entity.Player
import com.charlatano.game.entity.dead
import com.charlatano.game.entity.onGround
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.game.offsets.ScaleFormOffsets
import com.charlatano.settings.BUNNY_HOP_KEY
import com.charlatano.utils.extensions.uint
import com.charlatano.utils.hook

val onGround = hook(4) {
	val me: Player = clientDLL.uint(dwLocalPlayer)
	if (me <= 0 || me.dead()) return@hook false
	
	me.onGround() && !scaleFormDLL.boolean(ScaleFormOffsets.bCursorEnabled)
}