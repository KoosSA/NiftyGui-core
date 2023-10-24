package de.lessvoid.nifty.controls.dynamic.attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.loaderv2.types.EffectTypeOnHover;
import de.lessvoid.nifty.loaderv2.types.EffectValueType;
import de.lessvoid.nifty.loaderv2.types.HoverType;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class ControlEffectOnHoverAttributes.
 */
public class ControlEffectOnHoverAttributes extends ControlEffectAttributes {
  
  /** The control hover attributes. */
  private ControlHoverAttributes controlHoverAttributes;

  /**
	 * Instantiates a new control effect on hover attributes.
	 */
  public ControlEffectOnHoverAttributes() {
  }

  /**
	 * Instantiates a new control effect on hover attributes.
	 *
	 * @param attributes   the attributes
	 * @param effectValues the effect values
	 * @param hoverType    the hover type
	 */
  public ControlEffectOnHoverAttributes(
      @Nonnull final Attributes attributes,
      @Nonnull final List<EffectValueType> effectValues,
      @Nonnull final HoverType hoverType) {
    this.attributes = new Attributes(attributes);
    this.effectValues = new ArrayList<EffectValueType>(effectValues);
    Collections.copy(this.effectValues, effectValues);
    this.controlHoverAttributes = new ControlHoverAttributes(hoverType);
  }

  /**
	 * Sets the control hover attributes.
	 *
	 * @param controlHoverAttributesParam the new control hover attributes
	 */
  public void setControlHoverAttributes(final ControlHoverAttributes controlHoverAttributesParam) {
    controlHoverAttributes = controlHoverAttributesParam;
  }

  /**
	 * Creates the.
	 *
	 * @return the effect type on hover
	 */
  @Override
  @Nonnull
  public EffectTypeOnHover create() {
    final EffectTypeOnHover effectTypeOnHover;
    if (controlHoverAttributes == null) {
      effectTypeOnHover = new EffectTypeOnHover(attributes);
    } else {
      effectTypeOnHover = new EffectTypeOnHover(attributes, controlHoverAttributes);
    }
    effectTypeOnHover.addValues(effectValues);

    return effectTypeOnHover;
  }
}
