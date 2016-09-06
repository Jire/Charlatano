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

package com.charlatano.overlay

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.awt.Graphics
import java.util.Collections.synchronizedList
import javax.swing.JPanel
import javax.swing.JWindow

object Overlay : JWindow() {
	
	private val hooks = synchronizedList(ObjectArrayList<Graphics.() -> Unit>(1024))
	
	private val frame = object : JPanel() {
		override fun paintComponent(g: Graphics) {
			for (i in 0..hooks.size - 1) hooks[i](g)
			hooks.clear()
		}
	}
	
	init {
/*		isAlwaysOnTop = true
		size = Dimension(SCREEN_SIZE.width, SCREEN_SIZE.height)
		background = Color(0, 0, 0, 0)
		
		frame.size = Dimension(SCREEN_SIZE.width, SCREEN_SIZE.height)
		add(frame)
		
		isVisible = true
		
		WindowTools.setTransparent(this)
		
		every(4) { repaint() }*/
	}
	
	operator fun invoke(body: Graphics.() -> Unit) {
		hooks.add(body)
	}
	
}