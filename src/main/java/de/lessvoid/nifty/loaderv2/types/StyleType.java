package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class StyleType.
 */
public class StyleType extends XmlBaseType {
  
  /** The attributes type. */
  @Nullable
  private AttributesType attributesType;
  
  /** The effects type. */
  @Nullable
  private EffectsType effectsType;
  
  /** The interact type. */
  @Nullable
  private InteractType interactType;

  /**
	 * Instantiates a new style type.
	 */
  public StyleType() {
  }

  /**
	 * Instantiates a new style type.
	 *
	 * @param attributes the attributes
	 */
  public StyleType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  /**
	 * Sets the attributes.
	 *
	 * @param styleAttributesTypeParam the new attributes
	 */
  public void setAttributes(@Nonnull final AttributesType styleAttributesTypeParam) {
    attributesType = styleAttributesTypeParam;
  }

  /**
	 * Sets the effect.
	 *
	 * @param effectTypeParam the new effect
	 */
  public void setEffect(@Nonnull final EffectsType effectTypeParam) {
    effectsType = effectTypeParam;
  }

  /**
	 * Sets the interact.
	 *
	 * @param interactTypeParam the new interact
	 */
  public void setInteract(@Nonnull final InteractType interactTypeParam) {
    interactType = interactTypeParam;
  }

  /**
	 * Output.
	 *
	 * @param offset the offset
	 * @return the string
	 */
  @Override
  @Nonnull
  public String output(final int offset) {
    String result = StringHelper.whitespace(offset) + "<style> (" + getAttributes().toString() + ")";
    if (attributesType != null) {
      result += "\n" + attributesType.output(offset + 1);
    }
    if (interactType != null) {
      result += "\n" + interactType.output(offset + 1);
    }
    if (effectsType != null) {
      result += "\n" + effectsType.output(offset + 1);
    }
    return result;
  }

  /**
	 * Gets the style id.
	 *
	 * @return the style id
	 */
  @Nullable
  public String getStyleId() {
    return getAttributes().get("id");
  }

  /**
	 * Gets the base style id.
	 *
	 * @return the base style id
	 */
  @Nullable
  public String getBaseStyleId() {
    return getAttributes().get("base");
  }

  /**
	 * Apply to.
	 *
	 * @param elementType   the element type
	 * @param styleResolver the style resolver
	 */
  public void applyTo(@Nonnull final ElementType elementType, @Nonnull final StyleResolver styleResolver) {
    applyToBaseStyleInternal(styleResolver, elementType);
    applyToInternal(elementType);
  }

  /**
	 * Apply to internal.
	 *
	 * @param elementType the element type
	 */
  void applyToInternal(@Nonnull final ElementType elementType) {
    String styleId = getStyleId();
    if (styleId == null) {
      return;
    }
    elementType.removeWithTag(styleId);
    if (attributesType != null) {
      attributesType.apply(elementType.getAttributes(), styleId);
    }
    if (effectsType != null) {
      effectsType.apply(elementType.getEffects(), styleId);
    }
    if (interactType != null) {
      interactType.apply(elementType.getInteract(), styleId);
    }
  }

  /**
	 * Apply to base style internal.
	 *
	 * @param styleResolver the style resolver
	 * @param elementType   the element type
	 */
  void applyToBaseStyleInternal(@Nonnull final StyleResolver styleResolver, @Nonnull final ElementType elementType) {
    StyleType baseStyle = styleResolver.resolve(getBaseStyleId());
    if (baseStyle != null) {
      baseStyle.applyTo(elementType, styleResolver);
    }
  }

  /**
	 * Hash code.
	 *
	 * @return the int
	 */
  @Override
  public int hashCode() {
    int hash = 5;
    String styleId = this.getStyleId();
    String baseStyleId = this.getBaseStyleId();
    hash = 97 * hash + (styleId != null ? styleId.hashCode() : 0);
    hash = 97 * hash + (baseStyleId != null ? baseStyleId.hashCode() : 0);
    return hash;
  }

  /**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final StyleType other = (StyleType) obj;
    String styleId = this.getStyleId();
    String baseStyleId = this.getBaseStyleId();
    String otherStyleId = other.getStyleId();
    String otherBaseStyleId = other.getBaseStyleId();
    if ((styleId == null) ? (otherStyleId != null) : !styleId.equals(otherStyleId)) {
      return false;
    }
    if ((baseStyleId == null) ? (otherBaseStyleId != null) : !baseStyleId.equals(otherBaseStyleId)) {
      return false;
    }

    String output = this.output(0);
    String otherOutput = other.output(0);

    return output.equals(otherOutput);
  }

}
