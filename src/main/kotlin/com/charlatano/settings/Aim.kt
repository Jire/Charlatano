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

import com.charlatano.utils.Dojo.Setting

val FORCE_AIM_KEY: Int by Setting()

val AIM_FOV: Int by Setting()

val AIM_SPEED_MIN: Int by Setting()
val AIM_SPEED_MAX: Int by Setting()

val AIM_STRICTNESS: Double by Setting()

val PERFECT_AIM: Boolean by Setting()
val PERFECT_AIM_FOV: Int by Setting()
val PERFECT_AIM_CHANCE: Int by Setting()

val AIM_ASSIST_MODE: Boolean by Setting()
val AIM_ASSIST_STRICTNESS: Int by Setting()

val AIM_DURATION: Int by Setting()

val SHIFT_TO_SHOULDER_SHOTS: Int by Setting()
val SHIFT_TO_BODY_SHOTS: Int by Setting()