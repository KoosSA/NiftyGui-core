package de.lessvoid.nifty.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyStopwatch;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.StringHelper;

/**
 * A single screen with elements and input focus.
 *
 * @author void
 */
public class Screen {
  
  /** The layout layers call count. */
  public int layoutLayersCallCount = 0;
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(Screen.class.getName());
  
  /** The screen id. */
  @Nonnull
  private final String screenId;
  
  /** The screen controller. */
  @Nonnull
  private final ScreenController screenController;
  
  /** The screen controller bound. */
  private boolean screenControllerBound = false;

  /** The layer elements. */
  @Nonnull
  private final List<Element> layerElements = new ArrayList<Element>();
  
  /** The layer elements to add. */
  @Nonnull
  private final Queue<Element> layerElementsToAdd = new LinkedList<Element>();
  
  /** The layer elements to remove. */
  @Nonnull
  private final Queue<Element> layerElementsToRemove = new LinkedList<Element>();

  /**
   * Popup layers are dynamic layers on top of the normal layers.
   * They are treated as "normal" layers and are added to the layerElements variable. In the
   * popupLayer variable we remember the pop ups additionally, so that we can send
   * input events only to these elements when they are present.
   */
  @Nonnull
  private final List<Element> popupElements = new ArrayList<Element>();
  
  /** The popup elements to add. */
  @Nonnull
  private final Queue<Element> popupElementsToAdd = new LinkedList<Element>();
  
  /** The popup elements to remove. */
  @Nonnull
  private final Deque<ElementWithEndNotify> popupElementsToRemove = new LinkedList<ElementWithEndNotify>();
  
  /** The time provider. */
  @Nonnull
  private final TimeProvider timeProvider;
  
  /** The focus handler. */
  @Nonnull
  private final FocusHandler focusHandler;
  
  /** The mouse over handler. */
  @Nonnull
  private final MouseOverHandler mouseOverHandler;
  
  /** The nifty. */
  @Nonnull
  private final Nifty nifty;
  
  /** The post input handlers. */
  @Nonnull
  private final List<InputHandlerWithMapping> postInputHandlers = new ArrayList<InputHandlerWithMapping>();
  
  /** The pre input handlers. */
  @Nonnull
  private final List<InputHandlerWithMapping> preInputHandlers = new ArrayList<InputHandlerWithMapping>();
  
  /** The root element. */
  @Nullable
  private Element rootElement;
  
  /** The default focus element id. */
  @Nullable
  private String defaultFocusElementId;
  
  /** The running. */
  private boolean running = false;
  
  /** The registered ids. */
  @Nonnull
  private final Set<String> registeredIds = new HashSet<String>();

  /** The bound. */
  private boolean bound;

  /**
	 * Instantiates a new screen.
	 *
	 * @param newNifty            the new nifty
	 * @param newId               the new id
	 * @param newScreenController the new screen controller
	 * @param newTimeProvider     the new time provider
	 */
  public Screen(
      @Nonnull final Nifty newNifty,
      @Nonnull final String newId,
      @Nonnull final ScreenController newScreenController,
      @Nonnull final TimeProvider newTimeProvider) {
    nifty = newNifty;
    screenId = newId;
    screenController = newScreenController;
    timeProvider = newTimeProvider;
    focusHandler = new FocusHandler();
    mouseOverHandler = new MouseOverHandler();
  }

  /**
	 * Register element id.
	 *
	 * @param id the id
	 */
  public void registerElementId(@Nonnull final String id) {
    if (registeredIds.contains(id)) {
      log.warning("Possible conflicting id [" + id + "] detected. Consider making all Ids unique or use #id in " +
          "control-definitions.");
    } else {
      registeredIds.add(id);
    }
  }

  /**
	 * Unregister element id.
	 *
	 * @param id the id
	 */
  public void unregisterElementId(@Nonnull final String id) {
    registeredIds.remove(id);
  }

  /**
	 * Gets the screen id.
	 *
	 * @return the screen id
	 */
  @Nonnull
  public String getScreenId() {
    return screenId;
  }

  /**
	 * Gets the layer elements.
	 *
	 * @return the layer elements
	 */
  @Nonnull
  public List<Element> getLayerElements() {
    return layerElements;
  }

  /**
	 * Adds the layer element.
	 *
	 * @param layerElement the layer element
	 */
  public void addLayerElement(@Nonnull final Element layerElement) {
    layerElementsToAdd.add(layerElement);
  }

  /**
	 * Removes the layer element.
	 *
	 * @param layerElement the layer element
	 */
  public void removeLayerElement(@Nonnull final Element layerElement) {
    layerElementsToRemove.add(layerElement);
  }

  /**
	 * Removes the layer element.
	 *
	 * @param layerId the layer id
	 */
  public void removeLayerElement(@Nonnull final String layerId) {
    for (int i = 0; i < layerElements.size(); i++) {
      Element layer = layerElements.get(i);
      if (layerId.equals(layer.getId())) {
        removeLayerElement(layer);
        return;
      }
    }
  }

  /**
	 * Adds the popup.
	 *
	 * @param popup               the popup
	 * @param defaultFocusElement the default focus element
	 */
  public void addPopup(@Nonnull final Element popup, @Nullable final Element defaultFocusElement) {

    // This enforced all mouse buttons to the released state when a new popup
    // is about to be created. But I can't remember what that was for :)
    //
    // It made problems in the drop down example where the window controls have
    // been used. Since drop down controls are temporarily moved to a popup layer
    // so that they can be moved around above all other stuff this resetEvents()
    // call had one odd side effect: If you would click on a window title bar (to
    // move it around) - in this moment the control is being moved to the popup layer -
    // and then you would release the button this release would never been detected
    // by the window title bar (because the button has already been released by
    // this call to resetEvents() in here). In this case the window got stuck in the
    // popup until you'd press and release the mouse button again.
    //
    // Not calling resetEvents() in here fixes this issue but might break something
    // else although I'm currently not sure what that might be :)
    //
    // nifty.resetEvents();

    // create the callback
    EndNotify localEndNotify = new EndNotify() {
      @Override
      public final void perform() {
        for (int i = 0; i < layerElements.size(); i++) {
          Element w = layerElements.get(i);
          if (w.isEffectActive(EffectEventId.onStartScreen)) {
            return;
          }
        }
      }
    };

    Element mouseFocusElement = focusHandler.getMouseFocusElement();
    if (mouseFocusElement != null) {
      mouseFocusElement.stopEffect(EffectEventId.onHover);
    }

    focusHandler.pushState();

    // prepare pop up for display
    popup.resetEffects();
    popup.layoutElements();
    popup.initControls(true);
    popup.startEffect(EffectEventId.onStartScreen, localEndNotify);
    popup.startEffect(EffectEventId.onActive);
    popup.onStartScreen();
    if (defaultFocusElement != null) {
      defaultFocusElement.setFocus();
    } else {
      setDefaultFocus();
    }

    // add to layers and add as popup
    addLayerElement(popup);
    addPopupElement(popup);
  }

  /**
	 * Adds the popup element.
	 *
	 * @param popup the popup
	 */
  void addPopupElement(final Element popup) {
    popupElementsToAdd.add(popup);
  }

  /**
	 * Close popup.
	 *
	 * @param popup       the popup
	 * @param closeNotify the close notify
	 */
  public void closePopup(@Nonnull final Element popup, final EndNotify closeNotify) {
    popup.onEndScreen(this);
    nifty.resetMouseInputEvents();
    removeLayerElement(popup);
    schedulePopupElementRemoval(new ElementWithEndNotify(popup, closeNotify));
  }

  /**
	 * Schedule popup element removal.
	 *
	 * @param elementWithEndNotify the element with end notify
	 */
  private void schedulePopupElementRemoval(final ElementWithEndNotify elementWithEndNotify) {
    popupElementsToRemove.add(elementWithEndNotify);
  }

  /**
	 * Start screen.
	 */
  public void startScreen() {
    startScreen(null);
  }

  /**
	 * Start screen.
	 *
	 * @param startScreenEndNotify the start screen end notify
	 */
  public void startScreen(final EndNotify startScreenEndNotify) {
    NiftyStopwatch.start();
    running = false;

    nifty.getRenderEngine().screenStarted(this);

    focusHandler.resetFocusElements();
    resetLayers();
    layoutLayers();
    bindControls();

    // bind happens right BEFORE the onStartScreen
    if (!screenControllerBound) {
      screenController.bind(nifty, this);
      screenControllerBound = true;
    }

    // activate the onActive event right now
    activeEffectStart();

    // onStartScreen
    final StartScreenEndNotify endNotify = createScreenStartEndNotify(startScreenEndNotify);
    startLayers(EffectEventId.onStartScreen, endNotify);

    // default focus attribute has been set in onStartScreen
    // event of the elements. so we have to set the default focus
    // here after the onStartScreen is started.
    setDefaultFocus();
    NiftyStopwatch.stop("Screen.startScreen(" + layoutLayersCallCount + ")");
  }

  /**
	 * End screen.
	 *
	 * @param callback the callback
	 */
  public void endScreen(final EndNotify callback) {
    resetLayers();
    final EndScreenEndNotify endNotify = createScreenEndNotify(callback);
    startLayers(EffectEventId.onEndScreen, endNotify);
  }

  /**
	 * Layout layers.
	 */
  public void layoutLayers() {
    NiftyStopwatch.start();
    layoutLayersCallCount++;

    for (int i = 0; i < layerElements.size(); i++) {
      Element w = layerElements.get(i);
      w.layoutElements();
    }
    NiftyStopwatch.stop("Screen.layoutLayers()");
  }

  /**
	 * Reset layers.
	 */
  private void resetLayers() {
    nifty.resetMouseInputEvents();

    for (int i = 0; i < layerElements.size(); i++) {
      Element w = layerElements.get(i);
      w.resetEffects();
      w.reactivate();
    }
  }

  /**
	 * Start layers.
	 *
	 * @param effectEventId the effect event id
	 * @param endNotify     the end notify
	 */
  private void startLayers(@Nonnull final EffectEventId effectEventId, final EndNotify endNotify) {
    // create the callback
    LocalEndNotify localEndNotify = new LocalEndNotify(effectEventId, endNotify);

    // start the effect for all layers
    for (int i = 0; i < layerElements.size(); i++) {
      Element w = layerElements.get(i);
      w.startEffect(effectEventId, localEndNotify);

      if (effectEventId == EffectEventId.onStartScreen) {
        w.onStartScreen();
      }
    }

    // just in case there was no effect activated, we'll check here, if we're already done
    localEndNotify.enable();
    localEndNotify.perform();
  }

  /**
	 * Sets the default focus.
	 */
  public void setDefaultFocus() {
    if (focusHandler.getKeyboardFocusElement() != null) {
      return;
    }

    if (defaultFocusElementId != null) {
      Element defaultFocus = getFocusHandler().findElement(defaultFocusElementId);
      if (defaultFocus != null) {
        defaultFocus.setFocus();
        return;
      }
    }

    // fall back to first element
    Element firstFocus = getFocusHandler().getFirstFocusElement();
    if (firstFocus != null) {
      firstFocus.setFocus();
    }
  }

  /**
   * Start the onActive effect.
   */
  private void activeEffectStart() {
    for (int i = 0; i < layerElements.size(); i++) {
      Element w = layerElements.get(i);
      w.startEffect(EffectEventId.onActive, null);

      // in case this element is disabled we will start the disabled effect right here.
      if (!w.isEnabled()) {
        w.startEffect(EffectEventId.onDisabled, null);
      }
    }
  }

  /**
   * render all layers.
   *
   * @param renderDevice the renderDevice to use
   */
  public final void renderLayers(@Nonnull final NiftyRenderEngine renderDevice) {
    for (int i = 0; i < layerElements.size(); i++) {
      Element layer = layerElements.get(i);
      layer.render(renderDevice);
    }
  }

  /**
	 * Reset layout.
	 */
  public void resetLayout() {
    for (int i = 0; i < layerElements.size(); i++) {
      Element layer = layerElements.get(i);
      layer.resetLayout();
    }
  }

  /**
   * Handle Mouse Events for this screen. Forwards  the event to the layers.
   *
   * @param inputEvent MouseInputEvent
   * @return true when processed and false when not
   */
  public boolean mouseEvent(@Nonnull final NiftyMouseInputEvent inputEvent) {
    if (log.isLoggable(Level.FINE)) {
      log.fine("screen mouseEvent: " + inputEvent.toString());
    }
    if (!popupElements.isEmpty()) {
      return forwardMouseEventToLayers(popupElements, inputEvent);
    } else {
      return forwardMouseEventToLayers(layerElements, inputEvent);
    }
  }

  /**
   * forward mouse event to the given layer list.
   *
   * @param layerList  layer list
   * @param inputEvent the event that is published to all layers
   * @return {@code true} in case the event was handled by any layer
   */
  private boolean forwardMouseEventToLayers(
      @Nonnull final List<Element> layerList,
      @Nonnull final NiftyMouseInputEvent inputEvent) {
    mouseOverHandler.reset();

    long eventTime = timeProvider.getMsTime();

    if (focusHandler.hasAnyElementTheMouseFocus()) {
      Element e = focusHandler.getMouseFocusElement();
      mouseOverHandler.addMouseOverElement(e);
    } else {
      for (int i = 0; i < layerList.size(); i++) {
        Element layer = layerList.get(i);
        layer.buildMouseOverElements(inputEvent, eventTime, mouseOverHandler);
      }
    }

    if (log.isLoggable(Level.FINER)) {
      log.fine(mouseOverHandler.getInfoString());
    }

    mouseOverHandler.processMouseOverEvent(rootElement, inputEvent, eventTime);
    mouseOverHandler.processMouseEvent(inputEvent, eventTime);

    return mouseOverHandler.hitsElement();
  }

  /**
   * find an element by name.
   * this method is deprecated, use findElementById() instead
   *
   * @param name the id to find
   * @return the element or null
   * @see Screen#findElementById(java.lang.String)
   */
  @Nullable
  @Deprecated
  public Element findElementByName(final String name) {
    return findElementById(name);
  }

  /**
   * find an element by id.
   *
   * @param findId the id to find
   * @return the element or null
   */
  @Nullable
  public Element findElementById(@Nullable final String findId) {
    if (findId == null) {
      return null;
    }
    for (int i = 0; i < layerElements.size(); i++) {
      Element layer = layerElements.get(i);
      Element found = layer.findElementById(findId);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  /**
	 * Find elements by style.
	 *
	 * @param styleIds the style ids
	 * @return the list
	 */
  public List<Element> findElementsByStyle(@Nullable final String... styleIds) {
    if (styleIds == null || styleIds.length == 0) {
      return Collections.emptyList();
    }

    List<Element> elements = new ArrayList<Element>();
    for (Element e : layerElements) {
      elements.addAll(e.findElementsByStyle(styleIds));
    }

    return elements;
  }

  /**
	 * Find control.
	 *
	 * @param <T>                   the generic type
	 * @param elementName           the element name
	 * @param requestedControlClass the requested control class
	 * @return the t
	 */
  @Nullable
  public <T extends Controller> T findControl(final String elementName, @Nonnull final Class<T> requestedControlClass) {
    Element element = findElementById(elementName);
    if (element == null) {
      return null;
    }
    return element.getControl(requestedControlClass);
  }

  /**
   * Finds the Nifty Control matching the given id and control class on this screen
   * and returns it.
   *
   * @param <T>                   The type of the control class being looked for
   * @param elementName           The id of the control
   * @param requestedControlClass The class being looked for
   * @return Either a NiftyControl or a Null version of a NiftyControl for the matching control class.
   */
  @Nullable
  public <T extends NiftyControl> T findNiftyControl(
      @Nullable final String elementName,
      @Nonnull final Class<T> requestedControlClass) {
    if (elementName == null) {
      return null;
    }
    Element element = findElementById(elementName);
    if (element == null) {
      log.warning("missing element/control with id [" + elementName + "] for requested control class [" +
          requestedControlClass.getName() + "]");
      return null;
    }
    return element.getNiftyControl(requestedControlClass);
  }

  /**
   * set alternate key.
   *
   * @param alternateKey alternate key to set
   */
  public void setAlternateKey(@Nullable final String alternateKey) {
    for (int i = 0; i < layerElements.size(); i++) {
      Element layer = layerElements.get(i);
      layer.setAlternateKey(alternateKey);
    }
  }

  /**
	 * keyboard event.
	 *
	 * @param inputEvent keyboard event
	 * @return true, if successful
	 */
  public boolean keyEvent(@Nonnull final KeyboardInputEvent inputEvent) {
    for (int i = 0; i < preInputHandlers.size(); i++) {
      InputHandlerWithMapping handler = preInputHandlers.get(i);
      if (handler.process(inputEvent)) {
        return true;
      }
    }
    if (focusHandler.keyEvent(inputEvent)) {
      return true;
    }
    for (int i = 0; i < postInputHandlers.size(); i++) {
      InputHandlerWithMapping handler = postInputHandlers.get(i);
      if (handler.process(inputEvent)) {
        return true;
      }
    }
    return false;
  }

  /**
   * add a keyboard input handler.
   *
   * @param mapping mapping
   * @param handler new handler to add
   */
  public void addKeyboardInputHandler(
      @Nonnull final NiftyInputMapping mapping,
      @Nonnull final KeyInputHandler handler) {
    postInputHandlers.add(new InputHandlerWithMapping(mapping, handler));
  }

  /**
	 * Removes the keyboard input handler.
	 *
	 * @param handler the handler
	 */
  public void removeKeyboardInputHandler(final KeyInputHandler handler) {
    for (int i = 0; i < postInputHandlers.size(); i++) {
      if (postInputHandlers.get(i).getKeyInputHandler().equals(handler)) {
        postInputHandlers.remove(i);
        return;
      }
    }
  }

  /**
   * add a keyboard input handler.
   *
   * @param mapping mapping
   * @param handler new handler to add
   */
  public void addPreKeyboardInputHandler(
      @Nonnull final NiftyInputMapping mapping,
      @Nonnull final KeyInputHandler handler) {
    preInputHandlers.add(new InputHandlerWithMapping(mapping, handler));
  }

  /**
	 * Removes the pre keyboard input handler.
	 *
	 * @param handler the handler
	 */
  public void removePreKeyboardInputHandler(final KeyInputHandler handler) {
    for (int i = 0; i < preInputHandlers.size(); i++) {
      if (preInputHandlers.get(i).getKeyInputHandler().equals(handler)) {
        preInputHandlers.remove(i);
        return;
      }
    }
  }

  /**
	 * Debug output.
	 *
	 * @return the string
	 */
  @Nonnull
  public String debugOutput() {
    return debugOutput(".*", ".*");
  }

  /**
	 * Debug output focus elements.
	 *
	 * @return the string
	 */
  @Nonnull
  public String debugOutputFocusElements() {
    return focusHandler.toString();
  }

  /**
	 * Debug output.
	 *
	 * @param regexpElement   the regexp element
	 * @param regexpAttribute the regexp attribute
	 * @return the string
	 */
  @Nonnull
  public String debugOutput(@Nonnull final String regexpElement, @Nonnull final String regexpAttribute) {
    StringBuffer result = new StringBuffer();
    debugOutputLayerElements(regexpElement, regexpAttribute, result, layerElements);
    result.append("\n\n### popupElements: ").append(popupElements.size());
    debugOutputLayerElements(regexpElement, regexpAttribute, result, popupElements);
    result.append(focusHandler.toString());
    return result.toString();
  }

  /**
	 * Debug output layer elements.
	 *
	 * @param regexpElement   the regexp element
	 * @param regexpAttribute the regexp attribute
	 * @param result          the result
	 * @param layers          the layers
	 */
  private void debugOutputLayerElements(
      @Nonnull final String regexpElement,
      @Nonnull final String regexpAttribute,
      @Nonnull StringBuffer result,
      @Nonnull List<Element> layers) {
    for (int i = 0; i < layers.size(); i++) {
      Element layer = layers.get(i);
      String layerType = " +";
      if (!layer.isVisible()) {
        layerType = " -";
      }
      result.append("\n").append(layerType).append(getIdText(layer)).append("\n").append(StringHelper.whitespace
          (layerType.length())).append(layer.getElementStateString(StringHelper.whitespace(layerType.length()),
          regexpAttribute));
      result.append(outputElement(layer, "   ", regexpElement, regexpAttribute));
    }
  }

  /**
	 * Output element.
	 *
	 * @param w               the w
	 * @param offset          the offset
	 * @param regexpElement   the regexp element
	 * @param regexpAttribute the regexp attribute
	 * @return the string
	 */
  @Nonnull
  public String outputElement(
      @Nonnull final Element w,
      @Nonnull final String offset,
      @Nonnull final String regexpElement,
      @Nonnull final String regexpAttribute) {
    StringBuilder result = new StringBuilder();
    List<Element> wwElements = w.getChildren();
    for (int i = 0; i < wwElements.size(); i++) {
      Element ww = wwElements.get(i);
      String elementId = getIdText(ww);
      if (elementId.matches(regexpElement)) {
        result.append("\n").append(offset).append(elementId).append(" ").append(ww.getElementType().getClass()
            .getSimpleName()).append(" childLayout [").append(ww.getElementType().getAttributes().get("childLayout"))
            .append("]");
        result.append("\n").append(StringHelper.whitespace(offset.length())).append(ww.getElementStateString
            (StringHelper.whitespace(offset.length()), regexpAttribute));
        result.append(outputElement(ww, offset + " ", ".*", regexpAttribute));
      } else {
        result.append(outputElement(ww, offset + " ", regexpElement, regexpAttribute));
      }
    }
    return result.toString();
  }

  /**
	 * Gets the id text.
	 *
	 * @param ww the ww
	 * @return the id text
	 */
  @Nonnull
  private String getIdText(@Nonnull final Element ww) {
    String id = ww.getId();
    if (id == null) {
      return "[---]";
    } else {
      return "[" + id + "]";
    }
  }

  /**
   * get current attached screen controller.
   *
   * @return ScreenController
   */
  @Nonnull
  public ScreenController getScreenController() {
    return screenController;
  }

  /**
   * get the screens focus handler.
   *
   * @return focus handler
   */
  @Nonnull
  public FocusHandler getFocusHandler() {
    return focusHandler;
  }

  /**
   * Get RootElement.
   *
   * @return root element
   * @throws java.lang.IllegalStateException in case the element is requested before it was set
   */
  @Nonnull
  public Element getRootElement() {
    if (rootElement == null) {
      throw new IllegalStateException("Requested root element before it was set.");
    }
    return rootElement;
  }

  /**
   * Set RootElement.
   *
   * @param rootElementParam new root element
   */
  @SuppressWarnings("NullableProblems")
  public void setRootElement(@Nonnull final Element rootElementParam) {
    rootElement = rootElementParam;
    rootElement.bindControls(this);
  }

  /**
   * Do things when the current frame has ended.
   */
  public void processAddAndRemoveLayerElements() {
    // add/remove layer elements
    layerElements.addAll(layerElementsToAdd);
    layerElements.removeAll(layerElementsToRemove);
    layerElementsToAdd.clear();
    layerElementsToRemove.clear();

    // add/remove popup elements
    popupElements.addAll(popupElementsToAdd);
    popupElementsToAdd.clear();

    while (!popupElementsToRemove.isEmpty()) {
      popupElementsToRemove.pollFirst().remove();
    }
  }

  /**
	 * Checks for dynamic elements.
	 *
	 * @return true, if successful
	 */
  public boolean hasDynamicElements() {
    if (!layerElementsToAdd.isEmpty() || !layerElementsToRemove.isEmpty() || !popupElementsToAdd.isEmpty() ||
        !popupElementsToRemove.isEmpty()) {
      return true;
    }
    return false;
  }

  /**
	 * Sets the default focus element.
	 *
	 * @param defaultFocusElementIdParam the new default focus element
	 */
  public void setDefaultFocusElement(@Nullable final String defaultFocusElementIdParam) {
    defaultFocusElementId = defaultFocusElementIdParam;
  }

  /**
	 * Gets the default focus element id.
	 *
	 * @return the default focus element id
	 */
  @Nullable
  public String getDefaultFocusElementId() {
    return defaultFocusElementId;
  }

  /**
	 * The Class LocalEndNotify.
	 */
  private class LocalEndNotify implements EndNotify {
    
    /** The enabled. */
    private boolean enabled = false;
    
    /** The effect event id. */
    @Nonnull
    private final EffectEventId effectEventId;
    
    /** The end notify. */
    @Nullable
    private final EndNotify endNotify;

    /**
	 * Instantiates a new local end notify.
	 *
	 * @param effectEventIdParam the effect event id param
	 * @param endNotifyParam     the end notify param
	 */
    public LocalEndNotify(@Nonnull final EffectEventId effectEventIdParam, @Nullable final EndNotify endNotifyParam) {
      effectEventId = effectEventIdParam;
      endNotify = endNotifyParam;
    }

    /**
	 * Enable.
	 */
    public void enable() {
      enabled = true;
    }

    /**
	 * Perform.
	 */
    @Override
    public void perform() {
      if (enabled) {
        for (int i = 0; i < layerElements.size(); i++) {
          Element w = layerElements.get(i);
          if (w.isEffectActive(effectEventId)) {
            return;
          }
        }
        if (endNotify != null) {
          endNotify.perform();
        }
      }
    }
  }

  /**
	 * Creates the screen start end notify.
	 *
	 * @param startScreenEndNotify the start screen end notify
	 * @return the start screen end notify
	 */
  @Nonnull
  StartScreenEndNotify createScreenStartEndNotify(final EndNotify startScreenEndNotify) {
    return new StartScreenEndNotify(startScreenEndNotify);
  }

  /**
	 * The Class StartScreenEndNotify.
	 */
  class StartScreenEndNotify implements EndNotify {
    
    /** The additional end notify. */
    @Nullable
    private final EndNotify additionalEndNotify;

    /**
	 * Instantiates a new start screen end notify.
	 *
	 * @param additionalEndNotify the additional end notify
	 */
    public StartScreenEndNotify(@Nullable final EndNotify additionalEndNotify) {
      this.additionalEndNotify = additionalEndNotify;
    }

    /**
	 * Perform.
	 */
    @Override
    public void perform() {
      log.fine("onStartScreen has ended");

      if (additionalEndNotify != null) {
        additionalEndNotify.perform();
      }

      onStartScreenHasEnded();
    }
  }

  /**
	 * Creates the screen end notify.
	 *
	 * @param endScreenEndNotify the end screen end notify
	 * @return the end screen end notify
	 */
  @Nonnull
  EndScreenEndNotify createScreenEndNotify(final EndNotify endScreenEndNotify) {
    return new EndScreenEndNotify(endScreenEndNotify);
  }

  /**
	 * The Class EndScreenEndNotify.
	 */
  class EndScreenEndNotify implements EndNotify {
    
    /** The additional end notify. */
    @Nullable
    private final EndNotify additionalEndNotify;

    /**
	 * Instantiates a new end screen end notify.
	 *
	 * @param additionalEndNotify the additional end notify
	 */
    public EndScreenEndNotify(@Nullable final EndNotify additionalEndNotify) {
      this.additionalEndNotify = additionalEndNotify;
    }

    /**
	 * Perform.
	 */
    @Override
    public void perform() {
      log.fine("onEndScreen has ended - schedule further processing as end of frame action");
      nifty.scheduleEndOfFrameElementAction(new EndOfScreenAction(Screen.this), additionalEndNotify);
    }
  }

  /**
   * InputMappingWithMapping helper.
   *
   * @author void
   */
  public static class InputHandlerWithMapping {
    /**
     * Mapping.
     */
    @Nonnull
    private final NiftyInputMapping mapping;

    /**
     * KeyInputHandler.
     */
    @Nonnull
    private final KeyInputHandler handler;

    /**
     * Create InputHandlerWithMapping.
     *
     * @param newMapping NiftyInputMapping
     * @param newHandler KeyInputHandler
     */
    public InputHandlerWithMapping(
        @Nonnull final NiftyInputMapping newMapping,
        @Nonnull final KeyInputHandler newHandler) {
      mapping = newMapping;
      handler = newHandler;
    }

    /**
	 * Gets the key input handler.
	 *
	 * @return the key input handler
	 */
    @Nonnull
    public KeyInputHandler getKeyInputHandler() {
      return handler;
    }

    /**
     * Process Keyboard InputEvent.
     *
     * @param inputEvent KeyboardInputEvent
     * @return event has been processed or not
     */
    public boolean process(@Nonnull final KeyboardInputEvent inputEvent) {
      NiftyInputEvent event = mapping.convert(inputEvent);
      if (event == null) {
        return false;
      }
      return handler.keyEvent(event);
    }
  }

  /**
	 * Checks if is running.
	 *
	 * @return true, if is running
	 */
  public boolean isRunning() {
    return running;
  }

  /**
	 * On start screen has ended.
	 */
  void onStartScreenHasEnded() {
    nifty.subscribeAnnotations(screenController);

    // onStartScreen has ENDED so call the event.
    screenController.onStartScreen();

    // Process a fake mouse event with current coordinates so that Nifty will activate hover effects for any
    // element currently under the mouse, and will also deactivate any hover effects for any element NOT currently
    // under the mouse. This prevents the undesirable behavior of having an element under the mouse that should be in a
    // hovered-effect state, but is not due to the screen change, and of having an element that is NOT under the mouse
    // that should NOT be in a hovered-effect state, but IS due to the screen change.
    forceMouseHoverUpdate();

    running = true;
  }

  /**
	 * Force mouse hover update.
	 */
  private void forceMouseHoverUpdate() {
    NiftyMouseInputEvent event = new NiftyMouseInputEvent();
    event.initialize(nifty.getRenderEngine().convertFromNativeX(nifty.getNiftyMouse().getX()),
        nifty.getRenderEngine().convertFromNativeY(nifty.getNiftyMouse().getY()), 0, false, false, false);
    mouseEvent(event);
  }

  /**
	 * On end screen has ended.
	 */
  void onEndScreenHasEnded() {
    log.fine("onEndScreenHasEnded()");

    nifty.unsubscribeAnnotations(screenController);
    nifty.unsubscribeScreen(this);

    // onEndScreen has ENDED so call the event.
    screenController.onEndScreen();

    for (int i = 0; i < layerElements.size(); i++) {
      layerElements.get(i).onEndScreen(this);
    }
    nifty.getRenderEngine().screenEnded(this);
  }

  /**
	 * Checks if is effect active.
	 *
	 * @param effectEventId the effect event id
	 * @return true, if is effect active
	 */
  public boolean isEffectActive(@Nonnull final EffectEventId effectEventId) {
    if (!popupElements.isEmpty()) {
      return isEffectActive(popupElements, effectEventId);
    } else {
      return isEffectActive(layerElements, effectEventId);
    }
  }

  /**
	 * Checks if is effect active.
	 *
	 * @param elements      the elements
	 * @param effectEventId the effect event id
	 * @return true, if is effect active
	 */
  private boolean isEffectActive(@Nonnull final List<Element> elements, @Nonnull final EffectEventId effectEventId) {
    for (int i = 0; i < elements.size(); i++) {
      Element element = elements.get(i);
      if (element.isEffectActive(effectEventId)) {
        return true;
      }
    }
    return false;
  }

  /**
	 * Gets the top most popup.
	 *
	 * @return the top most popup
	 */
  @Nullable
  public Element getTopMostPopup() {
    if (popupElements.isEmpty()) {
      return null;
    }
    return popupElements.get(popupElements.size() - 1);
  }

  /**
	 * Checks if is active popup.
	 *
	 * @param id the id
	 * @return true, if is active popup
	 */
  public boolean isActivePopup(@Nonnull final String id) {
    for (int i = 0; i < popupElements.size(); i++) {
      if (id.equals(popupElements.get(i).getId())) {
        return true;
      }
    }
    return false;
  }

  /**
	 * Checks if is active popup.
	 *
	 * @param element the element
	 * @return true, if is active popup
	 */
  public boolean isActivePopup(final Element element) {
    return popupElements.contains(element);
  }

  /**
   * Checks if the mouse currently hovers any element that is able to handle mouse events.
   *
   * @return true if the mouse hovers an element that is visibleToMouse and
   * false if the mouse would hit the background and not any element at all
   */
  public boolean isMouseOverElement() {
    return mouseOverHandler.hitsElement();
  }

  /**
   * This returns an informational String containing all elements that Nifty is aware of that
   * could handle mouse events with the ones currently hovering the mouse sorted from top to
   * bottom.
   *
   * @return String for debug output purpose
   */
  @Nonnull
  public String getMouseOverInfoString() {
    return mouseOverHandler.getInfoString();
  }

  /**
	 * The Class ElementWithEndNotify.
	 */
  public class ElementWithEndNotify {
    
    /** The element. */
    private final Element element;
    
    /** The close notify. */
    private final EndNotify closeNotify;

    /**
	 * Instantiates a new element with end notify.
	 *
	 * @param element     the element
	 * @param closeNotify the close notify
	 */
    public ElementWithEndNotify(final Element element, final EndNotify closeNotify) {
      this.element = element;
      this.closeNotify = closeNotify;
    }

    /**
	 * Removes the.
	 */
    public void remove() {
      popupElements.remove(element);
      focusHandler.popState();
      if (closeNotify != null) {
        closeNotify.perform();
      }
      element.getNifty().internalPopupRemoved(element.getId());
    }
  }

  /**
	 * Bind controls.
	 */
  private void bindControls() {
    bound = true;
    for (int i = 0; i < layerElements.size(); i++) {
      layerElements.get(i).bindControls(this);
    }
    for (int i = 0; i < layerElements.size(); i++) {
      layerElements.get(i).initControls(false);
    }
  }

  /**
	 * Checks if is bound.
	 *
	 * @return true, if is bound
	 */
  public boolean isBound() {
    return bound;
  }

  /**
	 * Reset mouse down.
	 */
  public void resetMouseDown() {
    for (int i = 0; i < layerElements.size(); i++) {
      layerElements.get(i).resetMouseDown();
    }

  }
}
