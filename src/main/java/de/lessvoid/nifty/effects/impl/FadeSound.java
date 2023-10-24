package de.lessvoid.nifty.effects.impl;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * Fade a sound out.
 *
 * @author void
 */
public class FadeSound implements EffectImpl {
  
  /** The sound handle. */
  @Nullable
  private SoundHandle soundHandle;

  /**
	 * Activate.
	 *
	 * @param nifty     the nifty
	 * @param element   the element
	 * @param parameter the parameter
	 */
  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    String soundId = parameter.getProperty("sound");
    soundHandle = nifty.getSoundSystem().getSound(soundId);
  }

  /**
	 * Execute.
	 *
	 * @param element        the element
	 * @param normalizedTime the normalized time
	 * @param falloff        the falloff
	 * @param r              the r
	 */
  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (soundHandle == null) {
      return;
    }
    soundHandle.setVolume(1.0f - normalizedTime);
    if (normalizedTime >= 1.0f) {
      soundHandle.stop();
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
