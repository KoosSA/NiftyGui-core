package de.lessvoid.nifty.tools.time.interpolator;

import java.util.Properties;

import javax.annotation.Nonnull;

/**
 * Interpolates a value linear from 0.0 to 1.0 with the given time parameters.
 *
 * @author void
 */
public class LinearTime implements Interpolator {

  /**
	 * Initialize.
	 *
	 * @param parameter the parameter
	 */
  @Override
  public void initialize(@Nonnull final Properties parameter) {
  }

  /**
	 * Start.
	 */
  @Override
  public void start() {
  }

  /**
	 * Gets the value.
	 *
	 * @param lengthParam the length param
	 * @param timePassed  the time passed
	 * @return the value
	 */
  @Override
  public final float getValue(final long lengthParam, final long timePassed) {
    return 1.0f - ((lengthParam - timePassed) / (float) lengthParam);
  }
}
