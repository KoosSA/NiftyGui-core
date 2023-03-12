package de.lessvoid.xml.lwxs.elements;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.SubstitutionGroup;

public class XmlProcessorSubstituitionGroup {
  @Nonnull
  private final Collection<Element> elements = new ArrayList<Element>();
  @Nonnull
  private final SubstitutionGroup substitutionGroup = new SubstitutionGroup();
  private final Schema schema;

  public XmlProcessorSubstituitionGroup(final Schema schemaParam) {
    schema = schemaParam;
  }

  public void addElement(final Element e) throws Exception {
    elements.add(e);
  }

  @Nonnull
  public SubstitutionGroup getSubstGroup(@Nonnull final XmlType xmlType) throws Exception {
    for (Element e : elements) {
      e.addToSubstGroup(schema, substitutionGroup, xmlType);
    }
    return substitutionGroup;
  }
}
