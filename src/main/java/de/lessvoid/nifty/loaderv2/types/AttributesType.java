package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class AttributesType.
 */
public class AttributesType extends XmlBaseType {
  
  /**
	 * Instantiates a new attributes type.
	 */
  public AttributesType() {
  }

  /**
	 * Instantiates a new attributes type.
	 *
	 * @param attributes the attributes
	 */
  public AttributesType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  /**
	 * Instantiates a new attributes type.
	 *
	 * @param src the src
	 */
  public AttributesType(@Nonnull final AttributesType src) {
    super(src);
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
    return StringHelper.whitespace(offset) + "<attributes> (" + getAttributes().toString() + ")";
  }

  /**
	 * Apply.
	 *
	 * @param result  the result
	 * @param styleId the style id
	 */
  public void apply(@Nonnull final Attributes result, @Nonnull final String styleId) {
    result.mergeAndTag(getAttributes(), styleId);
  }
}
