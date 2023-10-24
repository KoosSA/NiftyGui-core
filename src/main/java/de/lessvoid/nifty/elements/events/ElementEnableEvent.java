package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;

/**
 * The Class ElementEnableEvent.
 */
public class ElementEnableEvent implements NiftyEvent {
  
  /** The element. */
  private final Element element;

  /**
	 * Instantiates a new element enable event.
	 *
	 * @param element the element
	 */
  public ElementEnableEvent(final Element element) {
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
