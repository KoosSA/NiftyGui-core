package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class NiftyMouseEvent.
 */
public class NiftyMouseEvent implements NiftyEvent {
  
  /** The element. */
  private final Element element;
  
  /** The mouse X. */
  private final int mouseX;
  
  /** The mouse Y. */
  private final int mouseY;
  
  /** The mouse wheel. */
  private final int mouseWheel;
  
  /** The button 0 down. */
  private final boolean button0Down;
  
  /** The button 1 down. */
  private final boolean button1Down;
  
  /** The button 2 down. */
  private final boolean button2Down;
  
  /** The button 0 initial down. */
  private final boolean button0InitialDown;
  
  /** The button 1 initial down. */
  private final boolean button1InitialDown;
  
  /** The button 2 initial down. */
  private final boolean button2InitialDown;
  
  /** The button 0 release. */
  private final boolean button0Release;
  
  /** The button 1 release. */
  private final boolean button1Release;
  
  /** The button 2 release. */
  private final boolean button2Release;

  /**
	 * Instantiates a new nifty mouse event.
	 *
	 * @param element the element
	 * @param source  the source
	 */
  public NiftyMouseEvent(final Element element, @Nonnull final NiftyMouseInputEvent source) {
    this.element = element;
    this.mouseX = source.getMouseX();
    this.mouseY = source.getMouseY();
    this.mouseWheel = source.getMouseWheel();
    this.button0Down = source.isButton0Down();
    this.button1Down = source.isButton1Down();
    this.button2Down = source.isButton2Down();
    this.button0InitialDown = source.isButton0InitialDown();
    this.button1InitialDown = source.isButton1InitialDown();
    this.button2InitialDown = source.isButton2InitialDown();
    this.button0Release = source.isButton0Release();
    this.button1Release = source.isButton1Release();
    this.button2Release = source.isButton2Release();
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

  /**
	 * Checks if is button 0 down.
	 *
	 * @return true, if is button 0 down
	 */
  public boolean isButton0Down() {
    return button0Down;
  }

  /**
	 * Checks if is button 1 down.
	 *
	 * @return true, if is button 1 down
	 */
  public boolean isButton1Down() {
    return button1Down;
  }

  /**
	 * Checks if is button 2 down.
	 *
	 * @return true, if is button 2 down
	 */
  public boolean isButton2Down() {
    return button2Down;
  }

  /**
	 * Checks if is any button down.
	 *
	 * @return true, if is any button down
	 */
  public boolean isAnyButtonDown() {
    return button0Down || button1Down || button2Down;
  }

  /**
	 * Checks if is button 0 initial down.
	 *
	 * @return true, if is button 0 initial down
	 */
  public boolean isButton0InitialDown() {
    return button0InitialDown;
  }

  /**
	 * Checks if is button 1 initial down.
	 *
	 * @return true, if is button 1 initial down
	 */
  public boolean isButton1InitialDown() {
    return button1InitialDown;
  }

  /**
	 * Checks if is button 2 initial down.
	 *
	 * @return true, if is button 2 initial down
	 */
  public boolean isButton2InitialDown() {
    return button2InitialDown;
  }

  /**
	 * Checks if is button 0 release.
	 *
	 * @return true, if is button 0 release
	 */
  public boolean isButton0Release() {
    return button0Release;
  }

  /**
	 * Checks if is button 1 release.
	 *
	 * @return true, if is button 1 release
	 */
  public boolean isButton1Release() {
    return button1Release;
  }

  /**
	 * Checks if is button 2 release.
	 *
	 * @return true, if is button 2 release
	 */
  public boolean isButton2Release() {
    return button2Release;
  }

  /**
	 * To string.
	 *
	 * @return the string
	 */
  @Override
  @Nonnull
  public String toString() {
    return
        "mouseX = " + mouseX + ", " +
            "mouseY = " + mouseY + ", " +
            "button0Down = " + button0Down + ", " +
            "button1Down = " + button1Down + ", " +
            "button2Down = " + button2Down + ", " +
            "button0InitialDown = " + button0InitialDown + ", " +
            "button1InitialDown = " + button1InitialDown + ", " +
            "button2InitialDown = " + button2InitialDown + ", " +
            "button0Release = " + button0Release + ", " +
            "button1Release = " + button1Release + ", " +
            "button2Release = " + button2Release;
  }
}
