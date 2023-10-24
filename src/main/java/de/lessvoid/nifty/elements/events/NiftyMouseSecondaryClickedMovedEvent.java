package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMouseSecondaryClickedMovedEvent.
 */
public class NiftyMouseSecondaryClickedMovedEvent extends NiftyMouseBaseEvent {
  
  /**
	 * Instantiates a new nifty mouse secondary clicked moved event.
	 *
	 * @param element    the element
	 * @param mouseEvent the mouse event
	 */
  public NiftyMouseSecondaryClickedMovedEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
