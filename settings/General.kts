/**
 * Set this to true if you're playing CS:CO (Counter-Strike: Classic Offensive).
 */
val CLASSIC_OFFENSIVE = false

/**
 * The global fire key, which you use to shoot/fire your weapon.
 *
 * By default, this is left click (1).
 */
val FIRE_KEY = 1

/**
 * The bone IDs of the respective bones for a player.
 *
 * The left-side number is for CS:CO, and the right-side number is for CS:GO.
 */
val HEAD_BONE = if (CLASSIC_OFFENSIVE) 6 else 8
val SHOULDER_BONE = if (CLASSIC_OFFENSIVE) 5 else 7
val BODY_BONE = if (CLASSIC_OFFENSIVE) 4 else 6