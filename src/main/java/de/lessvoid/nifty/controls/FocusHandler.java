package de.lessvoid.nifty.controls;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * FocusHandler.
 *
 * @author void
 */
public class FocusHandler {
  
  /** The Constant log. */
  private static final Logger log = Logger.getLogger(FocusHandler.class.getName());

  /** The entries. */
  @Nonnull
  private final ArrayList<Element> entries = new ArrayList<Element>();
  
  /** The element buffer. */
  @Nonnull
  private final ArrayList<ArrayList<Element>> elementBuffer = new ArrayList<ArrayList<Element>>();

  /** The mouse focus element. */
  @Nullable
  private Element mouseFocusElement;
  
  /** The mouse focus element buffer. */
  @Nonnull
  private final ArrayList<Element> mouseFocusElementBuffer = new ArrayList<Element>();

  /** The keyboard focus element. */
  @Nullable
  private Element keyboardFocusElement;
  
  /** The keyboard focus element buffer. */
  @Nonnull
  private final ArrayList<Element> keyboardFocusElementBuffer = new ArrayList<Element>();

  /**
	 * Instantiates a new focus handler.
	 */
  public FocusHandler() {
    mouseFocusElement = null;
    keyboardFocusElement = null;
  }

  /**
   * add the given element to the focushandler (as the last element).
   *
   * @param element element to add
   */
  public void addElement(final Element element) {
    addElement(element, null);
  }

  /**
   * add an element to the focus handler.
   *
   * @param element                      element to add
   * @param focusableInsertBeforeElement the element before which to add the new element
   */
  public void addElement(final Element element, @Nullable final Element focusableInsertBeforeElement) {
    if (focusableInsertBeforeElement == null) {
      entries.add(element);
    } else {
      int idx = entries.indexOf(focusableInsertBeforeElement);
      if (idx == -1) {
        log.warning("requesting to add focusable element before [" + focusableInsertBeforeElement + "] but I can't " +
            "find it on the current screen. Adding it to the end of the list (like in the regular case)");
        entries.add(element);
      } else {
        entries.add(idx, focusableInsertBeforeElement);
      }
    }
  }

  /**
	 * add an element to the focus handler after an existing element already added
	 * to it.
	 *
	 * @param existingElement element that already exists in the focushandler
	 * @param element         new element to add
	 */
  public void addElementAfter(final Element existingElement, final Element element) {
    int idx = entries.indexOf(existingElement);
    if (idx == -1) {
      log.warning("requesting to add focusable element after [" + existingElement + "] but I can't find it on the " +
          "current screen. Adding it to the end of the list (like in the regular case)");
      entries.add(element);
    } else {
      if (idx == entries.size() - 1) {
        entries.add(element);
      } else {
        entries.add(idx + 1, element);
      }
    }
  }

  /**
   * get next element.
   *
   * @param current current element
   * @return next element
   */
  @Nonnull
  public Element getNext(@Nonnull final Element current) {
    if (entries.isEmpty()) {
      return current;
    }

    int index = entries.indexOf(keyboardFocusElement);
    if (index == -1) {
      return current;
    }

    while (true) {
      index++;
      if (index >= entries.size()) {
        index = 0;
      }
      Element nextElement = entries.get(index);
      if (nextElement == current) {
        return current;
      }
      if (nextElement.isFocusable()) {
        return nextElement;
      }
    }
  }

  /**
   * get prev element.
   *
   * @param current current element
   * @return prev element
   */
  public Element getPrev(final Element current) {
    if (entries.isEmpty()) {
      return current;
    }

    int index = entries.indexOf(keyboardFocusElement);
    if (index == -1) {
      return current;
    }

    while (true) {
      index--;
      if (index < 0) {
        index = entries.size() - 1;
      }
      Element prevElement = entries.get(index);
      if (prevElement == current) {
        return current;
      }
      if (prevElement.isFocusable()) {
        return prevElement;
      }
    }
  }

  /**
   * remove this element.
   *
   * @param element element
   */
  public void remove(final Element element) {
    entries.remove(element);
    lostKeyboardFocus(element);
    lostMouseFocus(element);
  }

  /**
   * get first entry.
   *
   * @return first
   */
  @Nullable
  public Element getFirstFocusElement() {
    if (entries.isEmpty()) {
      return null;
    }
    for (int i = 0; i < entries.size(); i++) {
      if (entries.get(i).isFocusable()) {
        return entries.get(i);
      }
    }
    return null;
  }

  /**
   * save all states.
   */
  public void pushState() {
    ArrayList<Element> copy = new ArrayList<Element>();
    copy.addAll(entries);
    elementBuffer.add(copy);

    entries.clear();

    keyboardFocusElementBuffer.add(keyboardFocusElement);
    lostKeyboardFocus(keyboardFocusElement);

    mouseFocusElementBuffer.add(mouseFocusElement);
    lostMouseFocus(mouseFocusElement);
  }

  /**
   * restore all states.
   */
  public void popState() {
    entries.clear();
    entries.addAll(elementBuffer.get(elementBuffer.size() - 1));

    setKeyFocus(keyboardFocusElementBuffer.remove(keyboardFocusElementBuffer.size() - 1));
    mouseFocusElement = mouseFocusElementBuffer.remove(mouseFocusElementBuffer.size() - 1);

    // TODO: review this later for the moment let's just clear the mouseFocusElement
    //
    // background: at the moment a popup is opened there is a mouseFocusElement available with exclusive
    // access to the mouse. this element gets saved when the popup is opened and restored here. the mouse event
    // that informs the release of the mouse is long gone and leads to the effect that the mouse is still
    // exclusiv to this element.
    mouseFocusElement = null;
  }

  /**
	 * Reset focus elements.
	 */
  public void resetFocusElements() {
    entries.clear();
    lostKeyboardFocus(keyboardFocusElement);
    lostMouseFocus(mouseFocusElement);
  }

  /**
   * set the focus to the given element.
   *
   * @param newFocusElement new focus element
   */
  public void setKeyFocus(final Element newFocusElement) {
    if (keyboardFocusElement == newFocusElement) {
      return;
    }

    if (keyboardFocusElement != null) {
      keyboardFocusElement.stopEffect(EffectEventId.onFocus);
      keyboardFocusElement.startEffect(EffectEventId.onLostFocus);
    }

    boolean startOnGetFocus = false;
    if (keyboardFocusElement != newFocusElement) {
      startOnGetFocus = true;
    }

    keyboardFocusElement = newFocusElement;
    log.fine("keyboard focus element now changed to [" + (keyboardFocusElement == null ? "" : keyboardFocusElement
        .toString()) + "]");

    if (keyboardFocusElement != null) {
      keyboardFocusElement.startEffect(EffectEventId.onFocus);
      if (startOnGetFocus) {
        keyboardFocusElement.startEffect(EffectEventId.onGetFocus);
      }
    }
  }

  /**
	 * Lost keyboard focus.
	 *
	 * @param elementThatLostFocus the element that lost focus
	 */
  public void lostKeyboardFocus(@Nullable final Element elementThatLostFocus) {
    if (elementThatLostFocus != null) {
      log.fine("lostKeyboardFocus for [" + elementThatLostFocus.toString() + "]");
      if (keyboardFocusElement == elementThatLostFocus) {
        keyboardFocusElement.stopEffect(EffectEventId.onFocus);
        keyboardFocusElement.startEffect(EffectEventId.onLostFocus);
        keyboardFocusElement = null;
      }
    }
  }

  /**
	 * Key event.
	 *
	 * @param inputEvent the input event
	 * @return true, if successful
	 */
  public boolean keyEvent(@Nonnull final KeyboardInputEvent inputEvent) {
    if (keyboardFocusElement != null) {
      return keyboardFocusElement.keyEvent(inputEvent);
    }
    return false;
  }

  /**
	 * Request exclusive mouse focus.
	 *
	 * @param newFocusElement the new focus element
	 */
  public void requestExclusiveMouseFocus(final Element newFocusElement) {
    if (mouseFocusElement == newFocusElement) {
      return;
    }
    mouseFocusElement = newFocusElement;
    log.fine("requestExclusiveMouseFocus for [" + mouseFocusElement.toString() + "]");
  }

  /**
	 * Checks for exclusive mouse focus.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
  public boolean hasExclusiveMouseFocus(@Nonnull final Element element) {
    return element.equals(mouseFocusElement);
  }

  /**
	 * Can process mouse events.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
  public boolean canProcessMouseEvents(@Nonnull final Element element) {
    if (mouseFocusElement == null) {
      return true;
    }

    boolean canProcess = mouseFocusElement == element;
    log.fine(
        "canProcessMouseEvents for [" + element.toString() + "] ==> "
            + canProcess + " (" + mouseFocusElement.toString() + ")");
    return canProcess;
  }

  /**
	 * Lost mouse focus.
	 *
	 * @param elementThatLostFocus the element that lost focus
	 */
  public void lostMouseFocus(@Nullable final Element elementThatLostFocus) {
    if (elementThatLostFocus != null) {
      log.fine("lostMouseFocus for [" + elementThatLostFocus.toString() + "]");
      if (mouseFocusElement == elementThatLostFocus) {
        mouseFocusElement = null;
      }
    }
  }

  /**
	 * To string.
	 *
	 * @return the string
	 */
  @Override
  @Nonnull
  public String toString() {
    String mouseFocusString = "---";
    if (mouseFocusElement != null) {
      mouseFocusString = mouseFocusElement.toString();
    }

    String keyboardFocusString = "---";
    if (keyboardFocusElement != null) {
      keyboardFocusString = keyboardFocusElement.toString();
    }

    StringBuilder focusElements = new StringBuilder();
    for (int i = 0; i < entries.size(); i++) {
      Element e = entries.get(i);
      if (i > 0) {
        focusElements.append(", ");
      }
      focusElements.append(e.getId()).append(!e.isFocusable() ? "*" : "");
    }
    return
        "\n"
            + "focus element (mouse):    " + mouseFocusString + "\n"
            + "focus element (keyboard): " + keyboardFocusString + "\n"
            + "focus element size: " + entries.size() + " [" + focusElements.toString() + "]";
  }

  /**
	 * Checks for any element the keyboard focus.
	 *
	 * @return true, if successful
	 */
  public boolean hasAnyElementTheKeyboardFocus() {
    return keyboardFocusElement != null;
  }

  /**
	 * Checks for any element the mouse focus.
	 *
	 * @return true, if successful
	 */
  public boolean hasAnyElementTheMouseFocus() {
    return mouseFocusElement != null;
  }

  /**
	 * Find element.
	 *
	 * @param defaultFocusElementId the default focus element id
	 * @return the element
	 */
  @Nullable
  public Element findElement(@Nonnull final String defaultFocusElementId) {
    for (Element element : entries) {
      if (defaultFocusElementId.equals(element.getId())) {
        return element;
      }
    }
    return null;
  }

  /**
	 * Gets the keyboard focus element.
	 *
	 * @return the keyboard focus element
	 */
  @Nullable
  public Element getKeyboardFocusElement() {
    return keyboardFocusElement;
  }

  /**
	 * Gets the mouse focus element.
	 *
	 * @return the mouse focus element
	 */
  @Nullable
  public Element getMouseFocusElement() {
    return mouseFocusElement;
  }
}
