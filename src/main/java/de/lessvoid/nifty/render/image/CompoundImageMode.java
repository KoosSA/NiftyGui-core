package de.lessvoid.nifty.render.image;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.image.areaprovider.AreaProvider;
import de.lessvoid.nifty.render.image.renderstrategy.RenderStrategy;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * The Class CompoundImageMode.
 */
public class CompoundImageMode implements ImageMode {
  
  /** The m area provider. */
  private AreaProvider m_areaProvider;
  
  /** The m render strategy. */
  private RenderStrategy m_renderStrategy;

  /**
	 * Instantiates a new compound image mode.
	 *
	 * @param areaProvider   the area provider
	 * @param renderStrategy the render strategy
	 */
  public CompoundImageMode(AreaProvider areaProvider, RenderStrategy renderStrategy) {
    m_areaProvider = areaProvider;
    m_renderStrategy = renderStrategy;
  }

  /**
	 * Sets the parameters.
	 *
	 * @param parameters the new parameters
	 */
  @Override
  public void setParameters(final String parameters) {
    ImageModeFactory imageModeFactory = ImageModeFactory.getSharedInstance();

    String areaProviderProperty = ImageModeHelper.getAreaProviderProperty(parameters);
    m_areaProvider = imageModeFactory.getAreaProvider(areaProviderProperty);

    String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(parameters);
    m_renderStrategy = imageModeFactory.getRenderStrategy(renderStrategyProperty);
  }

  /**
	 * Render.
	 *
	 * @param renderDevice the render device
	 * @param renderImage  the render image
	 * @param x            the x
	 * @param y            the y
	 * @param width        the width
	 * @param height       the height
	 * @param color        the color
	 * @param scale        the scale
	 */
  @Override
  public void render(
      @Nonnull RenderDevice renderDevice,
      RenderImage renderImage,
      int x,
      int y,
      int width,
      int height,
      Color color,
      float scale) {
    m_renderStrategy.render(renderDevice, renderImage, m_areaProvider.getSourceArea(renderImage), x, y, width, height,
        color, scale);
  }

  /**
	 * Gets the image native size.
	 *
	 * @param image the image
	 * @return the image native size
	 */
  @Override
  public Size getImageNativeSize(NiftyImage image) {
    return m_areaProvider.getNativeSize(image);
  }
}
