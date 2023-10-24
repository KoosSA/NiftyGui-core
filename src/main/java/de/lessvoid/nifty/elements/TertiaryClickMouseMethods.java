package de.lessvoid.nifty.elements;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryClickedMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryMultiClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryReleaseEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class TertiaryClickMouseMethods.
 */
public class TertiaryClickMouseMethods extends MouseClickMethods {
  
  /**
	 * Instantiates a new tertiary click mouse methods.
	 *
	 * @param element the element
	 */
  public TertiaryClickMouseMethods(@Nonnull final Element element) {
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
      nifty.publishEvent(id, new NiftyMouseTertiaryClickedEvent(element, inputEvent));
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
      nifty.publishEvent(id, new NiftyMouseTertiaryMultiClickedEvent(element, inputEvent,clickCount));
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
      nifty.publishEvent(id, new NiftyMouseTertiaryClickedMovedEvent(element, inputEvent));
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
      nifty.publishEvent(id, new NiftyMouseTertiaryReleaseEvent(element, mouseEvent));
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
      nifty.publishEvent(id, new NiftyMouseTertiaryClickedEvent(element));
      nifty.publishEvent(id, new NiftyMouseTertiaryReleaseEvent(element));
    }
    super.clickAndRelease(nifty);
  }
}
