package de.lessvoid.nifty.input;


import javax.annotation.Nonnull;

/**
 * NiftyMouseInputEvent.
 * <p/>
 * Please Note: This object is pool managed which means it requires a default constructor and an initialize method
 * that resets all (!) member variables. Not doing this will lead to bad things happening while instances are reused!
 *
 * @author void
 */
public class NiftyMouseInputEvent {
  
  /** The mouse X. */
  private int mouseX;
  
  /** The mouse Y. */
  private int mouseY;
  
  /** The mouse wheel. */
  private int mouseWheel;
  
  /** The button 0 down. */
  private boolean button0Down;
  
  /** The button 0 initial down. */
  private boolean button0InitialDown;
  
  /** The button 0 release. */
  private boolean button0Release;
  
  /** The button 1 down. */
  private boolean button1Down;
  
  /** The button 1 initial down. */
  private boolean button1InitialDown;
  
  /** The button 1 release. */
  private boolean button1Release;
  
  /** The button 2 down. */
  private boolean button2Down;
  
  /** The button 2 initial down. */
  private boolean button2InitialDown;
  
  /** The button 2 release. */
  private boolean button2Release;

  /**
	 * Initialize.
	 *
	 * @param mouseX      the mouse X
	 * @param mouseY      the mouse Y
	 * @param mouseWheel  the mouse wheel
	 * @param button0Down the button 0 down
	 * @param button1Down the button 1 down
	 * @param button2Down the button 2 down
	 */
  public void initialize(
      final int mouseX,
      final int mouseY,
      final int mouseWheel,
      final boolean button0Down,
      final boolean button1Down,
      final boolean button2Down) {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
    this.mouseWheel = mouseWheel;
    this.button0Down = button0Down;
    this.button0InitialDown = false;
    this.button0Release = false;
    this.button1Down = button1Down;
    this.button1InitialDown = false;
    this.button1Release = false;
    this.button2Down = button2Down;
    this.button2InitialDown = false;
    this.button2Release = false;
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
	 * Checks if is button 0 initial down.
	 *
	 * @return true, if is button 0 initial down
	 */
  public boolean isButton0InitialDown() {
    return button0InitialDown;
  }

  /**
	 * Sets the button 0 initial down.
	 *
	 * @param button0InitialDown the new button 0 initial down
	 */
  public void setButton0InitialDown(final boolean button0InitialDown) {
    this.button0InitialDown = button0InitialDown;
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
	 * Sets the button 1 initial down.
	 *
	 * @param button1InitialDown the new button 1 initial down
	 */
  public void setButton1InitialDown(final boolean button1InitialDown) {
    this.button1InitialDown = button1InitialDown;
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
	 * Sets the button 2 initial down.
	 *
	 * @param button2InitialDown the new button 2 initial down
	 */
  public void setButton2InitialDown(final boolean button2InitialDown) {
    this.button2InitialDown = button2InitialDown;
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
	 * Sets the button 0 release.
	 *
	 * @param button0Release the new button 0 release
	 */
  public void setButton0Release(final boolean button0Release) {
    this.button0Release = button0Release;
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
	 * Sets the button 1 release.
	 *
	 * @param button1Release the new button 1 release
	 */
  public void setButton1Release(final boolean button1Release) {
    this.button1Release = button1Release;
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
	 * Sets the button 2 release.
	 *
	 * @param button2Release the new button 2 release
	 */
  public void setButton2Release(final boolean button2Release) {
    this.button2Release = button2Release;
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
        "mouseX=" + mouseX + ", " +
            "mouseY=" + mouseY + ", " +
            "mouseWheel=" + mouseWheel + ", " +
            "b0Down=" + button0Down + ", " +
            "b1Down=" + button1Down + ", " +
            "b2Down=" + button2Down + ", " +
            "b0InitialDown=" + button0InitialDown + ", " +
            "b1InitialDown=" + button1InitialDown + ", " +
            "b2InitialDown=" + button2InitialDown + ", " +
            "b0Release=" + button0Release + ", " +
            "b1Release=" + button1Release + ", " +
            "b2Release=" + button2Release;
  }
}
