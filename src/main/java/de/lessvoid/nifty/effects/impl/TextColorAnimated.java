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

/**
 * TextColor Effect.
 *
 * @author void
 */
public class TextColorAnimated implements EffectImpl {
  
  /** The current color. */
  @Nonnull
  private final Color currentColor = new Color("#000f");
  
  /** The temp color. */
  @Nonnull
  private final Color tempColor = new Color("#000f");
  
  /** The start color. */
  private Color startColor;
  
  /** The end color. */
  private Color endColor;

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
    startColor = new Color(parameter.getProperty("startColor", "#0000"));
    endColor = new Color(parameter.getProperty("endColor", "#ffff"));
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
    currentColor.linear(startColor, endColor, normalizedTime);
    if (falloff == null) {
      setColor(r, currentColor);
    } else {
      tempColor.multiply(currentColor, falloff.getFalloffValue());
      setColor(r, tempColor);
    }
  }

  /**
	 * Sets the color.
	 *
	 * @param r     the r
	 * @param color the color
	 */
  private void setColor(@Nonnull final NiftyRenderEngine r, @Nonnull final Color color) {
    if (r.isColorAlphaChanged()) {
      r.setColorIgnoreAlpha(color);
    } else {
      r.setColor(color);
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
