package de.lessvoid.nifty.loaderv2.types.apply;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Interface ApplyRenderer.
 */
public interface ApplyRenderer {
  
  /**
	 * Apply.
	 *
	 * @param screen       the screen
	 * @param element      the element
	 * @param attributes   the attributes
	 * @param renderEngine the render engine
	 */
  void apply(
      @Nonnull Screen screen,
      @Nonnull Element element,
      @Nonnull Attributes attributes,
      @Nonnull NiftyRenderEngine renderEngine);
}
