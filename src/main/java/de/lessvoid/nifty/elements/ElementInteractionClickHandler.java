package de.lessvoid.nifty.elements;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class ElementInteractionClickHandler.
 */
public class ElementInteractionClickHandler {
  
  /** The Constant logger. */
  private static final Logger logger = Logger.getLogger(ElementInteractionClickHandler.class.getName());

  /** The Constant REPEATED_CLICK_START_TIME_MS. */
  private static final long REPEATED_CLICK_START_TIME_MS = 100;
  
  /** The Constant REPEATED_CLICK_TIME_MS. */
  private static final long REPEATED_CLICK_TIME_MS = 100;
  
  /** The Constant CLICK_COUNT_RECORD_TIME_MS. */
  private static final int CLICK_COUNT_RECORD_TIME_MS = 500;

  /** The nifty. */
  private final Nifty nifty;
  
  /** The element. */
  private final Element element;
  
  /** The mouse methods. */
  private final MouseClickMethods mouseMethods;
  
  /** The is mouse down. */
  private boolean isMouseDown;
  
  /** The on click repeat enabled. */
  private boolean onClickRepeatEnabled;
  
  /** The last mouse X. */
  private int lastMouseX;
  
  /** The last mouse Y. */
  private int lastMouseY;
  
  /** The delta time ms. */
  private long deltaTimeMs;
  
  /** The mouse down time ms. */
  private long mouseDownTimeMs;
  
  /** The last click time ms. */
  private long lastClickTimeMs;
  
  /** The last repeat start time ms. */
  private long lastRepeatStartTimeMs;
  
  /** The click counter. */
  private int clickCounter;

  /**
	 * Instantiates a new element interaction click handler.
	 *
	 * @param nifty        the nifty
	 * @param element      the element
	 * @param mouseMethods the mouse methods
	 */
  public ElementInteractionClickHandler(
      final Nifty nifty,
      final Element element,
      final MouseClickMethods mouseMethods) {
    this.nifty = nifty;
    this.element = element;
    this.mouseMethods = mouseMethods;
    this.clickCounter = 1;
    setMouseDown(false, 0);
  }

  /**
	 * Gets the mouse methods.
	 *
	 * @return the mouse methods
	 */
  public MouseClickMethods getMouseMethods() {
    return mouseMethods;
  }

  /**
	 * Sets the on click repeat enabled.
	 *
	 * @param onClickRepeatEnabled the new on click repeat enabled
	 */
  public void setOnClickRepeatEnabled(final boolean onClickRepeatEnabled) {
    this.onClickRepeatEnabled = onClickRepeatEnabled;
  }

  /**
	 * Checks if is on click repeat enabled.
	 *
	 * @return true, if is on click repeat enabled
	 */
  public boolean isOnClickRepeatEnabled() {
    return onClickRepeatEnabled;
  }

  /**
	 * Process.
	 *
	 * @param mouseEvent           the mouse event
	 * @param isButtonDown         the is button down
	 * @param isInitialButtonDown  the is initial button down
	 * @param isButtonRelease      the is button release
	 * @param eventTimeMs          the event time ms
	 * @param mouseInside          the mouse inside
	 * @param canHandleInteraction the can handle interaction
	 * @param hasMouseAccess       the has mouse access
	 * @param onClickAlternateKey  the on click alternate key
	 * @return true, if successful
	 */
  public boolean process(
      @Nonnull final NiftyMouseInputEvent mouseEvent,
      final boolean isButtonDown,
      final boolean isInitialButtonDown,
      final boolean isButtonRelease,
      final long eventTimeMs,
      final boolean mouseInside,
      final boolean canHandleInteraction,
      final boolean hasMouseAccess,
      final String onClickAlternateKey) {
    if (onClickRepeatEnabled) {
      if (mouseInside && isMouseDown && isButtonDown) {
        long deltaTimeMs = eventTimeMs - mouseDownTimeMs;
        if (deltaTimeMs > REPEATED_CLICK_START_TIME_MS) {
          long pastTimeMs = deltaTimeMs - REPEATED_CLICK_START_TIME_MS;
          long repeatTimeMs = pastTimeMs - lastRepeatStartTimeMs;
          if (repeatTimeMs > REPEATED_CLICK_TIME_MS) {
            lastRepeatStartTimeMs = pastTimeMs;
            if (onClickMouse(element.getId(), mouseEvent, canHandleInteraction, onClickAlternateKey)) {
              return true;
            }
          }
        }
      }
    }
    boolean processed = false;
    if (mouseInside && !isMouseDown) {
        if (isButtonDown && isInitialButtonDown) {
            deltaTimeMs += eventTimeMs - lastClickTimeMs;
            setMouseDown(true, eventTimeMs);
            if (deltaTimeMs > calculateMulticlickThresholdTimeMs()) {
                if (logger.isLoggable(Level.FINE)) {
                  logger.fine("eventTimeMs: " + eventTimeMs + ", "
                            + "lastClickTimeMs: " + lastClickTimeMs + ", "
                            + "deltaTimeMs: " + deltaTimeMs + " => INITIAL CLICK");
                }
                lastClickTimeMs = eventTimeMs;
                deltaTimeMs = 0;
                clickCounter = 1;
                onInitialClick();
                onClickMouse(element.getId(), mouseEvent, canHandleInteraction, onClickAlternateKey);
            } else {
                clickCounter++;
                if (logger.isLoggable(Level.FINE)) {
                  logger.fine("eventTimeMs: " + eventTimeMs + ", "
                            + "lastClickTimeMs: " + lastClickTimeMs + ", "
                            + "deltaTimeMs: " + deltaTimeMs + " => MULTI CLICK: " + clickCounter);
                }
                onMultiClickMouse(element.getId(), mouseEvent, canHandleInteraction, onClickAlternateKey);
            }
            processed = true;
        }
    } else if (!isButtonDown && isMouseDown) {
      setMouseDown(false, eventTimeMs);
    }
    if (isButtonRelease) {
      if (mouseInside || hasMouseAccess) {
        onMouseRelease(mouseEvent);
        processed = true;
      }
    }
    if (isMouseDown) {
      onClickMouseMove(mouseEvent);
      processed = true;
    }
    return processed;
  }

  /**
	 * Sets the mouse down.
	 *
	 * @param newMouseDown the new mouse down
	 * @param eventTimeMs  the event time ms
	 */
  private void setMouseDown(final boolean newMouseDown, final long eventTimeMs) {
    this.mouseDownTimeMs = eventTimeMs;
    this.lastRepeatStartTimeMs = 0;
    this.isMouseDown = newMouseDown;
  }

  /**
	 * Reset mouse down.
	 */
  public void resetMouseDown() {
    this.isMouseDown = false;
  }

  /**
	 * On initial click.
	 */
  private void onInitialClick() {
    mouseMethods.onInitialClick();
  }

  /**
	 * On click mouse.
	 *
	 * @param elementId            the element id
	 * @param inputEvent           the input event
	 * @param canHandleInteraction the can handle interaction
	 * @param onClickAlternateKey  the on click alternate key
	 * @return true, if successful
	 */
  private boolean onClickMouse(
      final String elementId,
      @Nonnull final NiftyMouseInputEvent inputEvent,
      final boolean canHandleInteraction,
      final String onClickAlternateKey) {
    if (!canHandleInteraction) {
      return false;
    }

    lastMouseX = inputEvent.getMouseX();
    lastMouseY = inputEvent.getMouseY();

    return mouseMethods.onClick(nifty, onClickAlternateKey, inputEvent);
  }

  /**
	 * On multi click mouse.
	 *
	 * @param elementId            the element id
	 * @param inputEvent           the input event
	 * @param canHandleInteraction the can handle interaction
	 * @param onClickAlternateKey  the on click alternate key
	 * @return true, if successful
	 */
  private boolean onMultiClickMouse(
          final String elementId,
          @Nonnull final NiftyMouseInputEvent inputEvent,
          final boolean canHandleInteraction,
          final String onClickAlternateKey) {
    if (!canHandleInteraction) {
      return false;
    }

    lastMouseX = inputEvent.getMouseX();
    lastMouseY = inputEvent.getMouseY();

    return mouseMethods.onMultiClick(nifty, onClickAlternateKey, inputEvent, clickCounter);
  }

  /**
	 * On click mouse move.
	 *
	 * @param inputEvent the input event
	 * @return true, if successful
	 */
  private boolean onClickMouseMove(@Nonnull final NiftyMouseInputEvent inputEvent) {
    if (lastMouseX == inputEvent.getMouseX() && lastMouseY == inputEvent.getMouseY()) {
      return false;
    }

    lastMouseX = inputEvent.getMouseX();
    lastMouseY = inputEvent.getMouseY();

    return mouseMethods.onClickMouseMove(nifty, inputEvent);
  }

  /**
	 * On mouse release.
	 *
	 * @param mouseEvent the mouse event
	 * @return true, if successful
	 */
  private boolean onMouseRelease(@Nonnull final NiftyMouseInputEvent mouseEvent) {
    return mouseMethods.onRelease(nifty, mouseEvent);
  }

  /**
	 * Click and release mouse.
	 *
	 * @param nifty the nifty
	 */
  public void clickAndReleaseMouse(@Nonnull final Nifty nifty) {
    element.startEffectWithoutChildren(EffectEventId.onClick);
    mouseMethods.clickAndRelease(nifty);
  }

  /**
	 * Sets the on click method.
	 *
	 * @param onClickMethod the new on click method
	 */
  public void setOnClickMethod(final NiftyMethodInvoker onClickMethod) {
    mouseMethods.setOnClickMethod(onClickMethod);
  }
  
  /**
	 * Sets the on multi click method.
	 *
	 * @param onMultiClickMethod the new on multi click method
	 */
  public void setOnMultiClickMethod(final NiftyMethodInvoker onMultiClickMethod) {
    mouseMethods.setMultiClickMethod(onMultiClickMethod);
  }

  /**
	 * Sets the on click mouse move method.
	 *
	 * @param onClickMouseMoveMethod the new on click mouse move method
	 */
  public void setOnClickMouseMoveMethod(final NiftyMethodInvoker onClickMouseMoveMethod) {
    mouseMethods.setOnClickMouseMoveMethod(onClickMouseMoveMethod);
  }

  /**
	 * Sets the on release method.
	 *
	 * @param onReleaseMethod the new on release method
	 */
  public void setOnReleaseMethod(final NiftyMethodInvoker onReleaseMethod) {
    mouseMethods.setOnReleaseMethod(onReleaseMethod);
  }

  /**
   * Calculates the multiclick threshold, which is the amount of time in milliseconds between two consecutive mouse
   * clicks that determines whether or not they comprise a multiclick. Two consecutive clicks must be less than or
   * equal to the multiclick threshold to be considered a multiclick.
   *
   * @return Either: 1) the value of the global property MULTI_CLICK_TIME,
   * if it is set & the property is a valid {@link Integer},
   * otherwise 2) a default value specified by {@link #CLICK_COUNT_RECORD_TIME_MS}.
   */
  private int calculateMulticlickThresholdTimeMs() {
    Properties globalProperties = nifty.getGlobalProperties();

    if (globalProperties == null) {
      return ElementInteractionClickHandler.CLICK_COUNT_RECORD_TIME_MS;
    }

    String threshold = globalProperties.getProperty("MULTI_CLICK_TIME");

    try {
      return Integer.parseInt(threshold);
    } catch (NumberFormatException e) {
      logger.warning ("Invalid value for global property \"MULTI_CLICK_TIME\": " + threshold +
              " (ms). Falling back to default value of " + ElementInteractionClickHandler.CLICK_COUNT_RECORD_TIME_MS +
              " (ms).");
      return ElementInteractionClickHandler.CLICK_COUNT_RECORD_TIME_MS;
    }
  }
}
