package org.jglfont.impl.format;

import java.io.IOException;
import java.io.InputStream;

import org.jglfont.spi.JGLFontRenderer;
import org.jglfont.spi.ResourceLoader;



/**
 * A JGLFontLoader.
 * @author void
 */
public interface JGLFontLoader {
  
  /**
	 * Load a font.
	 *
	 * @param renderer       the renderer
	 * @param resourceLoader the resource loader
	 * @param in             InputStream
	 * @param filename       the filename
	 * @param size           the size
	 * @param style          the style
	 * @param params         the params
	 * @return JGLAbstractFontData
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  JGLAbstractFontData load(
          final JGLFontRenderer renderer,
          final ResourceLoader resourceLoader,
          final InputStream in,
          final String filename,
          final int size,
          final int style,
          String params
  ) throws IOException;
}
