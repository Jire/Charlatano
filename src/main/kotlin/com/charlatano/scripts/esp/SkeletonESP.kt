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

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.EntityType
import com.charlatano.game.entity.*
import com.charlatano.game.hooks.GlowIteration
import com.charlatano.game.offsets.EngineOffsets.studioModel
import com.charlatano.overlay.Overlay
import com.charlatano.utils.Vector
import com.charlatano.utils.uint
import com.charlatano.worldToScreen
import java.awt.Color

const val MAXSTUDIOBONES = 128

private val bones = Array(MAXSTUDIOBONES) { FloatArray(3) }
private val studiobones = Array(MAXSTUDIOBONES) { Bone() }
private val skeletons = Array(1024) { Line() }

var currentIdx = 0
fun skeletonEsp() = GlowIteration {
    if (glowIndex == glowObjectCount) currentIdx = 0

    if (entity == me) return@GlowIteration

    if (entity.type() == EntityType.CCSPlayer) {
        if (entity.dead() || entity.dormant()) return@GlowIteration

        val pStudiomodel = findStudioModel(entity.model())
        val numbones = csgoEXE.int(pStudiomodel + 0x9C)
        val boneIndex = csgoEXE.int(pStudiomodel + 0xA0)

        for (i in 0..numbones) {
            bones[i][0] = entity.bone(0xC, i)
            bones[i][1] = entity.bone(0x1C, i)
            bones[i][2] = entity.bone(0x2C, i)
        }

        var offset = 0
        for (i in 0..numbones) {
            studiobones[i].parent = csgoEXE.int(pStudiomodel + boneIndex + 0x4 + offset)
            studiobones[i].flags = csgoEXE.int(pStudiomodel + boneIndex + 0xA0 + offset)

            offset += 216
        }

        for (i in 0..numbones - 1) {
            if ((studiobones[i].flags and 0x100) == 0 || studiobones[i].parent == -1) continue
            drawBone(entity, studiobones[i].parent, i)
        }
    }

    Overlay {
        skeletons.forEach {
            if (it.color != Color.BLACK) {
                color = it.color
                drawLine(it.sX, it.sY, it.eX, it.eY)
            }
            it.reset()
        }
    }
}

fun findStudioModel(pModel: Long): Long {
    val ModelType = csgoEXE.uint(pModel + 0x0110)
    if (ModelType != 3L) return 0 //Type is not Studiomodel

    var ModelHandle = csgoEXE.uint(pModel + 0x0138) and 0xFFFF
    if (ModelHandle == 0xFFFFL) return 0 //Handle is not valid

    ModelHandle = ModelHandle shl 4

    var studioModel = engineDLL.uint(studioModel)
    studioModel = csgoEXE.uint(studioModel + 0x028)
    studioModel = csgoEXE.uint(studioModel + ModelHandle + 0x0C)

    return csgoEXE.uint(studioModel + 0x0074)
}

private val colors: Array<Color> = Array(101) {
    val red = 1 - (it / 100f)
    val green = (it / 100f)

    Color(red, green, 0f)
}

private val StartBonePos = ThreadLocal.withInitial { Vector() }
private val EndBonePos = ThreadLocal.withInitial { Vector() }

private val StartDrawPos = ThreadLocal.withInitial { Vector() }
private val EndDrawPos = ThreadLocal.withInitial { Vector() }

fun drawBone(target: Player, start: Int, end: Int) {
    val StartBonePos = StartBonePos.get()
    val EndBonePos = EndBonePos.get()

    StartBonePos.set(target.bone(0xC, start), target.bone(0x1C, start), target.bone(0x2C, start))
    EndBonePos.set(target.bone(0xC, end), target.bone(0x1C, end), target.bone(0x2C, end))

    val StartDrawPos = StartDrawPos.get()
    val EndDrawPos = EndDrawPos.get()

    if (!worldToScreen(StartBonePos, StartDrawPos) || !worldToScreen(EndBonePos, EndDrawPos))
        return

    skeletons[currentIdx].sX = StartDrawPos.x.toInt()
    skeletons[currentIdx].sY = StartDrawPos.y.toInt()
    skeletons[currentIdx].eX = EndDrawPos.x.toInt()
    skeletons[currentIdx].eY = EndDrawPos.y.toInt()
    skeletons[currentIdx++].color = colors[target.health()]
}

class Line() {
    var sX = -1
    var sY = -1
    var eX = -1
    var eY = -1
    var color: Color = Color.BLACK

    fun reset() {
        sX = -1
        sY = -1
        eX = -1
        eY = -1
        color = Color.BLACK
    }
}

class Bone() {

    var parent: Int = -1
    var flags: Int = -1

}