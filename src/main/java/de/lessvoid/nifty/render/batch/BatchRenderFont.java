package de.lessvoid.nifty.render.batch;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.jglfont.JGLFont;
import org.jglfont.JGLFontFactory;

import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * The Class BatchRenderFont.
 */
public class BatchRenderFont implements RenderFont {
  
  /** The batch render device. */
  private final BatchRenderDevice batchRenderDevice;
  
  /** The font. */
  private final JGLFont font;

  /**
	 * Instantiates a new batch render font.
	 *
	 * @param batchRenderDevice the batch render device
	 * @param name              the name
	 * @param factory           the factory
	 * @param resourceLoader    the resource loader
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  public BatchRenderFont(
      final BatchRenderDevice batchRenderDevice,
      @Nonnull final String name,
      @Nonnull final JGLFontFactory factory,
      @Nonnull final NiftyResourceLoader resourceLoader) throws IOException {
    this.batchRenderDevice = batchRenderDevice;
    this.font = factory.loadFont(resourceLoader.getResourceAsStream(name), name);
  }

  /**
	 * Gets the height.
	 *
	 * @return the height
	 */
  @Override
  public int getHeight() {
    return font.getHeight();
  }

  /**
	 * Gets the width.
	 *
	 * @param text the text
	 * @return the width
	 */
  @Override
  public int getWidth(@Nonnull final String text) {
    return font.getStringWidth(text);
  }

  /**
	 * Gets the width.
	 *
	 * @param text the text
	 * @param size the size
	 * @return the width
	 */
  @Override
  public int getWidth(@Nonnull final String text, final float size) {
    return font.getStringWidth(text, size);
  }

  /**
	 * Gets the character advance.
	 *
	 * @param currentCharacter the current character
	 * @param nextCharacter    the next character
	 * @param size             the size
	 * @return the character advance
	 */
  @Override
  public int getCharacterAdvance(final char currentCharacter, final char nextCharacter, final float size) {
    return font.getCharacterWidth(currentCharacter, nextCharacter, size);
  }

  /**
	 * Dispose.
	 */
  @Override
  public void dispose() {
    batchRenderDevice.disposeFont(this);
  }

  /**
	 * Gets the bitmap font.
	 *
	 * @return the bitmap font
	 */
  public JGLFont getBitmapFont() {
    return font;
  }
}
