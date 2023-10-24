package de.lessvoid.nifty.controls.dynamic.attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.loaderv2.types.EffectType;
import de.lessvoid.nifty.loaderv2.types.EffectValueType;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class ControlEffectAttributes.
 */
public class ControlEffectAttributes {
  
  /** The attributes. */
  @Nonnull
  protected Attributes attributes = new Attributes();
  
  /** The effect values. */
  @Nonnull
  protected List<EffectValueType> effectValues = new ArrayList<EffectValueType>();

  /**
	 * Instantiates a new control effect attributes.
	 */
  public ControlEffectAttributes() {
  }

  /**
	 * Instantiates a new control effect attributes.
	 *
	 * @param attributes   the attributes
	 * @param effectValues the effect values
	 */
  public ControlEffectAttributes(
      @Nonnull final Attributes attributes,
      @Nonnull final List<EffectValueType> effectValues) {
    this.attributes = new Attributes(attributes);
    this.effectValues = new ArrayList<EffectValueType>(effectValues);
    Collections.copy(this.effectValues, effectValues);
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
	 * Sets the inherit.
	 *
	 * @param inherit the new inherit
	 */
  public void setInherit(@Nonnull final String inherit) {
    attributes.set("inherit", inherit);
  }

  /**
	 * Sets the post.
	 *
	 * @param post the new post
	 */
  public void setPost(@Nonnull final String post) {
    attributes.set("post", post);
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
	 * Sets the alternate enable.
	 *
	 * @param alternateEnable the new alternate enable
	 */
  public void setAlternateEnable(@Nonnull final String alternateEnable) {
    attributes.set("alternateEnable", alternateEnable);
  }

  /**
	 * Sets the alternate disable.
	 *
	 * @param alternateDisable the new alternate disable
	 */
  public void setAlternateDisable(@Nonnull final String alternateDisable) {
    attributes.set("alternateDisable", alternateDisable);
  }

  /**
	 * Sets the custom key.
	 *
	 * @param customKey the new custom key
	 */
  public void setCustomKey(@Nonnull final String customKey) {
    attributes.set("customKey", customKey);
  }

  /**
	 * Sets the never stop rendering.
	 *
	 * @param neverStopRendering the new never stop rendering
	 */
  public void setNeverStopRendering(@Nonnull final String neverStopRendering) {
    attributes.set("neverStopRendering", neverStopRendering);
  }

  /**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
  public void setName(@Nonnull final String name) {
    attributes.set("name", name);
  }

  /**
	 * Sets the start delay.
	 *
	 * @param value the new start delay
	 */
  public void setStartDelay(@Nonnull final String value) {
    attributes.set("startDelay", value);
  }

  /**
	 * Sets the length.
	 *
	 * @param value the new length
	 */
  public void setLength(@Nonnull final String value) {
    attributes.set("length", value);
  }

  /**
	 * Sets the one shot.
	 *
	 * @param value the new one shot
	 */
  public void setOneShot(@Nonnull final String value) {
    attributes.set("oneShot", value);
  }

  /**
	 * Sets the time type.
	 *
	 * @param value the new time type
	 */
  public void setTimeType(@Nonnull final String value) {
    attributes.set("timeType", value);
  }

  /**
	 * Sets the on start effect callback.
	 *
	 * @param value the new on start effect callback
	 */
  public void setOnStartEffectCallback(@Nonnull final String value) {
    attributes.set("onStartEffect", value);
  }

  /**
	 * Sets the on end effect callback.
	 *
	 * @param value the new on end effect callback
	 */
  public void setOnEndEffectCallback(@Nonnull final String value) {
    attributes.set("onEndEffect", value);
  }

  /**
	 * Adds the effect values.
	 *
	 * @param value the value
	 */
  public void addEffectValues(final EffectValueType value) {
    effectValues.add(value);
  }

  /**
	 * Creates the.
	 *
	 * @return the effect type
	 */
  @Nonnull
  public EffectType create() {
    EffectType effectType = new EffectType(attributes);
    for (int i = 0; i < effectValues.size(); i++) {
      effectType.addValue(effectValues.get(i));
    }
    return effectType;
  }
}
