package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMouseTertiaryReleaseEvent.
 */
public class NiftyMouseTertiaryReleaseEvent extends NiftyMouseBaseEvent {
  
  /**
	 * Instantiates a new nifty mouse tertiary release event.
	 *
	 * @param element the element
	 */
  public NiftyMouseTertiaryReleaseEvent(final Element element) {
    super(element);
  }

  /**
	 * Instantiates a new nifty mouse tertiary release event.
	 *
	 * @param element    the element
	 * @param mouseEvent the mouse event
	 */
  public NiftyMouseTertiaryReleaseEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
