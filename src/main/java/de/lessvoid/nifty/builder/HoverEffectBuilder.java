package de.lessvoid.nifty.builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlHoverAttributes;
import de.lessvoid.nifty.effects.Falloff;

/**
 * The Class HoverEffectBuilder.
 */
public class HoverEffectBuilder extends EffectBuilder {
  
  /** The hover attributes. */
  @Nonnull
  private final ControlHoverAttributes hoverAttributes = new ControlHoverAttributes();

  /**
	 * Instantiates a new hover effect builder.
	 *
	 * @param effectName the effect name
	 */
  public HoverEffectBuilder(@Nonnull final String effectName) {
    super(new ControlEffectOnHoverAttributes(), effectName);
    getAttributes().setControlHoverAttributes(hoverAttributes);
  }

  /**
	 * Inherit.
	 *
	 * @param inherit the inherit
	 * @return the hover effect builder
	 */
  @Override
  @Nonnull
  public HoverEffectBuilder inherit(final boolean inherit) {
    super.inherit(inherit);
    return this;
  }

  /**
	 * Inherit.
	 *
	 * @return the hover effect builder
	 */
  @Override
  @Nonnull
  public HoverEffectBuilder inherit() {
    super.inherit();
    return this;
  }

  /**
	 * Post.
	 *
	 * @param post the post
	 * @return the hover effect builder
	 */
  @Override
  @Nonnull
  public HoverEffectBuilder post(final boolean post) {
    super.post(post);
    return this;
  }

  /**
	 * Overlay.
	 *
	 * @param overlay the overlay
	 * @return the hover effect builder
	 */
  @Override
  @Nonnull
  public HoverEffectBuilder overlay(final boolean overlay) {
    super.overlay(overlay);
    return this;
  }

  /**
	 * Alternate enable.
	 *
	 * @param alternateEnable the alternate enable
	 * @return the hover effect builder
	 */
  @Override
  @Nonnull
  public HoverEffectBuilder alternateEnable(@Nonnull final String alternateEnable) {
    super.alternateEnable(alternateEnable);
    return this;
  }

  /**
	 * Alternate disable.
	 *
	 * @param alternateDisable the alternate disable
	 * @return the hover effect builder
	 */
  @Override
  @Nonnull
  public HoverEffectBuilder alternateDisable(@Nonnull final String alternateDisable) {
    super.alternateDisable(alternateDisable);
    return this;
  }

  /**
	 * Custom key.
	 *
	 * @param customKey the custom key
	 * @return the hover effect builder
	 */
  @Override
  @Nonnull
  public HoverEffectBuilder customKey(@Nonnull final String customKey) {
    super.customKey(customKey);
    return this;
  }

  /**
	 * Never stop rendering.
	 *
	 * @param neverStopRendering the never stop rendering
	 * @return the hover effect builder
	 */
  @Override
  @Nonnull
  public HoverEffectBuilder neverStopRendering(final boolean neverStopRendering) {
    super.neverStopRendering(neverStopRendering);
    return this;
  }

  /**
	 * Effect parameter.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the hover effect builder
	 */
  @Override
  @Nonnull
  public HoverEffectBuilder effectParameter(@Nonnull final String key, @Nonnull final String value) {
    super.effectParameter(key, value);
    return this;
  }

  /**
	 * Hover parameter.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the hover effect builder
	 */
  @Nonnull
  public HoverEffectBuilder hoverParameter(@Nonnull final String key, @Nonnull final String value) {
    hoverAttributes.set(key, value);
    return this;
  }

  /**
	 * Hover falloff type.
	 *
	 * @param hoverFalloffType the hover falloff type
	 * @return the hover effect builder
	 */
  @Nonnull
  public HoverEffectBuilder hoverFalloffType(@Nonnull final Falloff.HoverFalloffType hoverFalloffType) {
    hoverAttributes.set("hoverFalloffType", hoverFalloffType.toString());
    return this;
  }

  /**
	 * Hover falloff constraint.
	 *
	 * @param hoverFalloffConstraint the hover falloff constraint
	 * @return the hover effect builder
	 */
  @Nonnull
  public HoverEffectBuilder hoverFalloffConstraint(@Nonnull final Falloff.HoverFalloffConstraint
      hoverFalloffConstraint) {
    hoverAttributes.set("hoverFalloffConstraint", hoverFalloffConstraint.toString());
    return this;
  }

  /**
	 * Hover width.
	 *
	 * @param hoverWidth the hover width
	 * @return the hover effect builder
	 */
  @Nonnull
  public HoverEffectBuilder hoverWidth(@Nonnull final String hoverWidth) {
    hoverAttributes.set("hoverWidth", hoverWidth);
    return this;
  }

  /**
	 * Hover height.
	 *
	 * @param hoverHeight the hover height
	 * @return the hover effect builder
	 */
  @Nonnull
  public HoverEffectBuilder hoverHeight(@Nonnull final String hoverHeight) {
    hoverAttributes.set("hoverHeight", hoverHeight);
    return this;
  }

  /**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
  @Override
  @Nonnull
  public ControlEffectOnHoverAttributes getAttributes() {
    return (ControlEffectOnHoverAttributes) attributes;
  }

  /**
	 * Effect value.
	 *
	 * @param values the values
	 * @return the hover effect builder
	 */
  @Override
  @Nonnull
  public HoverEffectBuilder effectValue(@Nullable final String... values) {
    super.effectValue(values);
    return this;
  }
}
