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

/**
 * This renders a quad AND interpolates the color between startColor and endColor
 * over the lifetime of the effect.
 *
 * @author void
 */
public class RenderQuad implements EffectImpl {
  
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
  
  /** The width. */
  private SizeValue width;

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
    width = new SizeValue(parameter.getProperty("width"));
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
    r.saveStates();

    currentColor.linear(startColor, endColor, normalizedTime);
    if (falloff == null) {
      r.setColor(currentColor);
    } else {
      tempColor.multiply(currentColor, falloff.getFalloffValue());
      r.setColor(tempColor);
    }

    int size = width.getValueAsInt(element.getParent().getWidth());
    if (size == -1) {
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
    } else {
      r.renderQuad((element.getX() + element.getWidth() / 2) - size / 2, element.getY(), size, element.getHeight());
    }

    r.restoreStates();
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
