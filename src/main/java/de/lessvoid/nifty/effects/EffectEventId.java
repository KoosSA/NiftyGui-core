package de.lessvoid.nifty.effects;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.effects.EffectProcessorImpl.Notify;

/**
 * This enum contains all the ID's of effect events known.
 *
 * @author void
 */
public enum EffectEventId {
  
  /** The on start screen. */
  onStartScreen(false),
  
  /** The on end screen. */
  onEndScreen(true),
  
  /** The on focus. */
  onFocus(true),
  
  /** The on get focus. */
  onGetFocus(false),
  
  /** The on lost focus. */
  onLostFocus(false),
  
  /** The on click. */
  onClick(false),
  
  /** The on hover. */
  onHover(true),
  
  /** The on start hover. */
  onStartHover(false),
  
  /** The on end hover. */
  onEndHover(false),
  
  /** The on active. */
  onActive(true),
  
  /** The on custom. */
  onCustom(false),
  
  /** The on hide. */
  onHide(true),
  
  /** The on show. */
  onShow(false),
  
  /** The on enabled. */
  onEnabled(true),
  
  /** The on disabled. */
  onDisabled(true);

  /** The never stop rendering. */
  private final boolean neverStopRendering;

  /**
	 * Instantiates a new effect event id.
	 *
	 * @param neverStopRendering the never stop rendering
	 */
  private EffectEventId(final boolean neverStopRendering) {
    this.neverStopRendering = neverStopRendering;
  }

  /**
	 * Creates the effect processor.
	 *
	 * @param notify the notify
	 * @return the effect processor
	 */
  @Nonnull
  EffectProcessor createEffectProcessor(@Nonnull final Notify notify) {
    return new EffectProcessorImpl(notify, neverStopRendering);
  }
}
