package de.lessvoid.nifty.controls;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;

/**
 * The Class NextPrevHelper.
 */
public class NextPrevHelper {
  
  /** The element. */
  @Nonnull
  private final Element element;
  
  /** The focus handler. */
  @Nonnull
  private final FocusHandler focusHandler;

  /**
	 * Instantiates a new next prev helper.
	 *
	 * @param elementParam      the element param
	 * @param focusHandlerParam the focus handler param
	 */
  public NextPrevHelper(@Nonnull final Element elementParam, @Nonnull final FocusHandler focusHandlerParam) {
    element = elementParam;
    focusHandler = focusHandlerParam;
  }

  /**
	 * Handle next prev.
	 *
	 * @param inputEvent the input event
	 * @return true, if successful
	 */
  public boolean handleNextPrev(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
      return true;
    }
    return false;
  }
}
