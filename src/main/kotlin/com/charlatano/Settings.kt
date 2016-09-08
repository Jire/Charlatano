/*
 * Charlatan is a premium CS:GO cheat ran on the JVM.
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
 * Force Aim Settings
 */
const val FORCE_AIM_SMOOTHING = 15 //Lower = stronger lock
const val FORCE_AIM_KEY = 5 //5 = Mouse forward button
const val FORCE_AIM_TARGET_BONE = 6 //6 = Head


/**
 * Bunnyhop Settings
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

const val BOMB_COLOR_RED = 0 //RGB colors
const val BOMB_COLOR_BLUE = 0 //RGB colors
const val BOMB_COLOR_GREEN = 255 //RGB colors
const val BOMB_COLOR_ALPHA = 0.6f //RGB colors