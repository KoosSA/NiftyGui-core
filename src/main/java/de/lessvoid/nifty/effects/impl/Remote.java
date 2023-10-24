package de.lessvoid.nifty.effects.impl;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.TargetElementResolver;

/**
 * The Class Remote.
 */
public class Remote implements EffectImpl {
  
  /** The target element. */
  @Nullable
  private Element targetElement;
  
  /** The effect event id. */
  @Nullable
  private EffectEventId effectEventId;

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
    Screen screen = nifty.getCurrentScreen();
    if (screen == null) {
      return;
    }
    TargetElementResolver resolver = new TargetElementResolver(screen, element);
    targetElement = resolver.resolve(parameter.getProperty("targetElement"));
    if (targetElement != null) {
      effectEventId = (EffectEventId) parameter.get("effectEventId");
      targetElement.startEffect(effectEventId, null);
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
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
    if (targetElement != null && effectEventId != null) {
      targetElement.stopEffect(effectEventId);
    }
  }
}


