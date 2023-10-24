package de.lessvoid.nifty.loaderv2.types;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.helper.CollectionLogger;

/**
 * The Class NiftyControlsType.
 */
public class NiftyControlsType extends XmlBaseType {
  
  /** The control definitions. */
  @Nonnull
  private final Collection<ControlDefinitionType> controlDefinitions = new ArrayList<ControlDefinitionType>();
  
  /** The use controls. */
  @Nonnull
  private final Collection<UseControlsType> useControls = new ArrayList<UseControlsType>();
  
  /** The popups. */
  @Nonnull
  private final Collection<PopupType> popups = new ArrayList<PopupType>();
  
  /** The resource bundles. */
  @Nonnull
  private final Collection<ResourceBundleType> resourceBundles = new ArrayList<ResourceBundleType>();

  /**
	 * Adds the control definition.
	 *
	 * @param controlDefinitionType the control definition type
	 */
  public void addControlDefinition(final ControlDefinitionType controlDefinitionType) {
    controlDefinitions.add(controlDefinitionType);
  }

  /**
	 * Adds the use controls.
	 *
	 * @param useControlsType the use controls type
	 */
  public void addUseControls(final UseControlsType useControlsType) {
    useControls.add(useControlsType);
  }

  /**
	 * Adds the popup.
	 *
	 * @param popupType the popup type
	 */
  public void addPopup(final PopupType popupType) {
    popups.add(popupType);
  }

  /**
	 * Adds the resource bundle.
	 *
	 * @param resourceBundle the resource bundle
	 */
  public void addResourceBundle(final ResourceBundleType resourceBundle) {
    resourceBundles.add(resourceBundle);
  }

  /**
	 * Load controls.
	 *
	 * @param niftyLoader the nifty loader
	 * @param niftyType   the nifty type
	 * @throws Exception the exception
	 */
  public void loadControls(
      @Nonnull final NiftyLoader niftyLoader,
      @Nonnull final NiftyType niftyType) throws Exception {
    for (UseControlsType useControl : useControls) {
      useControl.loadControl(niftyLoader, niftyType);
    }
    for (ControlDefinitionType controlDefinition : controlDefinitions) {
      niftyType.addControlDefinition(controlDefinition);
    }
    for (ResourceBundleType bundle : resourceBundles) {
      niftyType.addResourceBundle(bundle);
    }
    for (PopupType popup : popups) {
      niftyType.addPopup(popup);
    }
  }

  /**
	 * Output.
	 *
	 * @return the string
	 */
  @Nonnull
  public String output() {
    int offset = 1;
    return
        "\nNifty Data:\n" + CollectionLogger.out(offset, controlDefinitions, "controlDefinitions")
            + "\n" + CollectionLogger.out(offset, useControls, "useControls");
  }
}
