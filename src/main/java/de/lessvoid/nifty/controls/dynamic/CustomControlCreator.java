package de.lessvoid.nifty.controls.dynamic;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

/**
 * The Class CustomControlCreator.
 */
public class CustomControlCreator extends ControlAttributes {
  
  /**
	 * Instantiates a new custom control creator.
	 *
	 * @param source the source
	 */
  public CustomControlCreator(@Nonnull final ControlType source) {
    super(source);
  }

  /**
	 * Instantiates a new custom control creator.
	 *
	 * @param name the name
	 */
  public CustomControlCreator(@Nonnull final String name) {
    setAutoId();
    setName(name);
  }

  /**
	 * Instantiates a new custom control creator.
	 *
	 * @param id   the id
	 * @param name the name
	 */
  public CustomControlCreator(@Nonnull final String id, @Nonnull final String name) {
    setId(id);
    setName(name);
  }

  /**
	 * Parameter.
	 *
	 * @param name  the name
	 * @param value the value
	 */
  public void parameter(@Nonnull final String name, @Nonnull final String value) {
    set(name, value);
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
    return nifty.addControl(screen, parent, getStandardControl());
  }

  /**
	 * Creates the type.
	 *
	 * @return the element type
	 */
  @Nonnull
  @Override
  public ElementType createType() {
    return new ControlType(getAttributes());
  }
}
