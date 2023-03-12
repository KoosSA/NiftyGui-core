package de.lessvoid.xml.lwxs.elements;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

import de.lessvoid.xml.lwxs.Schema;

public class SubstitutionGroup {
  @Nonnull
  private final Collection<Element> elements = new ArrayList<Element>();

  public void addToProcessor(final Schema schema, @Nonnull final XmlProcessorType processor) throws Exception {
    XmlProcessorSubstituitionGroup subst = new XmlProcessorSubstituitionGroup(schema);
    for (Element e : elements) {
      subst.addElement(e);
    }
    processor.addSubstituitionGroup(subst);
  }

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
