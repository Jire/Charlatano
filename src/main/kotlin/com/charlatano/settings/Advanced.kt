package com.charlatano.settings

import com.charlatano.game.CSGO
import com.charlatano.game.offsets.ClientOffsets
import com.charlatano.utils.extensions.uint

const val OPENGL_GUI_FPS = 60

const val GAME_PITCH = 0.022
const val GAME_YAW = 0.022

const val MAX_ENTITIES = 1024
const val CLEANUP_TIME = 10_000 // Interval of recycling cached entities

val GAME_SENSITIVITY by lazy(LazyThreadSafetyMode.NONE) {
	val pointer = CSGO.clientDLL.address + ClientOffsets.dwSensitivityPtr
	val value = CSGO.clientDLL.uint(ClientOffsets.dwSensitivity) xor pointer
	
	java.lang.Float.intBitsToFloat(value.toInt()).toDouble()
}