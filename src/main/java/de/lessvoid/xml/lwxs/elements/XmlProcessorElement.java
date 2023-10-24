package de.lessvoid.xml.lwxs.elements;

import javax.annotation.Nonnull;

import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;

/**
 * The Class XmlProcessorElement.
 */
public class XmlProcessorElement {
  
  /** The element processor. */
  @Nonnull
  private XmlProcessorType elementProcessor;
  
  /** The element name. */
  @Nonnull
  private String elementName;
  
  /** The element occurs. */
  @Nonnull
  private OccursEnum elementOccurs;

  /**
	 * Instantiates a new xml processor element.
	 *
	 * @param elementProcessorParam the element processor param
	 * @param elementNameParam      the element name param
	 * @param elementOccursParam    the element occurs param
	 */
  public XmlProcessorElement(
      @Nonnull final XmlProcessorType elementProcessorParam,
      @Nonnull final String elementNameParam,
      @Nonnull final OccursEnum elementOccursParam) {
    elementProcessor = elementProcessorParam;
    elementName = elementNameParam;
    elementOccurs = elementOccursParam;
  }

  /**
	 * Process.
	 *
	 * @param xmlParser     the xml parser
	 * @param xmlTypeParent the xml type parent
	 * @throws Exception the exception
	 */
  public void process(
      @Nonnull final XmlParser xmlParser,
      @Nonnull final XmlType xmlTypeParent) throws Exception {
    if (elementOccurs.equals(OccursEnum.required)) {
      elementProcessor.parentLinkSet(xmlTypeParent, elementName);
      xmlParser.required(elementName, elementProcessor);
    } else if (elementOccurs.equals(OccursEnum.oneOrMore)) {
      elementProcessor.parentLinkAdd(xmlTypeParent, elementName);
      xmlParser.oneOrMore(elementName, elementProcessor);
    } else if (elementOccurs.equals(OccursEnum.optional)) {
      elementProcessor.parentLinkSet(xmlTypeParent, elementName);
      xmlParser.optional(elementName, elementProcessor);
    } else if (elementOccurs.equals(OccursEnum.zeroOrMore)) {
      elementProcessor.parentLinkAdd(xmlTypeParent, elementName);
      xmlParser.zeroOrMore(elementName, elementProcessor);
    }
  }

  /**
	 * Process subst group.
	 *
	 * @param xmlParser     the xml parser
	 * @param xmlTypeParent the xml type parent
	 * @param attributes    the attributes
	 * @throws Exception the exception
	 */
  public void processSubstGroup(
      @Nonnull final XmlParser xmlParser,
      @Nonnull final XmlType xmlTypeParent,
      @Nonnull final Attributes attributes) throws Exception {
    if (elementOccurs.equals(OccursEnum.required)) {
      elementProcessor.parentLinkSet(xmlTypeParent, elementName);
      elementProcessor.process(xmlParser, attributes);
    } else if (elementOccurs.equals(OccursEnum.oneOrMore)) {
      elementProcessor.parentLinkAdd(xmlTypeParent, elementName);
      elementProcessor.process(xmlParser, attributes);
    } else if (elementOccurs.equals(OccursEnum.optional)) {
      elementProcessor.parentLinkSet(xmlTypeParent, elementName);
      elementProcessor.process(xmlParser, attributes);
    } else if (elementOccurs.equals(OccursEnum.zeroOrMore)) {
      elementProcessor.parentLinkAdd(xmlTypeParent, elementName);
      elementProcessor.process(xmlParser, attributes);
    }
  }
}
