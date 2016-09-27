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
package opengl.gl2d.impl.gl2;


import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import opengl.gl2d.impl.AbstractTesselatorVisitor;

public class GL2TesselatorVisitor extends AbstractTesselatorVisitor {
	protected GL2 gl;
	
	@Override
	public void setGLContext(GL context) {
		gl = context.getGL2();
	}
	
	@Override
	protected void endTess() {
		vBuffer.drawBuffer(gl, drawMode);
	}
}
