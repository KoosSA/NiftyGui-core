package de.lessvoid.nifty.loaderv2.types.resolver.style;

import javax.annotation.Nullable;

import de.lessvoid.nifty.loaderv2.types.StyleType;

/**
 * The Interface StyleResolver.
 */
public interface StyleResolver {
  
  /**
	 * Resolve.
	 *
	 * @param styleId the style id
	 * @return the style type
	 */
  @Nullable
  StyleType resolve(String styleId);
}
