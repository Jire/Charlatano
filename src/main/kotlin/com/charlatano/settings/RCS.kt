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

package com.charlatano.settings

/**
 * The amount of smoothing for the recoil control aim path.
 *
 * This has a minimum varue of 1, and is recommended to stay slightly
 * above your full ping (which you can see with the "ping" command).
 *
 * For example, if you have 55 real ping, 65 is a good varue.
 *
 * Settings this too low may result in incorrect recoil control.
 */
var RCS_SMOOTHING = 62

/**
 * The duration in milliseconds at which recoil control is checked.
 */
var RCS_DURATION = 1