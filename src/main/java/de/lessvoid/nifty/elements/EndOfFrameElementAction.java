package de.lessvoid.nifty.elements;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.EndNotify;

/**
 * The Class EndOfFrameElementAction.
 */
public class EndOfFrameElementAction {
  
  /** The action. */
  @Nonnull
  private final Action action;
  
  /** The end notify. */
  @Nullable
  private final EndNotify endNotify;

  /**
	 * Instantiates a new end of frame element action.
	 *
	 * @param action    the action
	 * @param endNotify the end notify
	 */
  public EndOfFrameElementAction(
      @Nonnull final Action action,
      @Nullable final EndNotify endNotify) {
    this.action = action;
    this.endNotify = endNotify;
  }

  /**
	 * Perform.
	 */
  public void perform() {
    action.perform();
    if (endNotify != null) {
      endNotify.perform();
    }
  }
}