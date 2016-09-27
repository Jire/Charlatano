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

/**
 * Fills a simple convex polygon. This class does not test to determine if the
 * polygon is actually simple and convex.
 */
public class FillSimpleConvexPolygonVisitor extends SimplePathVisitor {
	protected GL2 gl;
	
	protected VertexBuffer vBuffer = VertexBuffer.getSharedBuffer();
	
	@Override
	public void setGLContext(GL context) {
		gl = context.getGL2();
	}
	
	@Override
	public void setStroke(BasicStroke stroke) {
		// nop
	}
	
	@Override
	public void beginPoly(int windingRule) {
		vBuffer.clear();

    /*
     * We don't care what the winding rule is, we disable face culling.
     */
		gl.glDisable(GL.GL_CULL_FACE);
	}
	
	@Override
	public void closeLine() {
		vBuffer.drawBuffer(gl, GL2.GL_POLYGON);
	}
	
	@Override
	public void endPoly() {
	}
	
	@Override
	public void lineTo(float[] vertex) {
		vBuffer.addVertex(vertex, 0, 1);
	}
	
	@Override
	public void moveTo(float[] vertex) {
		vBuffer.clear();
		vBuffer.addVertex(vertex, 0, 1);
	}
}
