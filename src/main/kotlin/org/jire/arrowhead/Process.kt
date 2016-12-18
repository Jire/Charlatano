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

package org.jire.arrowhead

/**
 * Rather than be useful and upfront, let's go the scenic route.
 *
 * Ensure of the following:
 *     * You understand what an operating system is
 *     * You're familiar with said operating system's process manager
 *
 * K, we're good.
 *
 * _**At heart**_, this is a magical time-travel vortex that will take you to the next dimension.
 *
 * _**At soul**_, this is like a butler that doesn't care what you ask because
 * he hires his own butler who actually fulfills your requests.
 */
interface Process : Source {
	
	/**
	 * The processes' special snowflake, thumbprint, or whatever you wanna' call it.
	 */
	val id: Int
	
	/**
	 * A map of module names to the modules themselves.
	 */
	val modules: Map<String, Module>
	
	/**
	 * Loads the modules into the module map.
	 *
	 * This function can be used to reload the modules as well.
	 */
	fun loadModules()
	
}