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

import com.charlatano.game.Color
import com.charlatano.settings.*

///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// --- ESP Types --- ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Whether or not to use skeleton ESP.
 */
SKELETON_ESP = false

/**
 * Whether or not to use box ESP.
 */
BOX_ESP = false

/**
 * Whether or not to use the within-game glow ESP.
 *
 * This ESP **CANNOT** be hidden from game capture for streaming.
 */
GLOW_ESP = true

/**
 * @@ Overrides ENEMY_COLOR @@
 * Health at 100% is represented with red.
 * 0% Health is represented with blue.
 * The damage the enemy has taken determines how blue they will be. 
 */
HEALTH_BASED_GLOW = true



///////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////// --- TOGGLES --- ////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Whether or not to highlight your team mates.
 */
SHOW_TEAM = true

/**
 * Whether or not to highlight enemies.
 */
SHOW_ENEMIES = true

/**
 * Whether or not to highlight "dormant" (unknown-location) players.
 *
 * Enabling this can allow you to see players at a further distance,
 * but you may see some "ghost" players which are really not there.
 */
SHOW_DORMANT = false

/**
 * Whether or not to highlight the bomb.
 */
SHOW_BOMB = true

/**
 * Whether or not to highlight weapons.
 */
SHOW_WEAPONS = false

/**
 * Whether or not to highlight grenades.
 */
SHOW_GRENADES = false



///////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////// --- COLORS --- ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The color to highlight your team mates.
 */
TEAM_COLOR = Color(0, 255, 0)

/**
 * The color to highlight your enemies.
 */
ENEMY_COLOR = Color(255, 0, 0)

/**
 * The color to highlight the bomb.
 */
BOMB_COLOR = Color(255, 255, 0, 1.0)

/**
 * The color to highlight weapons.
 */
WEAPON_COLOR = Color(0, 255, 0, 0.5)

/**
 * The color to highlight grenades.
 */
GRENADE_COLOR = Color(0, 255, 0, 1.0)


///////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// --- MISCELLANEOUS --- ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Paints the models with their respective colors.
 *
 * WARNING: This may cause random game crashes if you enable it.
 */
COLOR_MODELS = false