package de.lessvoid.nifty.render.image.areaprovider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * The Class CachedAreaProvider.
 */
public class CachedAreaProvider implements AreaProvider {
  
  /** The m cached provider. */
  private final AreaProvider m_cachedProvider;

  /** The m last processed image. */
  @Nullable
  private RenderImage m_lastProcessedImage;
  
  /** The m cached area. */
  @Nullable
  private Box m_cachedArea;

  /**
	 * Instantiates a new cached area provider.
	 *
	 * @param cachedProvider the cached provider
	 */
  public CachedAreaProvider(AreaProvider cachedProvider) {
    m_cachedProvider = cachedProvider;
  }

  /**
	 * Sets the parameters.
	 *
	 * @param parameters the new parameters
	 */
  @Override
  public void setParameters(String parameters) {
    m_lastProcessedImage = null;
    m_cachedArea = null;
  }

  /**
	 * Gets the source area.
	 *
	 * @param renderImage the render image
	 * @return the source area
	 */
  @Nullable
  @Override
  public Box getSourceArea(@Nonnull RenderImage renderImage) {
    if (renderImage != m_lastProcessedImage) {
      m_lastProcessedImage = renderImage;
      m_cachedArea = m_cachedProvider.getSourceArea(renderImage);
    }

    return m_cachedArea;
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
    return m_cachedProvider.getNativeSize(image);
  }
}
