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

import java.awt.*;

/**
 * Fulfills the contract of a {@code GraphicsDevice}.
 */
public class GLGraphicsDevice extends GraphicsDevice {
	protected final GLGraphicsConfiguration config;
	
	public GLGraphicsDevice(GLGraphicsConfiguration config) {
		this.config = config;
	}
	
	@Override
	public int getType() {
		if (config.getTarget().getChosenGLCapabilities().isOnscreen()) {
			return TYPE_RASTER_SCREEN;
		} else {
			return TYPE_IMAGE_BUFFER;
		}
	}
	
	@Override
	public String getIDstring() {
		return "glg2d";
	}
	
	@Override
	public GraphicsConfiguration[] getConfigurations() {
		return new GraphicsConfiguration[]{getDefaultConfiguration()};
	}
	
	@Override
	public GraphicsConfiguration getDefaultConfiguration() {
		return config;
	}
}
