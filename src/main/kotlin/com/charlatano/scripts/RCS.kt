package com.charlatano.scripts

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.CSGO.scaleFormDLL
import com.charlatano.game.angle
import com.charlatano.game.clientState
import com.charlatano.game.entity.Player
import com.charlatano.game.entity.weapon
import com.charlatano.game.me
import com.charlatano.game.netvars.NetVarOffsets.iShotsFired
import com.charlatano.game.netvars.NetVarOffsets.vecPunch
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.game.offsets.ScaleFormOffsets.bCursorEnabled
import com.charlatano.settings.*
import com.charlatano.utils.*
import com.charlatano.utils.extensions.uint

private @Volatile var prevFired = 0
private val lastPunch = DoubleArray(2)

fun rcs() = every(RCS_DURATION) {
	val myAddress: Player = clientDLL.uint(dwLocalPlayer)
	if (myAddress <= 0) return@every
	
	val shotsFired = csgoEXE.int(myAddress + iShotsFired)
	if (shotsFired <= 2 || shotsFired < prevFired || scaleFormDLL.boolean(bCursorEnabled)) {
		reset()
		return@every
	}
	
	if (!CLASSIC_OFFENSIVE) {
		val weapon = me.weapon()
		if (!weapon.automatic) {
			reset()
			return@every
		}
	}
	
	val punch = Vector(csgoEXE.float(myAddress + vecPunch).toDouble(),
			csgoEXE.float(myAddress + vecPunch + 4).toDouble(), 0.0)
	punch.x *= 2.0
	punch.y *= 2.0
	punch.z = 0.0
	punch.normalize()
	
	val newView: Angle = Vector(punch.x, punch.y, punch.z)
	newView.x -= lastPunch[0]
	newView.y -= lastPunch[1]
	newView.z = 0.0
	newView.normalize()
	
	val view = clientState.angle()
	view.x -= newView.x
	view.y -= newView.y
	view.z = 0.0
	view.normalize()
	
	aim(clientState.angle(), view, RCS_SMOOTHING)
	
	lastPunch[0] = punch.x
	lastPunch[1] = punch.y
	prevFired = shotsFired
	
	if (shotsFired >= SHIFT_TO_SHOULDER_SHOTS) {
		bone.set(if (shotsFired < SHIFT_TO_BODY_SHOTS) SHOULDER_BONE else BODY_BONE)
		perfect.set(false)
	}
}

private fun reset() {
	prevFired = 0
	lastPunch[0] = 0.0
	lastPunch[1] = 0.0
	bone.set(HEAD_BONE)
}