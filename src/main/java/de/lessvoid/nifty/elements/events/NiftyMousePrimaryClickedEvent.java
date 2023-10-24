package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMousePrimaryClickedEvent.
 */
public class NiftyMousePrimaryClickedEvent extends NiftyMouseBaseEvent {
  
  /**
	 * Instantiates a new nifty mouse primary clicked event.
	 *
	 * @param element the element
	 */
  public NiftyMousePrimaryClickedEvent(final Element element) {
    super(element);
  }

  /**
	 * Instantiates a new nifty mouse primary clicked event.
	 *
	 * @param element    the element
	 * @param mouseEvent the mouse event
	 */
  public NiftyMousePrimaryClickedEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
