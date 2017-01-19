package com.charlatano.settings

///////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////// --- GENERAL --- ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The key code of the force aim button.
 *
 * By default, this uses the backward mouse button
 * (button 5, the button on the bottom left of gaming mice).
 */
const val FORCE_AIM_KEY = 5

/**
 * The field of view of the aimbot, in degrees (0 to 360).
 */
const val AIM_FOV = 180

/**
 * The aimbot's "playback" speed, the higher the value the slower the playback.
 *
 * The minimum value is 1, and max must always be greater than min.
 */
const val AIM_SPEED_MIN = 36
const val AIM_SPEED_MAX = 44

/**
 * The strictness, or "stickiness" of the aimbot; the higher the number, the
 * less strict the aimbot will stick to targets.
 *
 * The minimum value is 1.0
 */
const val AIM_STRICTNESS = 2.4



///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// --- PERFECT AIM --- /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Whether or not to use perfect aim, which will instantaneously snap
 * to the aim bone once you are within the [PERFECT_AIM_FOV].
 */
const val PERFECT_AIM = false

/**
 * The FOV, in degrees (0 to 360) to snap for perfect aim.
 */
const val PERFECT_AIM_FOV = 30

/**
 * The chance, from 1% to 100% (0 to 100) for perfect aim to activate.
 */
const val PERFECT_AIM_CHANCE = 100



///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// --- AIM ASSIST --- //////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Enables "aim assist" mode, which has no stickiness, and gives you small
 * extra movements towards the aim bone.
 *
 * This setting should be used by high-level players who are experienced aimers.
 */
const val AIM_ASSIST_MODE = false

/**
 * The amount of strictness for the aim assist mode, with a mimimum value of 1.
 */
const val AIM_ASSIST_STRICTNESS = 40



///////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// --- MISCELLANEOUS --- ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The duration in milliseconds at which aimbot paths are recalculated.
 */
const val AIM_DURATION = 1

/**
 * The amount of sprayed shots until the aimbot shifts to aiming at the [SHOULDER_BONE].
 */
const val SHIFT_TO_SHOULDER_SHOTS = 3

/**
 * The amount of sprayed shots until the aimbot shifts to aiming at the [BODY_BONE].
 */
const val SHIFT_TO_BODY_SHOTS = 6