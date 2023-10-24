package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class LayerType.
 */
public class LayerType extends ElementType {
  
  /**
	 * Instantiates a new layer type.
	 */
  public LayerType() {
    super();
  }

  /**
	 * Instantiates a new layer type.
	 *
	 * @param src the src
	 */
  public LayerType(@Nonnull final LayerType src) {
    super(src);
  }

  /**
	 * Copy.
	 *
	 * @return the layer type
	 */
  @Override
  @Nonnull
  public LayerType copy() {
    return new LayerType(this);
  }

  /**
	 * Instantiates a new layer type.
	 *
	 * @param attributes the attributes
	 */
  public LayerType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  /**
	 * Make flat.
	 */
  @Override
  protected void makeFlat() {
    super.makeFlat();
    setTagName("<layer>");
    setElementRendererCreator(new ElementRendererCreator() {
      @Override
      public ElementRenderer[] createElementRenderer(@Nonnull final Nifty nifty) {
        return nifty.getRootLayerFactory().createPanelRenderer();
      }
    });
  }
}
