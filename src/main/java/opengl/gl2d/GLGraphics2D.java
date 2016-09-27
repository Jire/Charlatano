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

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import opengl.gl2d.impl.GLGraphicsConfiguration;
import opengl.gl2d.impl.gl2.*;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the standard {@code Graphics2D} functionality, but instead draws
 * to an OpenGL canvas.
 */
public class GLGraphics2D extends Graphics2D implements Cloneable {
	/**
	 * The parent graphics object, if we have one. This reference is used to pass
	 * control back to the parent.
	 */
	protected GLGraphics2D parent;
	
	/**
	 * When we are painting, this is the drawable/context we're painting into.
	 */
	protected GLDrawable glDrawable;
	protected GLContext glContext;
	
	/**
	 * Ensures we only dispose() once.
	 */
	private boolean isDisposed;
	
	/**
	 * Keeps the current viewport height for things like painting text.
	 */
	private int canvasHeight;
	
	/**
	 * All the drawing helpers or listeners to drawing events.
	 */
	protected G2DDrawingHelper[] helpers = new G2DDrawingHelper[0];

  /*
   * The following are specific drawing helpers used explicitly.
   */
	
	protected GLG2DShapeHelper shapeHelper;
	protected GLG2DImageHelper imageHelper;
	protected GLG2DTextHelper stringHelper;
	protected GLG2DTransformHelper matrixHelper;
	protected GLG2DColorHelper colorHelper;
	
	/**
	 * The current clip rectangle. This implementation supports only rectangular
	 * clip areas. This clip must be treated as immutable and replaced but never
	 * changed.
	 */
	protected Rectangle clip;
	
	protected GraphicsConfiguration graphicsConfig;
	
	/**
	 * The set of cached hints for this graphics object.
	 */
	protected RenderingHints hints;
	
	public GLGraphics2D() {
		hints = new RenderingHints(Collections.<Key, Object>emptyMap());
		createDrawingHelpers();
	}
	
	protected void createDrawingHelpers() {
		imageHelper = createImageHelper();
		stringHelper = createTextHelper();
		matrixHelper = createTransformHelper();
		colorHelper = createColorHelper();
		shapeHelper = createShapeHelper();
		
		addG2DDrawingHelper(imageHelper);
		addG2DDrawingHelper(stringHelper);
		addG2DDrawingHelper(shapeHelper);
		addG2DDrawingHelper(matrixHelper);
		addG2DDrawingHelper(colorHelper);
	}
	
	protected GLG2DShapeHelper createShapeHelper() {
		return new GL2ShapeDrawer();
	}
	
	protected GLG2DTextHelper createTextHelper() {
		return new GL2StringDrawer();
	}
	
	protected GLG2DImageHelper createImageHelper() {
		return new GL2ImageDrawer();
	}
	
	protected GLG2DTransformHelper createTransformHelper() {
		return new GL2Transformhelper();
	}
	
	protected GLG2DColorHelper createColorHelper() {
		return new GL2ColorHelper();
	}
	
	public void addG2DDrawingHelper(G2DDrawingHelper helper) {
	/*
	 * Essentially a persistent list so we don't modify other GLGraphics2D
     * objects.
     */
		helpers = Arrays.copyOf(helpers, helpers.length + 1);
		helpers[helpers.length - 1] = helper;
	}
	
	public void removeG2DDrawingHelper(G2DDrawingHelper helper) {
		for (int i = 0; i < helpers.length; i++) {
			if (helpers[i] == helper) {
				System.arraycopy(helpers, i + 1, helpers, i, helpers.length - (i + 1));
				helpers = Arrays.copyOf(helpers, helpers.length - 1);
				break;
			}
		}
	}
	
	public GLG2DShapeHelper getShapeHelper() {
		return shapeHelper;
	}
	
	public GLG2DTextHelper getStringHelper() {
		return stringHelper;
	}
	
	public GLG2DTransformHelper getMatrixHelper() {
		return matrixHelper;
	}
	
	public GLG2DColorHelper getColorHelper() {
		return colorHelper;
	}
	
	protected void setCanvas(GLContext context) {
		glDrawable = context.getGLDrawable();
		glContext = context;
		
		for (G2DDrawingHelper helper : helpers) {
			helper.setG2D(this);
		}
	}
	
	/**
	 * Sets up the graphics object in preparation for drawing. Initialization such
	 * as getting the viewport
	 */
	public void prePaint(GLContext context) {
		canvasHeight = GLG2DUtils.getViewportHeight(context.getGL());
		setCanvas(context);
		setDefaultState();
	}
	
	protected void setDefaultState() {
		setBackground(Color.black);
		setColor(Color.white);
		setFont(Font.getFont(Font.SANS_SERIF));
		setStroke(new BasicStroke());
		setComposite(AlphaComposite.SrcOver);
		setClip(null);
		setRenderingHints(null);
		graphicsConfig = new GLGraphicsConfiguration(glDrawable);
	}
	
	public void postPaint() {
		// could glFlush here, but not necessary
	}
	
	public GLContext getGLContext() {
		return glContext;
	}
	
	public int getCanvasHeight() {
		return canvasHeight;
	}
	
	public void glDispose() {
		for (G2DDrawingHelper helper : helpers) {
			helper.dispose();
		}
	}
	
	@Override
	public void draw(Shape s) {
		shapeHelper.draw(s);
	}
	
	@Override
	public void drawString(String str, int x, int y) {
		stringHelper.drawString(str, x, y);
	}
	
	@Override
	public void drawString(String str, float x, float y) {
		stringHelper.drawString(str, x, y);
	}
	
	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		stringHelper.drawString(iterator, x, y);
	}
	
	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {
		stringHelper.drawString(iterator, x, y);
	}
	
	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		shapeHelper.fill(g.getOutline(x, y));
	}
	
	@Override
	public void fill(Shape s) {
		shapeHelper.fill(s);
	}
	
	@Override
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		if (clip != null) {
			rect = clip.intersection(rect);
		}
		
		if (rect.isEmpty()) {
			return false;
		}
		
		if (onStroke) {
			s = shapeHelper.getStroke().createStrokedShape(s);
		}
		
		s = getTransform().createTransformedShape(s);
		return s.intersects(rect);
	}
	
	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return graphicsConfig;
	}
	
	@Override
	public Composite getComposite() {
		return colorHelper.getComposite();
	}
	
	@Override
	public void setComposite(Composite comp) {
		colorHelper.setComposite(comp);
	}
	
	@Override
	public void setPaint(Paint paint) {
		colorHelper.setPaint(paint);
	}
	
	@Override
	public void setRenderingHint(Key hintKey, Object hintValue) {
		if (!hintKey.isCompatibleValue(hintValue)) {
			throw new IllegalArgumentException(hintValue + " is not compatible with " + hintKey);
		} else {
			for (G2DDrawingHelper helper : helpers) {
				helper.setHint(hintKey, hintValue);
			}
		}
	}
	
	@Override
	public Object getRenderingHint(Key hintKey) {
		return hints.get(hintKey);
	}
	
	@Override
	public void setRenderingHints(Map<?, ?> hints) {
		resetRenderingHints();
		if (hints != null) {
			addRenderingHints(hints);
		}
	}
	
	protected void resetRenderingHints() {
		hints = new RenderingHints(Collections.<Key, Object>emptyMap());
		
		for (G2DDrawingHelper helper : helpers) {
			helper.resetHints();
		}
	}
	
	@Override
	public void addRenderingHints(Map<?, ?> hints) {
		for (Entry<?, ?> entry : hints.entrySet()) {
			if (entry.getKey() instanceof Key) {
				setRenderingHint((Key) entry.getKey(), entry.getValue());
			}
		}
	}
	
	@Override
	public RenderingHints getRenderingHints() {
		return (RenderingHints) hints.clone();
	}
	
	@Override
	public void translate(int x, int y) {
		matrixHelper.translate(x, y);
	}
	
	@Override
	public void translate(double x, double y) {
		matrixHelper.translate(x, y);
	}
	
	@Override
	public void rotate(double theta) {
		matrixHelper.rotate(theta);
	}
	
	@Override
	public void rotate(double theta, double x, double y) {
		matrixHelper.rotate(theta, x, y);
	}
	
	@Override
	public void scale(double sx, double sy) {
		matrixHelper.scale(sx, sy);
	}
	
	@Override
	public void shear(double shx, double shy) {
		matrixHelper.shear(shx, shy);
	}
	
	@Override
	public void transform(AffineTransform Tx) {
		matrixHelper.transform(Tx);
	}
	
	@Override
	public void setTransform(AffineTransform transform) {
		matrixHelper.setTransform(transform);
	}
	
	@Override
	public AffineTransform getTransform() {
		return matrixHelper.getTransform();
	}
	
	@Override
	public Paint getPaint() {
		return colorHelper.getPaint();
	}
	
	@Override
	public Color getColor() {
		return colorHelper.getColor();
	}
	
	@Override
	public void setColor(Color c) {
		colorHelper.setColor(c);
	}
	
	@Override
	public void setBackground(Color color) {
		colorHelper.setBackground(color);
	}
	
	@Override
	public Color getBackground() {
		return colorHelper.getBackground();
	}
	
	@Override
	public Stroke getStroke() {
		return shapeHelper.getStroke();
	}
	
	@Override
	public void setStroke(Stroke s) {
		shapeHelper.setStroke(s);
	}
	
	@Override
	public void setPaintMode() {
		colorHelper.setPaintMode();
	}
	
	@Override
	public void setXORMode(Color c) {
		colorHelper.setXORMode(c);
	}
	
	@Override
	public Font getFont() {
		return stringHelper.getFont();
	}
	
	@Override
	public void setFont(Font font) {
		stringHelper.setFont(font);
	}
	
	@Override
	public FontMetrics getFontMetrics(Font f) {
		return stringHelper.getFontMetrics(f);
	}
	
	@Override
	public FontRenderContext getFontRenderContext() {
		return stringHelper.getFontRenderContext();
	}
	
	@Override
	public Rectangle getClipBounds() {
		if (clip == null) {
			return null;
		} else {
			try {
				double[] pts = new double[8];
				pts[0] = clip.getMinX();
				pts[1] = clip.getMinY();
				pts[2] = clip.getMaxX();
				pts[3] = clip.getMinY();
				pts[4] = clip.getMaxX();
				pts[5] = clip.getMaxY();
				pts[6] = clip.getMinX();
				pts[7] = clip.getMaxY();
				getTransform().inverseTransform(pts, 0, pts, 0, 4);
				int minX = (int) Math.min(pts[0], Math.min(pts[2], Math.min(pts[4], pts[6])));
				int maxX = (int) Math.max(pts[0], Math.max(pts[2], Math.max(pts[4], pts[6])));
				int minY = (int) Math.min(pts[1], Math.min(pts[3], Math.min(pts[5], pts[7])));
				int maxY = (int) Math.max(pts[1], Math.max(pts[3], Math.max(pts[5], pts[7])));
				return new Rectangle(minX, minY, maxX - minX, maxY - minY);
			} catch (NoninvertibleTransformException e) {
				// Not sure why this would happen
				Logger.getLogger(GLGraphics2D.class.getName()).log(Level.WARNING, "User transform is non-invertible", e);
				
				return clip.getBounds();
			}
		}
	}
	
	@Override
	public void clip(Shape s) {
		setClip(s.getBounds(), true);
	}
	
	@Override
	public void clipRect(int x, int y, int width, int height) {
		setClip(new Rectangle(x, y, width, height), true);
	}
	
	@Override
	public void setClip(int x, int y, int width, int height) {
		setClip(new Rectangle(x, y, width, height), false);
	}
	
	@Override
	public Shape getClip() {
		return getClipBounds();
	}
	
	@Override
	public void setClip(Shape clipShape) {
		if (clipShape instanceof Rectangle2D) {
			setClip((Rectangle2D) clipShape, false);
		} else if (clipShape == null) {
			setClip(null, false);
		} else {
			setClip(clipShape.getBounds2D());
		}
	}
	
	protected void setClip(Rectangle2D clipShape, boolean intersect) {
		if (clipShape == null) {
			clip = null;
			scissor(false);
		} else if (intersect && clip != null) {
			Rectangle rect = getTransform().createTransformedShape(clipShape).getBounds();
			clip = rect.intersection(clip);
			scissor(true);
		} else {
			clip = getTransform().createTransformedShape(clipShape).getBounds();
			scissor(true);
		}
	}
	
	protected void scissor(boolean enable) {
		GL gl = getGLContext().getGL();
		if (enable) {
			gl.glScissor(clip.x, canvasHeight - clip.y - clip.height, Math.max(clip.width, 0), Math.max(clip.height, 0));
			gl.glEnable(GL.GL_SCISSOR_TEST);
		} else {
			clip = null;
			gl.glDisable(GL.GL_SCISSOR_TEST);
		}
	}
	
	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		colorHelper.copyArea(x, y, width, height, dx, dy);
	}
	
	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		shapeHelper.drawLine(x1, y1, x2, y2);
	}
	
	@Override
	public void fillRect(int x, int y, int width, int height) {
		shapeHelper.drawRect(x, y, width, height, true);
	}
	
	@Override
	public void clearRect(int x, int y, int width, int height) {
		Color c = getColor();
		colorHelper.setColorNoRespectComposite(getBackground());
		fillRect(x, y, width, height);
		colorHelper.setColorRespectComposite(c);
	}
	
	@Override
	public void drawRect(int x, int y, int width, int height) {
		shapeHelper.drawRect(x, y, width, height, false);
	}
	
	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		shapeHelper.drawRoundRect(x, y, width, height, arcWidth, arcHeight, false);
	}
	
	@Override
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		shapeHelper.drawRoundRect(x, y, width, height, arcWidth, arcHeight, true);
	}
	
	@Override
	public void drawOval(int x, int y, int width, int height) {
		shapeHelper.drawOval(x, y, width, height, false);
	}
	
	@Override
	public void fillOval(int x, int y, int width, int height) {
		shapeHelper.drawOval(x, y, width, height, true);
	}
	
	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		shapeHelper.drawArc(x, y, width, height, startAngle, arcAngle, false);
	}
	
	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		shapeHelper.drawArc(x, y, width, height, startAngle, arcAngle, true);
	}
	
	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		shapeHelper.drawPolyline(xPoints, yPoints, nPoints);
	}
	
	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		shapeHelper.drawPolygon(xPoints, yPoints, nPoints, false);
	}
	
	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		shapeHelper.drawPolygon(xPoints, yPoints, nPoints, true);
	}
	
	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return imageHelper.drawImage(img, xform, obs);
	}
	
	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		imageHelper.drawImage(img, op, x, y);
	}
	
	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		imageHelper.drawImage(img, xform);
	}
	
	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		imageHelper.drawImage(img, xform);
	}
	
	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return imageHelper.drawImage(img, x, y, null, observer);
	}
	
	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
		return imageHelper.drawImage(img, x, y, bgcolor, observer);
	}
	
	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
		return imageHelper.drawImage(img, x, y, width, height, null, observer);
	}
	
	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
		return imageHelper.drawImage(img, x, y, width, height, bgcolor, observer);
	}
	
	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		return imageHelper.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null, observer);
	}
	
	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor,
	                         ImageObserver observer) {
		return imageHelper.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
	}
	
	@Override
	public Graphics create() {
		GLGraphics2D newG2d = clone();
		
		for (G2DDrawingHelper helper : helpers) {
			helper.push(newG2d);
		}
		
		return newG2d;
	}
	
	@Override
	public void dispose() {
    /*
     * This is also called on the finalizer thread, which should not make OpenGL
     * calls. We also want to make sure that this only executes once.
     */
		if (!isDisposed) {
			isDisposed = true;
			
			if (parent != null) {
				// pop in reverse order
				for (int i = helpers.length - 1; i >= 0; i--) {
					helpers[i].pop(parent);
				}
				
				// the parent needs to set its clip
				parent.scissor(parent.clip != null);
			}
		}
	}
	
	@Override
	protected GLGraphics2D clone() {
		try {
			GLGraphics2D clone = (GLGraphics2D) super.clone();
			clone.parent = this;
			clone.hints = (RenderingHints) hints.clone();
			return clone;
		} catch (CloneNotSupportedException exception) {
			throw new AssertionError(exception);
		}
	}
}
