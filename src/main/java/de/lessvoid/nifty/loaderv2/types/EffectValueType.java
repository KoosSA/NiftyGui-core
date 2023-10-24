package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class EffectValueType.
 */
public class EffectValueType extends XmlBaseType implements Cloneable {
  
  /**
	 * Instantiates a new effect value type.
	 */
  public EffectValueType() {
  }

  /**
	 * Instantiates a new effect value type.
	 *
	 * @param e the e
	 */
  public EffectValueType(@Nonnull final EffectValueType e) {
    super(e);
  }

  /**
	 * Instantiates a new effect value type.
	 *
	 * @param attributes the attributes
	 */
  public EffectValueType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  /**
	 * Clone.
	 *
	 * @return the effect value type
	 * @throws CloneNotSupportedException the clone not supported exception
	 */
  @Override
  @Nonnull
  public EffectValueType clone() throws CloneNotSupportedException {
    try {
      return (EffectValueType) super.clone();
    } catch (ClassCastException e) {
      throw new CloneNotSupportedException("Cloning returned illegal class type.");
    }
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
    return StringHelper.whitespace(offset) + "<value> " + super.output(offset);
  }
}
