package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMouseTertiaryClickedEvent.
 */
public class NiftyMouseTertiaryClickedEvent extends NiftyMouseBaseEvent {
  
  /**
	 * Instantiates a new nifty mouse tertiary clicked event.
	 *
	 * @param element the element
	 */
  public NiftyMouseTertiaryClickedEvent(final Element element) {
    super(element);
  }

  /**
	 * Instantiates a new nifty mouse tertiary clicked event.
	 *
	 * @param element    the element
	 * @param mouseEvent the mouse event
	 */
  public NiftyMouseTertiaryClickedEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
