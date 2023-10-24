package de.lessvoid.nifty.tools;

/**
 * The Class StopWatch.
 */
public class StopWatch {
  
  /** The time provider. */
  private final de.lessvoid.nifty.spi.time.TimeProvider timeProvider;
  
  /** The start time. */
  private long startTime;

  /**
	 * Instantiates a new stop watch.
	 *
	 * @param timeProviderParam the time provider param
	 */
  public StopWatch(final de.lessvoid.nifty.spi.time.TimeProvider timeProviderParam) {
    timeProvider = timeProviderParam;
  }

  /**
	 * Stop.
	 *
	 * @return the long
	 */
  public long stop() {
    return timeProvider.getMsTime() - startTime;
  }

  /**
	 * Start.
	 */
  public void start() {
    startTime = timeProvider.getMsTime();
  }
}
