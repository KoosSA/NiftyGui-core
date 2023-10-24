package de.lessvoid.nifty.render.image.areaprovider;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * The Class SpriteAreaProvider.
 */
public class SpriteAreaProvider implements AreaProvider {
  
  /** The Constant log. */
  private static final Logger log = Logger.getLogger(SpriteAreaProvider.class.getName());

  /** The Constant SPRITE_ARGS_COUNT. */
  private static final int SPRITE_ARGS_COUNT = 3;

  /** The m index. */
  private int m_index;
  
  /** The m width. */
  private int m_width;
  
  /** The m height. */
  private int m_height;

  /**
	 * Sets the parameters.
	 *
	 * @param parameters the new parameters
	 */
  @Override
  public void setParameters(String parameters) {
    String[] args = getArguments(parameters);

    m_width = Integer.valueOf(args[0]);
    m_height = Integer.valueOf(args[1]);
    m_index = Integer.valueOf(args[2]);
  }

  /**
	 * Gets the arguments.
	 *
	 * @param parameters the parameters
	 * @return the arguments
	 */
  @Nonnull
  private String[] getArguments(@Nullable final String parameters) {
    String[] args = null;
    if (parameters != null) {
      args = parameters.split(",");
    }

    if ((args == null) || (args.length != SPRITE_ARGS_COUNT)) {
      int argCount = (args == null) ? 0 : args.length;
      throw new IllegalArgumentException("Trying to parse [" + this.getClass().getName()
          + "] : wrong parameter count (" + argCount + "). Expected [width,height,spriteIndex], found ["
          + parameters + "].");
    }
    return args;
  }

  /**
	 * Gets the source area.
	 *
	 * @param renderImage the render image
	 * @return the source area
	 */
  @Nonnull
  @Override
  public Box getSourceArea(@Nonnull RenderImage renderImage) {
    int imageWidth = renderImage.getWidth();
    int imageHeight = renderImage.getHeight();

    int spriteCountPerLine = imageWidth / m_width;
    int spriteX = m_index % spriteCountPerLine;
    int spriteY = m_index / spriteCountPerLine;

    int imageX = spriteX * m_width;
    int imageY = spriteY * m_height;

    if (((imageX + m_width) > imageWidth) || ((imageY + m_height) > imageHeight)) {
      log.warning("Sprite's area exceeds image's bounds.");
    }

    return new Box(imageX, imageY, m_width, m_height);
  }

  /**
	 * Gets the native size.
	 *
	 * @param image the image
	 * @return the native size
	 */
  @Nonnull
  @Override
  public Size getNativeSize(@Nonnull NiftyImage image) {
    return new Size(m_width, m_height);
  }
}
