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

import java.awt.*;

/**
 * Receives the calls from a {@link java.awt.geom.PathIterator} and draws the
 * shape as it is visited. The form of this interface and the documentation
 * closely mirror that class.
 * <p>
 * Note: The implementation should assume that the array being passed into each
 * of these calls is being modified when the call returns, the vertex array is
 * recycled and any storage of the points should guard against external
 * mutation.
 * </p>
 *
 * @see java.awt.geom.PathIterator
 */
public interface PathVisitor {
	/**
	 * Sets the GL context to be used for the next drawing session.
	 *
	 * @param context The GL context
	 */
	void setGLContext(GL context);
	
	/**
	 * Sets the stroke to be used when drawing a path. It's not needed for
	 * visitors that fill.
	 */
	void setStroke(BasicStroke stroke);
	
	/**
	 * Specifies the starting location for a new subpath.
	 *
	 * @param vertex An array where the first two values are the x,y coordinates of the
	 *               start of the subpath.
	 */
	void moveTo(float[] vertex);
	
	/**
	 * Specifies the end point of a line to be drawn from the most recently
	 * specified point.
	 *
	 * @param vertex An array where the first two values are the x,y coordinates of the
	 *               next point in the subpath.
	 */
	void lineTo(float[] vertex);
	
	/**
	 * Specifies a quadratic parametric curve is to be drawn. The curve is
	 * interpolated by solving the parametric control equation in the range
	 * <code>(t=[0..1])</code> using the previous point (CP), the first control
	 * point (P1), and the final interpolated control point (P2). The parametric
	 * control equation for this curve is:
	 * <p>
	 * <pre>
	 *          P(t) = B(2,0)*CP + B(2,1)*P1 + B(2,2)*P2
	 *          0 &lt;= t &lt;= 1
	 *
	 *        B(n,m) = mth coefficient of nth degree Bernstein polynomial
	 *               = C(n,m) * t^(m) * (1 - t)^(n-m)
	 *        C(n,m) = Combinations of n things, taken m at a time
	 *               = n! / (m! * (n-m)!)
	 * </pre>
	 *
	 * @param previousVertex The first control point. The same as the most recent specified
	 *                       vertex.
	 * @param control        The control point and the second vertex, in order.
	 */
	void quadTo(float[] previousVertex, float[] control);
	
	/**
	 * Specifies a cubic parametric curve to be drawn. The curve is interpolated
	 * by solving the parametric control equation in the range
	 * <code>(t=[0..1])</code> using the previous point (CP), the first control
	 * point (P1), the second control point (P2), and the final interpolated
	 * control point (P3). The parametric control equation for this curve is:
	 * <p>
	 * <pre>
	 *          P(t) = B(3,0)*CP + B(3,1)*P1 + B(3,2)*P2 + B(3,3)*P3
	 *          0 &lt;= t &lt;= 1
	 *
	 *        B(n,m) = mth coefficient of nth degree Bernstein polynomial
	 *               = C(n,m) * t^(m) * (1 - t)^(n-m)
	 *        C(n,m) = Combinations of n things, taken m at a time
	 *               = n! / (m! * (n-m)!)
	 * </pre>
	 * <p>
	 * This form of curve is commonly known as a B&eacute;zier curve.
	 *
	 * @param previousVertex The first control point. The same as the most recent specified
	 *                       vertex.
	 * @param control        The two control points and the second vertex, in order.
	 */
	void cubicTo(float[] previousVertex, float[] control);
	
	/**
	 * Specifies that the preceding subpath should be closed by appending a line
	 * segment back to the point corresponding to the most recent call to
	 * {@code #moveTo(float[])}.
	 */
	void closeLine();
	
	/**
	 * Starts the polygon or polyline.
	 *
	 * @param windingRule The winding rule for the polygon.
	 */
	void beginPoly(int windingRule);
	
	/**
	 * Signifies that the polygon or polyline has ended. All cleanup should occur
	 * and there will be no more calls for this shape.
	 */
	void endPoly();
}
