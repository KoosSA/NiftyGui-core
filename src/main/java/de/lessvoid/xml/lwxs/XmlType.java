package de.lessvoid.xml.lwxs;

import javax.annotation.Nonnull;

import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Interface XmlType.
 */
public interface XmlType {
  
  /**
	 * Apply attributes.
	 *
	 * @param attributes the attributes
	 */
  void applyAttributes(@Nonnull Attributes attributes);
}
