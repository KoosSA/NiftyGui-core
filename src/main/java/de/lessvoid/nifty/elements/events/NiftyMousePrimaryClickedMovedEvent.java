package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;


/**
 * The Class NiftyMousePrimaryClickedMovedEvent.
 */
public class NiftyMousePrimaryClickedMovedEvent extends NiftyMouseBaseEvent {
  
  /**
	 * Instantiates a new nifty mouse primary clicked moved event.
	 *
	 * @param element    the element
	 * @param mouseEvent the mouse event
	 */
  public NiftyMousePrimaryClickedMovedEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
