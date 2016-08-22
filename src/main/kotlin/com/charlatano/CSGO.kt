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

package com.charlatano

import org.jire.arrowhead.Module
import org.jire.arrowhead.Process
import kotlin.properties.Delegates.notNull

var csgoEXE: Process by notNull()
	internal set

var clientDLL: Module by notNull()
	internal set
var engineDLL: Module by notNull()
	internal set

const val ENTITY_SIZE = 16
const val GLOW_OBJECT_SIZE = 56