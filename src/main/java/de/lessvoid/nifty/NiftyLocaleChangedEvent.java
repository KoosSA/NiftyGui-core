package de.lessvoid.nifty;

import java.util.Locale;

/**
 * The Class NiftyLocaleChangedEvent.
 */
public class NiftyLocaleChangedEvent {
  
  /** The locale. */
  private final Locale locale;

  /**
	 * Instantiates a new nifty locale changed event.
	 *
	 * @param locale the locale
	 */
  public NiftyLocaleChangedEvent(final Locale locale) {
    this.locale = locale;
  }

  /**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
  public Locale getLocale() {
    return locale;
  }
}
