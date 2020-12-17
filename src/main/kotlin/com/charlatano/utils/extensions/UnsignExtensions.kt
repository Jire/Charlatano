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

/**
 * Converts to an unsigned byte represented in an int.
 */
fun Byte.unsign() = java.lang.Byte.toUnsignedInt(this)

/**
 * Converts to an unsigned short represented in an int.
 */
fun Short.unsign() = java.lang.Short.toUnsignedInt(this)

/**
 * Converts to an unsigned int represented in a long.
 */
fun Int.unsign() = java.lang.Integer.toUnsignedLong(this)