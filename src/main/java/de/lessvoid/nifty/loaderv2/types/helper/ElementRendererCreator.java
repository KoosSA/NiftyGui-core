package de.lessvoid.nifty.loaderv2.types.helper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;

/**
 * The Interface ElementRendererCreator.
 */
public interface ElementRendererCreator {
  
  /**
	 * Creates the element renderer.
	 *
	 * @param nifty the nifty
	 * @return the element renderer[]
	 */
  @Nullable
  ElementRenderer[] createElementRenderer(@Nonnull Nifty nifty);
}
