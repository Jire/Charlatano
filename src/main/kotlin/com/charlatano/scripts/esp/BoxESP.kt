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

package com.charlatano.scripts.esp

import com.charlatano.game.EntityType
import com.charlatano.game.entity.*
import com.charlatano.game.hooks.GlowIteration
import com.charlatano.overlay.Overlay
import com.charlatano.utils.Vector
import com.charlatano.worldToScreen
import java.awt.Color


private val vHead = Vector()
private val vFeet = Vector()

private val vTop = Vector(0F, 0F, 0F)
private val vBot = Vector(0F, 0F, 0F)

fun boxEsp() = GlowIteration {
    if (entity == me) return@GlowIteration

    if (entity.type() == EntityType.CCSPlayer) {
        if (entity.dead() || entity.dormant()) return@GlowIteration

        vHead.set(entity.bone(0xC), entity.bone(0x1C), entity.bone(0x2C) + 9)
        vFeet.set(vHead.x, vHead.y, vHead.z - 75)

        if (!worldToScreen(vHead, vTop))
            return@GlowIteration
        if (!worldToScreen(vFeet, vBot))
            return@GlowIteration

        val h = vBot.y - vTop.y
        val w = h / 5F

        val c = if (bomb > -1 && entity == bomb.carrier()) Color.GREEN else if (me.team() == entity.team()) Color.BLUE else Color.RED

        val sx = (vTop.x - w).toInt()
        val sy = vTop.y.toInt()

        Overlay {
            color = c
            drawRect(sx, sy, (w * 2).toInt(), h.toInt())
        }
    }
}