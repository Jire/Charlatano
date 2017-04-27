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

package com.charlatano.scripts.aim

import com.charlatano.game.entity.isScoped
import com.charlatano.game.me
import com.charlatano.settings.*
import com.charlatano.utils.pathAim

fun pathAim() = aimScript(AIM_DURATION, { ENABLE_PATH_AIM }) { dest, current, aimSpeed ->
	pathAim(current, dest, aimSpeed,
			sensMultiplier = if (me.isScoped()) 1.0 else AIM_STRICTNESS,
			perfect = perfect.getAndSet(false))
}