package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlHoverAttributes;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class EffectTypeOnHover.
 */
public class EffectTypeOnHover extends EffectType {
  
  /** The hover type. */
  @Nonnull
  private HoverType hoverType;

  /**
	 * Instantiates a new effect type on hover.
	 */
  public EffectTypeOnHover() {
    hoverType = new HoverType();
  }

  /**
	 * Instantiates a new effect type on hover.
	 *
	 * @param hoverAttributes the hover attributes
	 */
  public EffectTypeOnHover(@Nonnull final ControlHoverAttributes hoverAttributes) {
    hoverType = hoverAttributes.create();
  }

  /**
	 * Instantiates a new effect type on hover.
	 *
	 * @param attributes the attributes
	 */
  public EffectTypeOnHover(@Nonnull final Attributes attributes) {
    super(attributes);
    hoverType = new HoverType();
  }

  /**
	 * Clone.
	 *
	 * @return the effect type on hover
	 * @throws CloneNotSupportedException the clone not supported exception
	 */
  @Nonnull
  @Override
  public EffectTypeOnHover clone() throws CloneNotSupportedException {
    try {
      final EffectTypeOnHover newObject = (EffectTypeOnHover) super.clone();
      newObject.hoverType = new HoverType(hoverType);
      return newObject;
    } catch (ClassCastException e) {
      throw new CloneNotSupportedException("Cloning failed because the clone method created the wrong object.");
    }
  }

  /**
	 * Instantiates a new effect type on hover.
	 *
	 * @param attributes      the attributes
	 * @param hoverAttributes the hover attributes
	 */
  public EffectTypeOnHover(
      @Nonnull final Attributes attributes,
      @Nonnull final ControlHoverAttributes hoverAttributes) {
    super(attributes);
    hoverType = hoverAttributes.create();
  }

  /**
	 * This supports creating CustomControlCreator.
	 *
	 * @return the control effect on hover attributes
	 */
  @Override
  @Nonnull
  public ControlEffectOnHoverAttributes convert() {
    return new ControlEffectOnHoverAttributes(getAttributes(), effectValues, hoverType);
  }

  /**
	 * Output.
	 *
	 * @param offset the offset
	 * @return the string
	 */
  @Override
  public String output(final int offset) {
    String result = super.output(offset);
    result += "\n" + hoverType.output(offset + 1);
    return result;
  }

  /**
	 * Initialize effect.
	 *
	 * @param effect        the effect
	 * @param effectEventId the effect event id
	 */
  @Override
  protected void initializeEffect(@Nonnull final Effect effect, final EffectEventId effectEventId) {
    Falloff falloff = hoverType.materialize();
    effect.enableHover(falloff);
    if (!EffectEventId.onEndHover.equals(effectEventId)) {
      effect.enableInfinite();
    }
  }
}
