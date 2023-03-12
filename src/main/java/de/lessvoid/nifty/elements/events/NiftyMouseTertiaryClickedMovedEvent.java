package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseTertiaryClickedMovedEvent extends NiftyMouseBaseEvent {
  public NiftyMouseTertiaryClickedMovedEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
