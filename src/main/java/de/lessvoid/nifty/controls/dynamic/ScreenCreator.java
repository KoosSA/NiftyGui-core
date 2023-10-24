package de.lessvoid.nifty.controls.dynamic;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.xml.tools.ClassHelper;

/**
 * The Class ScreenCreator.
 */
public class ScreenCreator {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(ScreenCreator.class.getName());

  /** The id. */
  @Nonnull
  private final String id;
  
  /** The screen controller. */
  @Nullable
  private ScreenController screenController;
  
  /** The default focus element. */
  @Nullable
  private String defaultFocusElement;
  
  /** The input mapping. */
  @Nullable
  private String inputMapping;
  
  /** The input mapping pre. */
  @Nullable
  private String inputMappingPre;

  /**
	 * Instantiates a new screen creator.
	 *
	 * @param id the id
	 */
  public ScreenCreator(@Nonnull final String id) {
    this(id, null);
  }

  /**
	 * Instantiates a new screen creator.
	 *
	 * @param id               the id
	 * @param screenController the screen controller
	 */
  public ScreenCreator(@Nonnull final String id, @Nullable final ScreenController screenController) {
    this.id = id;
    this.screenController = screenController;
  }

  /**
	 * Sets the screen controller.
	 *
	 * @param screenController the new screen controller
	 */
  public void setScreenController(@Nullable final ScreenController screenController) {
    this.screenController = screenController;
  }

  /**
	 * Sets the default focus element.
	 *
	 * @param defaultFocusElement the new default focus element
	 */
  public void setDefaultFocusElement(@Nullable final String defaultFocusElement) {
    this.defaultFocusElement = defaultFocusElement;
  }

  /**
	 * Sets the input mapping.
	 *
	 * @param inputMapping the new input mapping
	 */
  public void setInputMapping(@Nullable final String inputMapping) {
    this.inputMapping = inputMapping;
  }

  /**
	 * Sets the input mapping pre.
	 *
	 * @param inputMappingPre the new input mapping pre
	 */
  public void setInputMappingPre(@Nullable final String inputMappingPre) {
    this.inputMappingPre = inputMappingPre;
  }

  /**
	 * Creates the.
	 *
	 * @param nifty the nifty
	 * @return the screen
	 */
  @Nonnull
  public Screen create(@Nonnull final Nifty nifty) {
    Screen screen = createScreen(nifty);

    addRootElement(nifty, screen);
    addDefaultFocusElement(screen);
    addInputMapping(screen, inputMapping);
    addPreInputMapping(screen, inputMappingPre);

    nifty.addScreen(id, screen);
    return screen;
  }

  /**
	 * Creates the screen.
	 *
	 * @param nifty the nifty
	 * @return the screen
	 */
  @Nonnull
  private Screen createScreen(@Nonnull final Nifty nifty) {
    ScreenController usedController = screenController;
    if (usedController == null) {
      log.warning("Missing ScreenController for screen [" + id + "] using DefaultScreenController() instead but this " +
          "might not be what you want.");
      usedController = new DefaultScreenController();
    }
    return new Screen(nifty, id, usedController, nifty.getTimeProvider());
  }

  /**
	 * Adds the root element.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 */
  private void addRootElement(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
    Element rootElement = nifty.getRootLayerFactory().createRootLayer("root", nifty, screen, nifty.getTimeProvider());
    screen.setRootElement(rootElement);
  }

  /**
	 * Adds the default focus element.
	 *
	 * @param screen the screen
	 */
  private void addDefaultFocusElement(@Nonnull final Screen screen) {
    screen.setDefaultFocusElement(defaultFocusElement);
  }

  /**
	 * Adds the input mapping.
	 *
	 * @param screen            the screen
	 * @param inputMappingClass the input mapping class
	 */
  private void addInputMapping(@Nonnull final Screen screen, @Nullable final String inputMappingClass) {
    if (inputMappingClass != null) {
      NiftyInputMapping inputMapping = ClassHelper.getInstance(inputMappingClass, NiftyInputMapping.class);
      if (inputMapping != null) {
        if (!(screenController instanceof KeyInputHandler)) {
          log.warning("class [" + screenController + "] tries to use inputMapping [" + inputMappingClass + "] but " +
              "does not implement [" + KeyInputHandler.class.getName() + "]");
        } else {
          screen.addKeyboardInputHandler(inputMapping, KeyInputHandler.class.cast(screenController));
        }
      }
    }
  }

  /**
	 * Adds the pre input mapping.
	 *
	 * @param screen            the screen
	 * @param inputMappingClass the input mapping class
	 */
  private void addPreInputMapping(@Nonnull final Screen screen, @Nullable final String inputMappingClass) {
    if (inputMappingClass != null) {
      NiftyInputMapping inputMapping = ClassHelper.getInstance(inputMappingClass, NiftyInputMapping.class);
      if (inputMapping != null) {
        if (!(screenController instanceof KeyInputHandler)) {
          log.warning("class [" + screenController + "] tries to use inputMapping [" + inputMappingClass + "] but " +
              "does not implement [" + KeyInputHandler.class.getName() + "]");
        } else {
          screen.addPreKeyboardInputHandler(inputMapping, KeyInputHandler.class.cast(screenController));
        }
      }
    }
  }
}
