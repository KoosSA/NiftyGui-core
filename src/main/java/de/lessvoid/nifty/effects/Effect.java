package de.lessvoid.nifty.effects;

import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.Falloff.HoverFalloffConstraint;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.time.TimeInterpolator;

/**
 * An effect can be active or not and is always attached to one element. It has a TimeInterpolator that manages the life
 * time of the effect. The actual effect implementation is done be EffectImpl implementations.
 *
 * @author void
 */
public class Effect {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(Effect.class.getName());
  
  /** The effect event id. */
  @Nonnull
  private final EffectEventId effectEventId;
  
  /** The active. */
  private boolean active;
  
  /** The element. */
  @Nonnull
  private final Element element;
  
  /** The time interpolator. */
  @Nonnull
  private final TimeInterpolator timeInterpolator;
  
  /** The effect impl. */
  @Nonnull
  private final EffectImpl effectImpl;
  
  /** The parameter. */
  @Nonnull
  private final EffectProperties parameter;
  
  /** The controller array. */
  @Nonnull
  private final Object[] controllerArray;
  
  /** The post. */
  private final boolean post;
  
  /** The overlay. */
  private final boolean overlay;
  
  /** The alternate enable. */
  @Nullable
  private final String alternateEnable;
  
  /** The alternate disable. */
  @Nullable
  private final String alternateDisable;
  
  /** The alternate. */
  @Nullable
  private String alternate;
  
  /** The custom key. */
  @Nullable
  private final String customKey;
  
  /** The inherit. */
  private final boolean inherit;
  
  /** The nifty. */
  @Nonnull
  private final Nifty nifty;
  
  /** The hover effect. */
  private boolean hoverEffect;
  
  /** The infinite effect. */
  private boolean infiniteEffect;
  
  /** The falloff. */
  @Nullable
  private Falloff falloff;
  
  /** The effect events. */
  @Nonnull
  private final EffectEvents effectEvents;
  
  /** The never stop rendering. */
  private final boolean neverStopRendering;
  
  /** The custom flag. */
  private boolean customFlag;

  /**
	 * Instantiates a new effect.
	 *
	 * @param nifty              the nifty
	 * @param inherit            the inherit
	 * @param post               the post
	 * @param overlay            the overlay
	 * @param alternateEnable    the alternate enable
	 * @param alternateDisable   the alternate disable
	 * @param customKey          the custom key
	 * @param neverStopRendering the never stop rendering
	 * @param effectEventId      the effect event id
	 * @param element            the element
	 * @param effectImpl         the effect impl
	 * @param parameter          the parameter
	 * @param timeProvider       the time provider
	 * @param controllers        the controllers
	 */
  public Effect(
      @Nonnull final Nifty nifty,
      final boolean inherit,
      final boolean post,
      final boolean overlay,
      @Nullable final String alternateEnable,
      @Nullable final String alternateDisable,
      @Nullable final String customKey,
      final boolean neverStopRendering,
      @Nonnull final EffectEventId effectEventId,
      @Nonnull final Element element,
      @Nonnull final EffectImpl effectImpl,
      @Nonnull final EffectProperties parameter,
      @Nonnull final TimeProvider timeProvider,
      @Nonnull final Collection<Object> controllers) {
    this.nifty = nifty;
    this.inherit = inherit;
    this.post = post;
    this.overlay = overlay;
    active = false;
    this.alternateEnable = alternateEnable;
    this.alternateDisable = alternateDisable;
    alternate = null;
    this.customKey = customKey;
    this.effectEventId = effectEventId;
    hoverEffect = false;
    infiniteEffect = false;
    this.neverStopRendering = neverStopRendering;
    effectEvents = new EffectEvents();
    customFlag = false;

    this.element = element;
    this.effectImpl = effectImpl;
    this.parameter = parameter;
    parameter.put("effectEventId", effectEventId);
    timeInterpolator = new TimeInterpolator(parameter, timeProvider, infiniteEffect);
    controllerArray = controllers.toArray();
    effectEvents.init(nifty, controllerArray, parameter);
    customFlag = false;
  }

  /**
	 * Enable hover.
	 *
	 * @param falloffParameter the falloff parameter
	 */
  public void enableHover(final Falloff falloffParameter) {
    hoverEffect = true;
    falloff = falloffParameter;
    setVisibleToMouse(true);
  }

  /**
	 * Disable hover.
	 */
  public void disableHover() {
    hoverEffect = false;
    falloff = null;
  }

  /**
	 * Disable infinite.
	 */
  public void disableInfinite() {
    setInfiniteEffect(false);
  }

  /**
	 * Enable infinite.
	 */
  public void enableInfinite() {
    setInfiniteEffect(true);
  }

  /**
	 * Sets the infinite effect.
	 *
	 * @param infinite the new infinite effect
	 */
  public void setInfiniteEffect(final boolean infinite) {
    if (infinite != infiniteEffect) {
      infiniteEffect = infinite;
      timeInterpolator.initialize(parameter, infinite);
    }
  }

  /**
	 * Sets the visible to mouse.
	 *
	 * @param visibleToMouse the new visible to mouse
	 */
  public void setVisibleToMouse(final boolean visibleToMouse) {
    element.setVisibleToMouseEvents(visibleToMouse);
  }

  /**
	 * Update parameters.
	 */
  public void updateParameters() {
    timeInterpolator.initialize(parameter, infiniteEffect);
    effectEvents.init(nifty, controllerArray, parameter);
  }

  /**
	 * Start.
	 *
	 * @param alternate the alternate
	 * @param customKey the custom key
	 * @return true, if successful
	 */
  public boolean start(@Nullable final String alternate, @Nullable final String customKey) {
    this.alternate = alternate;
    if (!canStartEffect(alternate, customKey)) {
      return false;
    }

    log.fine("starting effect [" + getStateString() + "] with customKey [" + customKey + "]");
    internalStart();
    return true;
  }

  /**
	 * Internal start.
	 */
  private void internalStart() {
    active = true;
    timeInterpolator.start();
    effectEvents.onStartEffect(parameter);
    effectImpl.activate(nifty, element, parameter);
  }

  /**
	 * Update.
	 */
  public void update() {
    setActiveInternal(timeInterpolator.update(), !neverStopRendering);
  }

  /**
	 * Execute.
	 *
	 * @param r the r
	 */
  public void execute(@Nonnull final NiftyRenderEngine r) {
    if (isHoverEffect()) {
      effectImpl.execute(element, timeInterpolator.getValue(), falloff, r);
    } else {
      effectImpl.execute(element, timeInterpolator.getValue(), null, r);
    }
  }

  /**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
  public boolean isActive() {
    return active;
  }

  /**
	 * Deactivate.
	 */
  public void deactivate() {
    setActiveInternal(false, true);
  }

  /**
	 * Sets the active internal.
	 *
	 * @param newActive      the new active
	 * @param callDeactivate the call deactivate
	 */
  private void setActiveInternal(final boolean newActive, final boolean callDeactivate) {
    boolean ended = false;
    if (active && !newActive) {
      if (callDeactivate) {
        effectImpl.deactivate();
      }
      ended = true;
    }
    active = newActive;

    if (ended) {
      effectEvents.onEndEffect();
    }
  }

  /**
	 * Checks if is post.
	 *
	 * @return true, if is post
	 */
  public boolean isPost() {
    return post;
  }

  /**
	 * Checks if is alternate disable.
	 *
	 * @return true, if is alternate disable
	 */
  public boolean isAlternateDisable() {
    return alternateDisable != null;
  }

  /**
	 * Custom key matches.
	 *
	 * @param customKeyToCheck the custom key to check
	 * @return true, if successful
	 */
  public boolean customKeyMatches(@Nullable final String customKeyToCheck) {
    if (customKeyToCheck == null) {
      return customKey == null;
    }
    return customKeyToCheck.equals(customKey);
  }

  /**
	 * Gets the state string.
	 *
	 * @return the state string
	 */
  @Nonnull
  public String getStateString() {
    return "(" + effectImpl.getClass().getSimpleName() + "[" + customKey + "]" + ")";
  }

  /**
	 * Gets the effect impl.
	 *
	 * @param <T>            the generic type
	 * @param requestedClass the requested class
	 * @return the effect impl
	 */
  @Nullable
  public <T extends EffectImpl> T getEffectImpl(@Nonnull final Class<T> requestedClass) {
    if (requestedClass.isInstance(effectImpl)) {
      return requestedClass.cast(effectImpl);
    }
    return null;
  }

  /**
	 * Checks if is inherit.
	 *
	 * @return true, if is inherit
	 */
  public boolean isInherit() {
    return inherit;
  }

  /**
	 * Checks if is hover effect.
	 *
	 * @return true, if is hover effect
	 */
  public boolean isHoverEffect() {
    return hoverEffect;
  }

  /**
	 * Hover distance.
	 *
	 * @param x the x
	 * @param y the y
	 */
  public void hoverDistance(final int x, final int y) {
    if (falloff != null) {
      falloff.updateFalloffValue(element, x, y);
    }
  }

  /**
	 * Checks if is inside falloff.
	 *
	 * @param x the x
	 * @param y the y
	 * @return true, if is inside falloff
	 */
  public boolean isInsideFalloff(final int x, final int y) {
    if (falloff != null && falloff.getFalloffConstraint() != HoverFalloffConstraint.none) {
      return falloff.isInside(element, x, y);
    } else {
      return element.isMouseInsideElement(x, y);
    }
  }

  /**
	 * Checks if is overlay.
	 *
	 * @return true, if is overlay
	 */
  public boolean isOverlay() {
    return overlay;
  }

  /**
	 * Checks if is never stop rendering.
	 *
	 * @return true, if is never stop rendering
	 */
  public boolean isNeverStopRendering() {
    return neverStopRendering;
  }

  /**
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
  @Nonnull
  public EffectProperties getParameters() {
    return parameter;
  }

  /**
	 * Gets the alternate.
	 *
	 * @return the alternate
	 */
  @Nullable
  public String getAlternate() {
    return alternate;
  }

  /**
	 * Gets the custom key.
	 *
	 * @return the custom key
	 */
  @Nullable
  public String getCustomKey() {
    return customKey;
  }

  /**
	 * Can start effect.
	 *
	 * @param alternate the alternate
	 * @param customKey the custom key
	 * @return true, if successful
	 */
  private boolean canStartEffect(@Nullable final String alternate, @Nullable final String customKey) {
    if (alternate == null) {
      // no alternate key given

      // don't start this effect when it has an alternateKey set.
      if (isAlternateEnable()) {
        log.fine(
            "starting effect [" + getStateString() + "] canceled because alternateKey [NULL] and effect " +
                "isAlternateEnable()");
        return false;
      }
    } else {
      // we have an alternate key
      if (isAlternateDisable() && alternateDisableMatches(alternate)) {
        // don't start this effect. it has an alternateKey set and should be used for disable matches only
        log.fine(
            "starting effect [" + getStateString() + "] canceled because alternateKey [" + alternate + "] matches " +
                "alternateDisableMatches()");
        return false;
      }

      if (isAlternateEnable() && !alternateEnableMatches(alternate)) {
        // start with alternateEnable but names don't match ... don't start
        log.fine(
            "starting effect [" + getStateString() + "] canceled because alternateKey [" + alternate + "] does not " +
                "match alternateEnableMatches()");
        return false;
      }
    }

    if (!customKeyMatches(customKey)) {
      log.fine(
          "starting effect [" + getStateString() + "] canceled because customKey [" + customKey + "] does not match " +
              "key set at the effect");
      return false;
    }

    return true;
  }

  /**
	 * Checks if is alternate enable.
	 *
	 * @return true, if is alternate enable
	 */
  private boolean isAlternateEnable() {
    return alternateEnable != null;
  }

  /**
	 * Alternate enable matches.
	 *
	 * @param alternate the alternate
	 * @return true, if successful
	 */
  private boolean alternateEnableMatches(@Nullable final String alternate) {
    return (alternate != null) && alternate.equals(alternateEnable);
  }

  /**
	 * Alternate disable matches.
	 *
	 * @param alternate the alternate
	 * @return true, if successful
	 */
  private boolean alternateDisableMatches(@Nullable final String alternate) {
    return (alternate != null) && alternate.equals(alternateDisable);
  }

  /**
	 * Gets the custom flag.
	 *
	 * @return the custom flag
	 */
  public boolean getCustomFlag() {
    return customFlag;
  }

  /**
	 * Sets the custom flag.
	 *
	 * @param customFlag the new custom flag
	 */
  public void setCustomFlag(final boolean customFlag) {
    this.customFlag = customFlag;
  }

  /**
	 * Gets the effect event id.
	 *
	 * @return the effect event id
	 */
  @Nonnull
  public EffectEventId getEffectEventId() {
    return effectEventId;
  }
}
