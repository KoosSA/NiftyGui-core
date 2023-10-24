package de.lessvoid.nifty.effects.impl;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Fade a sound out.
 *
 * @author void
 */
public class FadeMusic implements EffectImpl {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(FadeMusic.class.getName());
  
  /** The nifty. */
  @Nullable
  private Nifty nifty;
  
  /** The from volume. */
  private float fromVolume;
  
  /** The to volume. */
  private float toVolume;
  
  /** The music. */
  @Nullable
  private SoundHandle music;

  /**
	 * Activate.
	 *
	 * @param niftyParam the nifty param
	 * @param element    the element
	 * @param parameter  the parameter
	 */
  @Override
  public void activate(
      @Nonnull final Nifty niftyParam,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    nifty = niftyParam;
    music = nifty.getSoundSystem().getMusic(parameter.getProperty("sound"));
    if (music == null) {
      log.warning("Failed to get music for effect.");
    } else {
      fromVolume = new SizeValue(parameter.getProperty("from", "0%")).getValue(1.0f);
      toVolume = new SizeValue(parameter.getProperty("to", "100%")).getValue(1.0f);
    }
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
    if (nifty == null) {
      log.warning("FadeMusic effect is executed before it was activated.");
      return;
    }
    if (music != null) {
      float volume = normalizedTime * (toVolume - fromVolume) + fromVolume;
      music.setVolume(volume);
      nifty.getSoundSystem().setMusicVolume(volume);
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
