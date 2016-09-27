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
package opengl.gl2d.impl.gl2;


import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.awt.TextRenderer;
import opengl.gl2d.impl.AbstractTextDrawer;

import java.awt.*;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;

/**
 * Draws text for the {@code GLGraphics2D} class.
 */
public class GL2StringDrawer extends AbstractTextDrawer {
	protected FontRenderCache cache = new FontRenderCache();
	
	@Override
	public void dispose() {
		cache.dispose();
	}
	
	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {
		drawString(iterator, (int) x, (int) y);
	}
	
	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		StringBuilder builder = new StringBuilder(iterator.getEndIndex() - iterator.getBeginIndex());
		while (iterator.next() != AttributedCharacterIterator.DONE) {
			builder.append(iterator.current());
		}
		
		drawString(builder.toString(), x, y);
	}
	
	@Override
	public void drawString(String string, float x, float y) {
		drawString(string, (int) x, (int) y);
	}
	
	@Override
	public void drawString(String string, int x, int y) {
		TextRenderer renderer = getRenderer(getFont());
		
		begin(renderer);
		renderer.draw3D(string, x, g2d.getCanvasHeight() - y, 0, 1);
		end(renderer);
	}
	
	protected TextRenderer getRenderer(Font font) {
		return cache.getRenderer(font, stack.peek().antiAlias);
	}
	
	/**
	 * Sets the font color, respecting the AlphaComposite if it wants to
	 * pre-multiply an alpha.
	 */
	protected void setTextColorRespectComposite(TextRenderer renderer) {
		Color color = g2d.getColor();
		if (g2d.getComposite() instanceof AlphaComposite) {
			float alpha = ((AlphaComposite) g2d.getComposite()).getAlpha();
			if (alpha < 1) {
				float[] rgba = color.getRGBComponents(null);
				color = new Color(rgba[0], rgba[1], rgba[2], alpha * rgba[3]);
			}
		}
		
		renderer.setColor(color);
	}
	
	protected void begin(TextRenderer renderer) {
		setTextColorRespectComposite(renderer);
		
		GL2 gl = g2d.getGLContext().getGL().getGL2();
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glScalef(1, -1, 1);
		gl.glTranslatef(0, -g2d.getCanvasHeight(), 0);
		
		renderer.begin3DRendering();
	}
	
	protected void end(TextRenderer renderer) {
		renderer.end3DRendering();
		
		GL2 gl = g2d.getGLContext().getGL().getGL2();
		gl.glPopMatrix();
	}
	
	@SuppressWarnings("serial")
	public static class FontRenderCache extends HashMap<Font, TextRenderer[]> {
		public TextRenderer getRenderer(Font font, boolean antiAlias) {
			TextRenderer[] renderers = get(font);
			if (renderers == null) {
				renderers = new TextRenderer[2];
				put(font, renderers);
			}
			
			TextRenderer renderer = renderers[antiAlias ? 1 : 0];
			
			if (renderer == null) {
				renderer = new TextRenderer(font, antiAlias, false);
				renderers[antiAlias ? 1 : 0] = renderer;
			}
			
			return renderer;
		}
		
		public void dispose() {
			for (TextRenderer[] value : values()) {
				if (value[0] != null) {
					value[0].dispose();
				}
				if (value[1] != null) {
					value[1].dispose();
				}
			}
		}
	}
}
