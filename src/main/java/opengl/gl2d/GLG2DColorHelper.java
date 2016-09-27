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

public interface GLG2DColorHelper extends G2DDrawingHelper {
	void setComposite(Composite comp);
	
	Composite getComposite();
	
	void setPaint(Paint paint);
	
	Paint getPaint();
	
	void setColor(Color c);
	
	Color getColor();
	
	void setColorNoRespectComposite(Color c);
	
	void setColorRespectComposite(Color c);
	
	void setBackground(Color color);
	
	Color getBackground();
	
	void setPaintMode();
	
	void setXORMode(Color c);
	
	void copyArea(int x, int y, int width, int height, int dx, int dy);
}