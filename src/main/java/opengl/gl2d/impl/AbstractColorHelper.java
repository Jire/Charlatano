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

import com.jogamp.opengl.GL;
import opengl.gl2d.GLG2DColorHelper;
import opengl.gl2d.GLGraphics2D;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.util.ArrayDeque;
import java.util.Deque;

import static opengl.gl2d.impl.GLG2DNotImplemented.notImplemented;

public abstract class AbstractColorHelper implements GLG2DColorHelper {
	protected GLGraphics2D g2d;
	
	protected Deque<ColorState> stack = new ArrayDeque<ColorState>();
	
	@Override
	public void setG2D(GLGraphics2D g2d) {
		this.g2d = g2d;
		
		stack.clear();
		stack.push(new ColorState());
	}
	
	@Override
	public void push(GLGraphics2D newG2d) {
		stack.push(stack.peek().clone());
	}
	
	@Override
	public void pop(GLGraphics2D parentG2d) {
		stack.pop();
		
		// set all the states
		setComposite(getComposite());
		setColor(getColor());
		setBackground(getBackground());
	}
	
	@Override
	public void setHint(Key key, Object value) {
		// nop
	}
	
	@Override
	public void resetHints() {
		// nop
	}
	
	@Override
	public void dispose() {
	}
	
	@Override
	public void setComposite(Composite comp) {
		GL gl = g2d.getGLContext().getGL();
		gl.glEnable(GL.GL_BLEND);
		if (comp instanceof AlphaComposite) {
			switch (((AlphaComposite) comp).getRule()) {
	  /*
	   * Since the destination _always_ covers the entire canvas (i.e. there are
       * always color components for every pixel), some of these composites can
       * be collapsed into each other. They matter when Java2D is drawing into
       * an image and the destination may not take up the entire canvas.
       */
				case AlphaComposite.SRC:
				case AlphaComposite.SRC_IN:
					gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ZERO);
					break;
				
				case AlphaComposite.SRC_OVER:
				case AlphaComposite.SRC_ATOP:
					gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
					break;
				
				case AlphaComposite.SRC_OUT:
				case AlphaComposite.CLEAR:
					gl.glBlendFunc(GL.GL_ZERO, GL.GL_ZERO);
					break;
				
				case AlphaComposite.DST:
				case AlphaComposite.DST_OVER:
					gl.glBlendFunc(GL.GL_ZERO, GL.GL_ONE);
					break;
				
				case AlphaComposite.DST_IN:
				case AlphaComposite.DST_ATOP:
					gl.glBlendFunc(GL.GL_ZERO, GL.GL_SRC_ALPHA);
					break;
				
				case AlphaComposite.DST_OUT:
				case AlphaComposite.XOR:
					gl.glBlendFunc(GL.GL_ZERO, GL.GL_ONE_MINUS_SRC_ALPHA);
					break;
			}
			
			stack.peek().composite = comp;
			// need to pre-multiply the alpha
			setColor(getColor());
		} else {
			notImplemented("setComposite(Composite) with " + comp == null ? "null Composite" : comp.getClass().getSimpleName());
		}
	}
	
	@Override
	public Composite getComposite() {
		return stack.peek().composite;
	}
	
	@Override
	public void setColor(Color c) {
		if (c == null) {
			return;
		}
		
		stack.peek().color = c;
		setColorRespectComposite(c);
	}
	
	@Override
	public Color getColor() {
		return stack.peek().color;
	}
	
	@Override
	public void setBackground(Color color) {
		stack.peek().background = color;
	}
	
	@Override
	public Color getBackground() {
		return stack.peek().background;
	}
	
	@Override
	public void setPaint(Paint paint) {
		stack.peek().paint = paint;
	}
	
	@Override
	public Paint getPaint() {
		return stack.peek().paint;
	}
	
	protected static class ColorState implements Cloneable {
		public Composite composite;
		public Color color;
		public Paint paint;
		public Color background;
		
		@Override
		public ColorState clone() {
			try {
				return (ColorState) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new AssertionError(e);
			}
		}
	}
}