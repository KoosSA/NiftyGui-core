package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class XmlBaseType.
 */
public class XmlBaseType implements Cloneable, XmlType {
  
  /** The attributes. */
  @Nonnull
  private Attributes attributes;

  /**
	 * Instantiates a new xml base type.
	 */
  public XmlBaseType() {
    attributes = new Attributes();
  }

  /**
	 * Instantiates a new xml base type.
	 *
	 * @param src the src
	 */
  public XmlBaseType(@Nonnull final XmlBaseType src) {
    attributes = new Attributes(src.attributes);
  }

  /**
	 * Instantiates a new xml base type.
	 *
	 * @param attributesParam the attributes param
	 */
  public XmlBaseType(@Nonnull final Attributes attributesParam) {
    attributes = new Attributes(attributesParam);
  }

  /**
	 * Clone.
	 *
	 * @return the xml base type
	 * @throws CloneNotSupportedException the clone not supported exception
	 */
  @Nonnull
  @Override
  public XmlBaseType clone() throws CloneNotSupportedException {
    try {
      final XmlBaseType newObject = (XmlBaseType) super.clone();
      newObject.attributes = new Attributes(attributes);
      return newObject;
    } catch (ClassCastException e) {
      throw new CloneNotSupportedException("Cloning created a object with the wrong type.");
    }
  }

  /**
	 * Translate special values.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 */
  public void translateSpecialValues(@Nonnull final Nifty nifty, @Nullable final Screen screen) {
    attributes.translateSpecialValues(
        nifty.getResourceBundles(),
        screen == null ? null : screen.getScreenController(),
        nifty.getGlobalProperties(),
        nifty.getLocale());
  }

  /**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
  @Nonnull
  public Attributes getAttributes() {
    return attributes;
  }

  /**
	 * Merge from attributes.
	 *
	 * @param attributesParam the attributes param
	 */
  public void mergeFromAttributes(@Nonnull final Attributes attributesParam) {
    attributes.merge(attributesParam);
  }

  /**
	 * Output.
	 *
	 * @param offset the offset
	 * @return the string
	 */
  public String output(final int offset) {
    return "(" + attributes.toString() + ")";
  }

  /**
	 * Apply attributes.
	 *
	 * @param attributes the attributes
	 */
  @Override
  public void applyAttributes(@Nonnull final Attributes attributes) {
    this.attributes.overwrite(attributes);
  }

  /**
	 * To string.
	 *
	 * @return the string
	 */
  @Override
  public String toString() {
    return output(0);
  }

  /**
	 * Hash code.
	 *
	 * @return the int
	 */
  @Override
  public int hashCode() {
    int hash = 5;
    String output = this.output(0);
    hash = 97 * hash + (output != null ? output.hashCode() : 0);
    return hash;
  }

  /**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final StyleType other = (StyleType) obj;

    String output = this.output(0);
    String otherOutput = other.output(0);

    return output.equals(otherOutput);
  }

}
