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
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import opengl.gl2d.GLGraphics2D;
import opengl.gl2d.impl.AbstractMatrixHelper;

import java.awt.geom.AffineTransform;

public class GL2Transformhelper extends AbstractMatrixHelper {
	protected GL2 gl;
	
	private float[] matrixBuf = new float[16];
	
	@Override
	public void setG2D(GLGraphics2D g2d) {
		super.setG2D(g2d);
		gl = g2d.getGLContext().getGL().getGL2();
		
		setupGLView();
		flushTransformToOpenGL();
	}
	
	protected void setupGLView() {
		int[] viewportDimensions = new int[4];
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewportDimensions, 0);
		int width = viewportDimensions[2];
		int height = viewportDimensions[3];
		
		// setup projection
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, width, 0, height, -1, 1);
		
		// the MODELVIEW matrix will get adjusted later
		
		gl.glMatrixMode(GL.GL_TEXTURE);
		gl.glLoadIdentity();
	}
	
	/**
	 * Sends the {@code AffineTransform} that's on top of the stack to the video
	 * card.
	 */
	protected void flushTransformToOpenGL() {
		float[] matrix = getGLMatrix(stack.peek());
		
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadMatrixf(matrix, 0);
	}
	
	/**
	 * Gets the GL matrix for the {@code AffineTransform} with the change of
	 * coordinates inlined. Since Java2D uses the upper-left as 0,0 and OpenGL
	 * uses the lower-left as 0,0, we have to pre-multiply the matrix before
	 * loading it onto the video card.
	 */
	protected float[] getGLMatrix(AffineTransform transform) {
		matrixBuf[0] = (float) transform.getScaleX();
		matrixBuf[1] = -(float) transform.getShearY();
		matrixBuf[4] = (float) transform.getShearX();
		matrixBuf[5] = -(float) transform.getScaleY();
		matrixBuf[10] = 1;
		matrixBuf[12] = (float) transform.getTranslateX();
		matrixBuf[13] = g2d.getCanvasHeight() - (float) transform.getTranslateY();
		matrixBuf[15] = 1;
		
		return matrixBuf;
	}
}
