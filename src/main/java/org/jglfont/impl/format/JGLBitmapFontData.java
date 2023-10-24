package org.jglfont.impl.format;

import java.io.IOException;
import java.util.Map;

import org.jglfont.JGLFontException;
import org.jglfont.spi.JGLFontRenderer;
import org.jglfont.spi.ResourceLoader;

/**
 * User: iamtakingiteasy Date: 2013-12-28 Time: 22:37.
 */
public class JGLBitmapFontData extends JGLAbstractFontData {
  
  /** The filename. */
  private String filename;

  /**
	 * Instantiates a new JGL bitmap font data.
	 *
	 * @param renderer       the renderer
	 * @param resourceLoader the resource loader
	 * @param filename       the filename
	 */
  public JGLBitmapFontData(JGLFontRenderer renderer, ResourceLoader resourceLoader, String filename) {
    super(renderer, resourceLoader);
    this.filename = filename;
  }

  /**
	 * Inits the.
	 */
  @Override
  public void init() {
    initalize(extractPath(filename));
  }

  /**
	 * Initalize.
	 *
	 * @param path the path
	 */
  private void initalize(final String path) {
    for (Map.Entry<Integer, String> entry : getBitmaps().entrySet()) {
      try {
        String filename = path + entry.getValue();
        getRenderer().registerBitmap(
                bitmapKey(entry.getKey()),
                resourceLoader.load(filename),
                filename);
      } catch (IOException e) {
        throw new JGLFontException(e);
      }
    }

    initGlyphs();
    getRenderer().prepare();
  }

  /**
	 * Extract path.
	 *
	 * @param filename the filename
	 * @return the string
	 */
  private String extractPath(final String filename) {
    int idx = filename.lastIndexOf("/");
    if (idx == -1) {
      return "";
    } else {
      return filename.substring(0, idx) + "/";
    }
  }

  /**
	 * Inits the glyphs.
	 */
  private void initGlyphs() {
    for (Map.Entry<Integer, JGLFontGlyphInfo> entry : getGlyphs().entrySet()) {
      Integer c = entry.getKey();
      JGLFontGlyphInfo charInfo = entry.getValue();
      if (charInfo != null) {
        getRenderer().registerGlyph(
                charInfo.getPage(),
                c,
                charInfo.getXoffset(),
                charInfo.getYoffset(),
                charInfo.getWidth(),
                charInfo.getHeight(),
                charInfo.getX() / (float) getBitmapWidth(),
                charInfo.getY() / (float) getBitmapHeight(),
                (charInfo.getX() + charInfo.getWidth()) / (float) getBitmapWidth(),
                (charInfo.getY() + charInfo.getHeight()) / (float) getBitmapHeight());
      }
    }
  }


  /**
	 * Bitmap key.
	 *
	 * @param key the key
	 * @return the string
	 */
  private String bitmapKey(final int key) {
    return getName() + "-" + key;
  }
}
