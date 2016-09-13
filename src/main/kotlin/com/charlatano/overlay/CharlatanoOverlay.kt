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

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import it.unimi.dsi.fastutil.objects.ObjectArrayList


object CharlatanoOverlay : ApplicationAdapter() {

    class OverlayContext {
        lateinit var batch: SpriteBatch
        lateinit var camera: OrthographicCamera
        lateinit var shapeRenderer: ShapeRenderer
        lateinit var textRenderer: BitmapFont
    }

    lateinit var batch: SpriteBatch
    lateinit var camera: OrthographicCamera
    lateinit var shapeRenderer: ShapeRenderer
    lateinit var textRenderer: BitmapFont

    override fun create() {
        batch = SpriteBatch()
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.setToOrtho(true, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        shapeRenderer = ShapeRenderer()
        shapeRenderer.setAutoShapeType(true)
        textRenderer = BitmapFont()
        textRenderer.color = Color.RED
    }

    private val overlayIteration = ThreadLocal.withInitial { OverlayContext() }

    private val bodies = ObjectArrayList<OverlayContext.() -> Unit>()

    override fun render() {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        shapeRenderer.projectionMatrix = camera.combined

        batch.begin()

        val overlayIteration = overlayIteration.get()
        overlayIteration.batch = batch
        overlayIteration.camera = camera
        overlayIteration.shapeRenderer = shapeRenderer
        overlayIteration.textRenderer = textRenderer

        for (x in 0..bodies.size - 1) bodies[x](overlayIteration)

        batch.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    override fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
    }

    operator fun invoke(body: OverlayContext.() -> Unit) {
        bodies.add(body)
    }

}
