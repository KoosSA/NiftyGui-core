package de.lessvoid.nifty.effects.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * The Class ChangeMouseCursor.
 */
public class ChangeMouseCursor implements EffectImpl {
  
  /** The nifty mouse. */
  @Nullable
  private NiftyMouse niftyMouse;
  
  /** The old mouse id. */
  @Nullable
  private String oldMouseId;
  
  /** The new mouse id. */
  @Nullable
  private String newMouseId;

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
    niftyMouse = nifty.getNiftyMouse();
    oldMouseId = niftyMouse.getCurrentId();
    newMouseId = parameter.getProperty("id");
  }

  /**
	 * Execute.
	 *
	 * @param element    the element
	 * @param effectTime the effect time
	 * @param falloff    the falloff
	 * @param r          the r
	 */
  @Override
  public void execute(
      @Nonnull final Element element,
      final float effectTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (niftyMouse != null) {
      niftyMouse.enableMouseCursor(newMouseId);
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
    if (niftyMouse != null) {
      niftyMouse.enableMouseCursor(oldMouseId);
    }
  }
}
