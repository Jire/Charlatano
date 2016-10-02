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

import java.awt.event.KeyEvent

/**
 * Aim Settings
 */
const val AIM_DURATION = 16
const val FORCE_AIM_KEY = 5 // 5 = Mouse forward button
const val AIM_BONE = 6 // 6 = Head
const val AIM_FOV = Int.MAX_VALUE
const val AIM_SMOOTHING = 1 // 100 = slowest, 1 = fastest
const val AIM_ASSIST_MODE = true
const val AIM_CALCULATION_SMOOTHING = 40F

/**
 * RCS Settings
 */
const val RCS_DURATION = 1
const val RCS_SMOOTHING = 75

/**
 * Bunny Hop Settings
 */
const val BUNNY_HOP_KEY = KeyEvent.VK_SPACE //KeyEvent.VK_SPACE = Space button

/**
 * Glow ESP Settings
 */
const val TEAM_COLOR_RED = 0 //RGB colors
const val TEAM_COLOR_BLUE = 255 //RGB colors
const val TEAM_COLOR_GREEN = 0 //RGB colors
const val TEAM_COLOR_ALPHA = 0.6f //RGB colors

const val ENEMY_COLOR_RED = 255 //RGB colors
const val ENEMY_COLOR_BLUE = 0 //RGB colors
const val ENEMY_COLOR_GREEN = 0 //RGB colors
const val ENEMY_COLOR_ALPHA = 0.6f //RGB colors

const val BOMB_COLOR_RED = 0 //RGB colorsN
const val BOMB_COLOR_BLUE = 0 //RGB colors
const val BOMB_COLOR_GREEN = 255 //RGB colors
const val BOMB_COLOR_ALPHA = 0.6f //RGB colors

/**
 * ESP toggles
 */
const val SKELETON_ESP = true
const val GLOW_ESP = true //Glowesp causes Skeleton esp to flicker
const val BOX_ESP = true