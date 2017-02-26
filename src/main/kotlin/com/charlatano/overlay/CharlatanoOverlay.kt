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

package com.charlatano.overlay

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.charlatano.game.CSGO.gameHeight
import com.charlatano.game.CSGO.gameWidth
import com.charlatano.utils.paused
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.concurrent.atomic.AtomicReference

object CharlatanoOverlay : ApplicationAdapter() {
	
	var created: Boolean = false
	
	lateinit var batch: SpriteBatch
	lateinit var camera: OrthographicCamera
	lateinit var shapeRenderer: ShapeRenderer
	lateinit var textRenderer: BitmapFont
	
	private val bodies = ObjectArrayList<CharlatanoOverlay.() -> Unit>()
	
	override fun create() {
		camera = OrthographicCamera(gameWidth.toFloat(), gameHeight.toFloat()).apply {
			setToOrtho(true, gameWidth.toFloat(), gameHeight.toFloat())
		}
		batch = SpriteBatch().apply { projectionMatrix = camera.combined }
		shapeRenderer = ShapeRenderer().apply { setAutoShapeType(true) }
		textRenderer = BitmapFont(true).apply { color = Color.RED }
		
		created = true
	}
	
	override fun render() {
		gl.apply {
			glEnable(GL20.GL_BLEND)
			glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
			glClearColor(0F, 0F, 0F, 0F)
			glClear(GL20.GL_COLOR_BUFFER_BIT)
			
			if (paused) return
			
			camera.setToOrtho(true, gameWidth.toFloat(), gameHeight.toFloat())
			batch.projectionMatrix = camera.combined
			shapeRenderer.projectionMatrix = camera.combined
			
			for (i in 0..bodies.size - 1) bodies[i]()
			
			glDisable(GL20.GL_BLEND)
		}
	}
	
	override fun dispose() {
		batch.dispose()
		shapeRenderer.dispose()
		textRenderer.dispose()
	}
	
	operator fun invoke(body: CharlatanoOverlay.() -> Unit) {
		bodies.add(body)
	}
	
}