package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;

/**
 * The Class ElementDisableEvent.
 */
public class ElementDisableEvent implements NiftyEvent {
  
  /** The element. */
  private final Element element;

  /**
	 * Instantiates a new element disable event.
	 *
	 * @param element the element
	 */
  public ElementDisableEvent(final Element element) {
    this.element = element;
  }

  /**
	 * Gets the element.
	 *
	 * @return the element
	 */
  public Element getElement() {
    return element;
  }
}
