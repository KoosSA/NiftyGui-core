package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMouseSecondaryClickedEvent.
 */
public class NiftyMouseSecondaryClickedEvent extends NiftyMouseBaseEvent {
  
  /**
	 * Instantiates a new nifty mouse secondary clicked event.
	 *
	 * @param element the element
	 */
  public NiftyMouseSecondaryClickedEvent(final Element element) {
    super(element);
  }

  /**
	 * Instantiates a new nifty mouse secondary clicked event.
	 *
	 * @param element    the element
	 * @param mouseEvent the mouse event
	 */
  public NiftyMouseSecondaryClickedEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
