package de.lessvoid.nifty.elements;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.EndNotify;

public class EndOfFrameElementAction {
  @Nonnull
  private final Action action;
  @Nullable
  private final EndNotify endNotify;

  public EndOfFrameElementAction(
      @Nonnull final Action action,
      @Nullable final EndNotify endNotify) {
    this.action = action;
    this.endNotify = endNotify;
  }

  public void perform() {
    action.perform();
    if (endNotify != null) {
      endNotify.perform();
    }
  }
}