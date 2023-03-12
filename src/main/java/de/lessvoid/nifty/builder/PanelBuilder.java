package de.lessvoid.nifty.builder;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.controls.dynamic.PanelCreator;

public class PanelBuilder extends ElementBuilder {

  public PanelBuilder() {
    super(new PanelCreator());
  }

  public PanelBuilder(@Nonnull final String id) {
    this();
    this.id(id);
  }
}
