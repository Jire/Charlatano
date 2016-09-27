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

import java.awt.geom.AffineTransform;

public interface GLG2DTransformHelper extends G2DDrawingHelper {
	void translate(int x, int y);
	
	void translate(double tx, double ty);
	
	void rotate(double theta);
	
	void rotate(double theta, double x, double y);
	
	void scale(double sx, double sy);
	
	void shear(double shx, double shy);
	
	void transform(AffineTransform Tx);
	
	void setTransform(AffineTransform transform);
	
	AffineTransform getTransform();
}