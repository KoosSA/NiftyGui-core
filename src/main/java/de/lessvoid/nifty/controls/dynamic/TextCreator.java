package de.lessvoid.nifty.controls.dynamic;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.TextType;
import de.lessvoid.nifty.screen.Screen;

/**
 * The Class TextCreator.
 */
public class TextCreator extends ControlAttributes {
  
  /**
	 * Instantiates a new text creator.
	 *
	 * @param text the text
	 */
  public TextCreator(@Nonnull final String text) {
    setAutoId();
    setText(text);
  }

  /**
	 * Instantiates a new text creator.
	 *
	 * @param id   the id
	 * @param text the text
	 */
  public TextCreator(@Nonnull final String id, @Nonnull final String text) {
    setId(id);
    setText(text);
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
        return createText(nifty, screen, parent);
      }
    });
  }

  /**
	 * Creates the type.
	 *
	 * @return the element type
	 */
  @Nonnull
  @Override
  public ElementType createType() {
    return new TextType(getAttributes());
  }

  /**
	 * Sets the wrap.
	 *
	 * @param wrap the new wrap
	 */
  public void setWrap(final boolean wrap) {
    getAttributes().set("wrap", wrap ? "true" : "false");
  }
}
