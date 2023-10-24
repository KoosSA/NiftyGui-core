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
 * Play music.
 *
 * @author void
 */
public class PlayMusic implements EffectImpl {
  
  /** The done. */
  private boolean done;
  
  /** The repeat. */
  private boolean repeat;
  
  /** The music handle. */
  @Nullable
  private SoundHandle musicHandle;
  
  /** The nifty. */
  @Nullable
  private Nifty nifty;

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
    this.nifty = nifty;
    musicHandle = nifty.getSoundSystem().getMusic(parameter.getProperty("music"));
    repeat = Boolean.valueOf(parameter.getProperty("repeat", "false"));
    done = false;
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
    if (normalizedTime > 0.0f) {
      if (musicHandle != null) {
        if (!done) {
          playMusic();
          done = true;
        } else {
          // in repeat mode, when the sound is not playing anymore we'll simply start it again
          if (repeat) {
            if (!musicHandle.isPlaying()) {
              playMusic();
            }
          }
        }
      }
    }
  }

  /**
	 * Play music.
	 */
  private void playMusic() {
    if (musicHandle != null && nifty != null) {
      musicHandle.setVolume(nifty.getSoundSystem().getMusicVolume());
      musicHandle.play();
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
