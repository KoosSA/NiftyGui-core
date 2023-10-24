package de.lessvoid.nifty.tools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * The Class TargetElementResolver.
 */
public class TargetElementResolver {
  
  /** The Constant PARENT. */
  @Nonnull
  private final static String PARENT = "#parent";

  /** The screen. */
  @Nonnull
  private final Screen screen;
  
  /** The base. */
  @Nonnull
  private final Element base;

  /**
	 * Instantiates a new target element resolver.
	 *
	 * @param screen      the screen
	 * @param baseElement the base element
	 */
  public TargetElementResolver(@Nonnull final Screen screen, @Nonnull final Element baseElement) {
    this.screen = screen;
    this.base = baseElement;
  }

  /**
	 * Resolve.
	 *
	 * @param id the id
	 * @return the element
	 */
  @Nullable
  public Element resolve(@Nullable final String id) {
    if (id == null) {
      return null;
    }
    if (id.startsWith(PARENT)) {
      return resolveParents(id, base.getParent());
    }
    if (id.startsWith("#")) {
      return base.findElementById(id);
    }
    return screen.findElementById(id);
  }

  /**
	 * Resolve parents.
	 *
	 * @param id     the id
	 * @param parent the parent
	 * @return the element
	 */
  @Nullable
  private Element resolveParents(@Nonnull final String id, @Nonnull final Element parent) {
    String subParentId = id.replaceFirst(PARENT, "");
    if (!subParentId.startsWith(PARENT)) {
      if (subParentId.startsWith("#")) {
        return parent.findElementById(subParentId.replaceFirst("#", ""));
      }
      return parent;
    }
    return resolveParents(subParentId, parent.getParent());
  }
}
