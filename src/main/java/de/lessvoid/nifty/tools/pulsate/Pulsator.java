package de.lessvoid.nifty.tools.pulsate;

import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.pulsate.provider.NullPulsator;
import de.lessvoid.nifty.tools.pulsate.provider.RectanglePulsator;
import de.lessvoid.nifty.tools.pulsate.provider.SinusPulsator;
import de.lessvoid.nifty.tools.pulsate.provider.SinusRaisedPulsator;

/**
 * Pulsator class.
 *
 * @author void
 */
public class Pulsator {
  /**
   * the logger.
   */
  @Nonnull
  private static final Logger log = Logger.getLogger(Pulsator.class.getName());

  /**
   * the time provider.
   */
  @Nonnull
  private final TimeProvider timeProvider;

  /**
   * the PulsateProvider we use.
   */
  @Nonnull
  private final PulsatorProvider pulsateProvider;

  /**
   * initialize with the given parameters.
   *
   * @param parameter       parameter props
   * @param newTimeProvider TimeProvider to use
   */
  public Pulsator(@Nonnull final Properties parameter, @Nonnull final TimeProvider newTimeProvider) {
    this.timeProvider = newTimeProvider;

    // check for the given pulsateType to create the appropriate PulsateProvider
    String pulsateType = parameter.getProperty("pulsateType", "sin");
    if ("sin".equals(pulsateType)) {
      pulsateProvider = new SinusPulsator();
    } else if ("sinRaised".equals(pulsateType)) {
      pulsateProvider = new SinusRaisedPulsator();
    } else if ("rectangle".equals(pulsateType)) {
      pulsateProvider = new RectanglePulsator();
    } else {
      log.warning(pulsateType + " is not supported, using NullPulsator for fallback. probably not what you want...");
      pulsateProvider = new NullPulsator();
    }

    // initialize the provider
    pulsateProvider.initialize(parameter);
    reset();
  }

  /**
   * Reset the pulsator.
   */
  public void reset() {
    pulsateProvider.reset(timeProvider.getMsTime());
  }

  /**
   * update the value.
   *
   * @return true when still active and false when done
   */
  public float update() {
    return pulsateProvider.getValue(timeProvider.getMsTime());
  }
}
