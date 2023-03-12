package de.lessvoid.nifty.loaderv2.types.apply;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public interface ApplyRenderer {
  void apply(
      @Nonnull Screen screen,
      @Nonnull Element element,
      @Nonnull Attributes attributes,
      @Nonnull NiftyRenderEngine renderEngine);
}
