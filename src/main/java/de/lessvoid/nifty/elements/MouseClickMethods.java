package de.lessvoid.nifty.elements;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class MouseClickMethods.
 */
public abstract class MouseClickMethods {
  
  /** The on click method. */
  @Nullable
  private NiftyMethodInvoker onClickMethod;
  
  /** The on multi click method. */
  @Nullable
  private NiftyMethodInvoker onMultiClickMethod;
  
  /** The on click mouse move method. */
  @Nullable
  private NiftyMethodInvoker onClickMouseMoveMethod;
  
  /** The on release method. */
  @Nullable
  private NiftyMethodInvoker onReleaseMethod;
  
  /** The element. */
  @Nonnull
  protected final Element element;

  /**
	 * Instantiates a new mouse click methods.
	 *
	 * @param element the element
	 */
  public MouseClickMethods(@Nonnull final Element element) {
    this.element = element;
  }

  /**
	 * Sets the multi click method.
	 *
	 * @param onMultiClickMethod the new multi click method
	 */
  public void setMultiClickMethod(@Nullable NiftyMethodInvoker onMultiClickMethod){
    this.onMultiClickMethod = onMultiClickMethod;
  }

  /**
	 * Sets the on click method.
	 *
	 * @param onClickMethod the new on click method
	 */
  public void setOnClickMethod(@Nullable NiftyMethodInvoker onClickMethod) {
    this.onClickMethod = onClickMethod;
  }

  /**
	 * Sets the on click mouse move method.
	 *
	 * @param onClickMouseMoveMethod the new on click mouse move method
	 */
  public void setOnClickMouseMoveMethod(@Nullable NiftyMethodInvoker onClickMouseMoveMethod) {
    this.onClickMouseMoveMethod = onClickMouseMoveMethod;
  }

  /**
	 * Sets the on release method.
	 *
	 * @param onReleaseMethod the new on release method
	 */
  public void setOnReleaseMethod(@Nullable NiftyMethodInvoker onReleaseMethod) {
    this.onReleaseMethod = onReleaseMethod;
  }

  /**
	 * On initial click.
	 */
  public void onInitialClick() {
  }

  /**
	 * On click.
	 *
	 * @param nifty               the nifty
	 * @param onClickAlternateKey the on click alternate key
	 * @param inputEvent          the input event
	 * @return true, if successful
	 */
  public boolean onClick(
      @Nonnull final Nifty nifty,
      @Nullable final String onClickAlternateKey,
      @Nonnull final NiftyMouseInputEvent inputEvent) {
    if (onClickMethod != null) {
      nifty.setAlternateKey(onClickAlternateKey);
      return onClickMethod.invoke(inputEvent.getMouseX(), inputEvent.getMouseY());
    }
    return false;
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
  public boolean onMultiClick(
      @Nonnull final Nifty nifty,
      @Nullable final String onClickAlternateKey,
      @Nonnull final NiftyMouseInputEvent inputEvent,
      int clickCount) {
    if (onMultiClickMethod != null) {
      nifty.setAlternateKey(onClickAlternateKey);
      return onMultiClickMethod.invoke(inputEvent.getMouseX(), inputEvent.getMouseY(),clickCount);
    }
    return false;
  }

  /**
	 * On click mouse move.
	 *
	 * @param nifty      the nifty
	 * @param inputEvent the input event
	 * @return true, if successful
	 */
  public boolean onClickMouseMove(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent inputEvent) {
    if (onClickMouseMoveMethod != null) {
      return onClickMouseMoveMethod.invoke(inputEvent.getMouseX(), inputEvent.getMouseY());
    }
    return false;
  }

  /**
	 * On release.
	 *
	 * @param nifty      the nifty
	 * @param mouseEvent the mouse event
	 * @return true, if successful
	 */
  public boolean onRelease(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    if (onReleaseMethod != null) {
      return onReleaseMethod.invoke();
    }
    return false;
  }

  /**
	 * Click and release.
	 *
	 * @param nifty the nifty
	 */
  public void clickAndRelease(@Nonnull final Nifty nifty) {
    if (onClickMethod != null) {
      onClickMethod.invoke();
    }
    if (onReleaseMethod != null) {
      onReleaseMethod.invoke();
    }
  }
}
