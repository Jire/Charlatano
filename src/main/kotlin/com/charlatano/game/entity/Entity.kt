package com.charlatano.game.entity

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.me
import com.charlatano.game.netvars.NetVarOffsets.bSpottedByMask
import com.charlatano.game.netvars.NetVarOffsets.dwModel
import com.charlatano.game.netvars.NetVarOffsets.iTeamNum
import com.charlatano.game.netvars.NetVarOffsets.vecOrigin
import com.charlatano.game.netvars.NetVarOffsets.vecViewOffset
import com.charlatano.game.offsets.ClientOffsets.bDormant
import com.charlatano.game.offsets.ClientOffsets.dwIndex
import com.charlatano.utils.Angle
import com.charlatano.utils.Vector
import com.charlatano.utils.extensions.uint
import org.jire.arrowhead.get

typealias Entity = Long

internal fun Entity.spotted(): Boolean {
	val meID = csgoEXE.int(me + dwIndex) - 1
	val spottedByMask = csgoEXE.uint(this + bSpottedByMask)
	val result = spottedByMask and (1 shl meID).toLong()
	return result != 0L
}

internal fun Entity.dormant(): Boolean = try {
	csgoEXE[this + bDormant]
} catch (t: Throwable) {
	false
}

internal fun Entity.team(): Int = csgoEXE[this + iTeamNum]

internal fun Entity.model(): Long = csgoEXE.uint(this + dwModel)

internal fun Entity.position(): Angle = Vector(
		csgoEXE.float(this + vecOrigin).toDouble(),
		csgoEXE.float(this + vecOrigin + 4).toDouble(),
		csgoEXE.float(this + vecOrigin + 8).toDouble()
				+ csgoEXE.float(this + vecViewOffset + 8))