package com.charlatano.settings

const val FORCE_AIM_KEY = 5 // 5 = mouse forward button
val HEAD_BONE = if (CLASSIC_OFFENSIVE) 6 else 8
val SHOULDER_BONE = if (CLASSIC_OFFENSIVE) 5 else 7
val BODY_BONE = if (CLASSIC_OFFENSIVE) 4 else 6

const val SHIFT_TO_SHOULDER_SHOTS = 4 // amount of shots to spray before switching to shoulder bone
const val SHIFT_TO_BODY_SHOTS = 6 // amount of shots before switching to body bone

const val AIM_FOV = 180 // field of view, in degrees (0 to 360)
const val AIM_SPEED_MIN = 36 // higher = slower, minimum = 1
const val AIM_SPEED_MAX = 44

const val AIM_DURATION = 1 // duration at which fovAim paths are recalculated

const val AIM_STRICTNESS = 2.4 // higher = less strict
const val AIM_ASSIST_MODE = false

const val AIM_ASSIST_STRICTNESS = 40 // higher = less strict
const val PERFECT_AIM = false
const val PERFECT_AIM_FOV = 30

const val PERFECT_AIM_CHANCE = 100