package de.lessvoid.nifty.builder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyStopwatch;
import de.lessvoid.nifty.controls.dynamic.ScreenCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * The Class ScreenBuilder.
 */
public class ScreenBuilder {
  
  /** The creator. */
  private final ScreenCreator creator;
  
  /** The layer builders. */
  @Nonnull
  private final List<LayerBuilder> layerBuilders = new ArrayList<LayerBuilder>();

  /**
	 * Instantiates a new screen builder.
	 *
	 * @param id the id
	 */
  public ScreenBuilder(@Nonnull final String id) {
    creator = createScreenCreator(id);
  }

  /**
	 * Instantiates a new screen builder.
	 *
	 * @param id         the id
	 * @param controller the controller
	 */
  public ScreenBuilder(final String id, final ScreenController controller) {
    this(id);
    creator.setScreenController(controller);
  }

  /**
	 * Controller.
	 *
	 * @param controller the controller
	 * @return the screen builder
	 */
  public ScreenBuilder controller(final ScreenController controller) {
    creator.setScreenController(controller);
    return this;
  }

  /**
	 * Default focus element.
	 *
	 * @param defaultFocusElement the default focus element
	 * @return the screen builder
	 */
  public ScreenBuilder defaultFocusElement(final String defaultFocusElement) {
    creator.setDefaultFocusElement(defaultFocusElement);
    return this;
  }

  /**
	 * Input mapping.
	 *
	 * @param inputMapping the input mapping
	 * @return the screen builder
	 */
  public ScreenBuilder inputMapping(final String inputMapping) {
    creator.setInputMapping(inputMapping);
    return this;
  }

  /**
	 * Input mapping pre.
	 *
	 * @param inputMappingPre the input mapping pre
	 * @return the screen builder
	 */
  public ScreenBuilder inputMappingPre(final String inputMappingPre) {
    creator.setInputMappingPre(inputMappingPre);
    return this;
  }

  /**
	 * Layer.
	 *
	 * @param layerBuilder the layer builder
	 * @return the screen builder
	 */
  public ScreenBuilder layer(final LayerBuilder layerBuilder) {
    layerBuilders.add(layerBuilder);
    return this;
  }

  /**
	 * Builds the.
	 *
	 * @param nifty the nifty
	 * @return the screen
	 */
  @Nonnull
  public Screen build(@Nonnull final Nifty nifty) {
    NiftyStopwatch.start();
    Screen screen = creator.create(nifty);

    Element screenRootElement = screen.getRootElement();
    for (LayerBuilder layerBuilder : layerBuilders) {
      layerBuilder.build(nifty, screen, screenRootElement);
    }

    NiftyStopwatch.stop("ScreenBuilder.build ()");
    return screen;
  }

  /**
	 * Creates the screen creator.
	 *
	 * @param id the id
	 * @return the screen creator
	 */
  ScreenCreator createScreenCreator(@Nonnull final String id) {
    return new ScreenCreator(id);
  }
}
