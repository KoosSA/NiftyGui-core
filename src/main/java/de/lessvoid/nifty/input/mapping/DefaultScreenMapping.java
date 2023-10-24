package de.lessvoid.nifty.input.mapping;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * The Class DefaultScreenMapping.
 */
public class DefaultScreenMapping implements NiftyInputMapping {

  /**
	 * Convert.
	 *
	 * @param inputEvent the input event
	 * @return the nifty standard input event
	 */
  @Override
  @Nullable
  public NiftyStandardInputEvent convert(@Nonnull final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      if (inputEvent.getKey() == KeyboardInputEvent.KEY_ESCAPE) {
        return NiftyStandardInputEvent.Escape;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_LEFT) {
        return NiftyStandardInputEvent.MoveCursorLeft;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_RIGHT) {
        return NiftyStandardInputEvent.MoveCursorRight;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_UP) {
        return NiftyStandardInputEvent.MoveCursorUp;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_DOWN) {
        return NiftyStandardInputEvent.MoveCursorDown;
      }
    }
    return null;
  }
}
