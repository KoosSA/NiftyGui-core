package de.lessvoid.nifty.controls;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * The Interface StandardControl.
 */
public interface StandardControl {
  
  /**
	 * Creates the control.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 * @param parent the parent
	 * @return the element
	 */
  @Nonnull
  Element createControl(@Nonnull Nifty nifty, @Nonnull Screen screen, @Nonnull Element parent);
}
