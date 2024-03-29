package de.lessvoid.nifty.loaderv2.types.apply;

import java.util.logging.Logger;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.image.ImageMode;
import de.lessvoid.nifty.render.image.ImageModeHelper;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class ApplyRendererPanel.
 */
public class ApplyRendererPanel implements ApplyRenderer {
  
  /** The Constant log. */
  private static final Logger log = Logger.getLogger(ApplyRendererPanel.class.getName());
  
  /** The convert. */
  @Nonnull
  private final Convert convert;

  /**
	 * Instantiates a new apply renderer panel.
	 *
	 * @param convertParam the convert param
	 */
  public ApplyRendererPanel(@Nonnull final Convert convertParam) {
    convert = convertParam;
  }

  /**
	 * Apply.
	 *
	 * @param screen       the screen
	 * @param element      the element
	 * @param attributes   the attributes
	 * @param renderEngine the render engine
	 */
  @Override
  public void apply(
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Attributes attributes,
      @Nonnull final NiftyRenderEngine renderEngine) {
    PanelRenderer panelRenderer = element.getRenderer(PanelRenderer.class);
    if (panelRenderer == null) {
      return;
    }
    panelRenderer.setBackgroundColor(convert.color(attributes.get("backgroundColor")));

    ImageRenderer imageRenderer = element.getRenderer(ImageRenderer.class);
    if (imageRenderer == null) {
      return;
    }

    String backgroundImage = attributes.get("backgroundImage");
    if (backgroundImage == null) {
      return;
    }

    NiftyImage image =
        renderEngine.createImage(
            screen,
            backgroundImage,
            attributes.getAsBoolean("filter", Convert.DEFAULT_IMAGE_FILTER));
    if (image == null) {
      return;
    }

    String areaProviderProperty = ImageModeHelper.getAreaProviderProperty(attributes.getAttributes());
    String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(attributes.getAttributes());
    ImageMode imageMode = convert.imageMode(areaProviderProperty, renderStrategyProperty);

    image.setImageMode(imageMode);
    imageRenderer.setImage(image);
  }
}
