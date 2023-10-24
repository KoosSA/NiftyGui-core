package de.lessvoid.xml.lwxs.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.tools.ClassHelper;
import de.lessvoid.xml.tools.MethodInvoker;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

/**
 * The Class XmlProcessorType.
 */
public class XmlProcessorType implements XmlProcessor {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(XmlProcessorType.class.getName());
  
  /** The full class name. */
  @Nonnull
  private final String fullClassName;
  
  /** The elements. */
  @Nonnull
  private final Collection < XmlProcessorElement > elements = new ArrayList < XmlProcessorElement >();
  
  /** The subst groups. */
  @Nonnull
  private final Collection < XmlProcessorSubstituitionGroup > substGroups = new ArrayList < XmlProcessorSubstituitionGroup >();
  
  /** The xml type parent single. */
  @Nullable
  private XmlType xmlTypeParentSingle;
  
  /** The xml type parent multiple. */
  @Nullable
  private XmlType xmlTypeParentMultiple;
  
  /** The xml type parent name. */
  @Nullable
  private String xmlTypeParentName;
  
  /** The xml type. */
  @Nullable
  private XmlType xmlType;

  /**
	 * Instantiates a new xml processor type.
	 *
	 * @param fullClassNameParam the full class name param
	 */
  public XmlProcessorType(@Nonnull final String fullClassNameParam) {
    fullClassName = fullClassNameParam;
  }

  /**
	 * Adds the element processor.
	 *
	 * @param element the element
	 */
  public void addElementProcessor(@Nonnull final XmlProcessorElement element) {
    elements.add(element);
  }

  /**
	 * Adds the substituition group.
	 *
	 * @param element the element
	 */
  public void addSubstituitionGroup(@Nonnull final XmlProcessorSubstituitionGroup element) {
    substGroups.add(element);
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
    xmlType = ClassHelper.getInstance(fullClassName, XmlType.class);
    if (xmlType == null) {
      log.log(Level.SEVERE, "Failed to process XML. Requested class " + fullClassName + " failed to locate.");
    } else {
      xmlType.applyAttributes(attributes);
      if (xmlTypeParentSingle != null) {
        invoke(xmlType, xmlTypeParentSingle, "set");
      } else if (xmlTypeParentMultiple != null) {
        invoke(xmlType, xmlTypeParentMultiple, "add");
      }

      xmlParser.nextTag();
      for (XmlProcessorElement child : elements) {
        child.process(xmlParser, xmlType);
      }
      for (XmlProcessorSubstituitionGroup subst : substGroups) {
        xmlParser.zeroOrMore(subst.getSubstGroup(xmlType));
      }
    }
  }

  /**
	 * Gets the xml type.
	 *
	 * @return the xml type
	 */
  @Nullable
  public XmlType getXmlType() {
    return xmlType;
  }

  /**
	 * Invoke.
	 *
	 * @param child     the child
	 * @param parent    the parent
	 * @param qualifier the qualifier
	 */
  private void invoke(@Nonnull final XmlType child, @Nonnull final XmlType parent, @Nonnull final String qualifier) {
    MethodInvoker methodInvoker = new MethodInvoker(qualifier + xmlTypeParentName + "()", parent);
    methodInvoker.invoke(child);
  }

  /**
	 * Parent link set.
	 *
	 * @param xmlTypeParent the xml type parent
	 * @param elementName   the element name
	 */
  public void parentLinkSet(@Nonnull final XmlType xmlTypeParent, @Nonnull final String elementName) {
    xmlTypeParentSingle = xmlTypeParent;
    xmlTypeParentMultiple = null;
    xmlTypeParentName = elementName;
  }

  /**
	 * Parent link add.
	 *
	 * @param xmlTypeParent the xml type parent
	 * @param elementName   the element name
	 */
  public void parentLinkAdd(@Nonnull final XmlType xmlTypeParent, @Nonnull final String elementName) {
    xmlTypeParentSingle = null;
    xmlTypeParentMultiple = xmlTypeParent;
    xmlTypeParentName = elementName;
  }
}
