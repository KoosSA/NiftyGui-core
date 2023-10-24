package de.lessvoid.nifty.input.mouse;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.input.NiftyMouseInputEvent;


/**
 * The MouseInputEventProcessor keeps track of mouse event state.
 *
 * @author void
 */
public class MouseInputEventProcessor {
  
  /** The last mouse X. */
  private int lastMouseX = 0;
  
  /** The last mouse Y. */
  private int lastMouseY = 0;
  
  /** The last mouse wheel. */
  private int lastMouseWheel = 0;
  
  /** The last button down 0. */
  private boolean lastButtonDown0 = false;
  
  /** The last button down 1. */
  private boolean lastButtonDown1 = false;
  
  /** The last button down 2. */
  private boolean lastButtonDown2 = false;
  
  /** The had any events. */
  private boolean hadAnyEvents = false;

  /**
	 * Reset.
	 */
  public void reset() {
    lastButtonDown0 = false;
    lastButtonDown1 = false;
    lastButtonDown2 = false;
  }

  /**
	 * Begin.
	 */
  public void begin() {
    hadAnyEvents = false;
  }

  /**
	 * Process.
	 *
	 * @param mouse the mouse
	 */
  public void process(@Nonnull final NiftyMouseInputEvent mouse) {
    hadAnyEvents = true;
    mouse.setButton0InitialDown(!lastButtonDown0 && mouse.isButton0Down());
    mouse.setButton0Release(lastButtonDown0 && !mouse.isButton0Down());
    mouse.setButton1InitialDown(!lastButtonDown1 && mouse.isButton1Down());
    mouse.setButton1Release(lastButtonDown1 && !mouse.isButton1Down());
    mouse.setButton2InitialDown(!lastButtonDown2 && mouse.isButton2Down());
    mouse.setButton2Release(lastButtonDown2 && !mouse.isButton2Down());
    lastMouseX = mouse.getMouseX();
    lastMouseY = mouse.getMouseY();
    lastMouseWheel = mouse.getMouseWheel();
    lastButtonDown0 = mouse.isButton0Down();
    lastButtonDown1 = mouse.isButton1Down();
    lastButtonDown2 = mouse.isButton2Down();
  }

  /**
	 * Checks for last mouse down event.
	 *
	 * @return true, if successful
	 */
  public boolean hasLastMouseDownEvent() {
    return !hadAnyEvents && (lastButtonDown0 || lastButtonDown1 || lastButtonDown2);
  }

  /**
	 * Gets the last mouse down event.
	 *
	 * @return the last mouse down event
	 */
  @Nonnull
  public NiftyMouseInputEvent getLastMouseDownEvent() {
    NiftyMouseInputEvent result = new NiftyMouseInputEvent();
    result.initialize(lastMouseX, lastMouseY, lastMouseWheel, lastButtonDown0, lastButtonDown1, lastButtonDown2);
    return result;
  }
}
