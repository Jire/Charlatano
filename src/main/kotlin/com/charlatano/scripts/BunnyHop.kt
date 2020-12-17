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

package com.charlatano.scripts

import com.badlogic.gdx.math.MathUtils
import com.charlatano.game.CSGO
import com.charlatano.game.hooks.cursorEnable
import com.charlatano.game.hooks.onGround
import com.charlatano.game.offsets.ClientOffsets
import com.charlatano.settings.BUNNY_HOP_KEY
import com.charlatano.settings.ENABLE_BUNNY_HOP
import com.charlatano.settings.LEAGUE_MODE
import com.charlatano.utils.keyPressed
import com.charlatano.utils.mouseWheel
import org.jire.kna.set

fun bunnyHop() = onGround {
    if (ENABLE_BUNNY_HOP && !cursorEnable && keyPressed(BUNNY_HOP_KEY)) {
        if (LEAGUE_MODE) {
            mouseWheel(MathUtils.randomSign() * MathUtils.random(10, 70))
        } else {
            CSGO.clientDLL[ClientOffsets.dwForceJump] = 6//if (jump) 5 else 4
        }
    }
}