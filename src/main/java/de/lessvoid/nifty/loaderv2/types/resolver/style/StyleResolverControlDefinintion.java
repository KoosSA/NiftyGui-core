package de.lessvoid.nifty.loaderv2.types.resolver.style;

import javax.annotation.Nullable;

import de.lessvoid.nifty.loaderv2.types.StyleType;

/**
 * The Class StyleResolverControlDefinintion.
 */
public class StyleResolverControlDefinintion implements StyleResolver {
  
  /** The base style resolver. */
  private final StyleResolver baseStyleResolver;
  
  /** The base style id. */
  private final String baseStyleId;

  /**
	 * Instantiates a new style resolver control definintion.
	 *
	 * @param baseStyleResolverParam the base style resolver param
	 * @param baseStyleIdParam       the base style id param
	 */
  public StyleResolverControlDefinintion(
      final StyleResolver baseStyleResolverParam,
      final String baseStyleIdParam) {
    baseStyleResolver = baseStyleResolverParam;
    baseStyleId = baseStyleIdParam;
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
    int indexOf = styleId.indexOf("#");
    if (indexOf == -1) {
      return baseStyleResolver.resolve(styleId);
    } else if (indexOf == 0) {
      return baseStyleResolver.resolve(baseStyleId + styleId);
    } else {
      return baseStyleResolver.resolve(styleId);
    }
  }
}
