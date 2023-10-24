package org.jglfont.impl;

import org.jglfont.JGLFont;
import org.jglfont.impl.format.JGLAbstractFontData;
import org.jglfont.impl.format.JGLFontGlyphInfo;

/**
 * The core JGLFont class represents a bitmap font :) that can render text.
 * To create a JGLFont instance you need a JGLFontRenderer and a JGLFontLoader or
 * the plan JGLAbstractFontData.
 *
 * @author void
 */
public class JGLFontImpl implements JGLFont {
  
  /** The font data. */
  private final JGLAbstractFontData fontData;

  /** The custom render state. */
  // custom render state to be forwarded to the FontRenderer
  private Object customRenderState;

  /**
   * Create a JGLFont using the given JGLAbstractFontData.
   * @param fontData font data
   */
  public JGLFontImpl(final JGLAbstractFontData fontData)  {
    this.fontData = fontData;
  }

  /**
	 * Render text.
	 *
	 * @param x    the x
	 * @param y    the y
	 * @param text the text
	 */
  /* (non-Javadoc)
   * @see org.org.jglfont.impl.JGLFont#renderText(int, int, java.lang.String)
   */
  @Override
  public void renderText(
      final int x,
      final int y,
      final String text) {
    renderText(x, y, text, 1.f, 1.f, 1.f, 1.f, 1.f, 1.f);
  }

  /**
	 * Render text.
	 *
	 * @param x     the x
	 * @param y     the y
	 * @param text  the text
	 * @param sizeX the size X
	 * @param sizeY the size Y
	 * @param r     the r
	 * @param g     the g
	 * @param b     the b
	 * @param a     the a
	 */
  /* (non-Javadoc)
   * @see org.org.jglfont.impl.JGLFont#renderText(int, int, java.lang.String, float, float, float, float, float, float)
   */
  @Override
  public void renderText(
      final int x,
      final int y,
      final String text,
      final float sizeX,
      final float sizeY,
      final float r,
      final float g,
      final float b,
      final float a) {
    if (text.length() == 0) {
      return;
    }

    int xPos = x;
    int yPos = y;
    fontData.getRenderer().beforeRender(customRenderState);
    for (int offset = 0; offset < text.length(); /* no increment */) {
      offset = fontData.getRenderer().preProcess(text, offset);
      if (offset >= text.length()) {
        break;
      }
      int currentCodepoint = text.codePointAt(offset);
      int nextCodePoint = getNextCodepoint(text, offset);

      offset += Character.charCount(currentCodepoint);

      JGLFontGlyphInfo characterInfo = fontData.getGlyphs().get(currentCodepoint);
      if (characterInfo != null) {
        fontData.getRenderer().render(characterInfo.getPage(), xPos, yPos, currentCodepoint, sizeX, sizeY, r, g, b, a);
        xPos += (float) getCharacterWidth(currentCodepoint, nextCodePoint, sizeX);
      }
    }
    fontData.getRenderer().afterRender();
  }

  /**
	 * Gets the character width.
	 *
	 * @param currentCharacter the current character
	 * @param nextCharacter    the next character
	 * @return the character width
	 */
  @Override
  public int getCharacterWidth(final int currentCharacter, final int nextCharacter) {
    return getCharacterWidth(currentCharacter, nextCharacter, 1.f);
  }

  /**
	 * Gets the character width.
	 *
	 * @param currentCharacter the current character
	 * @param nextCharacter    the next character
	 * @param size             the size
	 * @return the character width
	 */
  @Override
  public int getCharacterWidth(final int currentCharacter, final int nextCharacter, final float size) {
    JGLFontGlyphInfo currentCharacterInfo = fontData.getGlyphs().get(currentCharacter);
    if (currentCharacterInfo == null) {
      return 0;
    }
    return (int) ((currentCharacterInfo.getXadvance() + getKerning(currentCharacterInfo, nextCharacter)) * size);
  }

  /**
	 * Gets the string width.
	 *
	 * @param text the text
	 * @return the string width
	 */
  @Override
  public int getStringWidth(final String text) {
    return getStringWidth(text, 1.f);
  }

  /**
	 * Gets the string width.
	 *
	 * @param text the text
	 * @param size the size
	 * @return the string width
	 */
  @Override
  public int getStringWidth(final String text, final float size) {
    int length = 0;

    for (int offset = 0; offset < text.length(); /* no increment */) {
      offset = fontData.getRenderer().preProcessForLength(text, offset);
      if (offset >= text.length()) {
        break;
      }
      int currentCodepoint = text.codePointAt(offset);
      int nextCodepoint = getNextCodepoint(text, offset);

      offset += Character.charCount(currentCodepoint);

      int w = getCharacterWidth(currentCodepoint, nextCodepoint, size);
      if (w != -1) {
        length += w;
      }
    }
    return length;
  }

  /**
	 * Gets the height.
	 *
	 * @return the height
	 */
  @Override
  public int getHeight() {
    return fontData.getLineHeight();
  }

  /**
	 * Sets the custom render state.
	 *
	 * @param o the new custom render state
	 */
  @Override
  public void setCustomRenderState(final Object o) {
    customRenderState = o;
  }

  /**
	 * Gets the next codepoint.
	 *
	 * @param text         the text
	 * @param currentIndex the current index
	 * @return the next codepoint
	 */
  private int getNextCodepoint(final String text, final int currentIndex) {
    int nextCodepoint = 0;
    if (currentIndex < text.length() - 1) {
      int currentCodepoint = text.codePointAt(currentIndex);
      nextCodepoint = text.codePointAt(currentIndex + Character.charCount(currentCodepoint));
    }
    return nextCodepoint;
  }

  /**
	 * Gets the kerning.
	 *
	 * @param currentCharacterInfo the current character info
	 * @param nextCharacter        the next character
	 * @return the kerning
	 */
  private int getKerning(final JGLFontGlyphInfo currentCharacterInfo, final int nextCharacter) {
    Integer kern = currentCharacterInfo.getKerning().get(nextCharacter);
    if (kern != null) {
      return kern;
    }
    return 0;
  }
}
