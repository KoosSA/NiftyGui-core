package de.lessvoid.nifty.builder;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.controls.dynamic.ImageCreator;

/**
 * The Class ImageBuilder.
 */
public class ImageBuilder extends ElementBuilder {

  /**
	 * Instantiates a new image builder.
	 */
  public ImageBuilder() {
    super(new ImageCreator());
  }

  /**
	 * Instantiates a new image builder.
	 *
	 * @param id the id
	 */
  public ImageBuilder(@Nonnull final String id) {
    this();
    this.id(id);
  }
}
