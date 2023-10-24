package de.lessvoid.nifty.tools.time.interpolator;

import java.util.Properties;

import javax.annotation.Nonnull;

/**
 * This interpolator returns {@code 0.f} at its first run and {@code 2.f} during all following runs.
 */
public class OneTime implements Interpolator {
  /**
   * Storage if the interpolator already run once.
   */
  private boolean hasRun;

  /**
	 * Initialize.
	 *
	 * @param parameter the parameter
	 */
  @Override
  public final void initialize(@Nonnull final Properties parameter) {
    hasRun = false;
  }

  /**
	 * Start.
	 */
  @Override
  public void start() {
    hasRun = false;
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
    if (!hasRun) {
      hasRun = true;
      return 0.0f;
    } else {
      return 2.0f;
    }
  }
}
