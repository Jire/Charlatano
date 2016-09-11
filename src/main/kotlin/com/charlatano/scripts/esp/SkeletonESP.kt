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

fun skeletonEsp() = GlowIteration {
    if (entity == me) return@GlowIteration

    if (entity.type() == EntityType.CCSPlayer) {
        if (entity.dead() || entity.dormant()) return@GlowIteration
        if (entity.team() == 2) {
            for (i in 0..6 - 1) drawBone(entity, i, i + 1)

            //neck to left arm
            for (i in 8..11 - 1) drawBone(entity, i, i + 1)

            //neck to right arm
            drawBone(entity, 8, 36)
            for (i in 36..38 - 1) drawBone(entity, i, i + 1)

            //crotch to left leg
            drawBone(entity, 0, 63)
            for (i in 63..66 - 1) drawBone(entity, i, i + 1)

            //crotch to right leg
            drawBone(entity, 0, 69)
            for (i in 69..72 - 1) drawBone(entity, i, i + 1)
        } else {
            for (i in 0..6 - 1) drawBone(entity, i, i + 1)

            //neck to left arm
            for (i in 8..11 - 1) drawBone(entity, i, i + 1)

            //neck to right shoulder
            drawBone(entity, 38, 67)

            //right shoulder to elbow
            drawBone(entity, 67, 40)

            //right elbow to hand
            drawBone(entity, 40, 41)

            //crotch to left knee
            drawBone(entity, 0, 75)

            //left knee to foot
            for (i in 75..77 - 1) drawBone(entity, i, i + 1)

            //crotch to right knee
            drawBone(entity, 0, 69)

            //right knee to foot
            for (i in 69..71 - 1) drawBone(entity, i, i + 1)
        }
    }
}

private val colors: Array<Color> = Array(101) {
    val red = 1 - (it / 100f)
    val green = (it / 100f)

    Color(red, green, 0f)
}

val StartDrawPos: Vector = Vector()
val EndDrawPos: Vector = Vector()

fun drawBone(target: Player, start: Int, end: Int) {
    val StartBonePos = Vector(target.bone(0xC, start), target.bone(0x1C, start), target.bone(0x2C, start))
    val EndBonePos = Vector(target.bone(0xC, end), target.bone(0x1C, end), target.bone(0x2C, end))

    StartDrawPos.reset()
    EndDrawPos.reset()

    if (!worldToScreen(StartBonePos, StartDrawPos))
        return
    if (!worldToScreen(EndBonePos, EndDrawPos))
        return

    val health = target.health()
    val sX = StartDrawPos.x.toInt()
    val sY = StartDrawPos.y.toInt()
    val eX = EndDrawPos.x.toInt()
    val eY = EndDrawPos.y.toInt()
    Overlay {
        color = colors[health]
        drawLine(sX, sY, eX, eY)
    }
}