package de.lessvoid.xml.tools;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * A BundleInfo that will use an already existing ResourceBundle. 
 * @author void
 */
public class BundleInfoResourceBundle implements BundleInfo {
  
  /** The default resource bundle. */
  private final ResourceBundle defaultResourceBundle;
  
  /** The resource bundles. */
  private Map<Locale, ResourceBundle> resourceBundles = new HashMap<Locale, ResourceBundle>();

  /**
	 * Instantiates a new bundle info resource bundle.
	 *
	 * @param resourceBundle the resource bundle
	 */
  public BundleInfoResourceBundle(final ResourceBundle resourceBundle) {
    this.defaultResourceBundle = resourceBundle;
    resourceBundles.put(resourceBundle.getLocale(), resourceBundle);
  }

  /**
	 * Adds the.
	 *
	 * @param resourceBundle the resource bundle
	 */
  public void add(final ResourceBundle resourceBundle) {
    resourceBundles.put(resourceBundle.getLocale(), resourceBundle);
  }

  /**
	 * Gets the string.
	 *
	 * @param resourceKey the resource key
	 * @param locale      the locale
	 * @return the string
	 */
  @Override
  public String getString(final String resourceKey, final Locale locale) {
    ResourceBundle res;
    if (locale == null) {
      res = defaultResourceBundle;
    } else {
      res = resourceBundles.get(locale);
      if (res == null) {
        res = defaultResourceBundle;
      }
    }
    return res.getString(resourceKey);
  }
}
