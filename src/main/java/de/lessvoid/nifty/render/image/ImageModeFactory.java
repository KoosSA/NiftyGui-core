package de.lessvoid.nifty.render.image;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.ParameterizedObjectFactory;
import de.lessvoid.nifty.render.image.areaprovider.AreaProvider;
import de.lessvoid.nifty.render.image.areaprovider.CachedAreaProvider;
import de.lessvoid.nifty.render.image.areaprovider.FullImageAreaProvider;
import de.lessvoid.nifty.render.image.areaprovider.SpriteAreaProvider;
import de.lessvoid.nifty.render.image.areaprovider.SubImageAreaProvider;
import de.lessvoid.nifty.render.image.renderstrategy.ClampStrategy;
import de.lessvoid.nifty.render.image.renderstrategy.NinePartResizeStrategy;
import de.lessvoid.nifty.render.image.renderstrategy.RenderDirectlyStrategy;
import de.lessvoid.nifty.render.image.renderstrategy.RenderStrategy;
import de.lessvoid.nifty.render.image.renderstrategy.RepeatStrategy;
import de.lessvoid.nifty.render.image.renderstrategy.ResizeStrategy;

/**
 * A factory for creating ImageMode objects.
 */
public class ImageModeFactory {

  /** The s shared instance. */
  @Nullable
  private static ImageModeFactory s_sharedInstance = null;

  /** The m area provider factory. */
  @Nonnull
  private final ParameterizedObjectFactory<AreaProvider> m_areaProviderFactory;
  
  /** The m render strategy factory. */
  @Nonnull
  private final ParameterizedObjectFactory<RenderStrategy> m_renderStrategyFactory;

  /**
	 * Instantiates a new image mode factory.
	 *
	 * @param areaProviderMapping        the area provider mapping
	 * @param fallbackAreaProvider       the fallback area provider
	 * @param renderStrategyMapping      the render strategy mapping
	 * @param fallbackRenderStrategyName the fallback render strategy name
	 */
  public ImageModeFactory(
      @Nonnull Map<String, Class<? extends AreaProvider>> areaProviderMapping,
      @Nonnull String fallbackAreaProvider,
      @Nonnull Map<String, Class<? extends RenderStrategy>> renderStrategyMapping,
      @Nonnull String fallbackRenderStrategyName) {
    m_areaProviderFactory = new ParameterizedObjectFactory<AreaProvider>(areaProviderMapping, fallbackAreaProvider);
    m_renderStrategyFactory = new ParameterizedObjectFactory<RenderStrategy>(renderStrategyMapping,
        fallbackRenderStrategyName);
  }

  /**
	 * Creates a new ImageMode object.
	 *
	 * @param areaProviderDescription   the area provider description
	 * @param renderStrategyDescription the render strategy description
	 * @return the image mode
	 */
  @Nonnull
  public ImageMode createImageMode(
      @Nullable final String areaProviderDescription,
      @Nullable final String renderStrategyDescription) {
    return new CompoundImageMode(
        new CachedAreaProvider(
            getAreaProvider(areaProviderDescription)),
        getRenderStrategy(renderStrategyDescription));
  }

  /**
	 * Gets the area provider.
	 *
	 * @param areaProviderDescription the area provider description
	 * @return the area provider
	 */
  @Nonnull
  AreaProvider getAreaProvider(@Nullable final String areaProviderDescription) {
    return m_areaProviderFactory.create(areaProviderDescription);
  }

  /**
	 * Gets the render strategy.
	 *
	 * @param renderStrategyDescription the render strategy description
	 * @return the render strategy
	 */
  @Nonnull
  RenderStrategy getRenderStrategy(@Nullable final String renderStrategyDescription) {
    return m_renderStrategyFactory.create(renderStrategyDescription);
  }

  /**
	 * Gets the shared instance.
	 *
	 * @return the shared instance
	 */
  @Nonnull
  synchronized public static ImageModeFactory getSharedInstance() {
    if (s_sharedInstance == null) {
      Map<String, Class<? extends AreaProvider>> areaProviderMapping = new HashMap<String,
          Class<? extends AreaProvider>>();
      areaProviderMapping.put("fullimage", FullImageAreaProvider.class);
      areaProviderMapping.put("sprite", SpriteAreaProvider.class);
      areaProviderMapping.put("subimage", SubImageAreaProvider.class);

      Map<String, Class<? extends RenderStrategy>> renderStrategyMapping = new HashMap<String,
          Class<? extends RenderStrategy>>();
      renderStrategyMapping.put("clamp", ClampStrategy.class);
      renderStrategyMapping.put("nine-part", NinePartResizeStrategy.class);
      renderStrategyMapping.put("repeat", RepeatStrategy.class);
      renderStrategyMapping.put("resize", ResizeStrategy.class);
      renderStrategyMapping.put("direct", RenderDirectlyStrategy.class);

      s_sharedInstance = new ImageModeFactory(areaProviderMapping, "fullimage", renderStrategyMapping, "resize");
    }

    return s_sharedInstance;
  }
}
