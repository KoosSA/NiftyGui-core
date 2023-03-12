package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseSecondaryClickedEvent extends NiftyMouseBaseEvent {
  public NiftyMouseSecondaryClickedEvent(final Element element) {
    super(element);
  }

  public NiftyMouseSecondaryClickedEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
