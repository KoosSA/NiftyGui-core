package de.lessvoid.xml.lwxs.elements;

import javax.annotation.Nonnull;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.SubstitutionGroup;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

/**
 * The Class Element.
 */
public class Element {
  
  /** The tag name. */
  @Nonnull
  private final String tagName;
  
  /** The tag type. */
  @Nonnull
  private final String tagType;
  
  /** The occurs. */
  @Nonnull
  private final OccursEnum occurs;

  /**
	 * Instantiates a new element.
	 *
	 * @param elementNameParam   the element name param
	 * @param elementTypeParam   the element type param
	 * @param elementOccursParam the element occurs param
	 * @throws Exception the exception
	 */
  public Element(
      @Nonnull final String elementNameParam,
      @Nonnull final String elementTypeParam,
      @Nonnull final OccursEnum elementOccursParam) throws Exception {
    tagName = elementNameParam;
    tagType = elementTypeParam;
    occurs = elementOccursParam;
  }

  /**
	 * Adds the to processor.
	 *
	 * @param schema    the schema
	 * @param processor the processor
	 * @throws Exception the exception
	 */
  public void addToProcessor(@Nonnull final Schema schema, @Nonnull final XmlProcessorType processor) throws Exception {
    Type type = schema.getType(tagType);
    type.addChildren(schema, processor, tagName, tagType, occurs);

    Type typeParent = type.getTypeParent(schema);
    if (typeParent != null) {
      typeParent.addChildren(schema, processor, tagName, tagType, occurs);
    }
  }

  /**
	 * Adds the to subst group.
	 *
	 * @param schema            the schema
	 * @param substitutionGroup the substitution group
	 * @param xmlType           the xml type
	 * @throws Exception the exception
	 */
  public void addToSubstGroup(
      @Nonnull final Schema schema,
      @Nonnull final SubstitutionGroup substitutionGroup,
      @Nonnull final XmlType xmlType) throws Exception {
    Type type = schema.getType(tagType);
    Type typeParent = type.getTypeParent(schema);
    if (typeParent != null) {
      XmlProcessorElement xmlProcessorElement = new XmlProcessorElement(
          typeParent.createXmlProcessorFromType(schema, type), tagName, occurs);
      substitutionGroup.add(getTagName(), new Helper(xmlType, xmlProcessorElement));
    } else {
      XmlProcessorElement xmlProcessorElement = new XmlProcessorElement(
          type.createXmlProcessor(schema), tagName, occurs);
      substitutionGroup.add(getTagName(), new Helper(xmlType, xmlProcessorElement));
    }
  }

  /**
	 * Gets the tag name.
	 *
	 * @return the tag name
	 */
  @Nonnull
  public String getTagName() {
    return tagName;
  }

  /**
	 * The Class Helper.
	 */
  private static class Helper implements XmlProcessor {
    
    /** The xml type parent. */
    private final XmlType xmlTypeParent;
    
    /** The xml processor element. */
    private final XmlProcessorElement xmlProcessorElement;

    /**
	 * Instantiates a new helper.
	 *
	 * @param xmlTypeParam             the xml type param
	 * @param xmlProcessorElementParam the xml processor element param
	 */
    public Helper(final XmlType xmlTypeParam, final XmlProcessorElement xmlProcessorElementParam) {
      xmlTypeParent = xmlTypeParam;
      xmlProcessorElement = xmlProcessorElementParam;
    }

    /**
	 * Process.
	 *
	 * @param xmlParser  the xml parser
	 * @param attributes the attributes
	 * @throws Exception the exception
	 */
    @Override
    public void process(@Nonnull final XmlParser xmlParser, @Nonnull final Attributes attributes) throws Exception {
      xmlProcessorElement.processSubstGroup(xmlParser, xmlTypeParent, attributes);
    }
  }

}
