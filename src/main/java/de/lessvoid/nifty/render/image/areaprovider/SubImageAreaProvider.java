package de.lessvoid.nifty.render.image.areaprovider;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * The Class SubImageAreaProvider.
 */
public class SubImageAreaProvider implements AreaProvider {
  
  /** The Constant log. */
  private static final Logger log = Logger.getLogger(SubImageAreaProvider.class.getName());

  /** The Constant SUBIMAGE_ARGS_COUNT. */
  private static final int SUBIMAGE_ARGS_COUNT = 4;

  /** The m sub image area. */
  private Box m_subImageArea;

  /**
	 * Sets the parameters.
	 *
	 * @param parameters the new parameters
	 */
  @Override
  public void setParameters(String parameters) {
    String[] args = getArguments(parameters);

    int x = Integer.valueOf(args[0]);
    int y = Integer.valueOf(args[1]);
    int width = Integer.valueOf(args[2]);
    int height = Integer.valueOf(args[3]);

    m_subImageArea = new Box(x, y, width, height);
  }

  /**
	 * Gets the arguments.
	 *
	 * @param parameters the parameters
	 * @return the arguments
	 */
  @Nonnull
  private String[] getArguments(@Nullable String parameters) {
    String[] args = null;
    if (parameters != null) {
      args = parameters.split(",");
    }

    if ((args == null) || (args.length != SUBIMAGE_ARGS_COUNT)) {
      int argCount = (args == null) ? 0 : args.length;
      throw new IllegalArgumentException("Trying to parse [" + this.getClass().getName()
          + "] : wrong parameter count (" + argCount + "). Expected [x,y,width,height], found ["
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
  @Override
  public Box getSourceArea(@Nonnull RenderImage renderImage) {
    int imageWidth = renderImage.getWidth();
    int imageHeight = renderImage.getHeight();

    if (((m_subImageArea.getX() + m_subImageArea.getWidth()) > imageWidth)
        || ((m_subImageArea.getY() + m_subImageArea.getHeight()) > imageHeight)) {
      log.warning("subImage's area exceeds image's bounds.");
    }

    return m_subImageArea;
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
    return new Size(image.getWidth(), image.getHeight());
  }
}
