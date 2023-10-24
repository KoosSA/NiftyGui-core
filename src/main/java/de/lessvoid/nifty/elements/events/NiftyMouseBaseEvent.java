package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMouseBaseEvent.
 */
public class NiftyMouseBaseEvent implements NiftyEvent {
  
  /** The element. */
  private final Element element;
  
  /** The mouse X. */
  private final int mouseX;
  
  /** The mouse Y. */
  private final int mouseY;
  
  /** The mouse wheel. */
  private final int mouseWheel;

  /**
	 * Instantiates a new nifty mouse base event.
	 *
	 * @param element the element
	 */
  public NiftyMouseBaseEvent(final Element element) {
    this.element = element;
    this.mouseX = 0;
    this.mouseY = 0;
    this.mouseWheel = 0;
  }

  /**
	 * Instantiates a new nifty mouse base event.
	 *
	 * @param element the element
	 * @param source  the source
	 */
  public NiftyMouseBaseEvent(final Element element, @Nonnull final NiftyMouseInputEvent source) {
    this.element = element;
    this.mouseX = source.getMouseX();
    this.mouseY = source.getMouseY();
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

  /**
	 * Gets the mouse wheel.
	 *
	 * @return the mouse wheel
	 */
  public int getMouseWheel() {
    return mouseWheel;
  }
}
