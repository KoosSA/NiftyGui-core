package org.jglfont.impl.format.awt;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

import org.jglfont.JGLFontFactory;
import org.jglfont.impl.format.JGLAbstractFontData;
import org.jglfont.impl.format.JGLAwtFontData;
import org.jglfont.impl.format.JGLFontLoader;
import org.jglfont.spi.JGLFontRenderer;
import org.jglfont.spi.ResourceLoader;

/**
 * User: iamtakingiteasy Date: 2013-12-29 Time: 03:24.
 */
public class AwtJGLFontLoader implements JGLFontLoader {
  
  /** The Constant log. */
  private static final Logger log = Logger.getLogger(AwtJGLFontLoader.class.getName());

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
  @Override
  public JGLAbstractFontData load(
          final JGLFontRenderer renderer,
          final ResourceLoader resourceLoader,
          final InputStream in,
          final String filename,
          final int size,
          final int style,
          final String params
  ) throws IOException {

    Font font;
    if (in != null) {
      try {
        font = Font.createFont(Font.TRUETYPE_FONT, in);
      } catch (FontFormatException e) {
        throw new IOException(e);
      }
    } else {
      font = Font.decode(filename);
    }

    int glyphSide = 256;
    boolean bold = (style & JGLFontFactory.FONT_STYLE_BOLD) != 0;
    boolean italic = (style & JGLFontFactory.FONT_STYLE_ITALIC) != 0;

    String[] pairs = params.split(";");
    for (String pair : pairs) {
      String[] keyvalue = pair.split("=");
      if (keyvalue.length == 2) {
        if (keyvalue[0].equalsIgnoreCase("glyphSide")) {
          try {
            glyphSide = Integer.parseInt(keyvalue[1]);
          } catch (NumberFormatException ignore) {
          }
        }
      }
    }

    if (in != null) {
      //noinspection unchecked
      Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) font.getAttributes();
      attributes.put(TextAttribute.SIZE, size);
      attributes.put(TextAttribute.WEIGHT, bold ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
      attributes.put(TextAttribute.POSTURE, italic ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
      attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
      font = font.deriveFont(attributes);
    }
    log.fine("Font loaded: " + font.toString());
    JGLAbstractFontData data = new JGLAwtFontData(renderer, resourceLoader, font, glyphSide);
    data.init();
    return data;
  }
}
