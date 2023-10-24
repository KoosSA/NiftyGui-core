package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMousePrimaryReleaseEvent.
 */
public class NiftyMousePrimaryReleaseEvent extends NiftyMouseBaseEvent {
  
  /**
	 * Instantiates a new nifty mouse primary release event.
	 *
	 * @param element the element
	 */
  public NiftyMousePrimaryReleaseEvent(final Element element) {
    super(element);
  }

  /**
	 * Instantiates a new nifty mouse primary release event.
	 *
	 * @param element    the element
	 * @param mouseEvent the mouse event
	 */
  public NiftyMousePrimaryReleaseEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
