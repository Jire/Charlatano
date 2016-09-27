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


import java.awt.*;

public interface GLG2DShapeHelper extends G2DDrawingHelper {
	void setStroke(Stroke stroke);
	
	Stroke getStroke();
	
	void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, boolean fill);
	
	void drawRect(int x, int y, int width, int height, boolean fill);
	
	void drawLine(int x1, int y1, int x2, int y2);
	
	void drawOval(int x, int y, int width, int height, boolean fill);
	
	void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle, boolean fill);
	
	void drawPolyline(int[] xPoints, int[] yPoints, int nPoints);
	
	void drawPolygon(int[] xPoints, int[] yPoints, int nPoints, boolean fill);
	
	void draw(Shape shape);
	
	void fill(Shape shape);
}