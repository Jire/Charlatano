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

package com.charlatano.scripts

import com.charlatano.FORCE_AIM_KEY
import com.charlatano.FORCE_AIM_SMOOTHING
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.entity.*
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.moveTo
import com.charlatano.utils.Vector
import com.charlatano.utils.every
import com.charlatano.utils.uint
import org.jire.arrowhead.keyPressed

private var target: Player = -1L

fun forceAim() = every(FORCE_AIM_SMOOTHING) {
	val pressed = keyPressed(FORCE_AIM_KEY) {
		val me: Player = clientDLL.uint(dwLocalPlayer)
		if (me <= 0x200 || me.dead()) return@keyPressed
		
		var currentTarget = target
		if (currentTarget == -1L) {
			currentTarget = me.target()
			if (currentTarget == -1L) return@keyPressed
			target = currentTarget
			return@keyPressed
		}
		
		if (target.dead()
				|| target.dormant()
				|| !target.spotted()
				|| target.team() == me.team()) {
			target = -1L
			return@keyPressed
		}
		
		val bonePosition = Vector(target.bone(0xC), target.bone(0x1C), target.bone(0x2C))
		moveTo(bonePosition)
	}
	if (!pressed) target = -1L
}