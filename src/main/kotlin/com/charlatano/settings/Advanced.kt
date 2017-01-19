package com.charlatano.settings

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.offsets.ClientOffsets.dwSensitivity
import com.charlatano.game.offsets.ClientOffsets.dwSensitivityPtr
import com.charlatano.utils.extensions.uint

/**
 * The amount of FPS to run the OpenGL overlay at.
 */
const val OPENGL_GUI_FPS = 60

/**
 * These should be set the same as your in-game "m_pitch" and "m_yaw" values.
 */
const val GAME_PITCH = 0.022 // m_pitch
const val GAME_YAW = 0.022 // m_yaw

/**
 * The value of your in-game "sensitivity" value.
 *
 * This should no longer need to be changed, as it is automatically read.
 */
val GAME_SENSITIVITY by lazy(LazyThreadSafetyMode.NONE) {
	val pointer = clientDLL.address + dwSensitivityPtr
	val value = clientDLL.uint(dwSensitivity) xor pointer
	
	java.lang.Float.intBitsToFloat(value.toInt()).toDouble()
}

/**
 * The maximum amount of entities that can be managed by the cached list.
 */
const val MAX_ENTITIES = 1024

/**
 * The interval in milliseconds to recycle entities in the cached list.
 */
const val CLEANUP_TIME = 10_000