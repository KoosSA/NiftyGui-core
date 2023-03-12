package de.lessvoid.nifty.loaderv2.types.resolver.style;

import java.util.Map;

import javax.annotation.Nullable;

import de.lessvoid.nifty.loaderv2.types.StyleType;

public class StyleResolverDefault implements StyleResolver {
  private final Map<String, StyleType> styles;

  public StyleResolverDefault(final Map<String, StyleType> stylesParam) {
    styles = stylesParam;
  }

  @Override
  @Nullable
  public StyleType resolve(@Nullable final String styleId) {
    if (styleId == null) {
      return null;
    }
    return styles.get(styleId);
  }
}
