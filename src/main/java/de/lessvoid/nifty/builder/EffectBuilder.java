package de.lessvoid.nifty.builder;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.loaderv2.types.EffectValueType;

/**
 * The Class EffectBuilder.
 */
public class EffectBuilder {
  
  /** The Constant logger. */
  private static final Logger logger = Logger.getLogger(EffectBuilder.class.getName());
  
  /** The attributes. */
  @Nonnull
  protected final ControlEffectAttributes attributes;

  /**
	 * Instantiates a new effect builder.
	 *
	 * @param attributes the attributes
	 * @param effectName the effect name
	 */
  public EffectBuilder(@Nonnull final ControlEffectAttributes attributes, @Nonnull final String effectName) {
    this.attributes = attributes;
    attributes.setName(effectName);
  }

  /**
	 * Instantiates a new effect builder.
	 *
	 * @param effectName the effect name
	 */
  public EffectBuilder(@Nonnull final String effectName) {
    this(new ControlEffectAttributes(), effectName);
  }

  /**
	 * Inherit.
	 *
	 * @param inherit the inherit
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder inherit(final boolean inherit) {
    attributes.setInherit(String.valueOf(inherit));
    return this;
  }

  /**
	 * Inherit.
	 *
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder inherit() {
    attributes.setInherit("true");
    return this;
  }

  /**
	 * Post.
	 *
	 * @param post the post
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder post(final boolean post) {
    attributes.setPost(String.valueOf(post));
    return this;
  }

  /**
	 * Overlay.
	 *
	 * @param overlay the overlay
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder overlay(final boolean overlay) {
    attributes.setOverlay(String.valueOf(overlay));
    return this;
  }

  /**
	 * Alternate enable.
	 *
	 * @param alternateEnable the alternate enable
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder alternateEnable(@Nonnull final String alternateEnable) {
    attributes.setAlternateEnable(alternateEnable);
    return this;
  }

  /**
	 * Alternate disable.
	 *
	 * @param alternateDisable the alternate disable
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder alternateDisable(@Nonnull final String alternateDisable) {
    attributes.setAlternateDisable(alternateDisable);
    return this;
  }

  /**
	 * Custom key.
	 *
	 * @param customKey the custom key
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder customKey(@Nonnull final String customKey) {
    attributes.setCustomKey(customKey);
    return this;
  }

  /**
	 * Never stop rendering.
	 *
	 * @param neverStopRendering the never stop rendering
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder neverStopRendering(final boolean neverStopRendering) {
    attributes.setNeverStopRendering(String.valueOf(neverStopRendering));
    return this;
  }

  /**
	 * Effect parameter.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder effectParameter(@Nonnull final String key, @Nonnull final String value) {
    attributes.setAttribute(key, value);
    return this;
  }

  /**
	 * Start delay.
	 *
	 * @param ms the ms
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder startDelay(final int ms) {
    attributes.setStartDelay(String.valueOf(ms));
    return this;
  }

  /**
	 * Length.
	 *
	 * @param ms the ms
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder length(final int ms) {
    attributes.setLength(String.valueOf(ms));
    return this;
  }

  /**
	 * One shot.
	 *
	 * @param oneShot the one shot
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder oneShot(final boolean oneShot) {
    attributes.setOneShot(String.valueOf(oneShot));
    return this;
  }

  /**
	 * Time type.
	 *
	 * @param timeType the time type
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder timeType(@Nonnull final String timeType) {
    attributes.setTimeType(timeType);
    return this;
  }

  /**
	 * On start effect callback.
	 *
	 * @param callback the callback
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder onStartEffectCallback(@Nonnull final String callback) {
    attributes.setOnStartEffectCallback(callback);
    return this;
  }

  /**
	 * On end effect callback.
	 *
	 * @param callback the callback
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder onEndEffectCallback(@Nonnull final String callback) {
    attributes.setOnEndEffectCallback(callback);
    return this;
  }

  /**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
  @Nonnull
  public ControlEffectAttributes getAttributes() {
    return attributes;
  }

  /**
	 * Effect value.
	 *
	 * @param values the values
	 * @return the effect builder
	 */
  @Nonnull
  public EffectBuilder effectValue(@Nullable final String... values) {
    if (values == null || values.length % 2 != 0) {
      logger.warning("effect values must be given in pairs, example: effectValue(\"color\", \"#f00f\")");
      return this;
    }
    EffectValueType effectValue = new EffectValueType();
    for (int i = 0; i < values.length / 2; i++) {
      String key = values[(i * 2)];
      String value = values[i * 2 + 1];
      effectValue.getAttributes().set(key, value);
    }
    attributes.addEffectValues(effectValue);
    return this;
  }
}
