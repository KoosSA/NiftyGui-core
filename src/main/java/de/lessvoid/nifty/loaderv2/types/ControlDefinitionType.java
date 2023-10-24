package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.loaderv2.types.helper.NullElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class ControlDefinitionType.
 */
public class ControlDefinitionType extends ElementType {
  
  /**
	 * Instantiates a new control definition type.
	 */
  public ControlDefinitionType() {
    super();
  }

  /**
	 * Instantiates a new control definition type.
	 *
	 * @param attributes the attributes
	 */
  public ControlDefinitionType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  /**
	 * Instantiates a new control definition type.
	 *
	 * @param src the src
	 */
  public ControlDefinitionType(@Nonnull final ControlDefinitionType src) {
    super(src);
  }

  /**
	 * Copy.
	 *
	 * @return the element type
	 */
  @Override
  @Nonnull
  public ElementType copy() {
    return new ControlDefinitionType(this);
  }

  /**
	 * Make flat.
	 */
  @Override
  public void makeFlat() {
    super.makeFlat();
    setTagName("<controlDefinition>");
    setElementRendererCreator(new NullElementRendererCreator());
  }

  /**
	 * Gets the name.
	 *
	 * @return the name
	 */
  @Nullable
  public String getName() {
    return getAttributes().get("name");
  }

  /**
	 * Gets the control element type.
	 *
	 * @return the control element type
	 */
  @Nullable
  public ElementType getControlElementType() {
    if (!hasElements()) {
      return null;
    }

    return getFirstElement();
  }
}
