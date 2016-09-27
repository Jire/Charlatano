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


import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import opengl.gl2d.VertexBuffer;
import opengl.gl2d.impl.SimplePathVisitor;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * Draws a line using the native GL implementation of a line. This is only
 * appropriate if the width of the line is less than a certain number of pixels
 * (not coordinate units) so that the user cannot see that the join and
 * endpoints are different. See {@link #isValid(BasicStroke)} for a set of
 * useful criteria.
 */
public class FastLineVisitor extends SimplePathVisitor {
	protected float[] testMatrix = new float[16];
	
	protected VertexBuffer buffer = VertexBuffer.getSharedBuffer();
	
	protected GL2 gl;
	
	protected BasicStroke stroke;
	
	protected float glLineWidth;
	
	@Override
	public void setGLContext(GL context) {
		gl = context.getGL2();
	}
	
	@Override
	public void setStroke(BasicStroke stroke) {
		gl.glLineWidth(glLineWidth);
		gl.glPointSize(glLineWidth);
	
    /*
     * Not perfect copy of the BasicStroke implementation, but it does get
     * decently close. The pattern is pretty much the same. I think it's pretty
     * much impossible to do with out a fragment shader and only the fixed
     * function pipeline.
     */
		float[] dash = stroke.getDashArray();
		if (dash != null) {
			float totalLength = 0;
			for (float f : dash) {
				totalLength += f;
			}
			
			float lengthSoFar = 0;
			int prevIndex = 0;
			int mask = 0;
			for (int i = 0; i < dash.length; i++) {
				lengthSoFar += dash[i];
				
				int nextIndex = (int) (lengthSoFar / totalLength * 16);
				for (int j = prevIndex; j < nextIndex; j++) {
					mask |= (~i & 1) << j;
				}
				
				prevIndex = nextIndex;
			}

      /*
       * XXX Should actually use the stroke phase, but not sure how yet.
       */
			
			gl.glEnable(GL2.GL_LINE_STIPPLE);
			int factor = (int) totalLength;
			gl.glLineStipple(factor >> 4, (short) mask);
		} else {
			gl.glDisable(GL2.GL_LINE_STIPPLE);
		}
		
		this.stroke = stroke;
	}
	
	/**
	 * Returns {@code true} if this class can reasonably render the line. This
	 * takes into account whether or not the transform will blow the line width
	 * out of scale and it obvious that we aren't drawing correct corners and line
	 * endings.
	 * <p>
	 * <p>
	 * Note: This must be called before {@link #setStroke(BasicStroke)}. If this
	 * returns {@code false} then this renderer should not be used.
	 * </p>
	 */
	public boolean isValid(BasicStroke stroke) {
		// if the dash length is odd, I don't know how to handle that yet
		float[] dash = stroke.getDashArray();
		if (dash != null && (dash.length & 1) == 1) {
			return false;
		}
		
		gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, testMatrix, 0);
		
		float scaleX = Math.abs(testMatrix[0]);
		float scaleY = Math.abs(testMatrix[5]);
		
		// scales are different, we can't get a good line width
		if (Math.abs(scaleX - scaleY) > 1e-6) {
			return false;
		}
		
		float strokeWidth = stroke.getLineWidth();
		
		// gl line width is in pixels, convert to pixel width
		glLineWidth = strokeWidth * scaleX;
		
		// we'll only try if it's a thin line
		return glLineWidth <= 2;
	}
	
	@Override
	public void moveTo(float[] vertex) {
		drawLine(false);
		buffer.addVertex(vertex, 0, 1);
	}
	
	@Override
	public void lineTo(float[] vertex) {
		buffer.addVertex(vertex, 0, 1);
	}
	
	@Override
	public void closeLine() {
		drawLine(true);
	}
	
	protected void drawLine(boolean close) {
		FloatBuffer buf = buffer.getBuffer();
		int p = buf.position();
		buffer.drawBuffer(gl, close ? GL2.GL_LINE_LOOP : GL2.GL_LINE_STRIP);

    /*
     * We'll ignore butt endcaps, but we'll pretend like we're drawing round,
     * bevel or miter corners as well as round or square corners by just putting
     * a point there. Since our line should be very thin, pixel-wise, it
     * shouldn't be noticeable.
     */
		if (stroke.getDashArray() == null) {
			buf.position(p);
			buffer.drawBuffer(gl, GL2.GL_POINTS);
		}
		
		buffer.clear();
	}
	
	@Override
	public void beginPoly(int windingRule) {
		buffer.clear();

    /*
     * pen hangs down and to the right. See java.awt.Graphics
     */
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glTranslatef(0.5f, 0.5f, 0);
		
		gl.glPushAttrib(GL2.GL_LINE_BIT | GL2.GL_POINT_BIT);
	}
	
	@Override
	public void endPoly() {
		drawLine(false);
		gl.glDisable(GL2.GL_LINE_STIPPLE);
		gl.glPopMatrix();
		
		gl.glPopAttrib();
	}
}
