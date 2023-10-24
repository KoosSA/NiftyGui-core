package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * The Class FocusLostEvent.
 */
public class FocusLostEvent implements NiftyEvent {
  
  /** The controller. */
  private final Controller controller;
  
  /** The nifty control. */
  private final NiftyControl niftyControl;

  /**
	 * Instantiates a new focus lost event.
	 *
	 * @param controller   the controller
	 * @param niftyControl the nifty control
	 */
  public FocusLostEvent(final Controller controller, final NiftyControl niftyControl) {
    this.controller = controller;
    this.niftyControl = niftyControl;
  }

  /**
	 * Gets the controller.
	 *
	 * @return the controller
	 */
  public Controller getController() {
    return controller;
  }

  /**
	 * Gets the nifty control.
	 *
	 * @return the nifty control
	 */
  public NiftyControl getNiftyControl() {
    return niftyControl;
  }
}
