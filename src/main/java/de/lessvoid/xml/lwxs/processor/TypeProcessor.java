package de.lessvoid.xml.lwxs.processor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.SubstitutionGroup;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

/**
 * The Class TypeProcessor.
 */
public class TypeProcessor implements XmlProcessor {
  
  /** The nifty xml schema. */
  @Nonnull
  private final Schema niftyXmlSchema;

  /**
	 * Instantiates a new type processor.
	 *
	 * @param niftyXmlSchemaParam the nifty xml schema param
	 */
  public TypeProcessor(@Nonnull final Schema niftyXmlSchemaParam) {
    niftyXmlSchema = niftyXmlSchemaParam;
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
    String name = getNameAttribute(attributes);

    Type type = new Type(name, getExtendsAttribute(attributes));
    niftyXmlSchema.addType(name, type);

    SubstitutionGroup substGroup = new SubstitutionGroup();
    substGroup.add("element", new TypeProcessorElement(type));
    substGroup.add("group", new TypeProcessorSubstitutionGroup(type));

    xmlParser.nextTag();
    xmlParser.zeroOrMore(substGroup);
  }

  /**
	 * Gets the name attribute.
	 *
	 * @param attributes the attributes
	 * @return the name attribute
	 * @throws Exception the exception
	 */
  @Nonnull
  private String getNameAttribute(@Nonnull final Attributes attributes) throws Exception {
    String name = attributes.get("name");
    if (name == null) {
      throw new Exception("[name] attribute is a required attribute");
    }
    return name;
  }

  /**
	 * Gets the extends attribute.
	 *
	 * @param attributes the attributes
	 * @return the extends attribute
	 */
  @Nullable
  private String getExtendsAttribute(@Nonnull final Attributes attributes) {
    return attributes.get("extends");
  }
}
