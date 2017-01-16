/*
 * Charlatano is a premium CS:GO cheat ran on the JVM.
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

package com.charlatano.game.hooks

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.scaleFormDLL
import com.charlatano.game.entity.Player
import com.charlatano.game.entity.dead
import com.charlatano.game.entity.onGround
import com.charlatano.game.offsets.ClientOffsets.localPlayer
import com.charlatano.game.offsets.ScaleFormOffsets
import com.charlatano.utils.extensions.uint
import com.charlatano.utils.hook

val onGround = hook(4) {
	val me: Player = clientDLL.uint(localPlayer())
	if (me <= 0x200 || me.dead()) return@hook false
	
	me.onGround() && !scaleFormDLL.boolean(ScaleFormOffsets.bCursorEnabled)
}