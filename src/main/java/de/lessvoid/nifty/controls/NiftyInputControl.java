package de.lessvoid.nifty.controls;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * NiftyInputControl.
 *
 * @author void
 */
public class NiftyInputControl {
  
  /** The controller. */
  @Nonnull
  private final Controller controller;
  
  /** The input mapper. */
  @Nonnull
  private final NiftyInputMapping inputMapper;

  /** The pre input handler. */
  @Nonnull
  private final List<KeyInputHandler> preInputHandler = new ArrayList<KeyInputHandler>();
  
  /** The post input handler. */
  @Nonnull
  private final List<KeyInputHandler> postInputHandler = new ArrayList<KeyInputHandler>();

  /**
	 * Instantiates a new nifty input control.
	 *
	 * @param controllerParam  controller
	 * @param inputMapperParam input mapper
	 */
  public NiftyInputControl(
      @Nonnull final Controller controllerParam,
      @Nonnull final NiftyInputMapping inputMapperParam) {
    this.controller = controllerParam;
    this.inputMapper = inputMapperParam;
  }

  /**
	 * keyboard event.
	 *
	 * @param nifty      nifty
	 * @param inputEvent keyboard event
	 * @param elementId  the element id
	 * @return return true when the input event has been processed and false when it
	 *         has not been handled
	 */
  public boolean keyEvent(
      @Nonnull final Nifty nifty,
      @Nonnull final KeyboardInputEvent inputEvent,
      @Nonnull final String elementId) {
    NiftyInputEvent converted = inputMapper.convert(inputEvent);
    if (converted == null) {
      return false;
    }

    for (KeyInputHandler handler : preInputHandler) {
      if (handler.keyEvent(converted)) {
        return true;
      }
    }

    nifty.publishEvent(elementId, converted);

    if (controller.inputEvent(converted)) {
      return true;
    }

    for (KeyInputHandler handler : postInputHandler) {
      if (handler.keyEvent(converted)) {
        return true;
      }
    }
    return false;
  }

  /**
	 * Adds the input handler.
	 *
	 * @param handler the handler
	 */
  public void addInputHandler(@Nonnull final KeyInputHandler handler) {
    postInputHandler.add(handler);
  }

  /**
	 * Adds the pre input handler.
	 *
	 * @param handler the handler
	 */
  public void addPreInputHandler(@Nonnull final KeyInputHandler handler) {
    preInputHandler.add(handler);
  }

  /**
	 * On start screen.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 */
  public void onStartScreen(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
    controller.onStartScreen();
  }

  /**
	 * On end screen.
	 *
	 * @param nifty     the nifty
	 * @param screen    the screen
	 * @param elementId the element id
	 */
  public void onEndScreen(@Nonnull final Nifty nifty, @Nonnull final Screen screen, @Nullable final String elementId) {
    controller.onEndScreen();
    nifty.unsubscribeAnnotations(controller);
    if (elementId != null) {
      nifty.unsubscribeElement(screen, elementId);
    }
  }

  /**
   * forward the onForward method to the controller.
   *
   * @param getFocus get focus
   */
  public void onFocus(final boolean getFocus) {
    controller.onFocus(getFocus);
  }

  /**
   * get controller.
   *
   * @return controller
   */
  @Nonnull
  public Controller getController() {
    return controller;
  }

  /**
   * Get control when it matches the given class.
   *
   * @param <T>                   type of class
   * @param requestedControlClass class that is requested
   * @return the instance or null
   */
  @Nullable
  public <T extends Controller> T getControl(@Nonnull final Class<T> requestedControlClass) {
    if (requestedControlClass.isInstance(controller)) {
      return requestedControlClass.cast(controller);
    }
    return null;
  }

  /**
	 * Gets the nifty control.
	 *
	 * @param <T>                   the generic type
	 * @param requestedControlClass the requested control class
	 * @return the nifty control
	 */
  @Nullable
  public <T extends NiftyControl> T getNiftyControl(@Nonnull final Class<T> requestedControlClass) {
    if (requestedControlClass.isInstance(controller)) {
      return requestedControlClass.cast(controller);
    }
    return null;
  }

  /**
	 * Bind control.
	 *
	 * @param nifty      the nifty
	 * @param screen     the screen
	 * @param element    the element
	 * @param attributes the attributes
	 */
  public void bindControl(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Attributes attributes) {
    controller.bind(
        nifty,
        screen,
        element,
        new Parameters(attributes.createProperties()));
  }

  /**
	 * Inits the control.
	 *
	 * @param attributes the attributes
	 */
  public void initControl(@Nonnull final Attributes attributes) {
    controller.init(new Parameters(attributes.createProperties()));
  }
}
