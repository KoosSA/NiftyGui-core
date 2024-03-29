package de.lessvoid.xml.tools;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The Class SpecialValuesReplace.
 *
 * @author Marc Pompl
 * @author void (added some additional features)
 * @created 12.06.2010
 */
public class SpecialValuesReplace {
  
  /** The Constant KEY_PROP. */
  private static final String KEY_PROP = "PROP.";
  
  /** The Constant KEY_ENV. */
  private static final String KEY_ENV = "ENV.";
  
  /** The Constant KEY_CALL. */
  private static final String KEY_CALL = "CALL.";

  /** The Constant log. */
  private static final Logger log = Logger.getLogger(SpecialValuesReplace.class.getName());

  /**
	 * Tries to replace values surrounded by "${...}". All pieces of the input that
	 * are not part of a "${...}" expression are returned unmodified. Replacement is
	 * executed for all "${...}" values contained in the input. Yes, this means
	 * there is support for multiple "${...}" expressions in input! :)<br>
	 * <br>
	 * <b>Example inputs:</b>
	 * <ul>
	 * <li>{@code ${ENV.myEnv}} checks and returns if {@code myEnv} exists in
	 * {@link System.getEnv()}.</li>
	 * 
	 * <li>{@code ${PROP.myProp}} checks and returns if </ode>myProp</code> exists
	 * in the given properties. If properties is {@code null},
	 * {@link System.getProperties()} is checked instead.</li
	 * 
	 * <li>{@code ${CALL.myMethod()}} calls the method {@code myMethod()} on the
	 * given {@code object} if it's not {@code null}</li>
	 * </ul>
	 * 
	 * <li>{@code ${resourceBundleId.key}} tries to find the ResourceBundle with the
	 * given {@code id} on the given list of ResourceBundles. And then calls
	 * {@code resourceBundle.get(key)} to translate the value.</li>
	 * </ul>
	 *
	 * @param input            (may be {@code null})
	 * @param resourceBundles  Map of pre loaded ResourceBundles with a String id
	 * @param methodCallTarget if the input contains ${CALL...} the target object to
	 *                         call the method with
	 * @param properties       if the input contains ${PROP...} the properties to
	 *                         use (may be {@code null} in this case
	 *                         System.getProperties() are used)
	 * @param locale           the locale
	 * @return the parsed input
	 */
  @Nonnull
  public static String replace(
      @Nullable final String input,
      @Nonnull final Map<String, BundleInfo> resourceBundles,
      @Nullable final Object methodCallTarget,
      @Nullable final Properties properties,
      @Nonnull final Locale locale) {
    if (input == null) {
      return "";
    }
    if (!Split.containsKey(input)) {
      return input;
    }
    List<String> parts = Split.split(input);
    for (int idx=0; idx<parts.size(); idx++) {
      String part = parts.get(idx);
      String prev = getPrev(parts, idx);
      if (isSpecialTag(part, prev)) {
        String value = removeQuotes(part);
        if (value.startsWith(KEY_ENV)) {
          parts.set(idx, handleENV(part));
        } else if (value.startsWith(KEY_PROP)) {
          parts.set(idx, handleProperties(part, properties));
        } else if (value.startsWith(KEY_CALL)) {
          parts.set(idx, handleCall(part, methodCallTarget));
        } else {
          parts.set(idx, handleLocalize(part, resourceBundles, locale));
        }
      } else {
        if (endsWithQuote(prev)) {
          assert prev != null; // endsWithQuote does not allow null values
          parts.set(idx-1, prev.substring(0, prev.length()-1));
        }
      }
    }
    String result = Split.join(parts);
    if (log.isLoggable(Level.FINER)) {
      log.finer(MessageFormat.format("Parsed input \"{0}\" to \"{1}\"", input, result));
    }
    return result;
  }

  /**
	 * Ends with quote.
	 *
	 * @param prev the prev
	 * @return true, if successful
	 */
  private static boolean endsWithQuote(@Nullable String prev) {
    return prev != null && prev.endsWith("\\");
  }

  /**
	 * Gets the prev.
	 *
	 * @param parts the parts
	 * @param idx   the idx
	 * @return the prev
	 */
  @Nullable
  private static String getPrev(@Nonnull final List<String> parts, final int idx) {
    if (idx == 0) {
      return null;
    }
    return parts.get(idx - 1);
  }

  /**
	 * Removes the quotes.
	 *
	 * @param input the input
	 * @return the string
	 */
  @Nonnull
  private static String removeQuotes(@Nonnull final String input) {
    return input.substring(2, input.length()-1);
  }

  /**
	 * Checks if is special tag.
	 *
	 * @param input the input
	 * @param prev  the prev
	 * @return true, if is special tag
	 */
  private static boolean isSpecialTag(@Nullable final String input, @Nullable final String prev) {
    boolean isSpecialTag = input != null && input.startsWith("${") && input.endsWith("}");
    if (!isSpecialTag) {
      return false;
    }
    if (endsWithQuote(prev)) {
      return false;
    }
    return true;
  }
  

  /**
	 * Handle ENV.
	 *
	 * @param value the value
	 * @return the string
	 */
  private static String handleENV(@Nonnull final String value) {
    String name = removeQuotes(value).substring(KEY_ENV.length());
    if (System.getenv().containsKey(name)) {
      String env = System.getenv().get(name);
      if (env != null && env.length() > 0) {
        return env;
      }
    }
    return value;
  }

  /**
	 * Handle properties.
	 *
	 * @param input      the input
	 * @param properties the properties
	 * @return the string
	 */
  @Nullable
  private static String handleProperties(@Nonnull final String input, final Properties properties) {
    String name = removeQuotes(input).substring(KEY_PROP.length());
    String value = readFromProperties(name, properties);
    if (value == null) {
      value = readFromProperties(name, System.getProperties());
    }
    if (value != null) {
      return value;
    }
    return input;
  }

  /**
	 * Read from properties.
	 *
	 * @param name       the name
	 * @param properties the properties
	 * @return the string
	 */
  @Nullable
  private static String readFromProperties(final String name, @Nullable final Properties properties) {
    if (properties != null) {
      if (properties.containsKey(name)) {
        String value = properties.getProperty(name);
        if (value != null && value.length() > 0) {
          return value;
        }
      }
    }
    return null;
  }

  /**
	 * Handle call.
	 *
	 * @param value  the value
	 * @param object the object
	 * @return the string
	 */
  private static String handleCall(@Nonnull final String value, @Nullable final Object object) {
    if (object != null) {
      String methodName = removeQuotes(value).substring(KEY_CALL.length());
      MethodInvoker methodInvoker = new MethodInvoker(methodName, object);
      Object response = methodInvoker.invoke();
      if (response != null) {
        return response.toString();
      }
    }
    return value;
  }

  /**
	 * Handle localize.
	 *
	 * @param value           the value
	 * @param resourceBundles the resource bundles
	 * @param locale          the locale
	 * @return the string
	 */
  private static String handleLocalize(
      @Nonnull final String value,
      @Nonnull final Map<String, BundleInfo> resourceBundles,
      @Nonnull final Locale locale) {
    if (value.contains(".")) {
      String removedQuotes = removeQuotes(value);
      String resourceSelector = removedQuotes.substring(0, removedQuotes.indexOf("."));
      String resourceKey = removedQuotes.substring(removedQuotes.indexOf(".") + 1);
      BundleInfo bundleInfo = resourceBundles.get(resourceSelector);
      if (bundleInfo == null) {
        if (log.isLoggable(Level.WARNING)) {
          log.warning("no resource bundle defined for: " + resourceSelector);
        }
        return value;
      }
      try {
        return bundleInfo.getString(resourceKey, locale);
      } catch(MissingResourceException e) {
        if (log.isLoggable(Level.WARNING)) {
          log.warning("Missing resource: " + resourceSelector + "." + resourceKey);
        }
        return "<" + resourceKey + ">";
      }
    }
    return value;
  }
}
