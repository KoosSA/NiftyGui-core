package de.lessvoid.nifty.tools;

import javax.annotation.Nonnull;

/**
 * The Class StringHelper.
 */
public final class StringHelper {
  
  /**
	 * Instantiates a new string helper.
	 */
  private StringHelper() {
  }

  /**
   * output length whitespaces.
   *
   * @param length number of whitespaces
   * @return string with whitespaces length times.
   */
  @Nonnull
  public static String whitespace(final int length) {
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < length; i++) {
      b.append(' ');
    }
    return b.toString();
  }

}
