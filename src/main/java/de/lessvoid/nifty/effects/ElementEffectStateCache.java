package de.lessvoid.nifty.effects;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The Class ElementEffectStateCache.
 */
public class ElementEffectStateCache {
  
  /** The states. */
  @Nonnull
  private final Map<EffectEventId, Boolean> states;

  /**
	 * Instantiates a new element effect state cache.
	 */
  public ElementEffectStateCache() {
    states = new EnumMap<EffectEventId, Boolean>(EffectEventId.class);
  }

  /**
	 * Gets the.
	 *
	 * @param effectEventId the effect event id
	 * @return true, if successful
	 */
  public boolean get(@Nonnull final EffectEventId effectEventId) {
    @Nullable Boolean value = states.get(effectEventId);
    if (value == null) {
      return false;
    }
    return value;
  }

  /**
	 * Sets the.
	 *
	 * @param eventId      the event id
	 * @param effectActive the effect active
	 */
  public void set(@Nonnull final EffectEventId eventId, final boolean effectActive) {
    states.put(eventId, effectActive);
  }
}
