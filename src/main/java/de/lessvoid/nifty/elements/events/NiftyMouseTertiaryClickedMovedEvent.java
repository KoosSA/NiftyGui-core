package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMouseTertiaryClickedMovedEvent.
 */
public class NiftyMouseTertiaryClickedMovedEvent extends NiftyMouseBaseEvent {
  
  /**
	 * Instantiates a new nifty mouse tertiary clicked moved event.
	 *
	 * @param element    the element
	 * @param mouseEvent the mouse event
	 */
  public NiftyMouseTertiaryClickedMovedEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
