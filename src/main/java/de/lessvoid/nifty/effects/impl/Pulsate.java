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
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

/**
 * Color - color overlay.
 *
 * @author void
 */
public class Pulsate implements EffectImpl {
  
  /** The current color. */
  @Nonnull
  private final Color currentColor = new Color("#000f");
  
  /** The start color. */
  @Nullable
  private Color startColor;
  
  /** The end color. */
  @Nullable
  private Color endColor;
  
  /** The width. */
  @Nullable
  private SizeValue width;
  
  /** The pulsator. */
  @Nullable
  private Pulsator pulsator;
  
  /** The change color only. */
  private boolean changeColorOnly = false;
  
  /** The activated. */
  private boolean activated = false;

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
    width = new SizeValue(parameter.getProperty("width"));
    changeColorOnly = Boolean.valueOf(parameter.getProperty("changeColorOnly", "false"));
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
    if (startColor == null || endColor == null || width == null || pulsator == null) {
      return;
    }
    if (!activated && normalizedTime > 0.0f) {
      activated = true;
      pulsator.reset();
    }

    if (activated) {
      if (!changeColorOnly) {
        r.saveStates();
      }

      float value = pulsator.update();
      currentColor.linear(startColor, endColor, value);
      r.setColor(currentColor);

      if (!changeColorOnly) {
        int size = width.getValueAsInt(element.getParent().getWidth());
        if (size == -1) {
          r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
        } else {
          r.renderQuad((element.getX() + element.getWidth() / 2) - size / 2, element.getY(), size, element.getHeight());
        }
      }

      if (!changeColorOnly) {
        r.restoreStates();
      }
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
    activated = true;
  }
}
