package de.lessvoid.nifty.input;

import javax.annotation.Nullable;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * NiftyInputMapping.
 *
 * @author void
 */
public interface NiftyInputMapping {

  /**
   * convert the given KeyboardInputEvent into a neutralized NiftyInputEvent.
   *
   * @param inputEvent input event
   * @return NiftInputEvent
   */
  @Nullable
  NiftyInputEvent convert(KeyboardInputEvent inputEvent);
}
