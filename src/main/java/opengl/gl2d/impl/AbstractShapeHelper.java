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


import opengl.gl2d.GLG2DShapeHelper;
import opengl.gl2d.GLGraphics2D;
import opengl.gl2d.PathVisitor;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.geom.*;
import java.util.ArrayDeque;
import java.util.Deque;

public abstract class AbstractShapeHelper implements GLG2DShapeHelper {
	/**
	 * We know this is single-threaded, so we can use these as archetypes.
	 */
	protected static final Ellipse2D.Float ELLIPSE = new Ellipse2D.Float();
	protected static final RoundRectangle2D.Float ROUND_RECT = new RoundRectangle2D.Float();
	protected static final Arc2D.Float ARC = new Arc2D.Float();
	protected static final Rectangle2D.Float RECT = new Rectangle2D.Float();
	protected static final Line2D.Float LINE = new Line2D.Float();
	
	protected Deque<Stroke> strokeStack = new ArrayDeque<Stroke>();
	
	public AbstractShapeHelper() {
		strokeStack.push(new BasicStroke());
	}
	
	@Override
	public void setG2D(GLGraphics2D g2d) {
		strokeStack.clear();
		strokeStack.push(new BasicStroke());
	}
	
	@Override
	public void push(GLGraphics2D newG2d) {
		strokeStack.push(newG2d.getStroke());
	}
	
	@Override
	public void pop(GLGraphics2D parentG2d) {
		strokeStack.pop();
	}
	
	@Override
	public void setHint(Key key, Object value) {
		// nop
	}
	
	@Override
	public void resetHints() {
		setHint(RenderingHints.KEY_ANTIALIASING, null);
	}
	
	@Override
	public void dispose() {
		// nop
	}
	
	@Override
	public void setStroke(Stroke stroke) {
		strokeStack.pop();
		strokeStack.push(stroke);
	}
	
	@Override
	public Stroke getStroke() {
		return strokeStack.peek();
	}
	
	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, boolean fill) {
		ROUND_RECT.setRoundRect(x, y, width, height, arcWidth, arcHeight);
		if (fill) {
			fill(ROUND_RECT, true);
		} else {
			draw(ROUND_RECT);
		}
	}
	
	@Override
	public void drawRect(int x, int y, int width, int height, boolean fill) {
		RECT.setRect(x, y, width, height);
		if (fill) {
			fill(RECT, true);
		} else {
			draw(RECT);
		}
	}
	
	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		LINE.setLine(x1, y1, x2, y2);
		draw(LINE);
	}
	
	@Override
	public void drawOval(int x, int y, int width, int height, boolean fill) {
		ELLIPSE.setFrame(x, y, width, height);
		if (fill) {
			fill(ELLIPSE, true);
		} else {
			draw(ELLIPSE);
		}
	}
	
	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle, boolean fill) {
		ARC.setArc(x, y, width, height, startAngle, arcAngle, fill ? Arc2D.PIE : Arc2D.OPEN);
		if (fill) {
			fill(ARC, true);
		} else {
			draw(ARC);
		}
	}
	
	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		drawPoly(xPoints, yPoints, nPoints, false, false);
	}
	
	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints, boolean fill) {
		drawPoly(xPoints, yPoints, nPoints, fill, true);
	}
	
	protected void drawPoly(int[] xPoints, int[] yPoints, int nPoints, boolean fill, boolean close) {
		Path2D.Float path = new Path2D.Float(PathIterator.WIND_NON_ZERO, nPoints);
		path.moveTo(xPoints[0], yPoints[0]);
		for (int i = 1; i < nPoints; i++) {
			path.lineTo(xPoints[i], yPoints[i]);
		}
		
		if (close) {
			path.closePath();
		}
		
		if (fill) {
			fill(path);
		} else {
			draw(path);
		}
	}
	
	@Override
	public void fill(Shape shape) {
		if (shape instanceof Rectangle2D ||
				shape instanceof Ellipse2D ||
				shape instanceof Arc2D ||
				shape instanceof RoundRectangle2D) {
			fill(shape, true);
		} else {
			fill(shape, false);
		}
	}
	
	protected abstract void fill(Shape shape, boolean isDefinitelySimpleConvex);
	
	protected void traceShape(Shape shape, PathVisitor visitor) {
		visitShape(shape, visitor);
	}
	
	public static void visitShape(Shape shape, PathVisitor visitor) {
		PathIterator iterator = shape.getPathIterator(null);
		visitor.beginPoly(iterator.getWindingRule());
		
		float[] coords = new float[10];
		float[] previousVertex = new float[2];
		for (; !iterator.isDone(); iterator.next()) {
			int type = iterator.currentSegment(coords);
			switch (type) {
				case PathIterator.SEG_MOVETO:
					visitor.moveTo(coords);
					break;
				
				case PathIterator.SEG_LINETO:
					visitor.lineTo(coords);
					break;
				
				case PathIterator.SEG_QUADTO:
					visitor.quadTo(previousVertex, coords);
					break;
				
				case PathIterator.SEG_CUBICTO:
					visitor.cubicTo(previousVertex, coords);
					break;
				
				case PathIterator.SEG_CLOSE:
					visitor.closeLine();
					break;
			}
			
			switch (type) {
				case PathIterator.SEG_LINETO:
				case PathIterator.SEG_MOVETO:
					previousVertex[0] = coords[0];
					previousVertex[1] = coords[1];
					break;
				
				case PathIterator.SEG_QUADTO:
					previousVertex[0] = coords[2];
					previousVertex[1] = coords[3];
					break;
				
				case PathIterator.SEG_CUBICTO:
					previousVertex[0] = coords[4];
					previousVertex[1] = coords[5];
					break;
			}
		}
		
		visitor.endPoly();
	}
}