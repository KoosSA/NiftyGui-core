package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class HoverType.
 */
public class HoverType extends XmlBaseType {
  
  /**
	 * Instantiates a new hover type.
	 *
	 * @param hoverType the hover type
	 */
  public HoverType(@Nonnull final HoverType hoverType) {
    super(hoverType);
  }

  /**
	 * Instantiates a new hover type.
	 */
  public HoverType() {
  }

  /**
	 * Instantiates a new hover type.
	 *
	 * @param attributes the attributes
	 */
  public HoverType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  /**
	 * Output.
	 *
	 * @param offset the offset
	 * @return the string
	 */
  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<hover> " + super.output(offset);
  }

  /**
	 * Materialize.
	 *
	 * @return the falloff
	 */
  @Nonnull
  public Falloff materialize() {
    return new Falloff(getAttributes().createProperties());
  }
}
