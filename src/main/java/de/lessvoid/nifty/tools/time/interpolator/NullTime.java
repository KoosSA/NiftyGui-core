package de.lessvoid.nifty.tools.time.interpolator;

import java.util.Properties;

import javax.annotation.Nonnull;

/**
 * Interpolator that always return 1.0f.
 *
 * @author void
 */
public class NullTime implements Interpolator {

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
	 * @param lengthParameter the length parameter
	 * @param timePassed      the time passed
	 * @return the value
	 */
  @Override
  public float getValue(final long lengthParameter, final long timePassed) {
    return 1.0f;
  }
}
