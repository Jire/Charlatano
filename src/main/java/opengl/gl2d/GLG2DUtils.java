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

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES1;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GLG2DUtils {
	private static final Logger LOGGER = Logger.getLogger(GLG2DUtils.class.getName());
	
	public static void setColor(GL2ES1 gl, Color c, float preMultiplyAlpha) {
		int rgb = c.getRGB();
		gl.glColor4ub((byte) (rgb >> 16 & 0xFF), (byte) (rgb >> 8 & 0xFF), (byte) (rgb & 0xFF), (byte) ((rgb >> 24 & 0xFF) * preMultiplyAlpha));
	}
	
	public static float[] getGLColor(Color c) {
		return c.getComponents(null);
	}
	
	public static int getViewportHeight(GL gl) {
		int[] viewportDimensions = new int[4];
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewportDimensions, 0);
		int canvasHeight = viewportDimensions[3];
		return canvasHeight;
	}
	
	public static void logGLError(GL gl) {
		int error = gl.glGetError();
		if (error != GL.GL_NO_ERROR) {
			LOGGER.log(Level.SEVERE, "GL Error: code " + error);
		}
	}
	
	public static int ensureIsGLBuffer(GL gl, int bufferId) {
		if (gl.glIsBuffer(bufferId)) {
			return bufferId;
		} else {
			return genBufferId(gl);
		}
	}
	
	public static int genBufferId(GL gl) {
		int[] ids = new int[1];
		gl.glGenBuffers(1, ids, 0);
		return ids[0];
	}
}
