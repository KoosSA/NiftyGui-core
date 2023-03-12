package de.lessvoid.nifty.screen;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Action;

public class EndOfScreenAction implements Action {
  @Nonnull
  private final Screen screen;

  public EndOfScreenAction(@Nonnull final Screen screen) {
    this.screen = screen;
  }

  @Override
  public void perform() {
    screen.onEndScreenHasEnded();
  }
}