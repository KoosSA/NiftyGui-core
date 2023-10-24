package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class PanelType.
 */
public class PanelType extends ElementType {
  
  /**
	 * Instantiates a new panel type.
	 */
  public PanelType() {
    super();
  }

  /**
	 * Instantiates a new panel type.
	 *
	 * @param src the src
	 */
  public PanelType(@Nonnull final PanelType src) {
    super(src);
  }

  /**
	 * Copy.
	 *
	 * @return the panel type
	 */
  @Override
  @Nonnull
  public PanelType copy() {
    return new PanelType(this);
  }

  /**
	 * Instantiates a new panel type.
	 *
	 * @param attributes the attributes
	 */
  public PanelType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  /**
	 * Make flat.
	 */
  @Override
  protected void makeFlat() {
    super.makeFlat();
    setTagName("<panel>");
    setElementRendererCreator(new ElementRendererCreator() {
      @Override
      public ElementRenderer[] createElementRenderer(@Nonnull final Nifty nifty) {
        return nifty.getRootLayerFactory().createPanelRenderer();
      }
    });
  }

  //  public String output(final int offset) {
  //    return StringHelper.whitespace(offset) + "<panel> " + super.output(offset);
  //  }

  //  public ElementRendererCreator getElementRendererBuilder() {
  //    return new ElementRendererCreator() {
  //      public ElementRenderer[] createElementRenderer(final Nifty nifty) {
  //        return NiftyFactory.getPanelRenderer();
  //      }
  //    };
  //  }
}
