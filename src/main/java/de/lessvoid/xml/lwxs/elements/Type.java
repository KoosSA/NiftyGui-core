package de.lessvoid.xml.lwxs.elements;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.xml.lwxs.Schema;

/**
 * The Class Type.
 */
public class Type {
  
  /** The substitution groups. */
  @Nonnull
  private final ArrayList < SubstitutionGroup > substitutionGroups = new ArrayList < SubstitutionGroup >();
  
  /** The elements. */
  @Nonnull
  private final ArrayList < Element > elements = new ArrayList < Element >();
  
  /** The class name. */
  @Nonnull
  private final String className;
  
  /** The extends name. */
  @Nullable
  private final String extendsName;

  /**
	 * Instantiates a new type.
	 *
	 * @param classNameParam   the class name param
	 * @param extendsNameParam the extends name param
	 */
  public Type(@Nonnull final String classNameParam, @Nullable final String extendsNameParam) {
    className = classNameParam;
    extendsName = extendsNameParam;
  }

  /**
	 * Adds the element.
	 *
	 * @param child the child
	 */
  public void addElement(final Element child) {
    elements.add(child);
  }

  /**
	 * Adds the substitution group.
	 *
	 * @param substitutionGroup the substitution group
	 */
  public void addSubstitutionGroup(final SubstitutionGroup substitutionGroup) {
    substitutionGroups.add(substitutionGroup);
  }

  /**
	 * Creates the xml processor.
	 *
	 * @param schema the schema
	 * @return the xml processor type
	 * @throws Exception the exception
	 */
  @Nonnull
  public XmlProcessorType createXmlProcessor(@Nonnull final Schema schema) throws Exception {
    ArrayList < SubstitutionGroup > substitutionGroups = new ArrayList < SubstitutionGroup >();
    ArrayList < Element > elements = new ArrayList < Element >();

    Type typeParent = getTypeParent(schema);

    if (typeParent != null) {
      substitutionGroups.addAll(typeParent.getSubstituitionGroup());
      elements.addAll(typeParent.getElements());
    }

    substitutionGroups.addAll(this.substitutionGroups);
    elements.addAll(this.elements);
    return schema.getInstance(className, elements, substitutionGroups);
  }

  /**
	 * Gets the elements.
	 *
	 * @return the elements
	 */
  @Nonnull
  private Collection < ? extends Element > getElements() {
    return elements;
  }

  /**
	 * Gets the substituition group.
	 *
	 * @return the substituition group
	 */
  @Nonnull
  private Collection < ? extends SubstitutionGroup > getSubstituitionGroup() {
    return substitutionGroups;
  }

  /**
	 * Creates the xml processor from type.
	 *
	 * @param schema     the schema
	 * @param typeParent the type parent
	 * @return the xml processor type
	 * @throws Exception the exception
	 */
  @Nonnull
  public XmlProcessorType createXmlProcessorFromType(@Nonnull final Schema schema, @Nonnull final Type typeParent) throws Exception {
    return schema.getInstance(typeParent.className, elements, substitutionGroups);
  }

  /**
	 * Gets the type parent.
	 *
	 * @param schema the schema
	 * @return the type parent
	 * @throws Exception the exception
	 */
  @Nullable
  public Type getTypeParent(@Nonnull final Schema schema) throws Exception {
    if (extendsName == null) {
      return null;
    }
    if (schema.isTypeAvailable(extendsName)) {
      return schema.getType(extendsName);
    } else {
      return null;
    }
  }

  /**
	 * Adds the children.
	 *
	 * @param schema    the schema
	 * @param processor the processor
	 * @param tagName   the tag name
	 * @param tagType   the tag type
	 * @param occurs    the occurs
	 * @throws Exception the exception
	 */
  public void addChildren(
      @Nonnull final Schema schema,
      @Nonnull final XmlProcessorType processor,
      @Nonnull final String tagName,
      final String tagType,
      @Nonnull final OccursEnum occurs) throws Exception {
    childElement(schema, processor, tagName, occurs);
  }

  /**
	 * Child element.
	 *
	 * @param schema    the schema
	 * @param processor the processor
	 * @param tagName   the tag name
	 * @param occurs    the occurs
	 * @throws Exception the exception
	 */
  private void childElement(
      @Nonnull final Schema schema,
      @Nonnull final XmlProcessorType processor,
      @Nonnull final String tagName,
      @Nonnull final OccursEnum occurs) throws Exception {
    XmlProcessorElement child = new XmlProcessorElement(
        createXmlProcessor(schema),
        tagName,
        occurs);
    processor.addElementProcessor(child);
  }

  /**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
  @Nonnull
  public String getClassName() {
    return className;
  }
}
