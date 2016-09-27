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

package opengl.gl2d;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import javax.swing.*;

/**
 * Wraps a {@code JComponent} and paints it using a {@code GLGraphics2D}. This
 * object will paint the entire component fully for each frame.
 * <p>
 * <p>
 * update the size and layout of the painted Swing component.
 * </p>
 */
public class GLG2DSimpleEventListener implements GLEventListener {
	/**
	 * The cached object.
	 */
	protected GLGraphics2D g2d;
	
	/**
	 * The component to paint.
	 */
	protected JComponent comp;
	
	public GLG2DSimpleEventListener(JComponent component) {
		if (component == null) {
			throw new NullPointerException("component is null");
		}
		
		this.comp = component;
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		prePaint(drawable);
		paintGL(g2d);
		postPaint(drawable);
	}
	
	/**
	 * Called before any painting is done. This should setup the matrices and ask
	 * the {@code GLGraphics2D} object to setup any client state.
	 */
	protected void prePaint(GLAutoDrawable drawable) {
		setupViewport(drawable);
		g2d.prePaint(drawable.getContext());
		
		// clip to only the component we're painting
		g2d.translate(comp.getX(), comp.getY());
		g2d.clipRect(0, 0, comp.getWidth(), comp.getHeight());
	}
	
	/**
	 * Defines the viewport to paint into.
	 */
	protected void setupViewport(GLAutoDrawable drawable) {
		drawable.getGL().glViewport(0, 0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
	}
	
	/**
	 * Called after all Java2D painting is complete.
	 */
	protected void postPaint(GLAutoDrawable drawable) {
		g2d.postPaint();
	}
	
	/**
	 * Paints using the {@code GLGraphics2D} object. This could be forwarded to
	 * any code that expects to draw using the Java2D framework.
	 * <p>
	 * Currently is paints the component provided, turning off double-buffering in
	 * the {@code RepaintManager} to force drawing directly to the
	 * {@code Graphics2D} object.
	 * </p>
	 */
	protected void paintGL(GLGraphics2D g2d) {
		boolean wasDoubleBuffered = comp.isDoubleBuffered();
		comp.setDoubleBuffered(false);
		
		comp.paint(g2d);
		
		comp.setDoubleBuffered(wasDoubleBuffered);
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		g2d = createGraphics2D(drawable);
	}
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	}
	
	/**
	 * Creates the {@code Graphics2D} object that forwards Java2D calls to OpenGL
	 * calls.
	 */
	protected GLGraphics2D createGraphics2D(GLAutoDrawable drawable) {
		return new GLGraphics2D();
	}
	
	@Override
	public void dispose(GLAutoDrawable arg0) {
		if (g2d != null) {
			g2d.glDispose();
			g2d = null;
		}
	}
}
