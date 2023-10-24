package de.lessvoid.nifty.controls.dynamic;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.LayerType;
import de.lessvoid.nifty.screen.Screen;

/**
 * The Class LayerCreator.
 */
public class LayerCreator extends ControlAttributes {
  
  /**
	 * Instantiates a new layer creator.
	 */
  public LayerCreator() {
    setAutoId();
  }

  /**
	 * Instantiates a new layer creator.
	 *
	 * @param id the id
	 */
  public LayerCreator(@Nonnull final String id) {
    setId(id);
  }

  /**
	 * Creates the.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 * @param parent the parent
	 * @return the element
	 */
  @Nonnull
  public Element create(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    Element layerElement = createLayer(nifty, screen, parent);
    screen.addLayerElement(layerElement);
    screen.processAddAndRemoveLayerElements();
    // if this startControl is called with a screen that is already running (which means
    // that the onStartScreen Event has been called already before) we have to call
    // onStartScreen on the newControl here manually. It won't be called by the screen
    // anymore.
    if (screen.isBound()) {
      layerElement.bindControls(screen);
      layerElement.initControls(false);
    }
    if (screen.isRunning()) {
      layerElement.startEffect(EffectEventId.onStartScreen);
      layerElement.startEffect(EffectEventId.onActive);
      layerElement.onStartScreen();
    }
    return layerElement;
  }

  /**
	 * Creates the type.
	 *
	 * @return the element type
	 */
  @Nonnull
  @Override
  public ElementType createType() {
    return new LayerType(getAttributes());
  }
}
