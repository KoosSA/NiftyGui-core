package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class PopupType.
 */
public class PopupType extends ElementType {
  
  /**
	 * Instantiates a new popup type.
	 */
  public PopupType() {
    super();
  }

  /**
	 * Instantiates a new popup type.
	 *
	 * @param src the src
	 */
  public PopupType(@Nonnull final PopupType src) {
    super(src);
  }

  /**
	 * Copy.
	 *
	 * @return the popup type
	 */
  @Override
  @Nonnull
  public PopupType copy() {
    return new PopupType(this);
  }

  /**
	 * Instantiates a new popup type.
	 *
	 * @param attributes the attributes
	 */
  public PopupType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  /**
	 * Make flat.
	 */
  @Override
  protected void makeFlat() {
    super.makeFlat();
    setTagName("<popup>");
    setElementRendererCreator(new ElementRendererCreator() {
      @Nonnull
      @Override
      public ElementRenderer[] createElementRenderer(@Nonnull final Nifty nifty) {
        return nifty.getRootLayerFactory().createPanelRenderer();
      }
    });
  }
}
