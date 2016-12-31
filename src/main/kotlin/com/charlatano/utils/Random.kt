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

package com.charlatano.utils

import java.util.concurrent.ThreadLocalRandom.current as tlr

fun randDouble(min: Double, max: Double) = tlr().nextDouble(min, max)
fun randDouble() = tlr().nextDouble()

fun randInt(min: Int, max: Int) = tlr().nextInt(min, max)
fun randInt(min: Int) = tlr().nextInt(min)
fun randInt() = tlr().nextInt()

fun randLong(min: Long, max: Long) = tlr().nextLong(min, max)
fun randLong(min: Long) = tlr().nextLong(min)
fun randLong() = tlr().nextLong()

fun randBoolean() = tlr().nextBoolean()