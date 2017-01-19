package com.charlatano.game.hooks

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.entity.Player
import com.charlatano.game.entity.dead
import com.charlatano.game.netvars.NetVarOffsets.flFlashMaxAlpha
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.utils.extensions.uint
import com.charlatano.utils.hook

val onFlash = hook(256) {
	val me: Player = clientDLL.uint(dwLocalPlayer)
	if (me <= 0x200 || me.dead()) return@hook false
	
	val flashAlpha = csgoEXE.float(me + flFlashMaxAlpha)
	flashAlpha > 0f
}