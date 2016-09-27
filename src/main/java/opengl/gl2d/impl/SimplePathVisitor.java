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

import opengl.gl2d.PathVisitor;

/**
 * This is a fast B&eacute;zier curve implementation. I can't use OpenGL's
 * built-in evaluators because subclasses need to do something with the points,
 * not just pass them directly to glVertex2f. This algorithm uses forward
 * differencing. Most of this is taken from <a
 * href="http://www.niksula.hut.fi/~hkankaan/Homepages/bezierfast.html"
 * >http://www.niksula.hut.fi/~hkankaan/Homepages/bezierfast.html</a>. I derived
 * the implementation for the quadratic on my own, but it's simple.
 */
public abstract class SimplePathVisitor implements PathVisitor {
	/**
	 * Just a guess. Would be nice to make a better guess based on what's visually
	 * acceptable.
	 */
	public static final int CURVE_STEPS = 30;
	
	protected int steps = CURVE_STEPS;
	
	/**
	 * Sets the number of steps to take in a quadratic or cubic curve spline.
	 */
	public void setNumCurveSteps(int steps) {
		this.steps = steps;
	}
	
	/**
	 * Gets the number of steps to take in a quadratic or cubic curve spline.
	 */
	public int getNumCurveSteps() {
		return steps;
	}
	
	@Override
	public void quadTo(float[] previousVertex, float[] control) {
		float[] p = new float[2];
		
		float xd, xdd, xdd_per_2;
		float yd, ydd, ydd_per_2;
		float t = 1F / steps;
		float tt = t * t;
		
		// x
		p[0] = previousVertex[0];
		xd = 2 * (control[0] - previousVertex[0]) * t;
		xdd_per_2 = 1 * (previousVertex[0] - 2 * control[0] + control[2]) * tt;
		xdd = xdd_per_2 + xdd_per_2;
		
		// y
		p[1] = previousVertex[1];
		yd = 2 * (control[1] - previousVertex[1]) * t;
		ydd_per_2 = 1 * (previousVertex[1] - 2 * control[1] + control[3]) * tt;
		ydd = ydd_per_2 + ydd_per_2;
		
		for (int loop = 0; loop < steps; loop++) {
			lineTo(p);
			
			p[0] = p[0] + xd + xdd_per_2;
			xd = xd + xdd;
			
			p[1] = p[1] + yd + ydd_per_2;
			yd = yd + ydd;
		}
		
		// use exactly the last point
		p[0] = control[2];
		p[1] = control[3];
		lineTo(p);
	}
	
	@Override
	public void cubicTo(float[] previousVertex, float[] control) {
		float[] p = new float[2];
		
		float xd, xdd, xddd, xdd_per_2, xddd_per_2, xddd_per_6;
		float yd, ydd, yddd, ydd_per_2, yddd_per_2, yddd_per_6;
		float t = 1F / steps;
		float tt = t * t;
		
		// x
		p[0] = previousVertex[0];
		xd = 3 * (control[0] - previousVertex[0]) * t;
		xdd_per_2 = 3 * (previousVertex[0] - 2 * control[0] + control[2]) * tt;
		xddd_per_2 = 3 * (3 * (control[0] - control[2]) + control[4] - previousVertex[0]) * tt * t;
		
		xddd = xddd_per_2 + xddd_per_2;
		xdd = xdd_per_2 + xdd_per_2;
		xddd_per_6 = xddd_per_2 / 3;
		
		// y
		p[1] = previousVertex[1];
		yd = 3 * (control[1] - previousVertex[1]) * t;
		ydd_per_2 = 3 * (previousVertex[1] - 2 * control[1] + control[3]) * tt;
		yddd_per_2 = 3 * (3 * (control[1] - control[3]) + control[5] - previousVertex[1]) * tt * t;
		
		yddd = yddd_per_2 + yddd_per_2;
		ydd = ydd_per_2 + ydd_per_2;
		yddd_per_6 = yddd_per_2 / 3;
		
		for (int loop = 0; loop < steps; loop++) {
			lineTo(p);
			
			p[0] = p[0] + xd + xdd_per_2 + xddd_per_6;
			xd = xd + xdd + xddd_per_2;
			xdd = xdd + xddd;
			xdd_per_2 = xdd_per_2 + xddd_per_2;
			
			p[1] = p[1] + yd + ydd_per_2 + yddd_per_6;
			yd = yd + ydd + yddd_per_2;
			ydd = ydd + yddd;
			ydd_per_2 = ydd_per_2 + yddd_per_2;
		}
		
		// use exactly the last point
		p[0] = control[4];
		p[1] = control[5];
		lineTo(p);
	}
}
