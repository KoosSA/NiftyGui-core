package de.lessvoid.nifty.elements.render;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * The element renderer.
 *
 * @author void
 */
public interface ElementRenderer {
  /**
   * Render the element.
   *
   * @param w the Widget
   * @param r the RenderDevice for output.
   */
  void render(@Nonnull Element w, @Nonnull NiftyRenderEngine r);
}
