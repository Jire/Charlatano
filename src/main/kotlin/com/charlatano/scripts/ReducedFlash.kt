package com.charlatano.scripts

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.entity.Player
import com.charlatano.game.entity.dead
import com.charlatano.game.hooks.onFlash
import com.charlatano.game.netvars.NetVarOffsets.flFlashMaxAlpha
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.settings.FLASH_MAX_ALPHA
import com.charlatano.utils.extensions.uint

fun reducedFlash() = onFlash {
	val me: Player = clientDLL.uint(dwLocalPlayer)
	if (me <= 0 || me.dead()) return@onFlash
	
	csgoEXE[me + flFlashMaxAlpha] = FLASH_MAX_ALPHA
}