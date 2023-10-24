package de.lessvoid.nifty.builder;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;

/**
 * The Class ControlBuilder.
 */
public class ControlBuilder extends ElementBuilder {
  
  /** The creator. */
  @Nonnull
  private final CustomControlCreator creator;

  /**
	 * Instantiates a new control builder.
	 *
	 * @param creator the creator
	 */
  private ControlBuilder(@Nonnull final CustomControlCreator creator) {
    super(creator);
    this.creator = creator;
  }

  /**
	 * Instantiates a new control builder.
	 *
	 * @param name the name
	 */
  public ControlBuilder(@Nonnull final String name) {
    this(new CustomControlCreator(name));
  }

  /**
	 * Instantiates a new control builder.
	 *
	 * @param id   the id
	 * @param name the name
	 */
  public ControlBuilder(@Nonnull final String id, @Nonnull final String name) {
    this(new CustomControlCreator(id, name));
  }

  /**
	 * Parameter.
	 *
	 * @param name  the name
	 * @param value the value
	 * @return the control builder
	 */
  public ControlBuilder parameter(@Nonnull final String name, @Nonnull final String value) {
    creator.parameter(name, value);
    return this;
  }
}
