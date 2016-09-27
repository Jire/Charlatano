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
import com.jogamp.opengl.GL2GL3;
import opengl.gl2d.GLGraphics2D;
import opengl.gl2d.impl.AbstractColorHelper;

import java.awt.*;

import static opengl.gl2d.impl.GLG2DNotImplemented.notImplemented;

public class GL2ColorHelper extends AbstractColorHelper {
	protected GL2 gl;
	
	@Override
	public void setG2D(GLGraphics2D g2d) {
		super.setG2D(g2d);
		gl = g2d.getGLContext().getGL().getGL2();
	}
	
	@Override
	public void setPaint(Paint paint) {
		if (paint instanceof Color) {
			setColor((Color) paint);
		} else if (paint instanceof GradientPaint) {
			setColor(((GradientPaint) paint).getColor1());
			notImplemented("setPaint(Paint) with GradientPaint");
		} else if (paint instanceof MultipleGradientPaint) {
			setColor(((MultipleGradientPaint) paint).getColors()[0]);
			notImplemented("setPaint(Paint) with MultipleGradientPaint");
		} else {
			notImplemented("setPaint(Paint) with " + paint.getClass().getSimpleName());
			// This will probably be easier to handle with a fragment shader
			// in the shader pipeline, not sure how to handle it in the fixed-
			// function pipeline.
		}
	}
	
	@Override
	public Paint getPaint() {
		return getColor();
	}
	
	@Override
	public void setColorNoRespectComposite(Color c) {
		setColor(gl, c, 1);
	}
	
	/**
	 * Sets the current color with a call to glColor4*. But it respects the
	 * AlphaComposite if any. If the AlphaComposite wants to pre-multiply an
	 * alpha, pre-multiply it.
	 */
	@Override
	public void setColorRespectComposite(Color c) {
		float alpha = 1;
		Composite composite = getComposite();
		if (composite instanceof AlphaComposite) {
			alpha = ((AlphaComposite) composite).getAlpha();
		}
		
		setColor(gl, c, alpha);
	}
	
	private void setColor(GL2 gl, Color c, float preMultiplyAlpha) {
		int rgb = c.getRGB();
		gl.glColor4ub((byte) (rgb >> 16 & 0xFF), (byte) (rgb >> 8 & 0xFF), (byte) (rgb & 0xFF), (byte) ((rgb >> 24 & 0xFF) * preMultiplyAlpha));
	}
	
	@Override
	public void setPaintMode() {
		notImplemented("setPaintMode()");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void setXORMode(Color c) {
		notImplemented("setXORMode(Color)");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		// glRasterPos* is transformed, but CopyPixels is not
		int x2 = x + dx;
		int y2 = y + dy + height;
		gl.glRasterPos2i(x2, y2);
		
		int x1 = x;
		int y1 = g2d.getCanvasHeight() - (y + height);
		gl.glCopyPixels(x1, y1, width, height, GL2GL3.GL_COLOR);
	}
}
