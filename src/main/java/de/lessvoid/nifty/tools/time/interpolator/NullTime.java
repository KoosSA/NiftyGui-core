package de.lessvoid.nifty.tools.time.interpolator;

import java.util.Properties;

import javax.annotation.Nonnull;

/**
 * Interpolator that always return 1.0f.
 *
 * @author void
 */
public class NullTime implements Interpolator {

  @Override
  public void initialize(@Nonnull final Properties parameter) {
  }

  @Override
  public void start() {
  }

  @Override
  public float getValue(final long lengthParameter, final long timePassed) {
    return 1.0f;
  }
}
