package com.charlatano.settings

import com.charlatano.utils.Dojo.Setting

val FORCE_AIM_KEY: Int by Setting()

val AIM_FOV: Int by Setting()

val AIM_SPEED_MIN: Int by Setting()
val AIM_SPEED_MAX: Int by Setting()

val AIM_STRICTNESS: Double by Setting()

val PERFECT_AIM: Boolean by Setting()
val PERFECT_AIM_FOV: Int by Setting()
val PERFECT_AIM_CHANCE: Int by Setting()

val AIM_ASSIST_MODE: Boolean by Setting()
val AIM_ASSIST_STRICTNESS: Int by Setting()

val AIM_DURATION: Int by Setting()

val SHIFT_TO_SHOULDER_SHOTS: Int by Setting()
val SHIFT_TO_BODY_SHOTS: Int by Setting()