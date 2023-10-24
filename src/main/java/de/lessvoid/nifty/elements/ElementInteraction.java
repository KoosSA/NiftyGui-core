package de.lessvoid.nifty.elements;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * All ElementInteraction is handled in here.
 *
 * @author void
 */
public class ElementInteraction {
  
  /** The on click alternate key. */
  private String onClickAlternateKey;
  
  /** The on mouse over method. */
  @Nullable
  private NiftyMethodInvoker onMouseOverMethod;
  
  /** The on mouse wheel method. */
  private NiftyMethodInvoker onMouseWheelMethod;

  /** The primary. */
  @Nonnull
  private final ElementInteractionClickHandler primary;
  
  /** The secondary. */
  @Nonnull
  private final ElementInteractionClickHandler secondary;
  
  /** The tertiary. */
  @Nonnull
  private final ElementInteractionClickHandler tertiary;
  
  /** The move. */
  @Nonnull
  private final ElementInteractionMoveHandler move;

  /**
	 * Instantiates a new element interaction.
	 *
	 * @param niftyParam the nifty param
	 * @param element    the element
	 */
  public ElementInteraction(@Nonnull final Nifty niftyParam, @Nonnull final Element element) {
    primary = new ElementInteractionClickHandler(niftyParam, element, new PrimaryClickMouseMethods(element));
    secondary = new ElementInteractionClickHandler(niftyParam, element, new SecondaryClickMouseMethods(element));
    tertiary = new ElementInteractionClickHandler(niftyParam, element, new TertiaryClickMouseMethods(element));
    move = new ElementInteractionMoveHandler(niftyParam, element);
  }

  /**
	 * Reset mouse down.
	 */
  public void resetMouseDown() {
    primary.resetMouseDown();
    secondary.resetMouseDown();
    tertiary.resetMouseDown();
  }

  /**
	 * Gets the primary.
	 *
	 * @return the primary
	 */
  @Nonnull
  public ElementInteractionClickHandler getPrimary() {
    return primary;
  }

  /**
	 * Gets the secondary.
	 *
	 * @return the secondary
	 */
  @Nonnull
  public ElementInteractionClickHandler getSecondary() {
    return secondary;
  }

  /**
	 * Gets the tertiary.
	 *
	 * @return the tertiary
	 */
  @Nonnull
  public ElementInteractionClickHandler getTertiary() {
    return tertiary;
  }

  /**
	 * Sets the on mouse over.
	 *
	 * @param method the new on mouse over
	 */
  public void setOnMouseOver(@Nullable final NiftyMethodInvoker method) {
    onMouseOverMethod = method;
  }

  /**
	 * Sets the on mouse wheel method.
	 *
	 * @param method the new on mouse wheel method
	 */
  public void setOnMouseWheelMethod(final NiftyMethodInvoker method) {
    onMouseWheelMethod = method;
  }

  /**
	 * On mouse over.
	 *
	 * @param element    the element
	 * @param inputEvent the input event
	 * @return true, if successful
	 */
  public boolean onMouseOver(final Element element, final NiftyMouseInputEvent inputEvent) {
    if (onMouseOverMethod != null) {
      return onMouseOverMethod.invoke(element, inputEvent);
    }
    return false;
  }

  /**
	 * On mouse wheel.
	 *
	 * @param element    the element
	 * @param inputEvent the input event
	 * @return true, if successful
	 */
  public boolean onMouseWheel(final Element element, final NiftyMouseInputEvent inputEvent) {
    if (onMouseWheelMethod != null) {
      return onMouseWheelMethod.invoke(element, inputEvent);
    }
    return false;
  }

  /**
	 * Sets the alternate key.
	 *
	 * @param newAlternateKey the new alternate key
	 */
  public void setAlternateKey(final String newAlternateKey) {
    onClickAlternateKey = newAlternateKey;
  }

  /**
	 * Process.
	 *
	 * @param mouseEvent           the mouse event
	 * @param eventTime            the event time
	 * @param mouseInside          the mouse inside
	 * @param canHandleInteraction the can handle interaction
	 * @param hasMouseAccess       the has mouse access
	 * @return true, if successful
	 */
  public boolean process(
      @Nonnull final NiftyMouseInputEvent mouseEvent,
      final long eventTime,
      final boolean mouseInside,
      final boolean canHandleInteraction,
      final boolean hasMouseAccess) {
    final boolean moveResult = move.process(canHandleInteraction, mouseInside, hasMouseAccess, mouseEvent);
    final boolean clickResult =
        primary.process(mouseEvent, mouseEvent.isButton0Down(), mouseEvent.isButton0InitialDown(),
            mouseEvent.isButton0Release(), eventTime, mouseInside, canHandleInteraction, hasMouseAccess,
            onClickAlternateKey) ||
            secondary.process(mouseEvent, mouseEvent.isButton1Down(), mouseEvent.isButton1InitialDown(),
                mouseEvent.isButton1Release(), eventTime, mouseInside, canHandleInteraction, hasMouseAccess,
                onClickAlternateKey) ||
            tertiary.process(mouseEvent, mouseEvent.isButton2Down(), mouseEvent.isButton2InitialDown(),
                mouseEvent.isButton2Release(), eventTime, mouseInside, canHandleInteraction, hasMouseAccess,
                onClickAlternateKey);
    return moveResult || clickResult;
  }

  /**
	 * Click and release primary mouse button.
	 *
	 * @param nifty the nifty
	 */
  public void clickAndReleasePrimaryMouseButton(@Nonnull final Nifty nifty) {
    primary.clickAndReleaseMouse(nifty);
  }

  /**
	 * Click and release secondary mouse button.
	 *
	 * @param nifty the nifty
	 */
  public void clickAndReleaseSecondaryMouseButton(@Nonnull final Nifty nifty) {
    secondary.clickAndReleaseMouse(nifty);
  }

  /**
	 * Click and release tertiary mouse button.
	 *
	 * @param nifty the nifty
	 */
  public void clickAndReleaseTertiaryMouseButton(@Nonnull final Nifty nifty) {
    tertiary.clickAndReleaseMouse(nifty);
  }
}
