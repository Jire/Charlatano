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

///////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////// --- GENERAL --- ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Whether or not to aim when using the [FIRE_KEY] (by default left click).
 *
 * You should disable this if you don't want aim to activate when left clicking.
 */
var ACTIVATE_FROM_FIRE_KEY = true

/**
 * The key code of the force aim button.
 *
 * By default, this uses the backward mouse button
 * (button 5, the button on the bottom left of gaming mice).
 */
var FORCE_AIM_KEY = 5

/**
 * The field of view of the aimbot, in degrees (0 to 360).
 */
var AIM_FOV = 190
var FORCE_AIM_FOV = 360

/**
 * The aimbot's "playback" speed, the higher the value the slower the playback.
 *
 * The minimum value is 1, and max must always be greater than min.
 */
var AIM_SPEED_MIN = 34
var AIM_SPEED_MAX = 37

/**
 * The strictness, or "stickiness" of the aimbot; the higher the number, the
 * less strict the aimbot will stick to targets.
 *
 * The minimum value is 1.0
 */
var AIM_STRICTNESS = 2.4


///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// --- PERFECT AIM --- /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Whether or not to use perfect aim, which will instantaneously snap
 * to the aim bone once you are within the [PERFECT_AIM_FOV].
 */
var PERFECT_AIM = false

/**
 * The FOV, in degrees (0 to 360) to snap for perfect aim.
 */
var PERFECT_AIM_FOV = 27

/**
 * The chance, from 1% to 100% (0 to 100) for perfect aim to activate.
 */
var PERFECT_AIM_CHANCE = 100


///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// --- AIM ASSIST --- //////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Enables "aim assist" mode, which has no stickiness, and gives you small
 * extra movements towards the aim bone.
 *
 * This setting should be used by high-level players who are experienced aimers.
 */
var AIM_ASSIST_MODE = false

/**
 * The amount of strictness for the aim assist mode, with a mimimum value of 1.
 */
var AIM_ASSIST_STRICTNESS = 40


///////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// --- MISCELLANEOUS --- ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The duration in milliseconds at which aimbot paths are recalculated.
 */
var AIM_DURATION = 1

/**
 * The amount of sprayed shots until the aimbot shifts to aiming at the [SHOULDER_BONE].
 */
var SHIFT_TO_SHOULDER_SHOTS = 4

/**
 * The amount of sprayed shots until the aimbot shifts to aiming at the [BODY_BONE].
 */
var SHIFT_TO_BODY_SHOTS = 7