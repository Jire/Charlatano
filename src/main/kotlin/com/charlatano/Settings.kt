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

/**
 * Aim Settings
 */

const val FORCE_AIM_KEY = 5 // 5 = mouse forward button
const val AIM_BONE = 8 // 6 = Head

const val AIM_FOV = 140 // field of view, in degrees (0 to 360)
const val AIM_SPEED_MIN = 9 // higher = slower, minimum = 1
const val AIM_SPEED_MAX = 12
const val AIM_DURATION = 1 // duration at which aim paths are recalculated

const val AIM_STRICTNESS = 2.1/*2.0*//*2.6*/ // higher = less strict
const val AIM_STRICTNESS_BASELINE_MODIFIER = 0.85 // multiplied by the aim strictness when baseline is met
const val AIM_STRICTNESS_BASELINE_DISTANCE = 900 // the distance at which strictness scales
const val AIM_VELOCITY_STRICTNESS = 15 // higher = more strict

const val AIM_ASSIST_MODE = false
const val AIM_ASSIST_STRICTNESS = 40 // higher = less strict

const val PERFECT_AIM = false
const val PERFECT_AIM_FOV = 100
const val PERFECT_AIM_CHANCE = 100

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
 * ESP toggles
 */
const val SKELETON_ESP = false
const val GLOW_ESP = true
const val BOX_ESP = false