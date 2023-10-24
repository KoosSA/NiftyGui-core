package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMouseWheelEvent.
 */
public class NiftyMouseWheelEvent implements NiftyEvent {
  
  /** The element. */
  private final Element element;
  
  /** The mouse wheel. */
  private final int mouseWheel;

  /**
	 * Instantiates a new nifty mouse wheel event.
	 *
	 * @param element the element
	 * @param source  the source
	 */
  public NiftyMouseWheelEvent(final Element element, @Nonnull final NiftyMouseInputEvent source) {
    this.element = element;
    this.mouseWheel = source.getMouseWheel();
  }

  /**
	 * Gets the element.
	 *
	 * @return the element
	 */
  public Element getElement() {
    return element;
  }

  /**
	 * Gets the mouse wheel.
	 *
	 * @return the mouse wheel
	 */
  public int getMouseWheel() {
    return mouseWheel;
  }
}
