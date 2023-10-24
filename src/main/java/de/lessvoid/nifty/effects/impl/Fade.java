package de.lessvoid.nifty.effects.impl;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Alpha;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.LinearInterpolator;

/**
 * Fade effect - blend stuff in or out.
 *
 * @author void
 */
public class Fade implements EffectImpl {
  
  /** The start. */
  @Nonnull
  private Alpha start = Alpha.ZERO;
  
  /** The end. */
  @Nonnull
  private Alpha end = Alpha.FULL;
  
  /** The interpolator. */
  @Nullable
  private LinearInterpolator interpolator;

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
    // startColor and endColor (only alpha component used) are the old version of this
    // and are kept here only for backward compatibility. The current attributes are "start" and "end" alpha values.
    if (parameter.getProperty("startColor") != null) {
      start = new Alpha(new Color(parameter.getProperty("startColor", "#000000ff")).getAlpha());
    }
    if (parameter.getProperty("endColor") != null) {
      end = new Alpha(new Color(parameter.getProperty("endColor", "#ffffffff")).getAlpha());
    }
    if (parameter.getProperty("start") != null) {
      start = new Alpha(parameter.getProperty("start"));
    }
    if (parameter.getProperty("end") != null) {
      end = new Alpha(parameter.getProperty("end"));
    }
    interpolator = parameter.getInterpolator();
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
    if (interpolator != null) {
      r.setColorAlpha(interpolator.getValue(normalizedTime));
    } else {
      Alpha a = start.linear(end, normalizedTime);
      r.setColorAlpha(a.getAlpha());
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}


