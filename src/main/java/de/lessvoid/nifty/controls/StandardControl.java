package de.lessvoid.nifty.controls;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public interface StandardControl {
  @Nonnull
  Element createControl(@Nonnull Nifty nifty, @Nonnull Screen screen, @Nonnull Element parent);
}
