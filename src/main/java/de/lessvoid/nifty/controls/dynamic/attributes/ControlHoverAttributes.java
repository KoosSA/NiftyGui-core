package de.lessvoid.nifty.controls.dynamic.attributes;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.loaderv2.types.HoverType;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class ControlHoverAttributes.
 */
public class ControlHoverAttributes {
  
  /** The attributes. */
  @Nonnull
  private Attributes attributes = new Attributes();

  /**
	 * Instantiates a new control hover attributes.
	 */
  public ControlHoverAttributes() {
  }

  /**
	 * Instantiates a new control hover attributes.
	 *
	 * @param hoverType the hover type
	 */
  public ControlHoverAttributes(@Nonnull final HoverType hoverType) {
    this.attributes = new Attributes(hoverType.getAttributes());
  }

  /**
	 * Sets the.
	 *
	 * @param key   the key
	 * @param value the value
	 */
  public void set(@Nonnull final String key, @Nonnull final String value) {
    attributes.set(key, value);
  }

  /**
	 * Sets the hover falloff type.
	 *
	 * @param hoverFalloffType the new hover falloff type
	 */
  public void setHoverFalloffType(@Nonnull final String hoverFalloffType) {
    attributes.set("hoverFalloffType", hoverFalloffType);
  }

  /**
	 * Sets the hover falloff constraint.
	 *
	 * @param hoverFalloffConstraint the new hover falloff constraint
	 */
  public void setHoverFalloffConstraint(@Nonnull final String hoverFalloffConstraint) {
    attributes.set("hoverFalloffConstraint", hoverFalloffConstraint);
  }

  /**
	 * Sets the hover width.
	 *
	 * @param hoverWidth the new hover width
	 */
  public void setHoverWidth(@Nonnull final String hoverWidth) {
    attributes.set("hoverWidth", hoverWidth);
  }

  /**
	 * Sets the hover height.
	 *
	 * @param hoverHeight the new hover height
	 */
  public void setHoverHeight(@Nonnull final String hoverHeight) {
    attributes.set("hoverHeight", hoverHeight);
  }

  /**
	 * Creates the.
	 *
	 * @return the hover type
	 */
  @Nonnull
  public HoverType create() {
    return new HoverType(attributes);
  }
}
