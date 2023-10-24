package de.lessvoid.nifty.controls.dynamic;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.loaderv2.types.ControlDefinitionType;
import de.lessvoid.nifty.loaderv2.types.ElementType;

/**
 * The Class ControlDefinitionCreator.
 */
public class ControlDefinitionCreator extends ControlAttributes {
  
  /**
	 * Instantiates a new control definition creator.
	 *
	 * @param name the name
	 */
  public ControlDefinitionCreator(@Nonnull final String name) {
    setName(name);
  }

  /**
	 * Creates the type.
	 *
	 * @return the element type
	 */
  @Nonnull
  @Override
  public ElementType createType() {
    return new ControlDefinitionType(getAttributes());
  }
}
