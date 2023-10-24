package de.lessvoid.nifty.builder;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.PopupCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.PopupType;

/**
 * The Class PopupBuilder.
 */
public class PopupBuilder extends ElementBuilder {

  /**
	 * Instantiates a new popup builder.
	 */
  public PopupBuilder() {
    super(new PopupCreator());
  }

  /**
	 * Instantiates a new popup builder.
	 *
	 * @param id the id
	 */
  public PopupBuilder(@Nonnull final String id) {
    this();
    this.id(id);
  }

  /**
	 * Builds the.
	 *
	 * @param parent the parent
	 * @return the element
	 */
  @Override
  @Nonnull
  public Element build(@Nonnull final Element parent) {
    throw new RuntimeException("you can't build popups using the PopupBuilder. Please call register() instead to " +
        "dynamically register popups with Nifty.");
  }

  /**
	 * Builds the.
	 *
	 * @param parent the parent
	 * @param index  the index
	 * @return the element
	 */
  @Override
  public Element build(@Nonnull final Element parent, final int index) {
    throw new RuntimeException("you can't build popups using the PopupBuilder. Please call register() instead to " +
        "dynamically register popups with Nifty.");
  }

  /**
	 * Register popup.
	 *
	 * @param nifty the nifty
	 */
  public void registerPopup(@Nonnull final Nifty nifty) {
    PopupType popupType = (PopupType) buildElementType();
    if (popupType != null) {
      nifty.registerPopup(popupType);
    }
  }
}
