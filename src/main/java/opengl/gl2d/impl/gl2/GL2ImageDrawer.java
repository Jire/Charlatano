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
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.util.texture.Texture;
import opengl.gl2d.GLGraphics2D;
import opengl.gl2d.impl.AbstractImageHelper;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GL2ImageDrawer extends AbstractImageHelper {
	protected GL2 gl;
	
	protected AffineTransform savedTransform;
	
	@Override
	public void setG2D(GLGraphics2D g2d) {
		super.setG2D(g2d);
		gl = g2d.getGLContext().getGL().getGL2();
	}
	
	@Override
	protected void begin(Texture texture, AffineTransform xform, Color bgcolor) {
		gl.glTexEnvi(GL2ES1.GL_TEXTURE_ENV, GL2ES1.GL_TEXTURE_ENV_MODE, GL2ES1.GL_MODULATE);
		gl.glTexParameterf(GL2ES1.GL_TEXTURE_ENV, GL2ES1.GL_TEXTURE_ENV_MODE, GL.GL_BLEND);

    /*
     * FIXME This is unexpected since we never disable blending, but in some
     * cases it interacts poorly with multiple split panes, scroll panes and the
     * text renderer to disable blending.
     */
		g2d.setComposite(g2d.getComposite());
		
		texture.enable(gl);
		texture.bind(gl);
		
		savedTransform = null;
		if (xform != null && !xform.isIdentity()) {
			savedTransform = g2d.getTransform();
			g2d.transform(xform);
		}
		
		g2d.getColorHelper().setColorRespectComposite(bgcolor == null ? Color.white : bgcolor);
	}
	
	@Override
	protected void end(Texture texture) {
		if (savedTransform != null) {
			g2d.setTransform(savedTransform);
		}
		
		texture.disable(gl);
		g2d.getColorHelper().setColorRespectComposite(g2d.getColor());
	}
	
	@Override
	protected void applyTexture(Texture texture, int dx1, int dy1, int dx2, int dy2, float sx1, float sy1, float sx2, float sy2) {
		gl.glBegin(GL2.GL_QUADS);
		
		// SW
		gl.glTexCoord2f(sx1, sy2);
		gl.glVertex2i(dx1, dy2);
		// SE
		gl.glTexCoord2f(sx2, sy2);
		gl.glVertex2i(dx2, dy2);
		// NE
		gl.glTexCoord2f(sx2, sy1);
		gl.glVertex2i(dx2, dy1);
		// NW
		gl.glTexCoord2f(sx1, sy1);
		gl.glVertex2i(dx1, dy1);
		
		gl.glEnd();
	}
}
