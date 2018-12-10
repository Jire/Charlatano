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
 * The range of recoil control you want to have applied.
 *
 * If both values are equal, there will be no randomization.
 *
 * Having imperfect RCS will greatly lower league ban rate.
 */
var RCS_MIN = 1.88
var RCS_MAX = 1.98

/**
 * The duration in milliseconds at which recoil control is checked.
 *
 * The higher these values, the lower the "shakiness", but also the lower the accuracy.
 *
 * Max must always be greater than min. Set to 1 and 1 for perfect control.
 */
var RCS_MIN_DURATION = 8
var RCS_MAX_DURATION = 16