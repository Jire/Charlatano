/*
 * Charlatan is a premium CS:GO cheat ran on the JVM.
 * Copyright (C) 2016 Thomas Nappo, Jonathan Beaudoin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.charlatano.scripts

import com.badlogic.gdx.graphics.Color
import com.charlatano.game.entity.location
import com.charlatano.game.entity.timeLeft
import com.charlatano.game.hooks.bombPlanted
import com.charlatano.game.hooks.location
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.scripts.esp.bomb


fun bombTimer() = bombPlanted {
    val hasKit = false
    val canDefuse = bomb.timeLeft() >= if (hasKit) 5 else 10

    if (location.isEmpty()) location = bomb.location()

    CharlatanoOverlay {
        textRenderer.color = Color.ORANGE
        textRenderer.draw(batch, "Location: $location, ${bomb.timeLeft()} seconds, can defuse? $canDefuse", 200f, 20f)
    }
}

