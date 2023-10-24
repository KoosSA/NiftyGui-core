package org.jglfont.impl.format;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.jglfont.spi.JGLFontRenderer;
import org.jglfont.spi.ResourceLoader;

/**
 * User: iamtakingiteasy Date: 2013-12-29 Time: 03:25.
 */
public class JGLAwtFontData extends JGLAbstractFontData {
  
  /** The font. */
  private Font font;
  
  /** The font metrics. */
  private FontMetrics fontMetrics;

  /** The glyph graphics. */
  private Graphics2D glyphGraphics;
  
  /** The glyph image. */
  private BufferedImage glyphImage;

  /** The glyph side. */
  private int glyphSide;
  
  /** The glyph width. */
  private int glyphWidth;
  
  /** The glyph height. */
  private int glyphHeight;

  /**
	 * Instantiates a new JGL awt font data.
	 *
	 * @param renderer       the renderer
	 * @param resourceLoader the resource loader
	 * @param font           the font
	 * @param glyphSide      the glyph side
	 */
  public JGLAwtFontData(final JGLFontRenderer renderer, final ResourceLoader resourceLoader, final Font font, int glyphSide) {
    super(renderer, resourceLoader);
    this.font = font;
    this.glyphSide = glyphSide;

    glyphImage = new BufferedImage(glyphSide, glyphSide, BufferedImage.TYPE_BYTE_GRAY);

    glyphGraphics = createGraphics();

    fontMetrics = glyphGraphics.getFontMetrics(font);

    glyphWidth = fontMetrics.getMaxAdvance();
    glyphHeight = fontMetrics.getHeight();

    setLineHeight(glyphHeight);

    setBitmapWidth(glyphWidth * 16);
    setBitmapHeight(glyphHeight * 16);
  }

  /**
	 * Creates the graphics.
	 *
	 * @return the graphics 2 D
	 */
  public Graphics2D createGraphics() {
    Graphics2D glyphGraphics = glyphImage.createGraphics();
    glyphGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    glyphGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    glyphGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    glyphGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    glyphGraphics.setComposite(AlphaComposite.Clear);
    glyphGraphics.fillRect(0, 0, glyphSide, glyphSide);
    glyphGraphics.setFont(font);

    glyphGraphics.setComposite(AlphaComposite.SrcOver);
    glyphGraphics.setColor(Color.black);
    glyphGraphics.fillRect(0, 0, glyphSide, glyphSide);

    return glyphGraphics;
  }

  /**
	 * Inits the.
	 */
  @Override
  public void init() {
  }

  /**
	 * Load page.
	 *
	 * @param page the page
	 */
  private void loadPage(int page) {
    ByteBuffer texture = ByteBuffer.allocateDirect(getBitmapWidth() * getBitmapHeight() * 4);
    texture.order(ByteOrder.LITTLE_ENDIAN);

    String bitmapId = font.getName() + "-"+ font.getSize() + "-" + font.getStyle() + "-" + glyphSide + "-" + page;

    for (int i = 0; i < 256; i++) {
      int ch = page * 256 + i;
      char[] codepoint = Character.toChars(ch);

      GlyphVector glyphVector = font.layoutGlyphVector(glyphGraphics.getFontRenderContext(), codepoint, 0, codepoint.length, Font.LAYOUT_LEFT_TO_RIGHT);

      glyphGraphics.setBackground(new Color(255, 255, 255, 0));
      glyphGraphics.clearRect(0, 0, glyphSide, glyphSide);

      glyphGraphics.setColor(Color.white);
      String chrd = new String(codepoint);
      glyphGraphics.drawString(chrd, 0, fontMetrics.getHeight() - fontMetrics.getDescent());

      Rectangle2D bounds = glyphVector.getGlyphLogicalBounds(0).getBounds();

      int xPos = i%16;
      int yPos = i/16;

      int x = xPos * glyphWidth;
      int y = yPos * glyphHeight;

      int w = glyphWidth; // + 5 is the hack for the italic case
      int h = glyphHeight;


      JGLFontGlyphInfo info = new JGLFontGlyphInfo();
      info.setPage(bitmapId);
      info.setX(x);
      info.setY(y);
      info.setWidth(w);
      info.setHeight(h);
      info.setXadvance((int) glyphVector.getGlyphMetrics(0).getAdvanceX());
      info.setXoffset(0);
      info.setYoffset(0);

      characters.put(ch, info);

      WritableRaster raster = glyphImage.getRaster();
      byte[] row = new byte[glyphWidth];
      for (int m = 0; m < glyphHeight; m++) {
        raster.getDataElements(0, m, glyphWidth, 1, row);
        for (int n = 0; n < glyphWidth; n++) {
          byte b = row[n];

          int pos = (y+m) * getBitmapWidth() + (x+n);

          texture.put(pos*4,   (byte) 0xFF);
          texture.put(pos*4+1, (byte) 0XFF);
          texture.put(pos*4+2, (byte) 0xFF);
          texture.put(pos*4+3, b);
        }
      }
    }

    try {
      getRenderer().registerBitmap(bitmapId, texture, getBitmapWidth(), getBitmapHeight(), bitmapId);
      for (int i = 0; i < 256; i++) {
        int ch = page * 256 + i;
        JGLFontGlyphInfo info = characters.get(ch);
        getRenderer().registerGlyph(
                info.getPage(),
                ch,
                info.getXoffset(),
                info.getYoffset(),
                info.getWidth(),
                info.getHeight(),
                info.getX()/(float)getBitmapWidth(),
                info.getY()/(float)getBitmapHeight(),
                (info.getX() + info.getWidth()) / (float)getBitmapWidth(),
                (info.getY() + info.getHeight()) / (float)getBitmapHeight()
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
	 * Pre process glyph.
	 *
	 * @param codepoint the codepoint
	 */
  @Override
  public void preProcessGlyph(Integer codepoint) {
    if (!characters.containsKey(codepoint)) {
      int page = codepoint / 256;
      loadPage(page);
    }
  }
}
