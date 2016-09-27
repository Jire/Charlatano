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
package opengl.gl2d.impl;


import opengl.gl2d.GLG2DTextHelper;
import opengl.gl2d.GLGraphics2D;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.Deque;

import static java.lang.Math.ceil;

public abstract class AbstractTextDrawer implements GLG2DTextHelper {
	protected GLGraphics2D g2d;
	
	protected Deque<FontState> stack = new ArrayDeque<FontState>();
	
	@Override
	public void setG2D(GLGraphics2D g2d) {
		this.g2d = g2d;
		
		stack.clear();
		stack.push(new FontState());
	}
	
	@Override
	public void push(GLGraphics2D newG2d) {
		stack.push(stack.peek().clone());
	}
	
	@Override
	public void pop(GLGraphics2D parentG2d) {
		stack.pop();
	}
	
	@Override
	public void setHint(Key key, Object value) {
		if (key == RenderingHints.KEY_TEXT_ANTIALIASING) {
			stack.peek().antiAlias = value == RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
		}
	}
	
	@Override
	public void resetHints() {
		setHint(RenderingHints.KEY_TEXT_ANTIALIASING, null);
	}
	
	@Override
	public void setFont(Font font) {
		stack.peek().font = font;
	}
	
	@Override
	public Font getFont() {
		return stack.peek().font;
	}
	
	@Override
	public FontMetrics getFontMetrics(Font font) {
		return new GLFontMetrics(font, getFontRenderContext());
	}
	
	@Override
	public FontRenderContext getFontRenderContext() {
		return new FontRenderContext(g2d.getTransform(), stack.peek().antiAlias, false);
	}
	
	/**
	 * The default implementation is good enough for now.
	 */
	public static class GLFontMetrics extends FontMetrics {
		private static final long serialVersionUID = 3676850359220061793L;
		
		protected FontRenderContext fontRenderContext;
		
		public GLFontMetrics(Font font, FontRenderContext frc) {
			super(font);
			fontRenderContext = frc;
		}
		
		@Override
		public FontRenderContext getFontRenderContext() {
			return fontRenderContext;
		}
		
		@Override
		public int charsWidth(char[] data, int off, int len) {
			if (len <= 0) {
				return 0;
			}
			
			Rectangle2D bounds = font.getStringBounds(data, off, len, getFontRenderContext());
			return (int) ceil(bounds.getWidth());
		}
	}
	
	protected static class FontState implements Cloneable {
		public Font font;
		public boolean antiAlias;
		
		@Override
		public FontState clone() {
			try {
				return (FontState) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new AssertionError(e);
			}
		}
	}
}
