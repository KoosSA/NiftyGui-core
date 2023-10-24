package de.lessvoid.xml.lwxs.elements;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.SubstitutionGroup;

/**
 * The Class XmlProcessorSubstituitionGroup.
 */
public class XmlProcessorSubstituitionGroup {
  
  /** The elements. */
  @Nonnull
  private final Collection<Element> elements = new ArrayList<Element>();
  
  /** The substitution group. */
  @Nonnull
  private final SubstitutionGroup substitutionGroup = new SubstitutionGroup();
  
  /** The schema. */
  private final Schema schema;

  /**
	 * Instantiates a new xml processor substituition group.
	 *
	 * @param schemaParam the schema param
	 */
  public XmlProcessorSubstituitionGroup(final Schema schemaParam) {
    schema = schemaParam;
  }

  /**
	 * Adds the element.
	 *
	 * @param e the e
	 * @throws Exception the exception
	 */
  public void addElement(final Element e) throws Exception {
    elements.add(e);
  }

  /**
	 * Gets the subst group.
	 *
	 * @param xmlType the xml type
	 * @return the subst group
	 * @throws Exception the exception
	 */
  @Nonnull
  public SubstitutionGroup getSubstGroup(@Nonnull final XmlType xmlType) throws Exception {
    for (Element e : elements) {
      e.addToSubstGroup(schema, substitutionGroup, xmlType);
    }
    return substitutionGroup;
  }
}
