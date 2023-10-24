package de.lessvoid.nifty.tools.time.interpolator;

import java.util.Properties;

import javax.annotation.Nonnull;

/**
 * Interpolates a value exponential from 0.0 to 1.0 with the given time parameters.
 *
 * @author void
 */
public class ExpTime implements Interpolator {

  /**
   * factor.
   */
  private float factorParam = 0;

  /**
	 * Initialize.
	 *
	 * @param parameter the parameter
	 */
  @Override
  public final void initialize(@Nonnull final Properties parameter) {
    this.factorParam = Float.parseFloat(parameter.getProperty("factor", "1"));
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
  public final float getValue(final long lengthParameter, final long timePassed) {
    return (float) Math.pow(1.0f - ((lengthParameter - timePassed) / (float) lengthParameter), factorParam);
  }
}
