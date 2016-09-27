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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;

public interface GLG2DImageHelper extends G2DDrawingHelper {
	boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer);
	
	boolean drawImage(Image img, AffineTransform xform, ImageObserver observer);
	
	boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer);
	
	boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer);
	
	void drawImage(BufferedImage img, BufferedImageOp op, int x, int y);
	
	void drawImage(RenderedImage img, AffineTransform xform);
	
	void drawImage(RenderableImage img, AffineTransform xform);
}