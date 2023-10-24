package de.lessvoid.nifty.effects.impl;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

/**
 * ColorPulsate.
 *
 * @author void
 */
public class ColorPulsate implements EffectImpl {
  
  /** The start color. */
  private Color startColor;
  
  /** The end color. */
  private Color endColor;
  
  /** The pulsator. */
  private Pulsator pulsator;
  
  /** The current color. */
  @Nonnull
  private final Color currentColor = new Color("#000f");

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
    startColor = new Color(parameter.getProperty("startColor", "#00000000"));
    endColor = new Color(parameter.getProperty("endColor", "#ffffffff"));
    pulsator = new Pulsator(parameter, nifty.getTimeProvider());
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
    float value = pulsator.update();
    currentColor.linear(startColor, endColor, value);
    r.setColor(currentColor);
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
