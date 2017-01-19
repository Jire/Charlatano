package com.charlatano.settings

/**
 * Toggles
 */
const val GLOW_ESP = true
const val SKELETON_ESP = false
const val BOX_ESP = false

/**
 * Glow ESP Settings
 */

data class GlowColor(val red: Int, val green: Int, val blue: Int, val alpha: Double = 0.6)

val TEAM_COLOR = GlowColor(0, 0, 255)
val ENEMY_COLOR = GlowColor(255, 0, 0)

val BOMB_COLOR = GlowColor(255, 255, 0, 1.0)
val EQUIPMENT_COLOR = GlowColor(0, 255, 0, 0.5)
val GRENADE_COLOR = GlowColor(0, 255, 0, 1.0)

const val SHOW_TEAM = true
const val SHOW_DORMANT = false
const val SHOW_EQUIPMENT = false
const val SHOW_BOMB = true