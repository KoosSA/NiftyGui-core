package de.lessvoid.nifty.builder;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.dynamic.ControlDefinitionCreator;
import de.lessvoid.nifty.loaderv2.types.ControlDefinitionType;

/**
 * The Class ControlDefinitionBuilder.
 */
public class ControlDefinitionBuilder extends ElementBuilder {
  
  /** The creator. */
  @Nonnull
  private final ControlDefinitionCreator creator;

  /**
	 * Instantiates a new control definition builder.
	 *
	 * @param creator the creator
	 */
  private ControlDefinitionBuilder(@Nonnull final ControlDefinitionCreator creator) {
    super(creator);
    this.creator = creator;
  }

  /**
	 * Instantiates a new control definition builder.
	 *
	 * @param name the name
	 */
  public ControlDefinitionBuilder(@Nonnull String name) {
    this(new ControlDefinitionCreator(name));
  }

  /**
	 * Controller.
	 *
	 * @param controller the controller
	 * @return the control definition builder
	 */
  @Override
  public ControlDefinitionBuilder controller(@Nonnull final Controller controller) {
    creator.setController(controller.getClass().getName());
    return this;
  }

  /**
	 * Controller.
	 *
	 * @param controllerClass the controller class
	 * @return the control definition builder
	 */
  @Override
  public ControlDefinitionBuilder controller(@Nonnull final String controllerClass) {
    creator.setController(controllerClass);
    return this;
  }

  /**
	 * Control parameter.
	 *
	 * @param parameterName the parameter name
	 * @return the string
	 */
  @Nonnull
  public String controlParameter(@Nonnull final String parameterName) {
    return "$" + parameterName;
  }

  /**
	 * Register control defintion.
	 *
	 * @param nifty the nifty
	 */
  public void registerControlDefintion(@Nonnull final Nifty nifty) {
    ControlDefinitionType controlDefinitionType = (ControlDefinitionType) buildElementType();
    if (controlDefinitionType != null) {
      controlDefinitionType.translateSpecialValues(nifty, null);
      controlDefinitionType.makeFlat();
      nifty.registerControlDefintion(controlDefinitionType);
    }
  }
}
