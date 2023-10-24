package de.lessvoid.nifty.elements;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryClickedMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryMultiClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryReleaseEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class SecondaryClickMouseMethods.
 */
public class SecondaryClickMouseMethods extends MouseClickMethods {
  
  /**
	 * Instantiates a new secondary click mouse methods.
	 *
	 * @param element the element
	 */
  public SecondaryClickMouseMethods(@Nonnull final Element element) {
    super(element);
  }

  /**
	 * On click.
	 *
	 * @param nifty               the nifty
	 * @param onClickAlternateKey the on click alternate key
	 * @param inputEvent          the input event
	 * @return true, if successful
	 */
  @Override
  public boolean onClick(
      @Nonnull final Nifty nifty,
      @Nullable final String onClickAlternateKey,
      @Nonnull final NiftyMouseInputEvent inputEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseSecondaryClickedEvent(element, inputEvent));
    }
    return super.onClick(nifty, onClickAlternateKey, inputEvent);
  }

  /**
	 * On multi click.
	 *
	 * @param nifty               the nifty
	 * @param onClickAlternateKey the on click alternate key
	 * @param inputEvent          the input event
	 * @param clickCount          the click count
	 * @return true, if successful
	 */
  @Override
  public boolean onMultiClick(
          @Nonnull final Nifty nifty,
          @Nullable final String onClickAlternateKey,
          @Nonnull final NiftyMouseInputEvent inputEvent,
          int clickCount) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseSecondaryMultiClickedEvent(element, inputEvent,clickCount));
    }
    element.startEffectWithoutChildren(EffectEventId.onClick);
    return super.onMultiClick(nifty, onClickAlternateKey, inputEvent, clickCount);
  }

  /**
	 * On click mouse move.
	 *
	 * @param nifty      the nifty
	 * @param inputEvent the input event
	 * @return true, if successful
	 */
  @Override
  public boolean onClickMouseMove(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent inputEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseSecondaryClickedMovedEvent(element, inputEvent));
    }
    return super.onClickMouseMove(nifty, inputEvent);
  }

  /**
	 * On release.
	 *
	 * @param nifty      the nifty
	 * @param mouseEvent the mouse event
	 * @return true, if successful
	 */
  @Override
  public boolean onRelease(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseSecondaryReleaseEvent(element, mouseEvent));
    }
    return super.onRelease(nifty, mouseEvent);
  }

  /**
	 * Click and release.
	 *
	 * @param nifty the nifty
	 */
  @Override
  public void clickAndRelease(@Nonnull final Nifty nifty) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseSecondaryClickedEvent(element));
      nifty.publishEvent(id, new NiftyMouseSecondaryReleaseEvent(element));
    }
    super.clickAndRelease(nifty);
  }
}
