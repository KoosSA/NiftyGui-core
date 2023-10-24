package de.lessvoid.xml.lwxs.processor;

import javax.annotation.Nonnull;

import de.lessvoid.xml.lwxs.elements.SubstitutionGroup;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

/**
 * The Class TypeProcessorSubstitutionGroup.
 */
public class TypeProcessorSubstitutionGroup implements XmlProcessor {
  
  /** The parent. */
  private final Type parent;

  /**
	 * Instantiates a new type processor substitution group.
	 *
	 * @param parentParam the parent param
	 */
  public TypeProcessorSubstitutionGroup(final Type parentParam) {
    parent = parentParam;
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
    SubstitutionGroup substitutionGroup = new SubstitutionGroup();
    parent.addSubstitutionGroup(substitutionGroup);

    xmlParser.nextTag();
    xmlParser.oneOrMore("element", new TypeProcessorElement(substitutionGroup.getHelperType()));
  }
}
