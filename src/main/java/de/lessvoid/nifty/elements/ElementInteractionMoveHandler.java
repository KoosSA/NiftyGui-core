package de.lessvoid.nifty.elements;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.events.NiftyMouseEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseWheelEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * The Class ElementInteractionMoveHandler.
 */
public class ElementInteractionMoveHandler {
  
  /** The nifty. */
  @Nonnull
  private final Nifty nifty;
  
  /** The element. */
  @Nonnull
  private final Element element;
  
  /** The last mouse X. */
  private int lastMouseX;
  
  /** The last mouse Y. */
  private int lastMouseY;
  
  /** The last button 0 down. */
  private boolean lastButton0Down;
  
  /** The last button 1 down. */
  private boolean lastButton1Down;
  
  /** The last button 2 down. */
  private boolean lastButton2Down;

  /**
	 * Instantiates a new element interaction move handler.
	 *
	 * @param nifty   the nifty
	 * @param element the element
	 */
  public ElementInteractionMoveHandler(@Nonnull final Nifty nifty, @Nonnull final Element element) {
    this.nifty = nifty;
    this.element = element;
    this.lastMouseX = 0;
    this.lastMouseY = 0;
    this.lastButton0Down = false;
    this.lastButton1Down = false;
    this.lastButton2Down = false;
  }

  /**
	 * Process.
	 *
	 * @param canHandleInteraction the can handle interaction
	 * @param mouseInside          the mouse inside
	 * @param hasMouseAccess       the has mouse access
	 * @param mouseEvent           the mouse event
	 * @return true, if successful
	 */
  public boolean process(
      final boolean canHandleInteraction, final boolean mouseInside, final boolean hasMouseAccess,
      @Nonnull final NiftyMouseInputEvent mouseEvent) {
    if (canHandleInteraction && mouseInside) {
      final boolean moved = handleMoveEvent(mouseEvent);
      final boolean wheel = handleWheelEvent(mouseEvent);
      if (moved || wheel) {
        handleGeneralEvent(mouseEvent);
        return true;
      }

      boolean generateEvent = false;
      if (mouseEvent.isButton0Down() != lastButton0Down) {
        lastButton0Down = mouseEvent.isButton0Down();
        generateEvent = true;
      }
      if (mouseEvent.isButton1Down() != lastButton1Down) {
        lastButton1Down = mouseEvent.isButton1Down();
        generateEvent = true;
      }
      if (mouseEvent.isButton2Down() != lastButton2Down) {
        lastButton2Down = mouseEvent.isButton2Down();
        generateEvent = true;
      }
      if (generateEvent) {
        handleGeneralEvent(mouseEvent);
        return true;
      }
    }

    return false;
  }

  /**
	 * Handle move event.
	 *
	 * @param mouseEvent the mouse event
	 * @return true, if successful
	 */
  private boolean handleMoveEvent(@Nonnull final NiftyMouseInputEvent mouseEvent) {
    String id = element.getId();
    if (id != null && ((mouseEvent.getMouseX() != lastMouseX) || (mouseEvent.getMouseY() != lastMouseY))) {
      lastMouseX = mouseEvent.getMouseX();
      lastMouseY = mouseEvent.getMouseY();
      nifty.publishEvent(id, new NiftyMouseMovedEvent(element, mouseEvent));
      return true;
    }
    return false;
  }

  /**
	 * Handle wheel event.
	 *
	 * @param mouseEvent the mouse event
	 * @return true, if successful
	 */
  private boolean handleWheelEvent(@Nonnull final NiftyMouseInputEvent mouseEvent) {
    String id = element.getId();
    if (id != null && mouseEvent.getMouseWheel() != 0) {
      nifty.publishEvent(id, new NiftyMouseWheelEvent(element, mouseEvent));
      return true;
    }
    return false;
  }

  /**
	 * Handle general event.
	 *
	 * @param mouseEvent the mouse event
	 */
  private void handleGeneralEvent(@Nonnull final NiftyMouseInputEvent mouseEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseEvent(element, mouseEvent));
    }
  }
}
