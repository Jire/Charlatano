package com.charlatano.game.entity

import com.charlatano.game.CSGO.ENTITY_SIZE
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.netvars.NetVarOffsets.bBombDefused
import com.charlatano.game.netvars.NetVarOffsets.flC4Blow
import com.charlatano.game.netvars.NetVarOffsets.hOwnerEntity
import com.charlatano.game.netvars.NetVarOffsets.szLastPlaceName
import com.charlatano.game.offsets.ClientOffsets.dwEntityList
import com.charlatano.game.offsets.EngineOffsets.dwGlobalVars
import com.charlatano.utils.extensions.uint
import org.jire.arrowhead.get

typealias Bomb = Long

internal fun Bomb.defused(): Boolean = csgoEXE[this + bBombDefused]

internal fun Bomb.timeLeft(): Int = (-(engineDLL.float(dwGlobalVars + 16) - csgoEXE.float(this + flC4Blow))).toInt()

internal fun Bomb.planted() = this != -1L && !defused() && timeLeft() > 0

internal fun Bomb.carrier(): Player = (csgoEXE.int(this + hOwnerEntity) and 0xFFF) - 1L

internal fun Bomb.planter(): Player = clientDLL.uint(dwEntityList + (carrier() * ENTITY_SIZE))

internal fun Bomb.location(): String = csgoEXE.read(planter() + szLastPlaceName, 32, true)!!.getString(0)