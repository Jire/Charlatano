package com.charlatano.game.entity

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.me
import com.charlatano.game.netvars.NetVarOffsets.flNextPrimaryAttack
import com.charlatano.game.netvars.NetVarOffsets.iClip1
import org.jire.arrowhead.get

typealias Weapon = Long

internal fun Weapon.bullets(): Int = csgoEXE[this + iClip1]

internal fun Weapon.nextPrimaryAttack() = csgoEXE.float(this + flNextPrimaryAttack).toDouble()

internal fun Weapon.canFire(): Boolean {
	val nextAttack = nextPrimaryAttack()
	return nextAttack <= 0 || nextAttack < me.time()
}