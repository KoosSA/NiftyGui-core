package de.lessvoid.nifty.loaderv2.types.resolver.style;

import javax.annotation.Nullable;

import de.lessvoid.nifty.loaderv2.types.StyleType;

public interface StyleResolver {
  @Nullable
  StyleType resolve(String styleId);
}
