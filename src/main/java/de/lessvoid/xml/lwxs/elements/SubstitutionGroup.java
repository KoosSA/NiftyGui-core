package de.lessvoid.xml.lwxs.elements;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

import de.lessvoid.xml.lwxs.Schema;

/**
 * The Class SubstitutionGroup.
 */
public class SubstitutionGroup {
  
  /** The elements. */
  @Nonnull
  private final Collection<Element> elements = new ArrayList<Element>();

  /**
	 * Adds the to processor.
	 *
	 * @param schema    the schema
	 * @param processor the processor
	 * @throws Exception the exception
	 */
  public void addToProcessor(final Schema schema, @Nonnull final XmlProcessorType processor) throws Exception {
    XmlProcessorSubstituitionGroup subst = new XmlProcessorSubstituitionGroup(schema);
    for (Element e : elements) {
      subst.addElement(e);
    }
    processor.addSubstituitionGroup(subst);
  }

  /**
	 * Gets the helper type.
	 *
	 * @return the helper type
	 */
  @Nonnull
  public Type getHelperType() {
    return new Type("none", null) {
      @Override
      public void addElement(final Element element) {
        elements.add(element);
      }
    };
  }
}
