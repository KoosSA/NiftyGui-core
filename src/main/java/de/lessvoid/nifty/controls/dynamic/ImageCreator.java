package de.lessvoid.nifty.controls.dynamic;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.ImageType;
import de.lessvoid.nifty.screen.Screen;

/**
 * The Class ImageCreator.
 */
public class ImageCreator extends ControlAttributes {
  
  /**
	 * Instantiates a new image creator.
	 */
  public ImageCreator() {
    setAutoId();
  }

  /**
	 * Instantiates a new image creator.
	 *
	 * @param id the id
	 */
  public ImageCreator(@Nonnull final String id) {
    setId(id);
  }

  /**
	 * Creates the.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 * @param parent the parent
	 * @return the element
	 */
  @Nonnull
  public Element create(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    return nifty.addControl(screen, parent, new StandardControl() {
      @Nonnull
      @Override
      public Element createControl(
          @Nonnull final Nifty nifty,
          @Nonnull final Screen screen,
          @Nonnull final Element parent) {
        return createImage(nifty, screen, parent);
      }
    });
  }

  /**
	 * Creates the type.
	 *
	 * @return the element type
	 */
  @Override
  @Nonnull
  public ElementType createType() {
    return new ImageType(getAttributes());
  }
}
