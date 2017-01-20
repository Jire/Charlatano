import com.charlatano.scripts.esp.GameColor

///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// --- ESP Types --- ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Whether or not to use skeleton ESP.
 */
val SKELETON_ESP = false

/**
 * Whether or not to use box ESP.
 */
val BOX_ESP = false

/**
 * Whether or not to use the within-game glow ESP.
 *
 * This ESP **CANNOT** be hidden from game capture for streaming.
 */
val GLOW_ESP = true



///////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////// --- TOGGLES --- ////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Whether or not to highlight your team mates.
 */
val SHOW_TEAM = true

/**
 * Whether or not to highlight enemies.
 */
val SHOW_ENEMIES = true

/**
 * Whether or not to highlight "dormant" (unknown-location) players.
 *
 * Enabling this can allow you to see players at a further distance,
 * but you may see some "ghost" players which are really not there.
 */
val SHOW_DORMANT = false

/**
 * Whether or not to highlight the bomb.
 */
val SHOW_BOMB = true

/**
 * Whether or not to highlight weapons.
 */
val SHOW_WEAPONS = false

/**
 * Whether or not to highlight grenades.
 */
val SHOW_GRENADES = false



///////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////// --- COLORS --- ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The color to highlight your team mates.
 */
val TEAM_COLOR = GameColor(0, 0, 255)

/**
 * The color to highlight your enemies.
 */
val ENEMY_COLOR = GameColor(255, 0, 0)

/**
 * The color to highlight the bomb.
 */
val BOMB_COLOR = GameColor(255, 255, 0, 1.0)

/**
 * The color to highlight weapons.
 */
val WEAPON_COLOR = GameColor(0, 255, 0, 0.5)

/**
 * The color to highlight grenades.
 */
val GRENADE_COLOR = GameColor(0, 255, 0, 1.0)