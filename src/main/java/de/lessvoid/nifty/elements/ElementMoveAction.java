package de.lessvoid.nifty.elements;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * The Class ElementMoveAction.
 */
public class ElementMoveAction implements Action {
  
  /** The moved element. */
  @Nonnull
  private final Element movedElement;
  
  /** The destination element. */
  @Nonnull
  private final Element destinationElement;

  /**
	 * Instantiates a new element move action.
	 *
	 * @param movedElement       the moved element
	 * @param destinationElement the destination element
	 */
  public ElementMoveAction(
      @Nonnull final Element movedElement,
      @Nonnull final Element destinationElement) {
    this.movedElement = movedElement;
    this.destinationElement = destinationElement;
  }

  /**
	 * Perform.
	 */
  @Override
  public void perform() {
    if (movedElement.hasParent()) {
      movedElement.getParent().internalRemoveElement(movedElement);
    }
    movedElement.setParent(destinationElement);
    destinationElement.addChild(movedElement);

    // now we'll need to add elements back to the focus handler
    addToFocusHandler(movedElement);

    movedElement.getParent().layoutElements();
    destinationElement.layoutElements();
  }

  /**
	 * Adds the to focus handler.
	 *
	 * @param element the element
	 */
  private void addToFocusHandler(@Nonnull final Element element) {
    if (element.isFocusable()) {
      // currently add the element to the end of the focus handler
      //
      // this is not quite right but at the moment I don't have any idea on how
      // to find the right spot in the focus handler to insert the element into
      // (it should really be to spot where it has been removed from)
      element.getFocusHandler().addElement(element);
    }

    final List<Element> children = element.getChildren();
    final int size = children.size();
    for (int i = 0; i < size; i++) {
      addToFocusHandler(children.get(i));
    }
  }
}
