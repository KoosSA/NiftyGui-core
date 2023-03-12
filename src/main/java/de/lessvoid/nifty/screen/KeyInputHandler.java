package de.lessvoid.nifty.screen;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.input.NiftyInputEvent;

/**
 * KeyInputHandler.
 *
 * @author void
 */
public interface KeyInputHandler {

  /**
   * handle a key event.
   *
   * @param inputEvent key event to handle
   * @return true when the event has been consumend and false if not
   */
  boolean keyEvent(@Nonnull NiftyInputEvent inputEvent);
}
