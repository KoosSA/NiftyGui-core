package de.lessvoid.nifty.builder;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.controls.dynamic.TextCreator;

/**
 * The Class TextBuilder.
 */
public class TextBuilder extends ElementBuilder {
  
  /** The creator. */
  @Nonnull
  private final TextCreator creator;

  /**
	 * Instantiates a new text builder.
	 *
	 * @param creator the creator
	 */
  private TextBuilder(@Nonnull final TextCreator creator) {
    super(creator);
    this.creator = creator;
  }


  /**
	 * Instantiates a new text builder.
	 */
  public TextBuilder() {
    this(new TextCreator(""));
  }

  /**
	 * Instantiates a new text builder.
	 *
	 * @param id the id
	 */
  public TextBuilder(@Nonnull final String id) {
    this();
    this.id(id);
  }

  /**
	 * Wrap.
	 *
	 * @param wrap the wrap
	 * @return the text builder
	 */
  public TextBuilder wrap(final boolean wrap) {
    creator.setWrap(wrap);
    return this;
  }
}
