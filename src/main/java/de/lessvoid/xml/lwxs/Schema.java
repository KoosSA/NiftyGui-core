package de.lessvoid.xml.lwxs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xmlpull.v1.XmlPullParserFactory;

import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import de.lessvoid.xml.lwxs.elements.Element;
import de.lessvoid.xml.lwxs.elements.SubstitutionGroup;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.lwxs.elements.XmlProcessorType;
import de.lessvoid.xml.lwxs.processor.IncludeProcessor;
import de.lessvoid.xml.lwxs.processor.TypeProcessor;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

/**
 * The Class Schema.
 */
public class Schema implements XmlProcessor {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(Schema.class.getName());
  
  /** The types. */
  @Nonnull
  private final Map < String, Type > types = new HashMap < String, Type >();
  
  /** The package string. */
  @Nullable
  private String packageString;
  
  /** The root. */
  @Nullable
  private String root;
  
  /** The type. */
  @Nullable
  private String type;
  
  /** The parser factory. */
  @Nonnull
  private final XmlPullParserFactory parserFactory;
  
  /** The resource loader. */
  @Nonnull
  private final NiftyResourceLoader resourceLoader;

  /**
	 * Instantiates a new schema.
	 *
	 * @param parserFactory  the parser factory
	 * @param resourceLoader the resource loader
	 */
  public Schema(@Nonnull final XmlPullParserFactory parserFactory, @Nonnull final NiftyResourceLoader resourceLoader) {
    this.parserFactory = parserFactory;
    this.resourceLoader = resourceLoader;
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
    packageString = attributes.get("package");
    if (packageString == null) {
      throw new Exception("[package] attribute is a required attribute");
    }
    root = attributes.get("root");
    if (root == null) {
      throw new Exception("[root] attribute is a required attribute");
    }
    type = attributes.get("type");
    if (type == null) {
      throw new Exception("[type] attribute is a required attribute");
    }
    xmlParser.nextTag();
    xmlParser.zeroOrMore(
        new de.lessvoid.xml.xpp3.SubstitutionGroup()
          .add("include", new IncludeProcessor(parserFactory, resourceLoader, types))
          .add("type", new TypeProcessor(this)));
  }

  /**
	 * Adds the type.
	 *
	 * @param name      the name
	 * @param typeParam the type param
	 */
  public void addType(@Nonnull final String name, @Nonnull final Type typeParam) {
    types.put(name, typeParam);
  }

  /**
	 * Gets the type.
	 *
	 * @param name the name
	 * @return the type
	 * @throws Exception the exception
	 */
  @Nonnull
  public Type getType(@Nonnull final String name) throws Exception {
    Type t = types.get(name);
    if (t == null) {
      log.warning("Type [" + name + "] not found. Creating new one on the fly");
      t = new Type(name, null);
      addType(name, t);
    }
    return t;
  }

  /**
	 * Checks if is type available.
	 *
	 * @param name the name
	 * @return true, if is type available
	 */
  public boolean isTypeAvailable(@Nonnull final String name) {
    return types.containsKey(name);
  }

  /**
	 * Load xml.
	 *
	 * @param parser the parser
	 * @return the xml type
	 * @throws Exception the exception
	 */
  @Nonnull
  public XmlType loadXml(@Nonnull final XmlParser parser) throws Exception {
    if (type == null) {
      throw new Exception("The type is null, something is wrong.");
    }
    Type t = getType(type);
    XmlProcessorType xmlType = t.createXmlProcessor(this);
    parser.nextTag();
    if (root == null) {
      throw new Exception("Root element is not set.");
    }
    parser.required(root, xmlType);
    XmlType result = xmlType.getXmlType();
    if (result == null) {
      throw new Exception("Failed to resolve XML data to a proper XML type.");
    }
    return result;
  }

  /**
	 * Gets the single instance of Schema.
	 *
	 * @param className          the class name
	 * @param elements           the elements
	 * @param substitutionGroups the substitution groups
	 * @return single instance of Schema
	 * @throws Exception the exception
	 */
  @Nonnull
  public XmlProcessorType getInstance(
      @Nonnull final String className,
      @Nonnull final Collection < Element > elements,
      @Nonnull final Collection < SubstitutionGroup> substitutionGroups) throws Exception {
    XmlProcessorType processor = new XmlProcessorType(packageString + "." + className);
    for (Element child : elements) {
      child.addToProcessor(this, processor);
    }
    for (SubstitutionGroup subst : substitutionGroups) {
      subst.addToProcessor(this, processor);
    }
    return processor;
  }

  /**
	 * Gets the types.
	 *
	 * @return the types
	 */
  @Nonnull
  public Map < String, Type > getTypes() {
    return types;
  }
}
