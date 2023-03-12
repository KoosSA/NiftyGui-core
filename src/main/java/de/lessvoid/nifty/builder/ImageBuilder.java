package de.lessvoid.nifty.builder;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.controls.dynamic.ImageCreator;

public class ImageBuilder extends ElementBuilder {

  public ImageBuilder() {
    super(new ImageCreator());
  }

  public ImageBuilder(@Nonnull final String id) {
    this();
    this.id(id);
  }
}
