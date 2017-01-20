package com.charlatano.settings

import com.charlatano.scripts.esp.GameColor
import com.charlatano.utils.Dojo.Setting

val SKELETON_ESP: Boolean by Setting()
val BOX_ESP: Boolean by Setting()
val GLOW_ESP: Boolean by Setting()

val SHOW_TEAM: Boolean by Setting()
val SHOW_ENEMIES: Boolean by Setting()
val SHOW_DORMANT: Boolean by Setting()
val SHOW_BOMB: Boolean by Setting()
val SHOW_WEAPONS: Boolean by Setting()
val SHOW_GRENADES: Boolean by Setting()

val TEAM_COLOR: GameColor by Setting()
val ENEMY_COLOR: GameColor by Setting()
val BOMB_COLOR: GameColor by Setting()
val WEAPON_COLOR: GameColor by Setting()
val GRENADE_COLOR: GameColor by Setting()