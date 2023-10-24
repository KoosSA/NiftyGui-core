package de.lessvoid.nifty.builder;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.controls.dynamic.PanelCreator;

/**
 * The Class PanelBuilder.
 */
public class PanelBuilder extends ElementBuilder {

  /**
	 * Instantiates a new panel builder.
	 */
  public PanelBuilder() {
    super(new PanelCreator());
  }

  /**
	 * Instantiates a new panel builder.
	 *
	 * @param id the id
	 */
  public PanelBuilder(@Nonnull final String id) {
    this();
    this.id(id);
  }
}
