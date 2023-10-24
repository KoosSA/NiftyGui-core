package de.lessvoid.nifty.screen;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;

/**
 * The DefaultScreenController is attached to a screen when no ScreenController was specified.
 * It does nothing at the moment.
 */
public class DefaultScreenController implements ScreenController {
  
  /** The nifty. */
  protected Nifty nifty;

  /**
	 * Bind.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 */
  @Override
  public void bind(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
    this.nifty = nifty;
  }

  /**
	 * On start screen.
	 */
  @Override
  public void onStartScreen() {
  }

  /**
	 * On end screen.
	 */
  @Override
  public void onEndScreen() {
  }

  /**
	 * Goto screen.
	 *
	 * @param screenId the screen id
	 */
  public void gotoScreen(@Nonnull final String screenId) {
    nifty.gotoScreen(screenId);
  }
}
