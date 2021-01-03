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

package com.charlatano.utils.extensions

import com.charlatano.game.offsets.ModuleScan
import com.charlatano.game.offsets.Offset
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.windows.WindowsAttachedModule
import org.jire.kna.attach.windows.WindowsAttachedProcess
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal operator fun AttachedModule.invoke(
	patternOffset: Long = 0, addressOffset: Long = 0,
	read: Boolean = true, subtract: Boolean = true
) = ModuleScan(this, patternOffset, addressOffset, read, subtract)

internal operator fun AttachedModule.invoke(
	patternOffset: Long = 0, addressOffset: Long = 0,
	read: Boolean = true, subtract: Boolean = true, className: String
) = Offset(this, patternOffset, addressOffset, read, subtract, className.toByteArray(Charsets.UTF_8))

internal operator fun AttachedModule.invoke(
	patternOffset: Long = 0, addressOffset: Long = 0,
	read: Boolean = true, subtract: Boolean = true, offset: Long
) = Offset(
	this, patternOffset, addressOffset, read, subtract,
	ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(offset.toInt()).array()
)

fun WindowsAttachedModule.readForced(address: Long, buffer: Pointer, size: Int) =
	(process as WindowsAttachedProcess).readForced(offset(address), buffer, size)

fun WindowsAttachedModule.writeForced(address: Long, buffer: Pointer, size: Int) =
	(process as WindowsAttachedProcess).writeForced(offset(address), buffer, size)