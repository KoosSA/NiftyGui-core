package de.lessvoid.nifty.builder;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.controls.dynamic.LayerCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * The Class LayerBuilder.
 */
public class LayerBuilder extends ElementBuilder {

  /**
	 * Instantiates a new layer builder.
	 */
  public LayerBuilder() {
    super(new LayerCreator());
  }

  /**
	 * Instantiates a new layer builder.
	 *
	 * @param id the id
	 */
  public LayerBuilder(@Nonnull final String id) {
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
  public Element build(@Nonnull final Element parent) {
    Element e = super.build(parent);
    Screen screen = parent.getScreen();
    screen.addLayerElement(e);
    screen.processAddAndRemoveLayerElements();
    return e;
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
    Element e = super.build(parent, index);
    Screen screen = parent.getScreen();
    screen.addLayerElement(e);
    screen.processAddAndRemoveLayerElements();
    return e;
  }
}
