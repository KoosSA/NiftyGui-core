package de.lessvoid.nifty.controls;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * The Class AbstractController.
 */
public abstract class AbstractController implements Controller, NiftyControl {
  
  /** The Constant log. */
  private static final Logger log = Logger.getLogger(AbstractController.class.getName());
  
  /** The element. */
  @Nullable
  private Element element;
  
  /** The bound. */
  private boolean bound = false;

  /**
	 * Bind.
	 *
	 * @param element the element
	 */
  protected void bind(@Nonnull final Element element) {
    this.element = element;
  }

  /**
	 * Inits the.
	 *
	 * @param parameter the parameter
	 */
  @Override
  public void init(@Nonnull final Parameters parameter) {
    this.bound = true;
  }

  /**
	 * Gets the element.
	 *
	 * @return the element
	 */
  @Nullable
  @Override
  public Element getElement() {
    if (element == null) {
      log.warning("Requested element from controller before binding was performed.");
    }
    return element;
  }

  /**
	 * Enable.
	 */
  @Override
  public void enable() {
    setEnabled(true);
  }

  /**
	 * Disable.
	 */
  @Override
  public void disable() {
    setEnabled(false);
  }

  /**
	 * Sets the enabled.
	 *
	 * @param enabled the new enabled
	 */
  @Override
  public void setEnabled(final boolean enabled) {
    final Element element = getElement();
    if (element != null) {
      if (enabled) {
        element.enable();
      } else {
        element.disable();
      }
    }
  }

  /**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
  @Override
  public boolean isEnabled() {
    final Element element = getElement();
    return element != null && element.isEnabled();
  }

  /**
	 * Gets the id.
	 *
	 * @return the id
	 */
  @Nullable
  @Override
  public String getId() {
    final Element element = getElement();
    return element != null ? element.getId() : null;
  }

  /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
  @Override
  public void setId(@Nullable final String id) {
    final Element element = getElement();
    if (element != null) {
      element.setId(id);
    }
  }

  /**
	 * Gets the width.
	 *
	 * @return the width
	 */
  @Override
  public int getWidth() {
    final Element element = getElement();
    return element != null ? element.getWidth() : 0;
  }

  /**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
  @Override
  public void setWidth(@Nonnull final SizeValue width) {
    final Element element = getElement();
    if (element != null) {
      element.setConstraintWidth(width);
    }
  }

  /**
	 * Gets the height.
	 *
	 * @return the height
	 */
  @Override
  public int getHeight() {
    final Element element = getElement();
    return element != null ? element.getHeight() : 0;
  }

  /**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
  @Override
  public void setHeight(@Nonnull final SizeValue height) {
    final Element element = getElement();
    if (element != null) {
      element.setConstraintHeight(height);
    }
  }

  /**
	 * Gets the style.
	 *
	 * @return the style
	 */
  @Override
  public String getStyle() {
    final Element element = getElement();
    return element != null ? element.getStyle() : null;
  }

  /**
	 * Sets the style.
	 *
	 * @param style the new style
	 */
  @Override
  public void setStyle(@Nonnull final String style) {
    final Element element = getElement();
    if (element != null) {
      element.setStyle(element.getNifty().specialValuesReplace(style));
    }
  }

  /**
	 * Sets the focus.
	 */
  @Override
  public void setFocus() {
    final Element element = getElement();
    if (element != null) {
      element.setFocus();
    }
  }

  /**
	 * Sets the focusable.
	 *
	 * @param focusable the new focusable
	 */
  @Override
  public void setFocusable(final boolean focusable) {
    final Element element = getElement();
    if (element != null) {
      element.setFocusable(focusable);
    }
  }

  /**
	 * On focus.
	 *
	 * @param getFocus the get focus
	 */
  @Override
  public void onFocus(final boolean getFocus) {
    final Element element = getElement();
    if (element != null) {
      String id = element.getId();
      if (id != null) {
        if (getFocus) {
          element.getNifty().publishEvent(id, new FocusGainedEvent(this, this));
        } else {
          element.getNifty().publishEvent(id, new FocusLostEvent(this, this));
        }
      }
    }
  }

  /**
	 * Checks for focus.
	 *
	 * @return true, if successful
	 */
  @Override
  public boolean hasFocus() {
    final Element element = getElement();
    if (element == null) {
      return false;
    }
    return element == element.getFocusHandler().getKeyboardFocusElement();
  }

  /**
	 * Layout callback.
	 */
  @Override
  public void layoutCallback() {
  }

  /**
	 * Checks if is bound.
	 *
	 * @return true, if is bound
	 */
  @Override
  public boolean isBound() {
    return bound;
  }

  /**
	 * On end screen.
	 */
  @Override
  public void onEndScreen() {
  }

}
