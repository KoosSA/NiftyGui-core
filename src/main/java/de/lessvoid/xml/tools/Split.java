package de.lessvoid.xml.tools;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The Class Split.
 */
public class Split {
  
  /** The Constant BEGIN_KEY. */
  private static final String BEGIN_KEY = "${";
  
  /** The Constant END_KEY. */
  private static final String END_KEY = "}";

  /**
	 * Contains key.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
  public static boolean containsKey(@Nullable final String value) {
    if (value == null) {
      return false;
    }
    return value.contains(BEGIN_KEY);
  }

  /**
	 * Split.
	 *
	 * @param value the value
	 * @return the list
	 */
  @Nonnull
  public static List<String> split(@Nullable final String value) {

    List<String> result = new ArrayList<String>();
    if (value == null) {
      return result;
    }

    String remaining = value;
    int startIdx = 0;
    while (true) {
      remaining = remaining.substring(startIdx);
      if (remaining.length() == 0) {
        break;
      }

      boolean parsingKey = remaining.startsWith(BEGIN_KEY);
      int endIdx = findEndIdx(remaining, parsingKey);
      if (endIdx == -1) {
        result.add(remaining);
        break;
      }

      if (parsingKey) {
        result.add(remaining.substring(0, endIdx + 1));
        startIdx = endIdx + 1;
      } else {
        result.add(remaining.substring(0, endIdx));
        startIdx = endIdx;
      }
    }

    return result;
  }

  /**
	 * Join.
	 *
	 * @param parts the parts
	 * @return the string
	 */
  @Nonnull
  public static String join(@Nonnull final List<String> parts) {
    StringBuilder result = new StringBuilder();
    for (String part : parts) {
      result.append(part);
    }
    return result.toString();
  }

  /**
	 * Find end idx.
	 *
	 * @param remaining  the remaining
	 * @param parsingKey the parsing key
	 * @return the int
	 */
  private static int findEndIdx(@Nonnull final String remaining, final boolean parsingKey) {
    if (parsingKey) {
      return remaining.indexOf(END_KEY);
    } else {
      return remaining.indexOf(BEGIN_KEY);
    }
  }
}
