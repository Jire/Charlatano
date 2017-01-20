/**
 * The amount of smoothing for the recoil control aim path.
 *
 * This has a minimum value of 1, and is recommended to stay slightly
 * above your full ping (which you can see with the "ping" command).
 *
 * For example, if you have 55 real ping, 65 is a good value.
 *
 * Settings this too low may result in incorrect recoil control.
 */
val RCS_SMOOTHING = 72

/**
 * The duration in milliseconds at which recoil control is checked.
 */
val RCS_DURATION = 1