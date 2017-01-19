package com.charlatano.game

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.entity.Player
import com.charlatano.game.entity.position
import com.charlatano.game.entity.punch
import com.charlatano.game.netvars.NetVarOffsets.vecViewOffset
import com.charlatano.utils.Angle
import com.charlatano.utils.Vector
import com.charlatano.utils.normalize
import java.lang.Math.atan
import java.lang.Math.toDegrees

private val angles: ThreadLocal<Angle> = ThreadLocal.withInitial { Vector() }

fun calculateAngle(player: Player, dst: Vector): Angle {
	val angles = angles.get()
	
	val myPunch = player.punch()
	val myPosition = player.position()
	
	val dX = myPosition.x - dst.x
	val dY = myPosition.y - dst.y
	val dZ = myPosition.z + csgoEXE.float(player + vecViewOffset) - dst.z
	
	val hyp = Math.sqrt((dX * dX) + (dY * dY))
	
	angles.x = toDegrees(atan(dZ / hyp)) - myPunch.x * 2.0
	angles.y = toDegrees(atan(dY / dX)) - myPunch.y * 2.0
	angles.z = 0.0
	if (dX >= 0.0) angles.y += 180
	
	return angles.normalize()
}