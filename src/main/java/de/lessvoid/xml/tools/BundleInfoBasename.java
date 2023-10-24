package de.lessvoid.xml.tools;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A BundleInfo that will use a basename to resolve a ResourceBundle. 
 * @author void
 */
public class BundleInfoBasename implements BundleInfo {
  
  /** The base name. */
  private String baseName;

  /**
	 * Instantiates a new bundle info basename.
	 *
	 * @param baseName the base name
	 */
  public BundleInfoBasename(final String baseName) {
    this.baseName = baseName;
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
      res = ResourceBundle.getBundle(baseName);
    } else {
      res = ResourceBundle.getBundle(baseName, locale);
    }
    return res.getString(resourceKey);
  }
}
