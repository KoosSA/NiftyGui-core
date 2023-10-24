package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMouseSecondaryReleaseEvent.
 */
public class NiftyMouseSecondaryReleaseEvent extends NiftyMouseBaseEvent {
  
  /**
	 * Instantiates a new nifty mouse secondary release event.
	 *
	 * @param element the element
	 */
  public NiftyMouseSecondaryReleaseEvent(final Element element) {
    super(element);
  }

  /**
	 * Instantiates a new nifty mouse secondary release event.
	 *
	 * @param element    the element
	 * @param mouseEvent the mouse event
	 */
  public NiftyMouseSecondaryReleaseEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
