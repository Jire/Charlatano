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

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.game.CSGO.gameX
import com.charlatano.game.CSGO.gameY
import com.charlatano.utils.every
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef

object Overlay {

    fun open() {
        val cfg = LwjglApplicationConfiguration()
        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")
        cfg.width = gameWidth
        cfg.height = gameHeight
        cfg.x = gameX
        cfg.y = gameY
        cfg.resizable = false
        cfg.fullscreen = false
        cfg.initialBackgroundColor = Color(0f, 0f, 0f, 0f)

        LwjglApplication(CharlatanoOverlay, cfg)

        var hwnd: WinDef.HWND?
        while (true) {
            hwnd = User32.INSTANCE.FindWindow(null, "CharlatanoOverlay")
            if (hwnd != null) {
                break
            }
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        WindowTools.transparentWindow(hwnd!!)

        // Gdx.graphics.isContinuousRendering = false

        every(32) {
            //Gdx.graphics.requestRendering()
            User32.INSTANCE.MoveWindow(hwnd!!, gameX, gameY, gameWidth, gameHeight, false)
        }
    }

}