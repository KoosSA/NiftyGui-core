package de.lessvoid.nifty.controls.dynamic;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.PopupType;

/**
 * The Class PopupCreator.
 */
public class PopupCreator extends ControlAttributes {
  
  /**
	 * Instantiates a new popup creator.
	 */
  public PopupCreator() {
    setAutoId();
  }

  /**
	 * Instantiates a new popup creator.
	 *
	 * @param id the id
	 */
  public PopupCreator(@Nonnull final String id) {
    setId(id);
  }

  /**
	 * Creates the type.
	 *
	 * @return the element type
	 */
  @Nonnull
  @Override
  public ElementType createType() {
    return new PopupType(getAttributes());
  }
}
