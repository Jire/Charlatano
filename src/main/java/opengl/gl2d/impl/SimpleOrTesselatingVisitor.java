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

import com.jogamp.opengl.GL;
import opengl.gl2d.PathVisitor;
import opengl.gl2d.VertexBuffer;

import java.awt.*;
import java.nio.FloatBuffer;

import static java.lang.Math.acos;
import static java.lang.Math.sqrt;

/**
 * Tesselating is expensive. This is a simple workaround to check if we can just
 * draw the simple, convex polygon without tesselating. At each corner, we have
 * to check the sign of the z-component of the cross-product. If it's the same
 * all the way around, we know that every turn went the same direction. That
 * ensures it's convex. That's necessary, but not sufficient since we might
 * still have self-intersections. For that, we check that the total curvature
 * along the path is 2π. That ensures it's simple.
 * <p>
 * <p>
 * This checks every corner and if it has the same sign and total curvature is
 * 2π, we know the polygon is convex. Once we get to the end, we draw it. If
 * it's not convex, then we fall back to tesselating it.
 * </p>
 * <p>
 * There are many places where we could fail being a simple convex polygon and
 * then have to fail over to the tesselator. As soon as we fail over we need to
 * catch the tesselator up to the current position and then use the tesselator
 * from then on. For that reason, this class is a little messy.
 * </p>
 */
public class SimpleOrTesselatingVisitor extends SimplePathVisitor {
	/**
	 * This buffer is used to store points for the simple polygon, until we find
	 * out it's not simple. Then we push all this data to the tesselator and
	 * ignore the buffer.
	 */
	protected VertexBuffer buffer = new VertexBuffer(1024);
	
	/**
	 * This is the buffer of vertices we'll use to test the corner.
	 */
	protected float[] previousVertices;
	protected int numberOfPreviousVertices;
	
	/**
	 * The total curvature along the path. Since we know we close the path, if
	 * it's a simple, convex polygon, we'll have a total curvature of 2π.
	 */
	protected double totalCurvature;
	
	/**
	 * All corners must have the same sign.
	 */
	protected int sign;
	
	/**
	 * The flag to indicate if we currently believe this polygon to be simple and
	 * convex.
	 */
	protected boolean isConvexSoFar;
	
	/**
	 * The flag to indicate if we are on our first segment (move-to). If we have
	 * multiple move-to's, then we need to tesselate.
	 */
	protected boolean firstContour;
	
	/**
	 * Keep the winding rule for when we pass the information off to the
	 * tesselator.
	 */
	protected int windingRule;
	
	protected PathVisitor tesselatorFallback;
	protected PathVisitor simpleFallback;
	
	public SimpleOrTesselatingVisitor(PathVisitor simpleVisitor, PathVisitor tesselatorVisitor) {
		tesselatorFallback = tesselatorVisitor;
		simpleFallback = simpleVisitor;
	}
	
	@Override
	public void setGLContext(GL context) {
		simpleFallback.setGLContext(context);
		tesselatorFallback.setGLContext(context);
	}
	
	@Override
	public void setStroke(BasicStroke stroke) {
		// this is only used to fill, no need to consider stroke
	}
	
	@Override
	public void beginPoly(int windingRule) {
		isConvexSoFar = true;
		firstContour = true;
		sign = 0;
		totalCurvature = 0;
		
		this.windingRule = windingRule;
	}
	
	@Override
	public void moveTo(float[] vertex) {
		if (firstContour) {
			firstContour = false;
		} else if (isConvexSoFar) {
			setUseTesselator(false);
		}
		
		if (isConvexSoFar) {
			numberOfPreviousVertices = 1;
			previousVertices = new float[]{vertex[0], vertex[1], 0, 0};
			
			buffer.clear();
			buffer.addVertex(vertex[0], vertex[1]);
		} else {
			tesselatorFallback.closeLine();
			tesselatorFallback.moveTo(vertex);
		}
	}
	
	@Override
	public void lineTo(float[] vertex) {
		if (isConvexSoFar) {
			buffer.addVertex(vertex[0], vertex[1]);
			
			if (!isValidCorner(vertex)) {
				setUseTesselator(false);
			}
		} else {
			tesselatorFallback.lineTo(vertex);
		}
	}
	
	/**
	 * Returns true if the corner is correct, using the new vertex and the buffer
	 * of previous vertices. This always updates the buffer of previous vertices.
	 */
	protected boolean isValidCorner(float[] vertex) {
		if (numberOfPreviousVertices >= 2) {
			double diff1 = previousVertices[2] - previousVertices[0];
			double diff2 = previousVertices[3] - previousVertices[1];
			double diff3 = vertex[0] - previousVertices[0];
			double diff4 = vertex[1] - previousVertices[1];
			
			double cross2 = diff1 * diff4 - diff2 * diff3;

      /*
       * Check that the current sign of the cross-product is the same as the
       * others.
       */
			int currentSign = sign(cross2);
			if (sign == 0) {
				sign = currentSign;
				
				// allow for currentSign = 0, in which case we don't care
			} else if (currentSign * sign == -1) {
				return false;
			}

      /*
       * Check that the total curvature along the path is less than 2π.
       */
			double norm1sq = diff1 * diff1 + diff2 * diff2;
			double norm2sq = diff3 * diff3 + diff4 * diff4;
			double dot = diff1 * diff3 + diff2 * diff4;
			double cosThetasq = dot * dot / (norm1sq * norm2sq);
			double theta = acos(sqrt(cosThetasq));
			
			totalCurvature += theta;
			if (totalCurvature > 2 * Math.PI + 1e-3) {
				return false;
			}
		}
		
		numberOfPreviousVertices++;
		previousVertices[2] = previousVertices[0];
		previousVertices[3] = previousVertices[1];
		previousVertices[0] = vertex[0];
		previousVertices[1] = vertex[1];
		
		return true;
	}
	
	protected int sign(double value) {
		if (value > 1e-8) {
			return 1;
		} else if (value < -1e-8) {
			return -1;
		} else {
			return 0;
		}
	}
	
	@Override
	public void closeLine() {
		if (isConvexSoFar) {
	  /*
	   * If we're convex so far, we need to finish out all the corners to make
       * sure everything is kosher.
       */
			FloatBuffer buf = buffer.getBuffer();
			float[] vertex = new float[2];
			int position = buf.position();
			
			buf.rewind();
			buf.get(vertex);
			
			boolean good = false;
			if (isValidCorner(vertex)) {
				buf.get(vertex);
				if (isValidCorner(vertex)) {
					good = true;
				}
			}
			
			buf.position(position);
			
			if (!good) {
				setUseTesselator(true);
			}
		} else {
			tesselatorFallback.closeLine();
		}
	}
	
	@Override
	public void endPoly() {
		if (isConvexSoFar) {
			simpleFallback.beginPoly(windingRule);
			drawToVisitor(simpleFallback, true);
			simpleFallback.endPoly();
		} else {
			tesselatorFallback.endPoly();
		}
	}
	
	/**
	 * Sets the state to start using the tesselator. This will catch the
	 * tesselator up to the current position and then set {@code isConvexSoFar} to
	 * false so we can start using the tesselator exclusively.
	 * <p>
	 * If {@code doClose} is true, then we will also close the line when we update
	 * the tesselator. This is for when we realized it's not a simple poly after
	 * we already finished the first path.
	 */
	protected void setUseTesselator(boolean doClose) {
		isConvexSoFar = false;
		
		tesselatorFallback.beginPoly(windingRule);
		drawToVisitor(tesselatorFallback, doClose);
	}
	
	protected void drawToVisitor(PathVisitor visitor, boolean doClose) {
		FloatBuffer buf = buffer.getBuffer();
		buf.flip();
		
		float[] vertex = new float[2];
		
		if (buf.hasRemaining()) {
			buf.get(vertex);
			visitor.moveTo(vertex);
		}
		
		while (buf.hasRemaining()) {
			buf.get(vertex);
			visitor.lineTo(vertex);
		}
		
		if (doClose) {
			visitor.closeLine();
		}
		
		// put everything back the way it was
		buffer.clear();
	}
}
