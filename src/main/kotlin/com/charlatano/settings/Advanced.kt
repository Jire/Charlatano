package com.charlatano.settings

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.offsets.ClientOffsets.dwSensitivity
import com.charlatano.game.offsets.ClientOffsets.dwSensitivityPtr
import com.charlatano.utils.Dojo.Setting
import com.charlatano.utils.extensions.uint

val GAME_PITCH: Double by Setting()
val GAME_YAW: Double by Setting()

val GAME_SENSITIVITY by lazy(LazyThreadSafetyMode.NONE) {
	val pointer = clientDLL.address + dwSensitivityPtr
	val value = clientDLL.uint(dwSensitivity) xor pointer
	
	java.lang.Float.intBitsToFloat(value.toInt()).toDouble()
}

val SERVER_TICK_RATE: Int by Setting()
val MAX_ENTITIES: Int by Setting()
val CLEANUP_TIME: Int by Setting()