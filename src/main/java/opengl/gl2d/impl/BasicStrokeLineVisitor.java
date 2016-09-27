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

import com.jogamp.common.nio.Buffers;
import opengl.gl2d.VertexBuffer;

import java.awt.*;
import java.nio.FloatBuffer;

import static java.lang.Math.*;
import static opengl.gl2d.impl.GLG2DNotImplemented.notImplemented;

/**
 * Draws a line, as outlined by a {@link BasicStroke}. The current
 * implementation supports everything except dashes. This class draws a series
 * of quads for each line segment, joins corners and endpoints as appropriate.
 */
public abstract class BasicStrokeLineVisitor extends SimplePathVisitor {
	protected static float THETA_STEP = 0.5f;
	protected static float COS_STEP = (float) cos(THETA_STEP);
	protected static float SIN_STEP = (float) sin(THETA_STEP);
	
	protected int lineJoin;
	protected int endCap;
	protected float lineOffset;
	protected float miterLimit;
	
	protected float[] lastPoint;
	protected float[] secondLastPoint;
	protected float[] firstPoint;
	protected float[] secondPoint;
	
	protected VertexBuffer vBuffer = new VertexBuffer(1024);
	protected FloatBuffer tmpBuffer = Buffers.newDirectFloatBuffer(1024);
	
	@Override
	public void setStroke(BasicStroke stroke) {
		lineJoin = stroke.getLineJoin();
		lineOffset = stroke.getLineWidth() / 2;
		endCap = stroke.getEndCap();
		miterLimit = stroke.getMiterLimit();
		
		// TODO
		if (stroke.getDashArray() != null) {
			notImplemented("BasicStroke with dash array");
		}
	}
	
	@Override
	public void beginPoly(int windingRule) {
		clear();
	}
	
	@Override
	public void endPoly() {
		finishAndDrawLine();
	}
	
	@Override
	public void moveTo(float[] vertex) {
		finishAndDrawLine();
		
		lastPoint = new float[]{vertex[0], vertex[1]};
		firstPoint = lastPoint;
	}
	
	@Override
	public void lineTo(float[] vertex) {
		// ignore 0-length lines
		if (lastPoint[0] == vertex[0] && lastPoint[1] == vertex[1]) {
			return;
		}
		
		float[] vClone = new float[]{vertex[0], vertex[1]};
		if (secondPoint == null) {
			secondPoint = vClone;
		}
		
		if (secondLastPoint != null) {
			applyCorner(vertex);
		}
		
		secondLastPoint = lastPoint;
		lastPoint = vClone;
	}
	
	@Override
	public void closeLine() {
	/*
	 * Our first point we stroked is around the second point we hit. So we add
     * the first 2 points so we do all the corners. Then we end on the last two
     * points to finish the last two triangles.
     */
		if (firstPoint != null && secondPoint != null) {
			lineTo(firstPoint);
			lineTo(secondPoint);
			
			FloatBuffer buf = vBuffer.getBuffer();
			addVertex(buf.get(0), buf.get(1));
			addVertex(buf.get(2), buf.get(3));
			
			drawBuffer();
		}
		
		clear();
	}
	
	protected void clear() {
		vBuffer.clear();
		firstPoint = secondPoint = null;
		lastPoint = secondLastPoint = null;
	}
	
	protected void finishAndDrawLine() {
		if (firstPoint != null && secondPoint != null) {
			applyEndCap(secondLastPoint, lastPoint, false);
			
			FloatBuffer buf = vBuffer.getBuffer();
			if (tmpBuffer.capacity() < buf.position()) {
				tmpBuffer = Buffers.newDirectFloatBuffer(buf.position());
			}
			
			tmpBuffer.clear();
			
			buf.flip();
			tmpBuffer.put(buf);
			tmpBuffer.flip();
			
			buf.clear();
			applyEndCap(firstPoint, secondPoint, true);
			buf.put(tmpBuffer);
			
			drawBuffer();
		}
		
		clear();
	}
	
	@Override
	public void quadTo(float[] previousVertex, float[] control) {
		int originalJoin = lineJoin;
		
		// go around the corners quickly
		lineJoin = BasicStroke.JOIN_BEVEL;
		super.quadTo(previousVertex, control);
		lineJoin = originalJoin;
	}
	
	@Override
	public void cubicTo(float[] previousVertex, float[] control) {
		int originalJoin = lineJoin;
		
		// go around the corners quickly
		lineJoin = BasicStroke.JOIN_BEVEL;
		super.cubicTo(previousVertex, control);
		lineJoin = originalJoin;
	}
	
	protected void applyCorner(float[] vertex) {
		switch (lineJoin) {
			case BasicStroke.JOIN_BEVEL:
				drawCornerBevel(secondLastPoint, lastPoint, vertex);
				break;
			
			case BasicStroke.JOIN_ROUND:
				drawCornerRound(secondLastPoint, lastPoint, vertex);
				break;
			
			case BasicStroke.JOIN_MITER:
				drawCornerMiter(secondLastPoint, lastPoint, vertex);
				break;
			
			default:
				notImplemented("BasicStroke with unknown line join: " + lineJoin);
		}
	}
	
	protected void drawCornerRound(float[] secondLastPoint, float[] lastPoint, float[] point) {
		float[] offset1 = lineOffset(secondLastPoint, lastPoint);
		float[] offset2 = lineOffset(lastPoint, point);
		
		float[] v1 = subtract(lastPoint, secondLastPoint);
		normalize(v1);
		float[] v2 = subtract(lastPoint, point);
		normalize(v2);
		
		float[] rightPt1 = add(lastPoint, offset1);
		float[] rightPt2 = add(lastPoint, offset2);
		float[] leftPt1 = subtract(lastPoint, offset1);
		float[] leftPt2 = subtract(lastPoint, offset2);
		
		float alpha = getIntersectionAlpha(rightPt1, v1, rightPt2, v2);
		
		// get the outside angle (our vectors v1, v2 are unit vectors)
		float theta = (float) (Math.PI - acos(v1[0] * v2[0] + v1[1] * v2[1]));
		
		// if inside corner is right side
		if (alpha <= 0) {
			float[] rightInside = addScaled(rightPt1, v1, alpha);
			
			addVertex(rightInside[0], rightInside[1]);
			addVertex(leftPt1[0], leftPt1[1]);
			
			int max = (int) ceil(theta / THETA_STEP);
			// rotate the other way
			for (int i = 0; i < max; i++) {
				float newX = COS_STEP * offset1[0] + SIN_STEP * offset1[1];
				offset1[1] = -SIN_STEP * offset1[0] + COS_STEP * offset1[1];
				offset1[0] = newX;
				
				addVertex(rightInside[0], rightInside[1]);
				addVertex(lastPoint[0] - offset1[0], lastPoint[1] - offset1[1]);
			}
			
			addVertex(rightInside[0], rightInside[1]);
			addVertex(leftPt2[0], leftPt2[1]);
		} else {
			alpha = -alpha;
			float[] leftInside = addScaled(leftPt1, v1, alpha);
			
			addVertex(rightPt1[0], rightPt1[1]);
			addVertex(leftInside[0], leftInside[1]);
			
			int max = (int) ceil(theta / THETA_STEP);
			for (int i = 0; i < max; i++) {
				float newX = COS_STEP * offset1[0] - SIN_STEP * offset1[1];
				offset1[1] = SIN_STEP * offset1[0] + COS_STEP * offset1[1];
				offset1[0] = newX;
				
				addVertex(lastPoint[0] + offset1[0], lastPoint[1] + offset1[1]);
				addVertex(leftInside[0], leftInside[1]);
			}
			
			addVertex(rightPt2[0], rightPt2[1]);
			addVertex(leftInside[0], leftInside[1]);
		}
	}
	
	protected void drawCornerBevel(float[] secondLastPoint, float[] lastPoint, float[] point) {
		float[] offset1 = lineOffset(secondLastPoint, lastPoint);
		float[] offset2 = lineOffset(lastPoint, point);
		
		float[] v1 = subtract(lastPoint, secondLastPoint);
		normalize(v1);
		float[] v2 = subtract(lastPoint, point);
		normalize(v2);
		
		float[] rightPt1 = add(lastPoint, offset1);
		float[] rightPt2 = add(lastPoint, offset2);
		float[] leftPt1 = subtract(lastPoint, offset1);
		float[] leftPt2 = subtract(lastPoint, offset2);
		
		float alpha = getIntersectionAlpha(rightPt1, v1, rightPt2, v2);
		
		// if inside corner is right side
		if (alpha <= 0) {
			float[] rightInside = addScaled(rightPt1, v1, alpha);
			
			addVertex(rightInside[0], rightInside[1]);
			addVertex(leftPt1[0], leftPt1[1]);
			addVertex(rightInside[0], rightInside[1]);
			addVertex(leftPt2[0], leftPt2[1]);
		} else {
			// carry the math through and this turns out
			alpha = -alpha;
			float[] leftInside = addScaled(leftPt1, v1, alpha);
			
			addVertex(rightPt1[0], rightPt1[1]);
			addVertex(leftInside[0], leftInside[1]);
			addVertex(rightPt2[0], rightPt2[1]);
			addVertex(leftInside[0], leftInside[1]);
		}
	}
	
	protected void drawCornerMiter(float[] secondLastPoint, float[] lastPoint, float[] point) {
		float[] offset1 = lineOffset(secondLastPoint, lastPoint);
		float[] offset2 = lineOffset(lastPoint, point);
		
		float[] v1 = subtract(lastPoint, secondLastPoint);
		normalize(v1);
		float[] v2 = subtract(lastPoint, point);
		normalize(v2);
		
		float[] rightPt1 = add(lastPoint, offset1);
		float[] rightPt2 = add(lastPoint, offset2);
		float[] leftPt1 = subtract(lastPoint, offset1);
		
		float alpha = getIntersectionAlpha(rightPt1, v1, rightPt2, v2);
		float[] rightCorner = addScaled(rightPt1, v1, alpha);
		
		// other side is just the negative alpha
		alpha = -alpha;
		float[] leftCorner = addScaled(leftPt1, v1, alpha);
		
		// If we exceed the miter limit, draw beveled corner
		float dist = distance(rightCorner, leftCorner);
		
		if (dist > miterLimit * lineOffset * 2) {
			drawCornerBevel(secondLastPoint, lastPoint, point);
		} else {
			addVertex(rightCorner[0], rightCorner[1]);
			addVertex(leftCorner[0], leftCorner[1]);
		}
	}
	
	protected float distance(float[] pt1, float[] pt2) {
		double diffX = pt1[0] - pt2[0];
		double diffY = pt1[1] - pt2[1];
		double distSq = diffX * diffX + diffY * diffY;
		return (float) sqrt(distSq);
	}
	
	protected float[] addScaled(float[] pt, float[] v, float alpha) {
		return new float[]{pt[0] + v[0] * alpha, pt[1] + v[1] * alpha};
	}
	
	protected void normalize(float[] v) {
		float norm = (float) sqrt(v[0] * v[0] + v[1] * v[1]);
		v[0] /= norm;
		v[1] /= norm;
	}
	
	protected float[] subtract(float[] pt1, float[] pt2) {
		return new float[]{pt1[0] - pt2[0], pt1[1] - pt2[1]};
	}
	
	protected float[] add(float[] pt1, float[] pt2) {
		return new float[]{pt2[0] + pt1[0], pt2[1] + pt1[1]};
	}
	
	protected float getIntersectionAlpha(float[] pt1, float[] v1, float[] pt2, float[] v2) {
		float t = (pt2[0] - pt1[0]) * v2[1] - (pt2[1] - pt1[1]) * v2[0];
		t /= v1[0] * v2[1] - v1[1] * v2[0];
		return t;
	}
	
	protected float[] lineOffset(float[] linePoint1, float[] linePoint2) {
		float[] vec = new float[2];
		vec[0] = linePoint2[0] - linePoint1[0];
		vec[1] = linePoint2[1] - linePoint1[1];
		
		float norm = vec[0] * vec[0] + vec[1] * vec[1];
		norm = (float) sqrt(norm);
		
		float scale = lineOffset / norm;
		float[] offset = new float[2];
		offset[0] = vec[1] * scale;
		offset[1] = -vec[0] * scale;
		return offset;
	}
	
	protected float[] lineCorners(float[] linePoint1, float[] linePoint2, float[] vertex, float offset) {
		float[] translated = new float[2];
		translated[0] = linePoint2[0] - linePoint1[0];
		translated[1] = linePoint2[1] - linePoint1[1];
		
		float norm = translated[0] * translated[0] + translated[1] * translated[1];
		norm = (float) sqrt(norm);
		
		float scale = offset / norm;
		float[] corners = new float[4];
		corners[0] = translated[1] * scale + vertex[0];
		corners[1] = -translated[0] * scale + vertex[1];
		corners[2] = -translated[1] * scale + vertex[0];
		corners[3] = translated[0] * scale + vertex[1];
		return corners;
	}
	
	/**
	 * Finds the intersection of two lines. This method was written to reduce the
	 * number of array creations and so is quite dense. However, it is easy to
	 * understand the theory behind the computation. I found this at <a
	 * href="http://mathforum.org/library/drmath/view/62814.html"
	 * >http://mathforum.org/library/drmath/view/62814.html</a>.
	 * <p>
	 * <p>
	 * We have two lines, specified by three points (P1, P2, P3). They share the
	 * second point. This gives us an easy way to represent the line in parametric
	 * form. For example the first line has the form
	 * <p>
	 * <pre>
	 * &lt;x, y&gt; = &lt;P1<sub>x</sub>, P1<sub>y</sub>&gt; + t * &lt;P2<sub>x</sub>-P1<sub>x</sub>, P2<sub>y</sub>-P1<sub>y</sub>&gt;
	 * </pre>
	 * <p>
	 * </p>
	 * <p>
	 * <code>&lt;P1<sub>x</sub>, P1<sub>y</sub>&gt;</code> is a point on the line,
	 * while
	 * <code>&lt;P2<sub>x</sub>-P1<sub>x</sub>, P2<sub>y</sub>-P1<sub>y</sub>&gt;</code>
	 * is the direction of the line. The method for solving for the intersection
	 * of these two parametric lines is straightforward. Let <code>o1</code> and
	 * <code>o2</code> be the points on the lines and <code>v1</code> and
	 * <code>v2</code> be the two direction vectors. Now we have
	 * <p>
	 * <pre>
	 * p1 = o1 + t * v1
	 * p2 = o2 + s * v2
	 * </pre>
	 * <p>
	 * We can solve to find the intersection by
	 * <p>
	 * <pre>
	 * o1 + t * v1 = o2 + s * v2
	 * t * v1 = o2 - o1 + s * v2
	 * (t * v1) x v2 = (o2 - o1 + s * v2) x v2    ; cross product by v2
	 * t * (v1 x v2) = (o2 - o1) x v2             ; to get rid of s term
	 * </pre>
	 * <p>
	 * Solving for <code>t</code> is easy since we only have the z component. Put
	 * <code>t</code> back into the first equation gives us our point of
	 * intersection.
	 * </p>
	 * <p>
	 * This method solves for <code>t</code>, but not directly for lines
	 * intersecting the point parameters. Since we're trying to use this for the
	 * miter corners, we want to solve for the intersections of the two outside
	 * edges of the lines that go from <code>secondLastPoint</code> to
	 * <code>lastPoint</code> and from <code>lastPoint</code> to
	 * <code>point</code>.
	 * </p>
	 */
	protected float[] getMiterIntersections(float[] secondLastPoint, float[] lastPoint, float[] point) {
		float[] o1 = lineCorners(secondLastPoint, lastPoint, lastPoint, lineOffset);
		float[] o2 = lineCorners(lastPoint, point, lastPoint, lineOffset);
		
		float[] v1 = new float[2];
		v1[0] = lastPoint[0] - secondLastPoint[0];
		v1[1] = lastPoint[1] - secondLastPoint[1];
		float[] v2 = new float[2];
		v2[0] = lastPoint[0] - point[0];
		v2[1] = lastPoint[1] - point[1];
		
		float norm = (float) sqrt(v1[0] * v1[0] + v1[1] * v1[1]);
		v1[0] /= norm;
		v1[1] /= norm;
		norm = (float) sqrt(v2[0] * v2[0] + v2[1] * v2[1]);
		v2[0] /= norm;
		v2[1] /= norm;
		
		float[] intersections = new float[4];
		
		float t = (o2[0] - o1[0]) * v2[1] - (o2[1] - o1[1]) * v2[0];
		t /= v1[0] * v2[1] - v1[1] * v2[0];
		intersections[0] = o1[0] + t * v1[0];
		intersections[1] = o1[1] + t * v1[1];
		
		t = (o2[2] - o1[2]) * v2[1] - (o2[3] - o1[3]) * v2[0];
		t /= v1[0] * v2[1] - v1[1] * v2[0];
		intersections[2] = o1[2] + t * v1[0];
		intersections[3] = o1[3] + t * v1[1];
		
		return intersections;
	}
	
	protected void applyEndCap(float[] point1, float[] point2, boolean first) {
		switch (endCap) {
			case BasicStroke.CAP_BUTT:
				drawCapButt(point1, point2, first);
				break;
			
			case BasicStroke.CAP_SQUARE:
				drawCapSquare(point1, point2, first);
				break;
			
			case BasicStroke.CAP_ROUND:
				drawCapRound(point1, point2, first);
				break;
		}
	}
	
	protected void drawCapButt(float[] point1, float[] point2, boolean first) {
		float[] offset = lineOffset(point1, point2);
		
		float[] pt = first ? point1 : point2;
		float[] cornerPt = add(pt, offset);
		addVertex(cornerPt[0], cornerPt[1]);
		cornerPt = subtract(pt, offset);
		addVertex(cornerPt[0], cornerPt[1]);
	}
	
	protected void drawCapSquare(float[] point1, float[] point2, boolean first) {
		float[] offset = lineOffset(point1, point2);
		
		float[] offsetRotated;
		float[] pt;
		if (first) {
			offsetRotated = new float[]{offset[1], -offset[0]};
			pt = point1;
		} else {
			offsetRotated = new float[]{-offset[1], offset[0]};
			pt = point2;
		}
		
		float[] cornerPt = add(add(pt, offset), offsetRotated);
		addVertex(cornerPt[0], cornerPt[1]);
		cornerPt = add(subtract(pt, offset), offsetRotated);
		addVertex(cornerPt[0], cornerPt[1]);
	}
	
	protected void drawCapRound(float[] point1, float[] point2, boolean first) {
	/*
	 * Instead of doing a triangle-fan around the cap, we're going to jump back
     * and forth from the tip toward the body of the line.
     */
		
		float[] offsetRight;
		float[] offsetLeft;
		float[] pt;
		if (first) {
			float[] v = subtract(point1, point2);
			normalize(v);
			v[0] *= lineOffset;
			v[1] *= lineOffset;
			
			offsetRight = v;
			offsetLeft = new float[]{v[0], v[1]};
			pt = point1;
		} else {
			offsetRight = lineOffset(point1, point2);
			offsetLeft = new float[]{-offsetRight[0], -offsetRight[1]};
			pt = point2;
		}
		
		int max = (int) ceil(Math.PI / 2 / THETA_STEP);
		for (int i = 0; i < max; i++) {
			addVertex(pt[0] + offsetRight[0], pt[1] + offsetRight[1]);
			addVertex(pt[0] + offsetLeft[0], pt[1] + offsetLeft[1]);
			
			float newX = COS_STEP * offsetRight[0] + -SIN_STEP * offsetRight[1];
			offsetRight[1] = SIN_STEP * offsetRight[0] + COS_STEP * offsetRight[1];
			offsetRight[0] = newX;
			
			newX = COS_STEP * offsetLeft[0] + SIN_STEP * offsetLeft[1];
			offsetLeft[1] = -SIN_STEP * offsetLeft[0] + COS_STEP * offsetLeft[1];
			offsetLeft[0] = newX;
		}
		
		if (first) {
			offsetRight = lineOffset(point1, point2);
			
			addVertex(pt[0] + offsetRight[0], point1[1] + offsetRight[1]);
			addVertex(pt[0] - offsetRight[0], point1[1] - offsetRight[1]);
		} else {
			float[] v = subtract(point2, point1);
			normalize(v);
			v[0] *= lineOffset;
			v[1] *= lineOffset;
			
			addVertex(pt[0] + v[0], pt[1] + v[1]);
		}
	}
	
	protected void addVertex(float x, float y) {
		vBuffer.addVertex(x, y);
	}
	
	protected abstract void drawBuffer();
}