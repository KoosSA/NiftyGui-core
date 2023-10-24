package de.lessvoid.nifty.controls.dynamic.attributes;

import java.util.Collection;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.loaderv2.types.EffectsType;
import de.lessvoid.nifty.tools.EnumStorage;
import de.lessvoid.nifty.tools.factories.CollectionFactory;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class ControlEffectsAttributes.
 */
public class ControlEffectsAttributes {
  
  /** The attributes. */
  @Nonnull
  private final Attributes attributes;

  /** The effect attributes. */
  @Nonnull
  private final EnumStorage<EffectEventId, Collection<ControlEffectAttributes>> effectAttributes;

  /**
	 * Instantiates a new control effects attributes.
	 */
  public ControlEffectsAttributes() {
    attributes = new Attributes();
    effectAttributes = new EnumStorage<EffectEventId, Collection<ControlEffectAttributes>>(
        EffectEventId.class, CollectionFactory.<ControlEffectAttributes>getArrayListInstance());
  }

  /**
	 * Support for CustomControlCreator.
	 *
	 * @param source the source
	 */
  public ControlEffectsAttributes(@Nonnull final EffectsType source) {
    attributes = new Attributes(source.getAttributes());
    effectAttributes = new EnumStorage<EffectEventId, Collection<ControlEffectAttributes>>(
        EffectEventId.class, CollectionFactory.<ControlEffectAttributes>getArrayListInstance());

    for (EffectEventId id : EffectEventId.values()) {
      if (source.hasEffectTypes(id)) {
        source.convertCopy(id, effectAttributes.get(id));
      }
    }
  }

  /**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
  @Nonnull
  public Attributes getAttributes() {
    return attributes;
  }

  /**
	 * Sets the attribute.
	 *
	 * @param name  the name
	 * @param value the value
	 */
  public void setAttribute(@Nonnull final String name, @Nonnull final String value) {
    attributes.set(name, value);
  }

  /**
	 * Sets the overlay.
	 *
	 * @param overlay the new overlay
	 */
  public void setOverlay(@Nonnull final String overlay) {
    attributes.set("overlay", overlay);
  }

  /**
	 * Adds the effect attribute.
	 *
	 * @param id        the id
	 * @param attribute the attribute
	 */
  public void addEffectAttribute(@Nonnull final EffectEventId id, @Nonnull final ControlEffectAttributes attribute) {
    effectAttributes.get(id).add(attribute);
  }

  /**
	 * Adds the on start screen.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnStartScreen(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onStartScreen, effectParam);
  }

  /**
	 * Adds the on end screen.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnEndScreen(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onEndScreen, effectParam);
  }

  /**
	 * Adds the on hover.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    addEffectAttribute(EffectEventId.onHover, effectParam);
  }

  /**
	 * Adds the on start hover.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnStartHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    addEffectAttribute(EffectEventId.onStartHover, effectParam);
  }

  /**
	 * Adds the on end hover.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnEndHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    addEffectAttribute(EffectEventId.onEndHover, effectParam);
  }

  /**
	 * Adds the on click.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnClick(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onClick, effectParam);
  }

  /**
	 * Adds the on focus.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnFocus(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onFocus, effectParam);
  }

  /**
	 * Adds the on lost focus.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnLostFocus(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onLostFocus, effectParam);
  }

  /**
	 * Adds the on get focus.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnGetFocus(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onGetFocus, effectParam);
  }

  /**
	 * Adds the on active.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnActive(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onActive, effectParam);
  }

  /**
	 * Adds the on show.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnShow(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onShow, effectParam);
  }

  /**
	 * Adds the on hide.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnHide(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onHide, effectParam);
  }

  /**
	 * Adds the on custom.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnCustom(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onCustom, effectParam);
  }

  /**
	 * Creates the.
	 *
	 * @return the effects type
	 */
  @Nonnull
  public EffectsType create() {
    EffectsType effectsType = new EffectsType(attributes);

    for (EffectEventId id : EffectEventId.values()) {
      if (effectAttributes.isSet(id)) {
        for (ControlEffectAttributes effectParam : effectAttributes.get(id)) {
          effectsType.addEventEffect(id, effectParam.create());
        }
      }
    }
    return effectsType;
  }

  /**
	 * Refresh effects type.
	 *
	 * @param effectsType the effects type
	 */
  public void refreshEffectsType(@Nonnull final EffectsType effectsType) {
    effectsType.getAttributes().refreshFromAttributes(getAttributes());
  }
}
