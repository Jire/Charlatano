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

import com.badlogic.gdx.graphics.Color
import com.charlatano.game.CSGO
import com.charlatano.game.entity.*
import com.charlatano.game.entityByType
import com.charlatano.game.offsets.EngineOffsets
import com.charlatano.overlay.CharlatanoOverlay
import com.charlatano.settings.ENABLE_BOMB_TIMER
import com.charlatano.utils.every
import org.jire.kna.float

private var bombState = BombState()

fun bombTimer() {
    bombUpdater()

    CharlatanoOverlay {
        bombState.apply {
            if (ENABLE_BOMB_TIMER && this.planted) {
                batch.begin()
                textRenderer.color = Color.ORANGE
                textRenderer.draw(batch, bombState.toString(), 20F, 500F)
                batch.end()
            }
        }
    }
}

private fun currentGameTicks(): Float = CSGO.engineDLL.float(EngineOffsets.dwGlobalVars + 16)

fun bombUpdater() = every(8, true) {
    if (!ENABLE_BOMB_TIMER) return@every

    val time = currentGameTicks()
    val bomb: Entity = entityByType(EntityType.CPlantedC4)?.entity ?: -1L

    bombState.apply {
        timeLeftToExplode = bomb.blowTime() - time
        hasBomb = bomb > 0 && !bomb.dormant()
        planted = hasBomb && !bomb.defused() && timeLeftToExplode > 0

        if (planted) {
            if (location.isEmpty()) location = bomb.plantLocation()

            val defuser = bomb.defuser()
            timeLeftToDefuse = bomb.defuseTime() - time
            gettingDefused = defuser > 0 && timeLeftToDefuse > 0
            canDefuse = gettingDefused && (timeLeftToExplode > timeLeftToDefuse)
        } else {
            location = ""
            canDefuse = false
            gettingDefused = false
        }
    }
}

private data class BombState(var hasBomb: Boolean = false,
                             var planted: Boolean = false,
                             var canDefuse: Boolean = false,
                             var gettingDefused: Boolean = false,
                             var timeLeftToExplode: Float = -1f,
                             var timeLeftToDefuse: Float = -1f,
                             var location: String = "") {

    private val sb = StringBuilder()

    override fun toString(): String {
        sb.setLength(0)
        sb.append("Bomb Planted!\n")

        sb.append("TimeToExplode : ${formatFloat(timeLeftToExplode)} \n")

        if (location.isNotBlank())
            sb.append("Location : $location \n")
        if (gettingDefused) {
//            sb.append("GettingDefused : $gettingDefused \n")
            sb.append("CanDefuse : $canDefuse \n")
            // Redundant as the UI already shows this, but may have a use case I'm missing
            sb.append("TimeToDefuse : ${formatFloat(timeLeftToDefuse)} ")
        }
        return sb.toString()
    }


    private fun formatFloat(f: Float): String {
        return "%.3f".format(f)
    }
}
