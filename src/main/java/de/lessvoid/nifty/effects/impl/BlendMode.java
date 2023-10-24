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
 * The Class BlendMode.
 */
public class BlendMode implements EffectImpl {
  
  /** The blend mode. */
  @Nullable
  de.lessvoid.nifty.render.BlendMode blendMode = null;

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
    String blendMode = parameter.getProperty("blendMode");
    if (blendMode != null) {
      if (blendMode.toLowerCase().equals("blend")) {
        this.blendMode = de.lessvoid.nifty.render.BlendMode.BLEND;
      } else if (blendMode.toLowerCase().equals("multiply")) {
        this.blendMode = de.lessvoid.nifty.render.BlendMode.MULIPLY;
      }
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
    if (blendMode != null) {
      r.setBlendMode(blendMode);
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}


