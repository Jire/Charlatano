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
import opengl.gl2d.GLGraphics2D;
import opengl.gl2d.impl.AbstractShapeHelper;
import opengl.gl2d.impl.SimpleOrTesselatingVisitor;

import java.awt.*;
import java.awt.RenderingHints.Key;

public class GL2ShapeDrawer extends AbstractShapeHelper {
	protected GL2 gl;
	
	protected FillSimpleConvexPolygonVisitor simpleFillVisitor;
	protected SimpleOrTesselatingVisitor complexFillVisitor;
	protected LineDrawingVisitor simpleStrokeVisitor;
	protected FastLineVisitor fastLineVisitor;
	
	public GL2ShapeDrawer() {
		simpleFillVisitor = new FillSimpleConvexPolygonVisitor();
		complexFillVisitor = new SimpleOrTesselatingVisitor(simpleFillVisitor, new GL2TesselatorVisitor());
		simpleStrokeVisitor = new LineDrawingVisitor();
		fastLineVisitor = new FastLineVisitor();
	}
	
	@Override
	public void setG2D(GLGraphics2D g2d) {
		super.setG2D(g2d);
		GL gl = g2d.getGLContext().getGL();
		simpleFillVisitor.setGLContext(gl);
		complexFillVisitor.setGLContext(gl);
		simpleStrokeVisitor.setGLContext(gl);
		fastLineVisitor.setGLContext(gl);
		
		this.gl = gl.getGL2();
	}
	
	@Override
	public void setHint(Key key, Object value) {
		super.setHint(key, value);
		
		if (key == RenderingHints.KEY_ANTIALIASING) {
			if (value == RenderingHints.VALUE_ANTIALIAS_ON) {
				gl.glEnable(GL.GL_MULTISAMPLE);
			} else {
				gl.glDisable(GL.GL_MULTISAMPLE);
			}
		}
	}
	
	@Override
	public void draw(Shape shape) {
		Stroke stroke = getStroke();
		if (stroke instanceof BasicStroke) {
			BasicStroke basicStroke = (BasicStroke) stroke;
			if (fastLineVisitor.isValid(basicStroke)) {
				fastLineVisitor.setStroke(basicStroke);
				traceShape(shape, fastLineVisitor);
				return;
			} else if (basicStroke.getDashArray() == null) {
				simpleStrokeVisitor.setStroke(basicStroke);
				traceShape(shape, simpleStrokeVisitor);
				return;
			}
		}
		
		// can fall through for various reasons
		fill(stroke.createStrokedShape(shape));
	}
	
	@Override
	protected void fill(Shape shape, boolean forceSimple) {
		if (forceSimple) {
			traceShape(shape, simpleFillVisitor);
		} else {
			traceShape(shape, complexFillVisitor);
		}
	}
}
