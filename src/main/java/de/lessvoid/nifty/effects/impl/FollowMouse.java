package de.lessvoid.nifty.effects.impl;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * This Nifty Effect will change the position of the element the effect is attached to to
 * the current mouse pointer position. Make sure that the element you apply this effect to
 * is a child of an element that uses childLayout="absolute" or this effect will not work correctly.
 *
 * @author void
 */
public class FollowMouse implements EffectImpl {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(FollowMouse.class.getName());
  
  /** The nifty. */
  @Nullable
  private Nifty nifty;
  
  /** The offset X. */
  private int offsetX;
  
  /** The offset Y. */
  private int offsetY;

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
    this.offsetX = Integer.valueOf(parameter.getProperty("offsetX", "20"));
    this.offsetY = Integer.valueOf(parameter.getProperty("offsetY", "20"));
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
    NiftyMouse niftyMouse = nifty.getNiftyMouse();

    int newPosX = borderCheck(niftyMouse.getX() + offsetX, element.getWidth(), r.getWidth());
    element.setConstraintX(SizeValue.px(newPosX));

    int newPosY = borderCheck(niftyMouse.getY() + offsetY, element.getHeight(), r.getHeight());
    element.setConstraintY(SizeValue.px(newPosY));

    element.getParent().layoutElements();
  }

  /**
	 * Border check.
	 *
	 * @param pos  the pos
	 * @param size the size
	 * @param max  the max
	 * @return the int
	 */
  private int borderCheck(final int pos, final int size, final int max) {
    if (pos + size > max) {
      return max - size;
    }
    return pos;
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}


