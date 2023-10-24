package de.lessvoid.nifty.loaderv2.types.resolver.style;

import java.util.Map;

import javax.annotation.Nullable;

import de.lessvoid.nifty.loaderv2.types.StyleType;

/**
 * The Class StyleResolverDefault.
 */
public class StyleResolverDefault implements StyleResolver {
  
  /** The styles. */
  private final Map<String, StyleType> styles;

  /**
	 * Instantiates a new style resolver default.
	 *
	 * @param stylesParam the styles param
	 */
  public StyleResolverDefault(final Map<String, StyleType> stylesParam) {
    styles = stylesParam;
  }

  /**
	 * Resolve.
	 *
	 * @param styleId the style id
	 * @return the style type
	 */
  @Override
  @Nullable
  public StyleType resolve(@Nullable final String styleId) {
    if (styleId == null) {
      return null;
    }
    return styles.get(styleId);
  }
}
