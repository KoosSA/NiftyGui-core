package de.lessvoid.nifty.elements;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.screen.Screen;

/**
 * This is the action to remove a element from the screen properly.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ElementRemoveAction implements Action {
  
  /** The screen. */
  @Nonnull
  private final Screen screen;
  
  /** The removed element. */
  @Nonnull
  private final Element removedElement;

  /**
	 * Instantiates a new element remove action.
	 *
	 * @param screen         the screen
	 * @param removedElement the removed element
	 */
  public ElementRemoveAction(@Nonnull final Screen screen, @Nonnull final Element removedElement) {
    this.screen = screen;
    this.removedElement = removedElement;
  }

  /**
	 * Perform.
	 */
  @Override
  public void perform() {
    removedElement.removeFromFocusHandler();

    // we'll now resetAllEffects here when an element is removed. this was especially
    // introduced to reset all onHover effects that were used for changing the mouse cursor image.
    // without this reset the mouse cursor image might hang when elements are being removed
    // that changed the image.
    removedElement.resetAllEffects();
    removedElement.onEndScreen(screen);

    removeSingleElement(removedElement);
    if (removedElement.hasParent()) {
      removedElement.getParent().internalRemoveElement(removedElement);
      removedElement.getParent().layoutElements();
    } else {
      screen.removeLayerElement(removedElement);
    }
  }

  /**
	 * Removes the single element.
	 *
	 * @param element the element
	 */
  private void removeSingleElement(@Nonnull final Element element) {
    element.internalRemoveElementWithChildren();
  }
}