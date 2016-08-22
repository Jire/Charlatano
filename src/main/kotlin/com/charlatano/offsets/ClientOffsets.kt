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

package com.charlatano.offsets

import com.charlatano.clientDLL

val m_dwWorldDecal by clientDLL.offset(read = false, subtract = false, className = "DT_TEWorldDecal")
val m_dwFirstClass by clientDLL.offset(patternOffset = 0x2B, subtract = false, offset = m_dwWorldDecal)

val m_dwForceJump by clientDLL(0x89, 0x15, 0, 0, 0, 0, 0x8B, 0x15, 0, 0, 0, 0, 0xF6,
		0xC2, 0x03, 0x74, 0x03, 0x83, 0xCE, 0x08).scan(2)
val m_dwLocalPlayer by clientDLL(0xA3, 0, 0, 0, 0, 0xC7, 0x05, 0, 0, 0, 0, 0, 0, 0, 0,
		0xE8, 0, 0, 0, 0, 0x59, 0xC3, 0x6A).scan(1, 16)
val m_dwGlowObject by clientDLL(0xA1, 0, 0, 0, 0, 0xA8, 0x01, 0x75, 0x4E, 0x0F, 0x57, 0xC0).scan(1, 4)

val m_dwEntityList by clientDLL(187, 0, 0, 0, 0, 131, 255, 1, 15, 140, 0, 0, 0, 0, 59, 248).scan(1)