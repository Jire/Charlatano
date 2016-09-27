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

import opengl.gl2d.GLG2DTransformHelper;
import opengl.gl2d.GLGraphics2D;

import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.Deque;

public abstract class AbstractMatrixHelper implements GLG2DTransformHelper {
	protected GLGraphics2D g2d;
	
	protected Deque<AffineTransform> stack = new ArrayDeque<AffineTransform>();
	
	@Override
	public void setG2D(GLGraphics2D g2d) {
		this.g2d = g2d;
		
		stack.clear();
		stack.push(new AffineTransform());
	}
	
	@Override
	public void push(GLGraphics2D newG2d) {
		stack.push(getTransform());
	}
	
	@Override
	public void pop(GLGraphics2D parentG2d) {
		stack.pop();
		flushTransformToOpenGL();
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
		// nop
	}
	
	@Override
	public void translate(int x, int y) {
		translate((double) x, (double) y);
		flushTransformToOpenGL();
	}
	
	@Override
	public void translate(double tx, double ty) {
		getTransform0().translate(tx, ty);
		flushTransformToOpenGL();
	}
	
	@Override
	public void rotate(double theta) {
		getTransform0().rotate(theta);
		flushTransformToOpenGL();
	}
	
	@Override
	public void rotate(double theta, double x, double y) {
		getTransform0().rotate(theta, x, y);
		flushTransformToOpenGL();
	}
	
	@Override
	public void scale(double sx, double sy) {
		getTransform0().scale(sx, sy);
		flushTransformToOpenGL();
	}
	
	@Override
	public void shear(double shx, double shy) {
		getTransform0().shear(shx, shy);
		flushTransformToOpenGL();
	}
	
	@Override
	public void transform(AffineTransform Tx) {
		getTransform0().concatenate(Tx);
		flushTransformToOpenGL();
	}
	
	@Override
	public void setTransform(AffineTransform transform) {
		getTransform0().setTransform(transform);
		flushTransformToOpenGL();
	}
	
	@Override
	public AffineTransform getTransform() {
		return (AffineTransform) getTransform0().clone();
	}
	
	/**
	 * Returns the {@code AffineTransform} at the top of the stack, <em>not</em> a
	 * copy.
	 */
	protected AffineTransform getTransform0() {
		return stack.peek();
	}
	
	/**
	 * Sends the {@code AffineTransform} that's on top of the stack to the video
	 * card.
	 */
	protected abstract void flushTransformToOpenGL();
}
