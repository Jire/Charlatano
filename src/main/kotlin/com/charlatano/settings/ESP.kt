/*
 * Charlatano: Free and open-source (FOSS) cheat for CS:GO/CS:CO
 * Copyright (C) 2017 - Thomas G. P. Nappo, Jonathan Beaudoin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.charlatano.settings

import com.charlatano.game.Color

///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// --- ESP Types --- ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Whether or not to use skeleton ESP.
 */
var SKELETON_ESP = false

/**
 * Whether or not to use box ESP.
 */
var BOX_ESP = false

/**
 * Whether or not to use the within-game glow ESP.
 *
 * This ESP **CANNOT** be hidden from game capture for streaming.
 */
var GLOW_ESP = true

/**
 * @@ Overrides ENEMY_COLOR @@
 * Health at 100% is represented with red.
 * 0% Health is represented with blue.
 * The damage the enemy has taken determines how blue they will be. 
 */
var HEALTH_BASED_GLOW = true


///////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////// --- TOGGLES --- ////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Whether or not to highlight your team mates.
 */
var SHOW_TEAM = true

/**
 * Whether or not to highlight enemies.
 */
var SHOW_ENEMIES = true

/**
 * Whether or not to highlight "dormant" (unknown-location) players.
 *
 * Enabling this can allow you to see players at a further distance,
 * but you may see some "ghost" players which are really not there.
 */
var SHOW_DORMANT = false

/**
 * Whether or not to highlight the bomb.
 */
var SHOW_BOMB = true

/**
 * Whether or not to highlight weapons.
 */
var SHOW_WEAPONS = false

/**
 * Whether or not to highlight grenades.
 */
var SHOW_GRENADES = false


///////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////// --- COLORS --- ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The color to highlight your team mates.
 */
var TEAM_COLOR = Color(0, 0, 255)

/**
 * The color to highlight your enemies.
 */
var ENEMY_COLOR = Color(255, 0, 0)

/**
 * The color to highlight the bomb.
 */
var BOMB_COLOR = Color(255, 255, 0, 1.0)

/**
 * The color to highlight weapons.
 */
var WEAPON_COLOR = Color(0, 255, 0, 0.5)

/**
 * The color to highlight grenades.
 */
var GRENADE_COLOR = Color(0, 255, 0, 1.0)


///////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// --- MISCELLANEOUS --- ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Paints the models with their respective colors.
 *
 * WARNING: This may cause random game crashes if you enable it.
 */
var COLOR_MODELS = false