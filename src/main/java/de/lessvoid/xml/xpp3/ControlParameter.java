package de.lessvoid.xml.xpp3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class ControlParameter.
 */
public class ControlParameter {
  
  /** The pattern. */
  private Pattern pattern = Pattern.compile(".*\\$\\((.*)\\).*");

  /**
	 * Checks if is parameter.
	 *
	 * @param value the value
	 * @return true, if is parameter
	 */
  public boolean isParameter(final String value) {
    if (value.startsWith("${")) {
      return false;
    }
    if (value.startsWith("$")) {
      return true;
    }
    return pattern.matcher(value).find();
  }

  /**
	 * Extract parameter.
	 *
	 * @param value the value
	 * @return the string
	 */
  public String extractParameter(final String value) {
    Matcher matcher = pattern.matcher(value);
    if (matcher.find()) {
      return matcher.group(1);
    }
    if (value.startsWith("$")) {
      return value.replaceFirst("\\$", "");
    }
    return value;
  }

  /**
	 * Apply parameter.
	 *
	 * @param originalValue the original value
	 * @param key           the key
	 * @param value         the value
	 * @return the string
	 */
  public String applyParameter(final String originalValue, final String key, final String value) {
    return originalValue.replaceAll("\\$" + key, value).replaceAll("\\$\\(" + key + "\\)", value);
  }
}
