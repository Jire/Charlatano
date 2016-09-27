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

import com.jogamp.opengl.GLDrawable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;

/**
 * Fulfills the contract of a {@code GraphicsConfiguration}.
 * <p>
 * <p>
 * Implementation note: this object is intended primarily to allow callers to
 * create compatible images. The transforms and bounds should be thought out
 * before being used.
 * </p>
 */
public class GLGraphicsConfiguration extends GraphicsConfiguration {
	private final GLDrawable target;
	
	private final GLGraphicsDevice device;
	
	public GLGraphicsConfiguration(GLDrawable drawable) {
		target = drawable;
		device = new GLGraphicsDevice(this);
	}
	
	@Override
	public GraphicsDevice getDevice() {
		return device;
	}
	
	@Override
	public BufferedImage createCompatibleImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	/*
	 * Any reasonable {@code ColorModel} can be transformed into a texture we can
	 * render in OpenGL. I'm not worried about creating an exactly correct one
	 * right now.
	 */
	@Override
	public ColorModel getColorModel() {
		return ColorModel.getRGBdefault();
	}
	
	@Override
	public ColorModel getColorModel(int transparency) {
		switch (transparency) {
			case Transparency.OPAQUE:
			case Transparency.TRANSLUCENT:
				return getColorModel();
			case Transparency.BITMASK:
				return new DirectColorModel(25, 0xff0000, 0xff00, 0xff, 0x1000000);
			default:
				return null;
		}
	}
	
	@Override
	public AffineTransform getDefaultTransform() {
		return new AffineTransform();
	}
	
	@Override
	public AffineTransform getNormalizingTransform() {
		return new AffineTransform();
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(target.getSurfaceWidth(), target.getSurfaceHeight());
	}
	
	public GLDrawable getTarget() {
		return target;
	}
}
