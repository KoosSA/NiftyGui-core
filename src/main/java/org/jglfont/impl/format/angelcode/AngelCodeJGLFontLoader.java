package org.jglfont.impl.format.angelcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jglfont.impl.format.JGLAbstractFontData;
import org.jglfont.impl.format.JGLBitmapFontData;
import org.jglfont.impl.format.JGLFontLoader;
import org.jglfont.spi.JGLFontRenderer;
import org.jglfont.spi.ResourceLoader;


/**
 * A BitmapFontDataLoader implementation for AngelCode Font file.
 * @author void
 */
public class AngelCodeJGLFontLoader implements JGLFontLoader {
  
  /** The Constant log. */
  private final static Logger log = Logger.getLogger(AngelCodeJGLFontLoader.class.getName());
  
  /** The parser. */
  private final AngelCodeLineParser parser = new AngelCodeLineParser();
  
  /** The parsed. */
  private final AngelCodeLineData parsed = new AngelCodeLineData();
  
  /** The line processors. */
  private final AngelCodeLineProcessors lineProcessors;

  /**
	 * Instantiates a new angel code JGL font loader.
	 *
	 * @param lineProcessors the line processors
	 */
  public AngelCodeJGLFontLoader(final AngelCodeLineProcessors lineProcessors) {
    this.lineProcessors = lineProcessors;
  }

  /**
	 * Load.
	 *
	 * @param renderer       the renderer
	 * @param resourceLoader the resource loader
	 * @param in             the in
	 * @param filename       the filename
	 * @param size           the size
	 * @param style          the style
	 * @param params         the params
	 * @return the JGL abstract font data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  public JGLAbstractFontData load(
          final JGLFontRenderer renderer,
          final ResourceLoader resourceLoader,
          final InputStream in,
          final String filename,
          final int size,
          final int style,
          String params
  ) throws IOException {
    JGLAbstractFontData result = new JGLBitmapFontData(renderer, resourceLoader, filename);
    load(in, result);
    result.init();
    return result;
  }

  /**
	 * Load.
	 *
	 * @param in         the in
	 * @param bitmapFont the bitmap font
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  private void load(final InputStream in, final JGLAbstractFontData bitmapFont) throws IOException {
    if (in == null) {
      throw new IOException("InputStream is null");
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    try {
      while (true) {
        String line = reader.readLine();
        if (line == null) {
          break;
        }

        String[] split = line.split(" ");
        if (split[0].length() == 0) {
          break;
        }

        parser.parse(line, parsed);

        AngelCodeLine processor = lineProcessors.get(split[0]);
        if (processor != null) {
          if (!processor.process(parsed, bitmapFont)) {
            log.warning("parsing error for line [" + line + "] using " + processor + " with " + parsed);
          }
        }
      }
    } catch (Exception e) {
      log.log(Level.WARNING, "error while parsing font file: ", e);
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
      }
    }
  }
}
