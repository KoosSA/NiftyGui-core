package de.lessvoid.nifty.effects;

import java.util.Properties;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;

/**
 * The Class EffectEvents.
 */
public class EffectEvents {
  
  /** The on start effect. */
  private NiftyMethodInvoker onStartEffect;
  
  /** The on end effect. */
  private NiftyMethodInvoker onEndEffect;

  /**
	 * Inits the.
	 *
	 * @param nifty       the nifty
	 * @param controllers the controllers
	 * @param parameter   the parameter
	 */
  public void init(final Nifty nifty, final Object[] controllers, @Nonnull final Properties parameter) {
    String onStartEffectString = parameter.getProperty("onStartEffect");
    if (onStartEffectString != null) {
      onStartEffect = new NiftyMethodInvoker(nifty, onStartEffectString, controllers);
    }
    String onEndEffectString = parameter.getProperty("onEndEffect");
    if (onEndEffectString != null) {
      onEndEffect = new NiftyMethodInvoker(nifty, onEndEffectString, controllers);
    }
  }

  /**
	 * On start effect.
	 *
	 * @param parameter the parameter
	 */
  public void onStartEffect(final Properties parameter) {
    if (onStartEffect != null) {
      onStartEffect.invoke(parameter);
    }
  }

  /**
	 * On end effect.
	 */
  public void onEndEffect() {
    if (onEndEffect != null) {
      onEndEffect.invoke();
    }
  }
}
