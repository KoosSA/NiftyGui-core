package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;

/**
 * The Class ElementShowEvent.
 */
public class ElementShowEvent implements NiftyEvent {
  
  /** The element. */
  @Nonnull
  private final Element element;

  /**
	 * Instantiates a new element show event.
	 *
	 * @param element the element
	 */
  public ElementShowEvent(@Nonnull final Element element) {
    this.element = element;
  }

  /**
	 * Gets the element.
	 *
	 * @return the element
	 */
  @Nonnull
  public Element getElement() {
    return element;
  }
}
