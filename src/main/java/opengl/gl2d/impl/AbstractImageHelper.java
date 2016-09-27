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

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import opengl.gl2d.GLG2DImageHelper;
import opengl.gl2d.GLG2DRenderingHints;
import opengl.gl2d.GLGraphics2D;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.awt.image.renderable.RenderableImage;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static opengl.gl2d.GLG2DRenderingHints.*;
import static opengl.gl2d.impl.GLG2DNotImplemented.notImplemented;

public abstract class AbstractImageHelper implements GLG2DImageHelper {
	private static final Logger LOGGER = Logger.getLogger(AbstractImageHelper.class.getName());
	
	/**
	 * See {@link GLG2DRenderingHints#KEY_CLEAR_TEXTURES_CACHE}
	 */
	protected TextureCache imageCache = new TextureCache();
	protected Object clearCachePolicy;
	
	protected GLGraphics2D g2d;
	
	protected abstract void begin(Texture texture, AffineTransform xform, Color bgcolor);
	
	protected abstract void applyTexture(Texture texture, int dx1, int dy1, int dx2, int dy2,
	                                     float sx1, float sy1, float sx2, float sy2);
	
	protected abstract void end(Texture texture);
	
	@Override
	public void setG2D(GLGraphics2D g2d) {
		this.g2d = g2d;
		
		if (clearCachePolicy == VALUE_CLEAR_TEXTURES_CACHE_EACH_PAINT) {
			imageCache.clear();
		}
	}
	
	@Override
	public void push(GLGraphics2D newG2d) {
		// nop
	}
	
	@Override
	public void pop(GLGraphics2D parentG2d) {
		// nop
	}
	
	@Override
	public void setHint(Key key, Object value) {
		if (key == KEY_CLEAR_TEXTURES_CACHE) {
			clearCachePolicy = value;
		}
	}
	
	@Override
	public void resetHints() {
		clearCachePolicy = VALUE_CLEAR_TEXTURES_CACHE_DEFAULT;
	}
	
	@Override
	public void dispose() {
		imageCache.clear();
	}
	
	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
		return drawImage(img, AffineTransform.getTranslateInstance(x, y), bgcolor, observer);
	}
	
	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver observer) {
		return drawImage(img, xform, (Color) null, observer);
	}
	
	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
		double imgHeight = img.getHeight(null);
		double imgWidth = img.getWidth(null);
		
		if (imgHeight < 0 || imgWidth < 0) {
			return false;
		}
		
		AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
		transform.scale(width / imgWidth, height / imgHeight);
		return drawImage(img, transform, bgcolor, observer);
	}
	
	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2,
	                         int sy2, Color bgcolor, ImageObserver observer) {
		Texture texture = getTexture(img, observer);
		if (texture == null) {
			return false;
		}
		
		float height = texture.getHeight();
		float width = texture.getWidth();
		begin(texture, null, bgcolor);
		applyTexture(texture, dx1, dy1, dx2, dy2, sx1 / width, sy1 / height, sx2 / width, sy2 / height);
		end(texture);
		
		return true;
	}
	
	protected boolean drawImage(Image img, AffineTransform xform, Color color, ImageObserver observer) {
		Texture texture = getTexture(img, observer);
		if (texture == null) {
			return false;
		}
		
		begin(texture, xform, color);
		applyTexture(texture);
		end(texture);
		
		return true;
	}
	
	protected void applyTexture(Texture texture) {
		int width = texture.getWidth();
		int height = texture.getHeight();
		TextureCoords coords = texture.getImageTexCoords();
		
		applyTexture(texture, 0, 0, width, height, coords.left(), coords.top(), coords.right(), coords.bottom());
	}
	
	/**
	 * Cache the texture if possible. I have a feeling this will run into issues
	 * later as images change. Just not sure how to handle it if they do. I
	 * suspect I should be using the ImageConsumer class and dumping pixels to the
	 * screen as I receive them.
	 * <p>
	 * <p>
	 * If an image is a BufferedImage, turn it into a texture and cache it. If
	 * it's not, draw it to a BufferedImage and see if all the image data is
	 * available. If it is, cache it. If it's not, don't cache it. But if not all
	 * the image data is available, we will draw it what we have, since we draw
	 * anything in the image to a BufferedImage.
	 * </p>
	 */
	protected Texture getTexture(Image image, ImageObserver observer) {
		Texture texture = imageCache.get(image);
		if (texture == null) {
			BufferedImage bufferedImage;
			if (image instanceof BufferedImage && ((BufferedImage) image).getType() != BufferedImage.TYPE_CUSTOM) {
				bufferedImage = (BufferedImage) image;
			} else {
				bufferedImage = toBufferedImage(image);
			}
			
			if (bufferedImage != null) {
				texture = create(bufferedImage);
				addToCache(image, texture);
			}
		}
		
		return texture;
	}
	
	protected Texture create(BufferedImage image) {
		// we'll assume the image is complete and can be rendered
		return AWTTextureIO.newTexture(g2d.getGLContext().getGL().getGLProfile(), image, false);
	}
	
	protected void destroy(Texture texture) {
		texture.destroy(g2d.getGLContext().getGL());
	}
	
	protected void addToCache(Image image, Texture texture) {
		if (clearCachePolicy instanceof Number) {
			int maxSize = ((Number) clearCachePolicy).intValue();
			if (imageCache.size() > maxSize) {
				if (LOGGER.isLoggable(Level.FINE)) {
					LOGGER.fine("Clearing texture cache with size " + imageCache.size());
				}
				
				imageCache.clear();
			}
		}
		
		imageCache.put(image, texture);
	}
	
	protected BufferedImage toBufferedImage(Image image) {
		if (image instanceof VolatileImage) {
			return ((VolatileImage) image).getSnapshot();
		}
		
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		if (width < 0 || height < 0) {
			return null;
		}
		
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		bufferedImage.createGraphics().drawImage(image, null, null);
		return bufferedImage;
	}
	
	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		notImplemented("drawImage(BufferedImage, BufferedImageOp, int, int)");
	}
	
	@Override
	public void drawImage(RenderedImage img, AffineTransform xform) {
		notImplemented("drawImage(RenderedImage, AffineTransform)");
	}
	
	@Override
	public void drawImage(RenderableImage img, AffineTransform xform) {
		notImplemented("drawImage(RenderableImage, AffineTransform)");
	}
	
	/**
	 * We could use a WeakHashMap here, but we want access to the ReferenceQueue
	 * so we can dispose the Textures when the Image is no longer referenced.
	 */
	@SuppressWarnings("serial")
	protected class TextureCache extends HashMap<WeakKey<Image>, Texture> {
		private ReferenceQueue<Image> queue = new ReferenceQueue<Image>();
		
		public void expungeStaleEntries() {
			Reference<? extends Image> ref = queue.poll();
			while (ref != null) {
				Texture texture = remove(ref);
				if (texture != null) {
					destroy(texture);
				}
				
				ref = queue.poll();
			}
		}
		
		public Texture get(Image image) {
			expungeStaleEntries();
			WeakKey<Image> key = new WeakKey<Image>(image, null);
			return get(key);
		}
		
		public Texture put(Image image, Texture texture) {
			expungeStaleEntries();
			WeakKey<Image> key = new WeakKey<Image>(image, queue);
			return put(key, texture);
		}
	}
	
	protected static class WeakKey<T> extends WeakReference<T> {
		private final int hash;
		
		public WeakKey(T value, ReferenceQueue<T> queue) {
			super(value, queue);
			hash = value.hashCode();
		}
		
		@Override
		public int hashCode() {
			return hash;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (obj instanceof WeakKey) {
				WeakKey<?> other = (WeakKey<?>) obj;
				return other.hash == hash && get() == other.get();
			} else {
				return false;
			}
		}
	}
}