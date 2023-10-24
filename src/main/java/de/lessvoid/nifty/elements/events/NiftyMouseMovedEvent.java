package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMouseMovedEvent.
 */
public class NiftyMouseMovedEvent implements NiftyEvent {
  
  /** The element. */
  private final Element element;
  
  /** The mouse X. */
  private final int mouseX;
  
  /** The mouse Y. */
  private final int mouseY;

  /**
	 * Instantiates a new nifty mouse moved event.
	 *
	 * @param element the element
	 * @param source  the source
	 */
  public NiftyMouseMovedEvent(final Element element, @Nonnull final NiftyMouseInputEvent source) {
    this.element = element;
    this.mouseX = source.getMouseX();
    this.mouseY = source.getMouseY();
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
	 * Gets the mouse X.
	 *
	 * @return the mouse X
	 */
  public int getMouseX() {
    return mouseX;
  }

  /**
	 * Gets the mouse Y.
	 *
	 * @return the mouse Y
	 */
  public int getMouseY() {
    return mouseY;
  }
}
