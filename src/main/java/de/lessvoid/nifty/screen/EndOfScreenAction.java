package de.lessvoid.nifty.screen;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Action;

/**
 * The Class EndOfScreenAction.
 */
public class EndOfScreenAction implements Action {
  
  /** The screen. */
  @Nonnull
  private final Screen screen;

  /**
	 * Instantiates a new end of screen action.
	 *
	 * @param screen the screen
	 */
  public EndOfScreenAction(@Nonnull final Screen screen) {
    this.screen = screen;
  }

  /**
	 * Perform.
	 */
  @Override
  public void perform() {
    screen.onEndScreenHasEnded();
  }
}