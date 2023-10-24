package org.jglfont.impl.format.angelcode;

import java.util.Hashtable;
import java.util.Map;

/**
 * LineData.
 *
 * @author void
 */
public class AngelCodeLineData {
  
  /** The map. */
  private Map<String, String> map = new Hashtable<String, String>();

  /**
	 * Put.
	 *
	 * @param key   the key
	 * @param value the value
	 */
  public void put(final String key, final String value) {
    map.put(key, value);
  }

  /**
	 * Gets the string.
	 *
	 * @param key the key
	 * @return the string
	 */
  public String getString(final String key) {
    return map.get(key);
  }

  /**
	 * Gets the int.
	 *
	 * @param key the key
	 * @return the int
	 */
  public int getInt(final String key) {
    String value = map.get(key);
    if (value == null) {
      return 0;
    }
    try {
      return Integer.valueOf(value);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  /**
	 * Clear.
	 */
  public void clear() {
    map.clear();
  }

  /**
	 * Checks for value.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
  public boolean hasValue(final String key) {
    return map.containsKey(key);
  }

  /**
	 * To string.
	 *
	 * @return the string
	 */
  public String toString() {
    return map.toString();
  }
}