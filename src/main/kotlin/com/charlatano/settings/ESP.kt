/*
 *     Charlatano: Free and open-source (FOSS) cheat for CS:GO/CS:CO
 *     Copyright (C) 2017 - Thomas G. P. Nappo, Jonathan Beaudoin
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.charlatano.settings

import com.charlatano.game.Color
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

val TEAM_COLOR: Color by Setting()
val ENEMY_COLOR: Color by Setting()
val BOMB_COLOR: Color by Setting()
val WEAPON_COLOR: Color by Setting()
val GRENADE_COLOR: Color by Setting()

val COLOR_MODELS: Boolean by Setting()