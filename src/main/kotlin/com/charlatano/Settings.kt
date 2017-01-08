/*
 * Charlatano is a premium CS:GO cheat ran on the JVM.
 * Copyright (C) 2016 Thomas Nappo, Jonathan Beaudoin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.charlatano

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.offsets.ClientOffsets.dwSensitivity
import com.charlatano.game.offsets.ClientOffsets.dwSensitivityPtr
import com.charlatano.utils.extensions.uint
import java.lang.Float.intBitsToFloat

/**
 * Global Settings
 */
const val GAME_PITCH = 0.022

const val GAME_YAW = 0.022

const val MAX_ENTITIES = 1024

const val CLEANUP_TIME = 10_000 //Interval of recycling cached entities

val GAME_SENSITIVITY by lazy(LazyThreadSafetyMode.NONE) {
	val sens_ptr = clientDLL.address + dwSensitivityPtr
	val sens_value = clientDLL.uint(dwSensitivity) xor sens_ptr
	
	intBitsToFloat(sens_value.toInt()).toDouble()
}

const val FIRE_KEY = 1 // Left click

/**
 * Aim Settings
 */

const val FORCE_AIM_KEY = 5 // 5 = mouse forward button
const val AIM_BONE = 8 // 8 = Head

const val AIM_FOV = 200 // field of view, in degrees (0 to 360)
const val AIM_SPEED_MIN = 49 // higher = slower, minimum = 1
const val AIM_SPEED_MAX = 57
const val AIM_DURATION = 1 // duration at which fovAim paths are recalculated

const val AIM_STRICTNESS = 5.6 // higher = less strict

const val AIM_ASSIST_MODE = false
const val AIM_ASSIST_STRICTNESS = 40 // higher = less strict

const val PERFECT_AIM = false
const val PERFECT_AIM_FOV = 22
const val PERFECT_AIM_CHANCE = 100

/**
 * Trigger Bot Settings
 */
const val TRIGGER_FOV = 12

/**
 * RCS Settings
 */
const val RCS_DURATION = 1
const val RCS_SMOOTHING = 66

/**
 * Bunny Hop Settings
 */
val BUNNY_HOP_KEY = 0x20 // spacebar

/**
 * Glow ESP Settings
 */
const val TEAM_COLOR_RED = 0
const val TEAM_COLOR_GREEN = 0
const val TEAM_COLOR_BLUE = 255
const val TEAM_COLOR_ALPHA = 0.6

const val ENEMY_COLOR_RED = 255
const val ENEMY_COLOR_GREEN = 0
const val ENEMY_COLOR_BLUE = 0
const val ENEMY_COLOR_ALPHA = 0.6

const val BOMB_COLOR_RED = 255
const val BOMB_COLOR_GREEN = 255
const val BOMB_COLOR_BLUE = 0
const val BOMB_COLOR_ALPHA = 1.0

const val EQUIPMENT_COLOR_RED = 0
const val EQUIPMENT_COLOR_GREEN = 255
const val EQUIPMENT_COLOR_BLUE = 0
const val EQUIPMENT_COLOR_ALPHA = 0.5

const val SHOW_TEAM = true
const val SHOW_DORMANT = false
const val SHOW_EQUIPMENT = false
const val SHOW_BOMB = true

/**
 * ESP Toggles
 */
const val GLOW_ESP = true
const val SKELETON_ESP = true
const val BOX_ESP = true

/**
 * Misc Toggles
 */
const val OPENGL_GUI = SKELETON_ESP or BOX_ESP