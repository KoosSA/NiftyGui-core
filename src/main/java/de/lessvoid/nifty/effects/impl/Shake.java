package de.lessvoid.nifty.effects.impl;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * Shake effect.
 *
 * @author void
 */
public class Shake implements EffectImpl {

  /** The distance. */
  private float distance;
  
  /** The global. */
  private boolean global;

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
    distance = Float.parseFloat(parameter.getProperty("distance", "10.0"));
    global = "true".equals(parameter.getProperty("global", "true").toLowerCase());
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
      float d = distance;
      float x = -d + (float) Math.random() * 2 * d;
      float y = -d + (float) Math.random() * 2 * d;

      if (normalizedTime >= 0.99f) {
        x = 0;
        y = 0;
      }

      if (global) {
        r.setGlobalPosition(x, y);
      } else {
        r.moveTo(x, y);
      }
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
