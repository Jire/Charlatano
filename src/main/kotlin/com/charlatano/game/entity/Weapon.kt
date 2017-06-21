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

package com.charlatano.game.entity

import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.me
import com.charlatano.game.netvars.NetVarOffsets.flNextPrimaryAttack
import com.charlatano.game.netvars.NetVarOffsets.iClip1
import com.charlatano.utils.extensions.uint

typealias Weapon = Long

internal fun Weapon.bullets() = csgoEXE.uint(this + iClip1)

internal fun Weapon.nextPrimaryAttack() = csgoEXE.float(this + flNextPrimaryAttack).toDouble()

internal fun Weapon.canFire(): Boolean {
	val nextAttack = nextPrimaryAttack()
	return nextAttack <= 0 || nextAttack < me.time()
}