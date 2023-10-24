package de.lessvoid.nifty.controls;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;

/**
 * The Class DefaultController.
 */
public class DefaultController implements Controller {
  
  /** The element. */
  private Element element;
  
  /** The next prev helper. */
  private NextPrevHelper nextPrevHelper;

  /**
	 * Bind.
	 *
	 * @param nifty     the nifty
	 * @param screen    the screen
	 * @param element   the element
	 * @param parameter the parameter
	 */
  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    this.element = element;
    FocusHandler focusHandler = screen.getFocusHandler();
    nextPrevHelper = new NextPrevHelper(element, focusHandler);
  }

  /**
	 * Inits the.
	 *
	 * @param parameter the parameter
	 */
  @Override
  public void init(@Nonnull final Parameters parameter) {
  }

  /**
	 * On start screen.
	 */
  @Override
  public void onStartScreen() {
  }

  /**
	 * Input event.
	 *
	 * @param inputEvent the input event
	 * @return true, if successful
	 */
  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (nextPrevHelper.handleNextPrev(inputEvent)) {
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
      element.onClickAndReleasePrimaryMouseButton();
      return true;
    }
    return false;
  }

  /**
	 * On focus.
	 *
	 * @param getFocus the get focus
	 */
  @Override
  public void onFocus(final boolean getFocus) {
  }

  /**
	 * On end screen.
	 */
  @Override
  public void onEndScreen() {
  }
}