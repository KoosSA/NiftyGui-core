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

public class AlphaHide implements EffectImpl {

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (normalizedTime >= 1.0f) {
      r.setColorAlpha(Color.WHITE.getAlpha());
    } else {
      r.setColorAlpha(Color.NONE.getAlpha());
    }
  }

  @Override
  public void deactivate() {
  }
}


