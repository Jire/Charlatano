package com.charlatano.game

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.offsets.EngineOffsets.dwViewAngles
import com.charlatano.utils.Angle
import com.charlatano.utils.Vector

typealias ClientState = Long

fun ClientState.angle(): Angle
		= Vector(csgoEXE.float(this + dwViewAngles).toDouble(),
		csgoEXE.float(this + dwViewAngles + 4).toDouble(),
		csgoEXE.float(this + dwViewAngles + 8).toDouble())