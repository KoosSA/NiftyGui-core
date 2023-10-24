package de.lessvoid.nifty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.EventTopicSubscriber;
import org.bushe.swing.event.ProxySubscriber;
import org.bushe.swing.event.ThreadSafeEventService;
import org.bushe.swing.event.annotation.ReferenceStrength;

import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Action;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.ElementMoveAction;
import de.lessvoid.nifty.elements.ElementRemoveAction;
import de.lessvoid.nifty.elements.EndOfFrameElementAction;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEventProcessor;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.ControllerFactory;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.RootLayerFactory;
import de.lessvoid.nifty.loaderv2.types.ControlDefinitionType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.LayerType;
import de.lessvoid.nifty.loaderv2.types.NiftyType;
import de.lessvoid.nifty.loaderv2.types.PopupType;
import de.lessvoid.nifty.loaderv2.types.RegisterEffectType;
import de.lessvoid.nifty.loaderv2.types.RegisterMusicType;
import de.lessvoid.nifty.loaderv2.types.RegisterSoundType;
import de.lessvoid.nifty.loaderv2.types.ResourceBundleType;
import de.lessvoid.nifty.loaderv2.types.StyleType;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolverDefault;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyMouseImpl;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.NiftyRenderEngineImpl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.FlipFlop;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import de.lessvoid.xml.tools.BundleInfo;
import de.lessvoid.xml.tools.BundleInfoBasename;
import de.lessvoid.xml.tools.BundleInfoResourceBundle;
import de.lessvoid.xml.tools.SpecialValuesReplace;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The main Nifty class.
 *
 * @author void
 */
public class Nifty {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(Nifty.class.getName());

  /** The render engine. */
  @Nonnull
  private final NiftyRenderEngine renderEngine;
  
  /** The sound system. */
  @Nonnull
  private final SoundSystem soundSystem;
  
  /** The input system. */
  @Nonnull
  private final InputSystem inputSystem;
  
  /** The time provider. */
  @Nonnull
  private final TimeProvider timeProvider;

  /** The resource loader. */
  @Nonnull
  private final NiftyResourceLoader resourceLoader;
  
  /** The loader. */
  @Nonnull
  private final NiftyLoader loader;
  
  /** The nifty mouse. */
  @Nonnull
  private final NiftyMouseImpl niftyMouse;
  
  /** The mouse input event processor. */
  @Nonnull
  private final MouseInputEventProcessor mouseInputEventProcessor;

  /** The screens. */
  @Nonnull
  private final Map<String, Screen> screens;
  
  /** The popup types. */
  @Nonnull
  private final Map<String, PopupType> popupTypes;
  
  /** The popups. */
  @Nonnull
  private final Map<String, Element> popups;
  
  /** The styles. */
  @Nonnull
  private final Map<String, StyleType> styles;

  /**
   * When nifty loads a new style also the styles of controls need to be
   * updated.The only way to do this is to collect here all the style id of updated
   * styles and then fire an event with eventbus
   * @see Nifty#loadStyleFile(java.lang.String)
   * @see Nifty#registerStyle(de.lessvoid.nifty.loaderv2.types.StyleType)
   * for further details.
   */
  @Nonnull
  private final Set<String> controlStylesChanged;
  
  /** The control definitions. */
  @Nonnull
  private final Map<String, ControlDefinitionType> controlDefinitions;
  
  /** The registered effects. */
  @Nonnull
  private final Map<String, RegisterEffectType> registeredEffects;
  
  /** The registered screen controllers. */
  @Nonnull
  private final Map<String, ScreenController> registeredScreenControllers;
  
  /** The controller factory. */
  @Nonnull
  private final ControllerFactory controllerFactory;

  /** The delayed method invokes. */
  @Nonnull
  private final FlipFlop<List<DelayedMethodInvoke>> delayedMethodInvokes;
  
  /** The end of frame element actions. */
  @Nonnull
  private final FlipFlop<List<EndOfFrameElementAction>> endOfFrameElementActions;

  /** The locale. */
  @Nonnull
  private Locale locale;

  /**
   * The screen that is currently displayed by the Nifty-GUI. This is {@code null} in case no screen is shown right
   * now.
   */
  @Nullable
  private Screen currentScreen;
  
  /** The current loaded. */
  @Nullable
  private String currentLoaded;
  
  /** The exit. */
  private boolean exit;
  
  /** The resolution changed. */
  private boolean resolutionChanged;
  
  /** The closed popups. */
  private final Set<String> closedPopups = new HashSet<String>();
  
  /** The close popup list. */
  @Nonnull
  private final List<ClosePopUp> closePopupList = new ArrayList<ClosePopUp>();
  
  /** The alternate key for next load xml. */
  @Nullable
  private String alternateKeyForNextLoadXml;
  
  /** The last time. */
  private long lastTime;
  
  /** The goto screen in progress. */
  private boolean gotoScreenInProgress;
  
  /** The alternate key. */
  @Nullable
  private String alternateKey;
  
  /** The resource bundles. */
  @Nonnull
  private final Map<String, BundleInfo> resourceBundles = new HashMap<String, BundleInfo>();
  
  /** The global properties. */
  @Nullable
  private Properties globalProperties;
  
  /** The root layer factory. */
  @Nonnull
  private final RootLayerFactory rootLayerFactory = new RootLayerFactory();
  
  /** The nifty input consumer. */
  @Nonnull
  private final NiftyInputConsumerImpl niftyInputConsumer = new NiftyInputConsumerImpl();
  
  /** The nifty input consumer notify. */
  private NiftyInputConsumerNotify niftyInputConsumerNotify = new NiftyInputConsumerNotifyDefault();
  
  /** The subscriber register. */
  @Nonnull
  private final SubscriberRegistry subscriberRegister = new SubscriberRegistry();
  
  /** The debug option panel colors. */
  private boolean debugOptionPanelColors;
  
  /** The clipboard. */
  @Nonnull
  private Clipboard clipboard;

  /** The ignore mouse events. */
  /*
   * when set to true Nifty will ignore all mouse events.
   */
  private boolean ignoreMouseEvents;

  /** The ignore keyboard events. */
  /*
   * when set to true Nifty will ignore all keyboard events.
   */
  private boolean ignoreKeyboardEvents;

  /** The nifty method invoker debug enabled. */
  // set to true when NiftyMethodInvoker should throw exceptions (true) instead of only logging them (false)
  private boolean niftyMethodInvokerDebugEnabled;

  /**
	 * Instantiates a new nifty.
	 *
	 * @param newRenderDevice the new render device
	 * @param newSoundDevice  the new sound device
	 * @param newInputSystem  the new input system
	 * @param newTimeProvider the new time provider
	 */
  public Nifty(
      @Nonnull final RenderDevice newRenderDevice,
      @Nonnull final SoundDevice newSoundDevice,
      @Nonnull final InputSystem newInputSystem,
      @Nonnull final TimeProvider newTimeProvider) {
    screens = new HashMap<String, Screen>();
    popupTypes = new HashMap<String, PopupType>();
    popups = new HashMap<String, Element>();
    styles = new HashMap<String, StyleType>();
    controlDefinitions = new HashMap<String, ControlDefinitionType>();
    registeredEffects = new HashMap<String, RegisterEffectType>();
    registeredScreenControllers = new HashMap<String, ScreenController>();
    controllerFactory = new ControllerFactory();
    controlStylesChanged = new HashSet<String>();

    delayedMethodInvokes = new FlipFlop<List<DelayedMethodInvoke>>(
        new ArrayList<DelayedMethodInvoke>(), new ArrayList<DelayedMethodInvoke>());
    endOfFrameElementActions = new FlipFlop<List<EndOfFrameElementAction>>(
        new ArrayList<EndOfFrameElementAction>(), new ArrayList<EndOfFrameElementAction>());

    resourceLoader = new NiftyResourceLoader();

    newRenderDevice.setResourceLoader(resourceLoader);
    newSoundDevice.setResourceLoader(resourceLoader);
    newInputSystem.setResourceLoader(resourceLoader);

    renderEngine = new NiftyRenderEngineImpl(newRenderDevice);
    soundSystem = new SoundSystem(newSoundDevice);
    inputSystem = newInputSystem;
    timeProvider = newTimeProvider;

    mouseInputEventProcessor = new MouseInputEventProcessor();
    niftyMouse = new NiftyMouseImpl(newRenderDevice, newInputSystem, newTimeProvider);
    loader = new NiftyLoader(this, timeProvider);

    locale = Locale.getDefault();

    try {
      Class.forName("java.awt.datatransfer.Clipboard", false, Nifty.class.getClassLoader());
      clipboard = new ClipboardAWT();
    } catch (Throwable e) {
      log.warning("unable to access class 'java.awt.datatransfer.Clipboard'. clipboard will be disabled.");
      clipboard = new ClipboardInternal();
    }

    initializeLoaderSchemas();
    NiftyDefaults.initDefaultEffects(this);
    initializeEventBus();

    lastTime = timeProvider.getMsTime();
  }

  /**
	 * Gets the version.
	 *
	 * @return the version
	 */
  public String getVersion() {
    String result = "N/A";
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    InputStream stream = Nifty.class.getClassLoader().getResourceAsStream("version");
    try {
      byte[] buffer = new byte[1024];
      int len;
      while ((len = stream.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
      result = out.toString("ISO-8859-1");
    } catch (Exception e) {
      log.log(Level.WARNING, "unable to read version file from classpath", e);
    } finally {
      try {
        if (stream != null) {
          stream.close();
        }
      } catch (IOException e) {
        log.log(Level.WARNING, "unable to close version file from classpath stream. this is a bit odd", e);
      }
    }
    return result;
  }

  /**
   * Fetch the schema files the loader uses to validate the XML files.
   */
  private void initializeLoaderSchemas() {
    loaderLoadSchema("nifty.nxs");
    loaderLoadSchema("nifty-styles.nxs");
    loaderLoadSchema("nifty-controls.nxs");
  }

  /**
	 * Loader load schema.
	 *
	 * @param schemaName the schema name
	 */
  private void loaderLoadSchema(@Nonnull final String schemaName) {
    try {
      final InputStream stream = getResourceAsStream(schemaName);
      if (stream == null) {
        throw new IOException("Failed to open stream to schema resource \"" + schemaName + "\".");
      }
      loader.registerSchema(schemaName, stream);
    } catch (Exception e) {
      log.log(Level.SEVERE, "Failed to load the schema \"" + schemaName + "\" for the NiftyLoader", e);
    }
  }

  /**
	 * Initialize event bus.
	 */
  private void initializeEventBus() {
    try {
      if (EventServiceLocator.getEventService("NiftyEventBus") == null) {
        EventServiceLocator.setEventService("NiftyEventBus", new ThreadSafeEventService());
      }
    } catch (EventServiceExistsException e) {
      log.log(Level.SEVERE, "Initialization failure. EventBus failed to initialize.", e);
    }
  }

  /**
	 * Gets the event service.
	 *
	 * @return the event service
	 */
  @Nonnull
  public EventService getEventService() {
    @Nullable EventService service = EventServiceLocator.getEventService("NiftyEventBus");
    if (service == null) {
      log.severe("NiftyEventBus service was not found. Problem during initialization is likely.");
      return EventServiceLocator.getEventBusService();
    }
    return service;
  }

  /**
	 * Publish event.
	 *
	 * @param id    the id
	 * @param event the event
	 */
  public void publishEvent(@Nonnull final String id, @Nonnull final NiftyEvent event) {
    getEventService().publish(id, event);
  }

  /**
	 * Subscribe annotations.
	 *
	 * @param object the object
	 */
  public void subscribeAnnotations(@Nonnull final Object object) {
    NiftyEventAnnotationProcessor.process(object);
  }

  /**
	 * Unsubscribe annotations.
	 *
	 * @param object the object
	 */
  public void unsubscribeAnnotations(@Nonnull final Object object) {
    NiftyEventAnnotationProcessor.unprocess(object);
  }

  /**
	 * Subscribe.
	 *
	 * @param <T>        the generic type
	 * @param <S>        the generic type
	 * @param screen     the screen
	 * @param elementId  the element id
	 * @param eventClass the event class
	 * @param subscriber the subscriber
	 */
  public <T, S extends EventTopicSubscriber<? extends T>> void subscribe(
      @Nonnull final Screen screen,
      @Nonnull final String elementId,
      @Nonnull final Class<T> eventClass,
      @Nonnull final S subscriber) {
    ClassSaveEventTopicSubscriber theSubscriber = new ClassSaveEventTopicSubscriber(elementId, subscriber, eventClass);
    getEventService().subscribeStrongly(elementId, theSubscriber);
    log.fine("-> subscribe [" + elementId + "] screen [" + screen + "] -> [" + theSubscriber + "(" + subscriber + ")," +
        "(" + eventClass + ")]");

    subscriberRegister.register(screen, elementId, theSubscriber);
  }

  /**
	 * Unsubscribe.
	 *
	 * @param elementId the element id
	 * @param object    the object
	 */
  public void unsubscribe(@Nullable final String elementId, final Object object) {
    // This handles direct subscription
    if (object instanceof EventTopicSubscriber<?>) {
      if (elementId == null) {
        log.warning("trying to unsubscribe events for an element with elementId = null. this won't work. offending " +
            "object \"" + object + "\". try to find the offending element and give it an id!");
        return;
      }
      getEventService().unsubscribe(elementId, (EventTopicSubscriber<?>) object);
      log.fine("<- unsubscribe [" + elementId + "] -> [" + object + "]");
    }
  }

  /**
	 * Unsubscribe screen.
	 *
	 * @param screen the screen
	 */
  public void unsubscribeScreen(@Nonnull final Screen screen) {
    subscriberRegister.unsubscribeScreen(screen);
  }

  /**
	 * Unsubscribe element.
	 *
	 * @param screen    the screen
	 * @param elementId the element id
	 */
  public void unsubscribeElement(@Nonnull final Screen screen, @Nonnull final String elementId) {
    subscriberRegister.unsubscribeElement(screen, elementId);
  }

  /**
	 * Sets the alternate key for next load xml.
	 *
	 * @param alternateKeyForNextLoadXmlParam the new alternate key for next load
	 *                                        xml
	 */
  public void setAlternateKeyForNextLoadXml(@Nullable final String alternateKeyForNextLoadXmlParam) {
    alternateKeyForNextLoadXml = alternateKeyForNextLoadXmlParam;
  }

  /**
   * Update Nifty.
   *
   * @return true when nifty has finished processing the screen and false when rendering should continue.
   */
  public boolean update() {
    if (currentScreen != null) {
      mouseInputEventProcessor.begin();
      inputSystem.forwardEvents(niftyInputConsumer);
      if (mouseInputEventProcessor.hasLastMouseDownEvent()) {
        forwardMouseEventToScreen(mouseInputEventProcessor.getLastMouseDownEvent(), currentScreen);
      }
    }
    handleDynamicElements();
    updateSoundSystem();
    if (currentScreen != null) {
      if (log.isLoggable(Level.FINEST)) {
        log.finest(currentScreen.debugOutput());
      } else if (log.isLoggable(Level.FINER)) {
        log.fine(currentScreen.debugOutputFocusElements());
      }
    }
    return exit;
  }

  /**
	 * Forward mouse event to screen.
	 *
	 * @param mouseEvent the mouse event
	 * @param screen     the screen
	 * @return true, if successful
	 */
  private boolean forwardMouseEventToScreen(
      @Nonnull final NiftyMouseInputEvent mouseEvent,
      @Nonnull final Screen screen) {
    // update the nifty mouse that keeps track of the current mouse position too
    niftyMouse.updateMousePosition(mouseEvent.getMouseX(), mouseEvent.getMouseY());

    // and forward the event to the current screen
    return screen.mouseEvent(mouseEvent);
  }

  /**
   * Render Nifty.
   *
   * @param clearScreen true if nifty should clean the screen and false when you've done that already.
   */
  public void render(final boolean clearScreen) {
    renderEngine.beginFrame();
    if (clearScreen) {
      renderEngine.clear();
    }
    renderEngine.applyAbsoluteClip();
    if (currentScreen != null) {
      currentScreen.renderLayers(renderEngine);
    }

    if (exit) {
      renderEngine.clear();
    }
    renderEngine.endFrame();

    // now that the frame is complete we can reset the render device in case of the resolution change
    if (resolutionChanged) {
      resolutionChanged = false;
      displayResolutionChanged();
    }
  }

  /**
	 * Update sound system.
	 */
  private void updateSoundSystem() {
    long current = timeProvider.getMsTime();
    int delta = (int) (current - lastTime);
    soundSystem.update(delta);
    lastTime = current;
  }

  /**
	 * Reset mouse input events.
	 */
  public void resetMouseInputEvents() {
    niftyInputConsumer.resetMouseDown();
    mouseInputEventProcessor.reset();
    if (currentScreen != null) {
      currentScreen.resetMouseDown();
    }
  }

  /**
	 * Handle dynamic elements.
	 */
  private void handleDynamicElements() {
    while (hasDynamics()) {
      invokeMethods();
      closePopUps();
      removeLayerElements();
      executeEndOfFrameElementActionsInternal();
    }
  }

  /**
	 * Checks for dynamics.
	 *
	 * @return true, if successful
	 */
  private boolean hasDynamics() {
    return hasInvokeMethods() || hasClosePopups() || hasRemoveLayerElements() || hasEndOfFrameElementActions();
  }

  /**
	 * Checks for remove layer elements.
	 *
	 * @return true, if successful
	 */
  private boolean hasRemoveLayerElements() {
    if (currentScreen == null) {
      return false;
    }
    return currentScreen.hasDynamicElements();
  }

  /**
	 * Removes the layer elements.
	 */
  private void removeLayerElements() {
    if (currentScreen != null) {
      currentScreen.processAddAndRemoveLayerElements();
    }
  }

  /**
	 * Checks for close popups.
	 *
	 * @return true, if successful
	 */
  private boolean hasClosePopups() {
    return !closePopupList.isEmpty();
  }

  /**
	 * Close pop ups.
	 */
  private void closePopUps() {
    if (hasClosePopups()) {
      if (currentScreen == null) {
        closePopupList.clear();
        return;
      }
      ArrayList<ClosePopUp> copy = new ArrayList<ClosePopUp>(closePopupList);
      closePopupList.clear();

      for (int i = 0; i < copy.size(); i++) {
        ClosePopUp closePopup = copy.get(i);
        closePopup.close();
      }
    }
  }

  /**
	 * Execute end of frame element actions internal.
	 */
  private void executeEndOfFrameElementActionsInternal() {
    if (hasEndOfFrameElementActions()) {
      endOfFrameElementActions.flip();
      final List<EndOfFrameElementAction> workingCopy = endOfFrameElementActions.getSecond();

      final int size = workingCopy.size();
      for (int i = 0; i < size; i++) {
        workingCopy.get(i).perform();
      }
      workingCopy.clear();
    }
  }

  /**
	 * Execute end of frame element actions.
	 *
	 * @deprecated Calling this function from anywhere outside Nifty is a bad idea
	 *             in all cases. Nothing good comes from it.
	 */
  @Deprecated
  public void executeEndOfFrameElementActions() {
    log.warning("executeEndOfFrameElementActions() is a method that is basically the root of all evil. If you need " +
        "to use it, your application most likely has a real bad design flaw. The trouble you can cause using this " +
        "function is... big.");
    executeEndOfFrameElementActionsInternal();
  }

  /**
	 * Checks for end of frame element actions.
	 *
	 * @return true, if successful
	 */
  private boolean hasEndOfFrameElementActions() {
    return !endOfFrameElementActions.getFirst().isEmpty();
  }

  /**
   * Initialize this Nifty instance from the given xml file.
   *
   * @param filename    filename to nifty xml
   * @param startScreen screen to start exec
   */
  public void fromXml(@Nonnull final String filename, @Nonnull final String startScreen) {
    prepareScreens(filename);
    loadFromFile(filename);
    gotoScreen(startScreen);
  }

  /**
   * Initialize this Nifty instance from the given xml file.
   *
   * @param filename filename to nifty xml
   */
  public void fromXmlWithoutStartScreen(@Nonnull final String filename) {
    prepareScreens(filename);
    loadFromFile(filename);
  }

  /**
   * Initialize this Nifty instance from the given xml file.
   *
   * @param filename    filename to nifty xml
   * @param startScreen screen to start exec
   * @param controllers controllers to use
   */
  public void fromXml(
      @Nonnull final String filename,
      @Nonnull final String startScreen,
      final ScreenController... controllers) {
    registerScreenController(controllers);
    prepareScreens(filename);
    loadFromFile(filename);
    gotoScreen(startScreen);
  }

  /**
   * fromXml.
   *
   * @param fileId      fileId
   * @param input       inputStream
   * @param startScreen screen to start
   */
  public void fromXml(
      @Nonnull final String fileId,
      @Nonnull final InputStream input,
      @Nonnull final String startScreen) {
    prepareScreens(fileId);
    loadFromStream(input);
    gotoScreen(startScreen);
  }

  /**
   * fromXmlWithoutStartScreen.
   *
   * @param fileId fileId
   * @param input  inputStream
   */
  public void fromXmlWithoutStartScreen(@Nonnull final String fileId, @Nonnull final InputStream input) {
    prepareScreens(fileId);
    loadFromStream(input);
  }

  /**
   * fromXml with ScreenControllers.
   *
   * @param fileId      fileId
   * @param input       inputStream
   * @param startScreen screen to start
   * @param controllers controllers to use
   */
  public void fromXml(
      @Nonnull final String fileId,
      @Nonnull final InputStream input,
      @Nonnull final String startScreen,
      @Nonnull final ScreenController... controllers) {
    registerScreenController(controllers);
    prepareScreens(fileId);
    loadFromStream(input);
    gotoScreen(startScreen);
  }

  /**
   * Load an additional xml file without removing any of the data that might already been loaded.
   *
   * @param filename the file to load
   */
  public void addXml(@Nonnull final String filename) {
    loadFromFile(filename);
  }

  /**
   * Load an additional xml from a stream without removing any of the data that might already been loaded.
   *
   * @param stream the stream to load
   */
  public void addXml(@Nonnull @WillClose final InputStream stream) {
    loadFromStream(stream);
  }

  /**
   * Load and validate the given filename. If the file is valid, nothing happens. If it
   * is invalid you'll get an exception explaining the error.
   *
   * @param filename filename to check
   * @throws Exception exception describing the error
   */
  public void validateXml(@Nonnull final String filename) throws Exception {
    final InputStream stream = getResourceAsStream(filename);
    if (stream == null) {
      throw new IOException("Failed to open stream to resource \"" + filename + "\" for validating.");
    }
    validateXml(stream);
  }

  /**
   * Load and validate the given stream. If the stream is valid, nothing happens. If it
   * is invalid you'll get an exception explaining the error.
   *
   * @param stream the stream of the XML to check
   * @throws Exception exception describing the error
   */
  public void validateXml(@Nonnull @WillClose final InputStream stream) throws Exception {
    loader.validateNiftyXml("nifty.xsd", stream);
  }

  /**
   * load from the given file.
   *
   * @param filename filename to load
   */
  void loadFromFile(@Nonnull final String filename) {
    log.fine("loadFromFile [" + filename + "]");

    try {
      long start = timeProvider.getMsTime();
      final InputStream stream = getResourceAsStream(filename);
      if (stream == null) {
        throw new IOException("Failed to open stream to resource \"" + filename + "\" for loading.");
      }
      NiftyType niftyType = loader.loadNiftyXml("nifty.nxs", stream);
      niftyType.create(this, timeProvider);
      if (log.isLoggable(Level.FINE)) {
        log.fine(niftyType.output());
      }
      long end = timeProvider.getMsTime();
      log.fine("loadFromFile took [" + (end - start) + "]");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * load from the given file.
   *
   * @param stream stream to load
   */
  void loadFromStream(@Nonnull @WillClose final InputStream stream) {
    log.fine("loadFromStream []");

    try {
      long start = timeProvider.getMsTime();
      NiftyType niftyType = loader.loadNiftyXml("nifty.nxs", stream);
      niftyType.create(this, timeProvider);
      if (log.isLoggable(Level.FINE)) {
        log.fine(niftyType.output());
      }
      long end = timeProvider.getMsTime();
      log.fine("loadFromStream took [" + (end - start) + "]");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * prepare/reset screens.
   *
   * @param xmlId xml id
   */
  void prepareScreens(@Nonnull final String xmlId) {
    renderEngine.screensClear(screens.values());
    screens.clear();

    // this.currentScreen = null;
    this.currentLoaded = xmlId;
    this.exit = false;
  }

  /**
   * goto screen command. this will send first an endScreen event to the current screen.
   *
   * @param id the new screen id we should go to.
   */
  public void gotoScreen(@Nonnull final String id) {
    if (gotoScreenInProgress) {
      log.fine("gotoScreen [" + id + "] aborted because still in gotoScreenInProgress phase");
      return;
    }

    log.fine("gotoScreen [" + id + "]");
    gotoScreenInProgress = true;

    if (currentScreen == null) {
      gotoScreenInternal(id);
    } else {
      // end current screen
      currentScreen.endScreen(new EndNotify() {
        @Override
        public void perform() {
          gotoScreenInternal(id);
        }
      });
    }
  }

  /**
   * goto new screen.
   *
   * @param id the new screen id we should go to.
   */
  private void gotoScreenInternal(@Nonnull final String id) {
    log.fine("gotoScreenInternal [" + id + "]");

    // When someone calls nifty.closePopup() directly followed by a nifty.gotoScreen() the gotoScreen will now win and
    // we don't wait for the pending Popups to be closed. We'll simply remove the close Popup events since they would be
    // gone anyway on the new Screen. This is done because the close popups are handled at the end of frame when we
    // might already be on the new Screen.
    //
    // If the user wants to actually see the popup to be closed (maybe because he has added some effects) then now he'll
    // use the closePopup() method with the EndNotify and call nifty.gotoScreen() when the EndNotify fires.
    if (hasClosePopups()) {
      ArrayList<ClosePopUp> copy = new ArrayList<ClosePopUp>(closePopupList);
      closePopupList.clear();

      for (int i = 0; i < copy.size(); i++) {
        ClosePopUp closePopup = copy.get(i);
        closePopup.forcedCloseWithoutEndNotify();
      }
    }

    currentScreen = screens.get(id);
    if (currentScreen == null) {
      log.warning("screen [" + id + "] not found");
      gotoScreenInProgress = false;
      return;
    }

    // start the new screen
    if (alternateKeyForNextLoadXml != null) {
      currentScreen.setAlternateKey(alternateKeyForNextLoadXml);
      alternateKeyForNextLoadXml = null;
    }
    currentScreen.startScreen(new EndNotify() {
      @Override
      public void perform() {
        gotoScreenInProgress = false;
      }
    });
  }

  /**
   * Set alternate key for all screen. This could be used to change behavior on all screens.
   *
   * @param alternateKey the new alternate key to use
   */
  public void setAlternateKey(@Nullable final String alternateKey) {
    this.alternateKey = alternateKey;
    for (Screen screen : screens.values()) {
      screen.setAlternateKey(alternateKey);
    }
  }

  /**
	 * Returns a collection of the name of all screens.
	 *
	 * @return sn The collection containing the name of all screens
	 */
  @Nonnull
  public Collection<String> getAllScreensName() {
    Collection<String> sn = new LinkedList<String>();
    for (Screen screen : screens.values()) {
      sn.add(screen.getScreenId());
    }
    return sn;
  }

  /**
	 * Removes the screen.
	 *
	 * @param id the id
	 */
  public void removeScreen(@Nonnull final String id) {
    if (currentScreen != null) {
      if (currentScreen.getScreenId().equals(id)) {
        currentScreen.endScreen(new EndNotify() {
          @Override
          public void perform() {
            currentScreen = null;
            removeScreenInternal(id);
          }
        });
        return;
      }
      removeScreenInternal(id);
    }
  }

  /**
	 * Removes the screen internal.
	 *
	 * @param id the id
	 */
  private void removeScreenInternal(@Nonnull final String id) {
    Screen screen = screens.remove(id);
    if (screen == null) {
      log.log(Level.SEVERE, "Internal delete of screen \"" + id + "\" failed: Screen instance not found.");
    } else {
      renderEngine.screenRemoved(screen);
      if (screen.getLayerElements().size() == 0) {
        return;
      }
      for (int i = 0; i < screen.getLayerElements().size(); i++) {
        removeElement(screen, screen.getLayerElements().get(i));
      }
    }
  }

  /**
   * This returns all the style names currently registered with nifty.
   *
   * @return Collection of all style names
   */
  @Nonnull
  public Collection<String> getAllStylesName() {
    return styles.keySet();
  }

  /**
   * exit.
   */
  public void exit() {
    if (currentScreen == null) {
      return;
    }
    currentScreen.endScreen(
        new EndNotify() {
          @Override
          public final void perform() {
            exit = true;
            currentScreen = null;
          }
        });
  }

  /**
	 * Resolution changed.
	 */
  public void resolutionChanged() {
    resolutionChanged = true;
  }

  /**
	 * Display resolution changed.
	 */
  private void displayResolutionChanged() {
    getRenderEngine().displayResolutionChanged();

    resetMouseInputEvents();

    int newWidth = getRenderEngine().getWidth();
    int newHeight = getRenderEngine().getHeight();

    for (Screen screen : screens.values()) {
      updateLayoutPart(screen.getRootElement().getLayoutPart(), newWidth, newHeight);
      for (Element e : screen.getLayerElements()) {
        updateLayoutPart(e.getLayoutPart(), newWidth, newHeight);
      }
      screen.resetLayout();
    }

    for (Element e : popups.values()) {
      updateLayoutPart(e.getLayoutPart(), newWidth, newHeight);
    }

    if (currentScreen != null) {
      currentScreen.layoutLayers();
    }
  }

  /**
	 * Update layout part.
	 *
	 * @param layoutPart the layout part
	 * @param width      the width
	 * @param height     the height
	 */
  private void updateLayoutPart(@Nonnull final LayoutPart layoutPart, final int width, final int height) {
    Box box = layoutPart.getBox();
    box.setWidth(width);
    box.setHeight(height);
    BoxConstraints boxConstraints = layoutPart.getBoxConstraints();
    boxConstraints.setWidth(SizeValue.px(width));
    boxConstraints.setHeight(SizeValue.px(height));
  }

  /**
   * get a specific screen.
   *
   * @param id the id of the screen to retrieve.
   * @return the screen
   */
  @Nullable
  public Screen getScreen(@Nonnull final String id) {
    Screen screen = screens.get(id);
    if (screen == null) {
      log.warning("screen [" + id + "] not found");
      return null;
    }

    return screen;
  }

  /**
   * Get the SoundSystem.
   *
   * @return SoundSystem
   */
  @Nonnull
  public SoundSystem getSoundSystem() {
    return soundSystem;
  }

  /**
   * Return the RenderDevice.
   *
   * @return RenderDevice
   */
  @Nonnull
  public NiftyRenderEngine getRenderEngine() {
    return renderEngine;
  }

  /**
   * Get current screen.
   *
   * @return current screen
   */
  @Nullable
  public Screen getCurrentScreen() {
    return currentScreen;
  }

  /**
   * Check if nifty displays the file with the given filename and is at a screen with the given screenId.
   *
   * @param filename filename
   * @param screenId screenId
   * @return true if the given screen is active and false when not
   */
  public boolean isActive(@Nonnull final String filename, @Nonnull final String screenId) {
    if (currentLoaded != null && currentLoaded.equals(filename)) {
      if ((currentScreen != null) && currentScreen.getScreenId().equals(screenId)) {
        return true;
      }
    }
    return false;
  }

  /**
   * popup.
   *
   * @param popup popup
   */
  public void registerPopup(@Nonnull final PopupType popup) {
    popupTypes.put(popup.getAttributes().get("id"), popup);
  }

  /**
	 * show popup in the given screen.
	 *
	 * @param screen              screen
	 * @param id                  id
	 * @param defaultFocusElement the default focus element
	 */
  public void showPopup(
      @Nonnull final Screen screen,
      @Nonnull final String id,
      @Nullable final Element defaultFocusElement) {
    @Nullable Element popup = popups.get(id);
    if (popup == null) {
      log.warning("missing popup [" + id + "] o_O");
    } else {
      screen.addPopup(popup, defaultFocusElement);
    }
  }

  /**
   * Create a popup from its type parameters.
   *
   * @param screen         the screen the popup is supposed to be shown on
   * @param popupTypeParam the type parameters
   * @param id             the id of the popup
   * @return the newly created popup or {@code null} in case there is currently no active screen that could receive
   * the popup
   */
  @Nonnull
  private Element createPopupFromType(
      @Nonnull final Screen screen,
      @Nonnull final PopupType popupTypeParam,
      @Nonnull final String id) {
    LayoutPart layerLayout = rootLayerFactory.createRootLayerLayoutPart(this);
    PopupType popupType = new PopupType(popupTypeParam);

    popupType.prepare(this, screen, screen.getRootElement().getElementType());
    Element element = popupType.create(screen.getRootElement(), this, screen, layerLayout);
    element.setId(id);
    fixupSubIds(element, id);
    if (screen.isBound()) {
      element.layoutElements();
      element.bindControls(screen);
    }
    return element;
  }

  /**
	 * Fixup sub ids.
	 *
	 * @param element  the element
	 * @param parentId the parent id
	 */
  private void fixupSubIds(@Nonnull final Element element, @Nonnull final String parentId) {
    String currentId = element.getId();
    if (currentId != null && currentId.startsWith("#")) {
      currentId = parentId + currentId;
      element.setId(currentId);
    }
    if (currentId == null) {
      currentId = parentId;
    }
    for (int i = 0; i < element.getChildren().size(); i++) {
      Element e = element.getChildren().get(i);
      fixupSubIds(e, currentId);
    }
  }

  /**
	 * Creates the popup.
	 *
	 * @param popupId the popup id
	 * @return the element
	 */
  @Nullable
  public Element createPopup(@Nonnull final String popupId) {
    return createPopupWithId(popupId, NiftyIdCreator.generate());
  }

  /**
	 * Creates the popup.
	 *
	 * @param screen  the screen
	 * @param popupId the popup id
	 * @return the element
	 */
  @Nonnull
  public Element createPopup(@Nonnull final Screen screen, @Nonnull final String popupId) {
    return createPopupWithId(screen, popupId, NiftyIdCreator.generate());
  }

  /**
	 * Creates the popup with id.
	 *
	 * @param popupId the popup id
	 * @param id      the id
	 * @return the element
	 */
  @Nullable
  public Element createPopupWithId(@Nonnull final String popupId, @Nonnull final String id) {
    return createPopupWithStyle(popupId, id, null, null);
  }

  /**
	 * Creates the popup with id.
	 *
	 * @param screen  the screen
	 * @param popupId the popup id
	 * @param id      the id
	 * @return the element
	 */
  @Nonnull
  public Element createPopupWithId(
      @Nonnull final Screen screen,
      @Nonnull final String popupId,
      @Nonnull final String id) {
    return createPopupWithStyle(screen, popupId, id, null, null);
  }

  /**
	 * Creates the popup with style.
	 *
	 * @param popupId the popup id
	 * @param style   the style
	 * @return the element
	 */
  @Nullable
  public Element createPopupWithStyle(@Nonnull final String popupId, @Nullable final String style) {
    return createPopupWithStyle(popupId, NiftyIdCreator.generate(), style);
  }

  /**
	 * Creates the popup with style.
	 *
	 * @param screen  the screen
	 * @param popupId the popup id
	 * @param style   the style
	 * @return the element
	 */
  @Nonnull
  public Element createPopupWithStyle(
      @Nonnull final Screen screen,
      @Nonnull final String popupId,
      @Nullable final String style) {
    return createPopupWithStyle(screen, popupId, NiftyIdCreator.generate(), style);
  }

  /**
	 * Creates the popup with style.
	 *
	 * @param popupId the popup id
	 * @param id      the id
	 * @param style   the style
	 * @return the element
	 */
  @Nullable
  public Element createPopupWithStyle(
      @Nonnull final String popupId,
      @Nonnull final String id,
      @Nullable final String style) {
    return createPopupWithStyle(popupId, id, style, null);
  }

  /**
	 * Creates the popup with style.
	 *
	 * @param screen  the screen
	 * @param popupId the popup id
	 * @param id      the id
	 * @param style   the style
	 * @return the element
	 */
  @Nonnull
  public Element createPopupWithStyle(
      @Nonnull final Screen screen,
      @Nonnull final String popupId,
      @Nonnull final String id,
      @Nullable final String style) {
    return createPopupWithStyle(screen, popupId, id, style, null);
  }

  /**
	 * Creates the popup with style.
	 *
	 * @param popupId    the popup id
	 * @param style      the style
	 * @param parameters the parameters
	 * @return the element
	 */
  @Nullable
  public Element createPopupWithStyle(
      @Nonnull final String popupId,
      @Nullable final String style,
      @Nullable final Attributes parameters) {
    return createPopupWithStyle(popupId, NiftyIdCreator.generate(), style, parameters);
  }

  /**
	 * Creates the popup with style.
	 *
	 * @param screen     the screen
	 * @param popupId    the popup id
	 * @param style      the style
	 * @param parameters the parameters
	 * @return the element
	 */
  @Nonnull
  public Element createPopupWithStyle(
      @Nonnull final Screen screen,
      @Nonnull final String popupId,
      @Nullable final String style,
      @Nullable final Attributes parameters) {
    return createPopupWithStyle(screen, popupId, NiftyIdCreator.generate(), style, parameters);
  }

  /**
	 * Creates the popup with style.
	 *
	 * @param popupId    the popup id
	 * @param id         the id
	 * @param style      the style
	 * @param parameters the parameters
	 * @return the element
	 */
  @Nullable
  public Element createPopupWithStyle(
      @Nonnull final String popupId,
      @Nonnull final String id,
      @Nullable final String style,
      @Nullable final Attributes parameters) {
    final Screen screen = getCurrentScreen();
    if (screen == null) {
      return null;
    }
    return createPopupWithStyle(screen, popupId, id, style, parameters);
  }


  /**
	 * Creates the popup with style.
	 *
	 * @param screen     the screen
	 * @param popupId    the popup id
	 * @param id         the id
	 * @param style      the style
	 * @param parameters the parameters
	 * @return the element
	 */
  @Nonnull
  public Element createPopupWithStyle(
      @Nonnull final Screen screen,
      @Nonnull final String popupId,
      @Nonnull final String id,
      @Nullable final String style,
      @Nullable final Attributes parameters) {
    @Nullable PopupType popupType = popupTypes.get(popupId);
    if (popupType == null) {
      throw new IllegalArgumentException("Popup ID \"" + popupId + "\" can't be matched to a popup type.");
    }
    popupType = popupType.copy();

    if (style != null) {
      popupType.getAttributes().set("style", style);
    }
    if (parameters != null) {
      popupType.getAttributes().merge(parameters);
    }
    return createAndAddPopup(screen, id, popupType);
  }

  /**
	 * Creates the and add popup.
	 *
	 * @param screen    the screen
	 * @param id        the id
	 * @param popupType the popup type
	 * @return the element
	 */
  @Nonnull
  private Element createAndAddPopup(
      @Nonnull final Screen screen,
      @Nonnull final String id,
      @Nonnull final PopupType popupType) {
    Element popupElement = createPopupFromType(screen, popupType, id);
    popups.put(id, popupElement);
    return popupElement;
  }

  /**
	 * Find popup by name.
	 *
	 * @param id the id
	 * @return the element
	 */
  public Element findPopupByName(final String id) {
    return popups.get(id);
  }

  /**
	 * Gets the top most popup.
	 *
	 * @return the top most popup
	 */
  @Nullable
  public Element getTopMostPopup() {
    if (currentScreen != null) {
      return currentScreen.getTopMostPopup();
    }
    return null;
  }

  /**
   * Close the Popup with the given id.
   *
   * @param id id of popup to close
   */
  public void closePopup(@Nonnull final String id) {
    closePopupInternal(id, null);
  }

  /**
   * Close the Popup with the given id. This calls the given EndNotify when the onEndScreen of the popup ends.
   *
   * @param id          id of popup to close
   * @param closeNotify EndNotify callback
   */
  public void closePopup(@Nonnull final String id, @Nullable final EndNotify closeNotify) {
    closePopupInternal(id, closeNotify);
  }

  /**
	 * Close popup internal.
	 *
	 * @param id          the id
	 * @param closeNotify the close notify
	 */
  private void closePopupInternal(@Nonnull final String id, @Nullable final EndNotify closeNotify) {
    Element popup = popups.get(id);
    if (popup == null) {
      log.warning("missing popup [" + id + "] o_O");
      return;
    }

    if (closedPopups.contains(id)) {
      log.fine("popup [" + id + "] already scheduled to be closed. Additional close call ignored.");
      return;
    }
    closedPopups.add(id);

    popup.resetAllEffects();
    popup.startEffect(EffectEventId.onEndScreen, new EndNotify() {
      @Override
      public void perform() {
        closePopupList.add(new ClosePopUp(id, closeNotify));
      }
    });
  }

  /**
   * Add a new control.
   *
   * @param screen          the screen the control is connected to
   * @param parent          the parent element of the control
   * @param standardControl the standard control that acts as template
   * @return the newly created element
   */
  @Nonnull
  public Element addControl(
      @Nonnull final Screen screen,
      @Nonnull final Element parent,
      @Nonnull final StandardControl standardControl) {
    try {
      final Element newControl = standardControl.createControl(this, screen, parent);

      if (screen.isBound()) {
        newControl.bindControls(screen);
        newControl.initControls(false);
      }
      if (screen.isRunning()) {
        newControl.startEffect(EffectEventId.onStartScreen);
        newControl.startEffect(EffectEventId.onActive);
        newControl.onStartScreen();
      }
      return newControl;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
	 * Removes the element.
	 *
	 * @param screen  the screen
	 * @param element the element
	 */
  public void removeElement(@Nonnull final Screen screen, @Nonnull final Element element) {
    removeElement(screen, element, null);
  }

  /**
	 * Removes the element.
	 *
	 * @param screen    the screen
	 * @param element   the element
	 * @param endNotify the end notify
	 */
  public void removeElement(
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nullable final EndNotify endNotify) {
    element.startEffect(EffectEventId.onEndScreen, new EndNotify() {
      @Override
      public void perform() {
        scheduleEndOfFrameElementAction(new ElementRemoveAction(screen, element), endNotify);
      }
    });
  }

  /**
	 * Move element.
	 *
	 * @param screen        the screen
	 * @param elementToMove the element to move
	 * @param destination   the destination
	 * @param endNotify     the end notify
	 */
  public void moveElement(
      @Nonnull final Screen screen,
      @Nonnull final Element elementToMove,
      @Nonnull final Element destination,
      @Nullable final EndNotify endNotify) {
    elementToMove.removeFromFocusHandler();
    scheduleEndOfFrameElementAction(new ElementMoveAction(elementToMove, destination), endNotify);
  }

  /**
	 * Schedule end of frame element action.
	 *
	 * @param screen    the screen
	 * @param element   the element
	 * @param action    the action
	 * @param endNotify the end notify
	 * @deprecated Contains useless arguments, use
	 *             {@link #scheduleEndOfFrameElementAction(de.lessvoid.nifty.elements.Action, EndNotify)}
	 */
  @Deprecated
  public void scheduleEndOfFrameElementAction(
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Action action,
      @Nullable final EndNotify endNotify) {
    scheduleEndOfFrameElementAction(action, endNotify);
  }

  /**
	 * Schedule end of frame element action.
	 *
	 * @param action    the action
	 * @param endNotify the end notify
	 */
  public void scheduleEndOfFrameElementAction(
      @Nonnull final Action action,
      @Nullable final EndNotify endNotify) {
    endOfFrameElementActions.getFirst().add(new EndOfFrameElementAction(action, endNotify));
  }

  /**
	 * Gets the mouse input event queue.
	 *
	 * @return the mouseInputEventQueue
	 */
  @Nonnull
  public MouseInputEventProcessor getMouseInputEventQueue() {
    return mouseInputEventProcessor;
  }

  /**
   * Register ScreenController instances.
   *
   * @param controllers ScreenController
   */
  public void registerScreenController(@Nonnull final ScreenController... controllers) {
    final int size = controllers.length;
    for (int i = 0; i < size; i++) {
      @Nonnull final ScreenController c = controllers[i];
      registeredScreenControllers.put(c.getClass().getName(), c);
    }
  }

  /**
   * find a ScreenController instance that matches the given controllerClass name.
   *
   * @param controllerClass controller class name
   * @return ScreenController instance
   */
  @Nullable
  public ScreenController findScreenController(@Nonnull final String controllerClass) {
    return registeredScreenControllers.get(controllerClass);
  }

  /**
   * Remove screen controller instances.
   *
   * @param controllers the instances to remove
   */
  public void unregisterScreenController(@Nonnull final ScreenController... controllers) {
    final int size = controllers.length;
    for (int i = 0; i < size; i++) {
      @Nonnull final ScreenController c = controllers[i];
      registeredScreenControllers.remove(c.getClass().getName());
    }
  }

  /**
	 * Obtain the controllerFactory, used to register and unregister Factories that
	 * create controllers.
	 * <p>
	 *
	 * @return the controller factory
	 */
  @Nonnull
  public ControllerFactory getControllerFactory() {
    return controllerFactory;
  }

  /**
	 * Gets the loader.
	 *
	 * @return the loader
	 */
  @Nonnull
  public NiftyLoader getLoader() {
    return loader;
  }

  /**
	 * Gets the time provider.
	 *
	 * @return the time provider
	 */
  @Nonnull
  public TimeProvider getTimeProvider() {
    return timeProvider;
  }

  /**
	 * Refresh styles.
	 *
	 * @param styleIds the style ids
	 */
  private void refreshStyles(String... styleIds) {
    for (Screen screen : screens.values()) {
      for (Element e : screen.findElementsByStyle(styleIds)) {
        e.refreshStyle(e.getStyle());
      }
    }
  }

  /**
	 * The Class ClosePopUp.
	 */
  public class ClosePopUp {
    
    /** The remove popup id. */
    @Nonnull
    private final String removePopupId;
    
    /** The close notify. */
    @Nullable
    private final EndNotify closeNotify;

    /**
	 * Instantiates a new close pop up.
	 *
	 * @param popupId          the popup id
	 * @param closeNotifyParam the close notify param
	 */
    public ClosePopUp(@Nonnull final String popupId, @Nullable final EndNotify closeNotifyParam) {
      removePopupId = popupId;
      closeNotify = closeNotifyParam;
    }

    /**
	 * Close.
	 */
    public void close() {
      close(closeNotify);
    }

    /**
	 * Forced close without end notify.
	 */
    public void forcedCloseWithoutEndNotify() {
      close(null);
    }

    /**
	 * Close.
	 *
	 * @param endNotify the end notify
	 */
    private void close(@Nullable final EndNotify endNotify) {
      if (currentScreen != null) {
        @Nullable Element popup = popups.get(removePopupId);
        if (popup != null) {
          currentScreen.closePopup(popup, endNotify);
        }
      }
    }
  }

  /**
	 * Adds the screen.
	 *
	 * @param id     the id
	 * @param screen the screen
	 */
  public void addScreen(@Nonnull final String id, @Nonnull final Screen screen) {
    screens.put(id, screen);
    renderEngine.screenAdded(screen);
  }

  /**
	 * Register style.
	 *
	 * @param style the style
	 */
  public void registerStyle(@Nonnull final StyleType style) {
    final String styleId = style.getStyleId();
    log.log(Level.FINE, "registerStyle {0}", styleId);

    // Handle the simple, normal case.
    // This is a new style, register it and return early.
    if (!styles.containsKey(styleId)) {
      styles.put(styleId, style);
      return;
    }

    // This style has already been registered.
    if (styles.get(styleId).equals(style)) {
      return; // Identical re-register, skip
    }

    // Log a warning and re-register it, overriding the previous value.

    log.log(Level.WARNING, "Style: {0} was already registered. The new definition will override the previous.", styleId);

    styles.put(styleId, style);

    // Handle re-registration of control styles (containing a '#')
    // & regular styles.
    if (styleId.contains("#")) {
      // A sub-style has been changed. We need to take its base style name that
      // is the first part of the name of this style (basename#subname).
      final String simpleId = styleId.split("#")[0];
      // We could fire the event here, but in case that more than one sub-style
      // is changed we will send the same event for each. So we are batching it
      // here and then fire an event in loadStyles method.
      controlStylesChanged.add(simpleId);
    } else {
      // This is a regular style, so just fire the event now.
      refreshStyles(styleId);
    }
  }

  /**
	 * Register control defintion.
	 *
	 * @param controlDefinition the control definition
	 */
  public void registerControlDefintion(@Nonnull final ControlDefinitionType controlDefinition) {
    controlDefinitions.put(controlDefinition.getName(), controlDefinition);
    // TODO: add the same behaviour of register style and try to updating
    // already registered control defintions.
  }

  /**
	 * Register effect.
	 *
	 * @param registerEffectType the register effect type
	 */
  public void registerEffect(@Nonnull final RegisterEffectType registerEffectType) {
    registeredEffects.put(registerEffectType.getName(), registerEffectType);
  }

  /**
	 * Resolve control definition.
	 *
	 * @param name the name
	 * @return the control definition type
	 */
  @Nullable
  public ControlDefinitionType resolveControlDefinition(@Nullable final String name) {
    if (name == null) {
      return null;
    }
    return controlDefinitions.get(name);
  }

  /**
	 * Resolve registered effect.
	 *
	 * @param name the name
	 * @return the register effect type
	 */
  @Nullable
  public RegisterEffectType resolveRegisteredEffect(@Nullable final String name) {
    if (name == null) {
      return null;
    }
    return registeredEffects.get(name);
  }

  /**
	 * Gets the default style resolver.
	 *
	 * @return the default style resolver
	 */
  @Nonnull
  public StyleResolver getDefaultStyleResolver() {
    return new StyleResolverDefault(styles);
  }

  /**
	 * Gets the alternate key.
	 *
	 * @return the alternate key
	 */
  @Nullable
  public String getAlternateKey() {
    return alternateKey;
  }

  /**
	 * Delayed method invoke.
	 *
	 * @param method the method
	 * @param params the params
	 */
  public void delayedMethodInvoke(@Nonnull final NiftyDelayedMethodInvoke method, @Nonnull final Object... params) {
    delayedMethodInvokes.getFirst().add(new DelayedMethodInvoke(method, params));
  }

  /**
	 * Invoke methods.
	 */
  public void invokeMethods() {
    if (hasInvokeMethods()) {
      delayedMethodInvokes.flip();
      final List<DelayedMethodInvoke> workingList = delayedMethodInvokes.getSecond();

      // process the working copy
      final int count = workingList.size();
      for (int i = 0; i < count; i++) {
        workingList.get(i).perform();
      }

      // clear the secondary list
      workingList.clear();
    }
  }

  /**
	 * Checks for invoke methods.
	 *
	 * @return true, if successful
	 */
  private boolean hasInvokeMethods() {
    return !delayedMethodInvokes.getFirst().isEmpty();
  }

  /**
	 * The Class DelayedMethodInvoke.
	 */
  private static class DelayedMethodInvoke {
    
    /** The method. */
    @Nonnull
    private final NiftyDelayedMethodInvoke method;
    
    /** The params. */
    @Nonnull
    private final Object[] params;

    /**
	 * Instantiates a new delayed method invoke.
	 *
	 * @param method the method
	 * @param params the params
	 */
    public DelayedMethodInvoke(@Nonnull final NiftyDelayedMethodInvoke method, @Nonnull final Object... params) {
      this.method = method;
      this.params = params;
    }

    /**
	 * Perform.
	 */
    public void perform() {
      method.performInvoke(params);
    }
  }

  /**
	 * Sets the locale.
	 *
	 * @param locale the new locale
	 */
  public void setLocale(@Nonnull final Locale locale) {
    this.locale = locale;
    getEventService().publish(new NiftyLocaleChangedEvent(locale));

    if (resourceBundles.size() > 0) {
      log.log(Level.WARNING, "Changing the locale will not effect ALL loaded resource bundles. TextRenderer should work now tho :)");
    }
  }

  /**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
  public Locale getLocale() {
    return locale;
  }

  /**
	 * Gets the resource bundles.
	 *
	 * @return the resource bundles
	 */
  @Nonnull
  public Map<String, BundleInfo> getResourceBundles() {
    return resourceBundles;
  }

  /**
	 * Adds the resource bundle.
	 *
	 * @param id       the id
	 * @param filename the filename
	 */
  public void addResourceBundle(@Nonnull final String id, @Nonnull final String filename) {
    resourceBundles.put(id, new BundleInfoBasename(filename));
  }

  /**
	 * Adds the resource bundle.
	 *
	 * @param id             the id
	 * @param resourceBundle the resource bundle
	 */
  public void addResourceBundle(@Nonnull final String id, @Nonnull final ResourceBundle resourceBundle) {
    BundleInfo bundleInfo = resourceBundles.get(id);
    if (bundleInfo != null && bundleInfo instanceof BundleInfoResourceBundle) {
      ((BundleInfoResourceBundle) bundleInfo).add(resourceBundle);
      return;
    }
    resourceBundles.put(id, new BundleInfoResourceBundle(resourceBundle));
  }

  /**
	 * Gets the global properties.
	 *
	 * @return the global properties
	 */
  @Nullable
  public Properties getGlobalProperties() {
    return globalProperties;
  }

  /**
	 * Sets the global properties.
	 *
	 * @param globalProperties the new global properties
	 */
  public void setGlobalProperties(@Nullable final Properties globalProperties) {
    this.globalProperties = globalProperties;
  }

  /**
	 * Gets the root layer factory.
	 *
	 * @return the root layer factory
	 */
  @Nonnull
  public RootLayerFactory getRootLayerFactory() {
    return rootLayerFactory;
  }

  /**
	 * Load style file.
	 *
	 * @param styleFile the style file
	 */
  public void loadStyleFile(@Nonnull final String styleFile) {
    try {
      NiftyType niftyType = new NiftyType();
      loader.loadStyleFile("nifty-styles.nxs", styleFile, niftyType, this);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.FINE)) {
        log.fine("loadStyleFile");
        log.fine(niftyType.output());
      }
      refreshStyles(controlStylesChanged.toArray(new String[0]));
      controlStylesChanged.clear();
    } catch (Exception e) {
      log.log(Level.WARNING, e.getMessage(), e);
    }
  }

  /**
	 * Load control file.
	 *
	 * @param controlFile the control file
	 */
  public void loadControlFile(@Nonnull final String controlFile) {
    try {
      NiftyType niftyType = new NiftyType();
      loader.loadControlFile("nifty-controls.nxs", controlFile, niftyType);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.FINE)) {
        log.fine("loadControlFile");
        log.fine(niftyType.output());
      }
    } catch (Exception e) {
      log.log(Level.WARNING, e.getMessage(), e);
    }
  }

  /**
	 * Register resource bundle.
	 *
	 * @param id       the id
	 * @param filename the filename
	 */
  public void registerResourceBundle(@Nonnull final String id, @Nonnull final String filename) {
    try {
      NiftyType niftyType = new NiftyType();
      ResourceBundleType resourceBundle = new ResourceBundleType();
      resourceBundle.getAttributes().set("id", id);
      resourceBundle.getAttributes().set("filename", filename);
      niftyType.addResourceBundle(resourceBundle);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.FINE)) {
        log.fine("registerResourceBundle");
        log.fine(niftyType.output());
      }
    } catch (Exception e) {
      log.log(Level.WARNING, e.getMessage(), e);
    }
  }

  /**
	 * Register effect.
	 *
	 * @param name       the name
	 * @param classParam the class param
	 */
  public void registerEffect(@Nonnull final String name, @Nonnull final String classParam) {
    try {
      NiftyType niftyType = new NiftyType();
      RegisterEffectType registerEffect = new RegisterEffectType(name, classParam);
      niftyType.addRegisterEffect(registerEffect);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.FINE)) {
        log.fine("registerEffect");
        log.fine(niftyType.output());
      }
    } catch (Exception e) {
      log.log(Level.WARNING, e.getMessage(), e);
    }
  }

  /**
	 * Register sound.
	 *
	 * @param id       the id
	 * @param filename the filename
	 */
  public void registerSound(@Nonnull final String id, @Nonnull final String filename) {
    try {
      NiftyType niftyType = new NiftyType();
      RegisterSoundType registerSound = new RegisterSoundType();
      registerSound.getAttributes().set("id", id);
      registerSound.getAttributes().set("filename", filename);
      niftyType.addRegisterSound(registerSound);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.FINE)) {
        log.fine("registerSound");
        log.fine(niftyType.output());
      }
    } catch (Exception e) {
      log.log(Level.WARNING, e.getMessage(), e);
    }
  }

  /**
	 * Register music.
	 *
	 * @param id       the id
	 * @param filename the filename
	 */
  public void registerMusic(@Nonnull final String id, @Nonnull final String filename) {
    try {
      NiftyType niftyType = new NiftyType();
      RegisterMusicType registerMusic = new RegisterMusicType();
      registerMusic.getAttributes().set("id", id);
      registerMusic.getAttributes().set("filename", filename);
      niftyType.addRegisterMusic(registerMusic);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.FINE)) {
        log.fine("registerMusic");
        log.fine(niftyType.output());
      }
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }

  /**
	 * Register mouse cursor.
	 *
	 * @param id       the id
	 * @param filename the filename
	 * @param hotspotX the hotspot X
	 * @param hotspotY the hotspot Y
	 */
  public void registerMouseCursor(
      @Nonnull final String id,
      @Nonnull final String filename,
      final int hotspotX,
      final int hotspotY) {
    try {
      getNiftyMouse().registerMouseCursor(id, filename, hotspotX, hotspotY);
    } catch (IOException e) {
      log.log(Level.WARNING, e.getMessage(), e);
    }
  }

  /**
	 * Gets the nifty mouse.
	 *
	 * @return the nifty mouse
	 */
  @Nonnull
  public NiftyMouse getNiftyMouse() {
    return niftyMouse;
  }

  /**
   * This is now an inner class to make sure no one calls it from the outside directly.
   * All InputSystem processing should go through the InputSystem.
   *
   * @author void
   */
  private class NiftyInputConsumerImpl implements NiftyInputConsumer {
    
    /** The button 0 down. */
    private boolean button0Down = false;
    
    /** The button 1 down. */
    private boolean button1Down = false;
    
    /** The button 2 down. */
    private boolean button2Down = false;

    /**
	 * Process mouse event.
	 *
	 * @param mouseX     the mouse X
	 * @param mouseY     the mouse Y
	 * @param mouseWheel the mouse wheel
	 * @param button     the button
	 * @param buttonDown the button down
	 * @return true, if successful
	 */
    @Override
    public boolean processMouseEvent(
        final int mouseX,
        final int mouseY,
        final int mouseWheel,
        final int button,
        final boolean buttonDown) {
      boolean processed = false;
      if (!isIgnoreMouseEvents()) {
        processed = processEvent(createEvent(mouseX, mouseY, mouseWheel, button, buttonDown));
        if (log.isLoggable(Level.FINE)) {
          log.fine("[processMouseEvent] [" + mouseX + ", " + mouseY + ", " + mouseWheel + ", " + button + ", " +
              "" + buttonDown + "] processed [" + processed + "]");
        }
      }
      niftyInputConsumerNotify.processedMouseEvent(mouseX, mouseY, mouseWheel, button, buttonDown, processed);
      return processed;
    }

    /**
	 * Process keyboard event.
	 *
	 * @param keyEvent the key event
	 * @return true, if successful
	 */
    @Override
    public boolean processKeyboardEvent(@Nonnull final KeyboardInputEvent keyEvent) {
      boolean processed = false;
      if (!isIgnoreKeyboardEvents()) {
        if (currentScreen != null) {
          processed = currentScreen.keyEvent(keyEvent);
          if (log.isLoggable(Level.FINE)) {
            log.fine("[processKeyboardEvent] " + keyEvent + " processed [" + processed + "]");
          }
        }
      }
      niftyInputConsumerNotify.processKeyboardEvent(keyEvent, processed);
      return processed;
    }

    /**
	 * Reset mouse down.
	 */
    void resetMouseDown() {
      button0Down = false;
      button1Down = false;
      button2Down = false;
    }

    /**
	 * Creates the event.
	 *
	 * @param mouseX     the mouse X
	 * @param mouseY     the mouse Y
	 * @param mouseWheel the mouse wheel
	 * @param button     the button
	 * @param buttonDown the button down
	 * @return the nifty mouse input event
	 */
    @Nonnull
    private NiftyMouseInputEvent createEvent(
        final int mouseX,
        final int mouseY,
        final int mouseWheel,
        final int button,
        final boolean buttonDown) {
      switch (button) {
        case 0:
          button0Down = buttonDown;
          break;
        case 1:
          button1Down = buttonDown;
          break;
        case 2:
          button2Down = buttonDown;
          break;
      }

      NiftyMouseInputEvent result = new NiftyMouseInputEvent();
      result.initialize(renderEngine.convertFromNativeX(mouseX), renderEngine.convertFromNativeY(mouseY),
          mouseWheel, button0Down, button1Down, button2Down);
      return result;
    }

    /**
	 * Process event.
	 *
	 * @param mouseInputEvent the mouse input event
	 * @return true, if successful
	 */
    private boolean processEvent(@Nonnull final NiftyMouseInputEvent mouseInputEvent) {
      mouseInputEventProcessor.process(mouseInputEvent);
      if (currentScreen == null) {
        return false;
      } else {
        boolean handled = forwardMouseEventToScreen(mouseInputEvent, currentScreen);
        handleDynamicElements();
        return handled;
      }
    }
  }

  /**
   * Helper class to connect better to the eventbus.
   *
   * @author void
   */
  @SuppressWarnings("rawtypes")
  private static class ClassSaveEventTopicSubscriber implements EventTopicSubscriber, ProxySubscriber {
    
    /** The element id. */
    @Nonnull
    private final String elementId;
    
    /** The target. */
    @Nullable
    private EventTopicSubscriber target;
    
    /** The event class. */
    @Nonnull
    private final Class eventClass;

    /**
	 * Instantiates a new class save event topic subscriber.
	 *
	 * @param elementId  the element id
	 * @param target     the target
	 * @param eventClass the event class
	 */
    private ClassSaveEventTopicSubscriber(
        @Nonnull final String elementId,
        @Nullable final EventTopicSubscriber target,
        @Nonnull final Class eventClass) {
      this.elementId = elementId;
      this.target = target;
      this.eventClass = eventClass;
    }

    /**
	 * To string.
	 *
	 * @return the string
	 */
    @Override
    @Nonnull
    public String toString() {
      return super.toString() + "{" + elementId + "}{" + target + "}{" + eventClass + "}";
    }

    /**
	 * On event.
	 *
	 * @param topic the topic
	 * @param data  the data
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(final String topic, final Object data) {
      if (target != null && eventClass.isInstance(data)) {
        target.onEvent(topic, data);
      }
    }

    /**
	 * Gets the element id.
	 *
	 * @return the element id
	 */
    @Nonnull
    public String getElementId() {
      return elementId;
    }

    /**
	 * Gets the proxied subscriber.
	 *
	 * @return the proxied subscriber
	 */
    @Nullable
    @Override
    public Object getProxiedSubscriber() {
      return target;
    }

    /**
	 * Proxy unsubscribed.
	 */
    @Override
    public void proxyUnsubscribed() {
      this.target = null;
    }

    /**
	 * Gets the reference strength.
	 *
	 * @return the reference strength
	 */
    @Nonnull
    @Override
    public ReferenceStrength getReferenceStrength() {
      return ReferenceStrength.STRONG;
    }
  }

  /**
	 * Creates an element from its type in a specific index in the list of parent.
	 *
	 * @param screen the screen
	 * @param parent the parent
	 * @param type   the type
	 * @param index  the index
	 * @return the Element created
	 */
  @Nonnull
  public Element createElementFromType(
      @Nonnull final Screen screen,
      @Nonnull final Element parent,
      final ElementType type,
      final int index) {
    if (type instanceof LayerType) {
      return createElementFromTypeInternal(screen, parent, type,
          getRootLayerFactory().createRootLayerLayoutPart(this), index);
    }
    return createElementFromTypeInternal(screen, parent, type, new LayoutPart(), index);
  }

  /**
	 * Creates the element from type.
	 *
	 * @param screen the screen
	 * @param parent the parent
	 * @param type   the type
	 * @return the element
	 */
  @Nonnull
  public Element createElementFromType(
      @Nonnull final Screen screen,
      @Nonnull final Element parent,
      final ElementType type) {
    if (type instanceof LayerType) {
      return createElementFromTypeInternal(screen, parent, type, getRootLayerFactory().createRootLayerLayoutPart
          (this), parent.getChildren().size());
    }
    return createElementFromTypeInternal(screen, parent, type, new LayoutPart(), parent.getChildren().size());
  }

  /**
	 * Creates the element from type internal.
	 *
	 * @param screen     the screen
	 * @param parent     the parent
	 * @param type       the type
	 * @param layoutPart the layout part
	 * @param index      the index
	 * @return the element
	 */
  @Nonnull
  private Element createElementFromTypeInternal(
      @Nonnull final Screen screen, @Nonnull final Element parent,
      @Nonnull final ElementType type,
      @Nonnull final LayoutPart layoutPart,
      final int index) {
    //ElementBuilder create a new type every time, so when ElementType#prepare() hasn't been called, it's not necessary to copy it
    //this is required to fix the issue https://github.com/nifty-gui/nifty-gui/issues/316 where a controller instance is directly
    //attached to the ElementType and would not be copied
    ElementType elementType = type.isPrepared() ? type.copy() : type;

    elementType.prepare(this, screen, screen.getRootElement().getElementType());
    elementType.connectParentControls(parent);
    Element element = elementType.create(parent, this, screen, layoutPart, index);
    if (screen.isBound()) {
      //screen.layoutLayers();
      element.bindControls(screen);
      element.initControls(false);
      element.startEffect(EffectEventId.onStartScreen);
      element.startEffect(EffectEventId.onActive);
      element.onStartScreen();
    }
    return element;
  }

  /**
   * Create a new Image. This is a helper method so that you don't need to get the RenderEngine.
   *
   * @param name         file name to use
   * @param filterLinear filter
   * @return RenderImage instance
   */
  @Nullable
  public NiftyImage createImage(@Nonnull final String name, final boolean filterLinear) {
    final Screen screen = getCurrentScreen();
    if (screen == null) {
      throw new IllegalStateException("Can't create a image with this method, while there is currently not active " +
          "screen");
    }
    return renderEngine.createImage(screen, name, filterLinear);
  }

  /**
   * Create a new Image. This is a helper method so that you don't need to get the RenderEngine.
   *
   * @param screen       the screen that is used to create the image
   * @param name         file name to use
   * @param filterLinear filter
   * @return RenderImage instance
   */
  @Nullable
  public NiftyImage createImage(@Nonnull final Screen screen, @Nonnull final String name, final boolean filterLinear) {
    return renderEngine.createImage(screen, name, filterLinear);
  }

  /**
   * You can set this option to true to let Nifty automatically render all panels in random
   * background colors for debugging purposes.
   *
   * @param option enable (true) or disable (false) this feature
   */
  public void setDebugOptionPanelColors(final boolean option) {
    this.debugOptionPanelColors = option;
  }

  /**
   * Returns true if the debug option to render panel colors in enabled.
   *
   * @return true if the option is enabled and false if not
   */
  public boolean isDebugOptionPanelColors() {
    return debugOptionPanelColors;
  }

  /**
	 * A helper method to call the special values replace method ${} syntax.
	 *
	 * @param value the value to perform the replace on
	 * @return the value with stuff replaced
	 */
  @Nonnull
  public String specialValuesReplace(@Nullable final String value) {
    return SpecialValuesReplace.replace(
        value,
        getResourceBundles(),
        currentScreen == null ? null : currentScreen.getScreenController(),
        globalProperties,
        locale);
  }

  /**
	 * The Class SubscriberRegistry.
	 */
  private class SubscriberRegistry {
    
    /** The screen based subscribers. */
    @Nonnull
    private final Map<Screen, Map<String, List<ClassSaveEventTopicSubscriber>>> screenBasedSubscribers = new
        HashMap<Screen, Map<String, List<ClassSaveEventTopicSubscriber>>>();

    /**
	 * Register.
	 *
	 * @param screen     the screen
	 * @param elementId  the element id
	 * @param subscriber the subscriber
	 */
    public void register(final Screen screen, final String elementId, final ClassSaveEventTopicSubscriber subscriber) {
      Map<String, List<ClassSaveEventTopicSubscriber>> elements = screenBasedSubscribers.get(screen);
      if (elements == null) {
        elements = new HashMap<String, List<ClassSaveEventTopicSubscriber>>();
        screenBasedSubscribers.put(screen, elements);
      }
      List<ClassSaveEventTopicSubscriber> list = elements.get(elementId);
      if (list == null) {
        list = new ArrayList<ClassSaveEventTopicSubscriber>();
        elements.put(elementId, list);
      }
      list.add(subscriber);
    }

    /**
	 * Unsubscribe screen.
	 *
	 * @param screen the screen
	 */
    public void unsubscribeScreen(@Nonnull final Screen screen) {
      Map<String, List<ClassSaveEventTopicSubscriber>> elements = screenBasedSubscribers.get(screen);
      if (elements != null && !elements.isEmpty()) {
        for (Map.Entry<String, List<ClassSaveEventTopicSubscriber>> entry : elements.entrySet()) {
          List<ClassSaveEventTopicSubscriber> list = entry.getValue();
          for (int i = 0; i < list.size(); i++) {
            ClassSaveEventTopicSubscriber subscriber = list.get(i);
            getEventService().unsubscribe(subscriber.getElementId(), subscriber);
            log.fine("<- unsubscribe screen for [" + screen + "] [" + subscriber.getElementId() + "] -> [" +
                subscriber + "]");
          }
          list.clear();
        }
        elements.clear();
      }
      screenBasedSubscribers.remove(screen);
    }

    /**
	 * Unsubscribe element.
	 *
	 * @param screen    the screen
	 * @param elementId the element id
	 */
    public void unsubscribeElement(@Nonnull final Screen screen, @Nonnull final String elementId) {
      Map<String, List<ClassSaveEventTopicSubscriber>> elements = screenBasedSubscribers.get(screen);
      if (elements != null && !elements.isEmpty()) {
        List<ClassSaveEventTopicSubscriber> list = elements.get(elementId);
        if (list != null && !list.isEmpty()) {
          for (int i = 0; i < list.size(); i++) {
            ClassSaveEventTopicSubscriber subscriber = list.get(i);
            getEventService().unsubscribe(subscriber.getElementId(), subscriber);
            log.fine("<- unsubscribe element [" + elementId + "] [" + subscriber.getElementId() + "] -> [" +
                subscriber + "]");
          }
          list.clear();
        }
      }
    }
  }

  /**
	 * Gets the clipboard.
	 *
	 * @return the clipboard
	 */
  @Nonnull
  public Clipboard getClipboard() {
    return clipboard;
  }

  /**
	 * Sets the clipboard.
	 *
	 * @param clipboard the new clipboard
	 */
  public void setClipboard(@Nonnull final Clipboard clipboard) {
    this.clipboard = clipboard;
  }

  /**
	 * Creates the font.
	 *
	 * @param name the name
	 * @return the render font
	 */
  @Nullable
  public RenderFont createFont(@Nonnull final String name) {
    return getRenderEngine().createFont(name);
  }

  /**
	 * Gets the fontname.
	 *
	 * @param font the font
	 * @return the fontname
	 */
  @Nonnull
  public String getFontname(@Nonnull final RenderFont font) {
    return getRenderEngine().getFontname(font);
  }

  /**
   * Enable automatic scaling of all GUI elements in relation to the given base resolution.
   *
   * @param baseResolutionX width, for instance 1024
   * @param baseResolutionY height, for instance 768
   */
  public void enableAutoScaling(final int baseResolutionX, final int baseResolutionY) {
    renderEngine.enableAutoScaling(baseResolutionX, baseResolutionY);
  }

  /**
	 * Enable auto scaling.
	 *
	 * @param baseResolutionX the base resolution X
	 * @param baseResolutionY the base resolution Y
	 * @param scaleX          the scale X
	 * @param scaleY          the scale Y
	 */
  public void enableAutoScaling(
      final int baseResolutionX,
      final int baseResolutionY,
      final float scaleX,
      final float scaleY) {
    renderEngine.enableAutoScaling(baseResolutionX, baseResolutionY, scaleX, scaleY);
  }

  /**
	 * Disable auto scaling.
	 */
  public void disableAutoScaling() {
    renderEngine.disableAutoScaling();
  }

  /**
   * Return an InputStream for the given resource name. This is resolved
   * using the currently registered ResourceLocations.
   *
   * @param ref the name of the resource to load
   * @return the InputStream of the resource data
   */
  @Nullable
  public InputStream getResourceAsStream(@Nonnull final String ref) {
    return resourceLoader.getResourceAsStream(ref);
  }

  /**
   * Return the ResourceLoader of this Nifty instance.
   *
   * @return the ResourceLoader to load resources
   */
  @Nonnull
  public NiftyResourceLoader getResourceLoader() {
    return resourceLoader;
  }

  /**
	 * Sets the ignore mouse events.
	 *
	 * @param newValue the new ignore mouse events
	 */
  public void setIgnoreMouseEvents(final boolean newValue) {
    ignoreMouseEvents = newValue;
  }

  /**
	 * Checks if is ignore mouse events.
	 *
	 * @return true, if is ignore mouse events
	 */
  public boolean isIgnoreMouseEvents() {
    return ignoreMouseEvents;
  }

  /**
	 * Sets the ignore keyboard events.
	 *
	 * @param newValue the new ignore keyboard events
	 */
  public void setIgnoreKeyboardEvents(final boolean newValue) {
    ignoreKeyboardEvents = newValue;
  }

  /**
	 * Checks if is ignore keyboard events.
	 *
	 * @return true, if is ignore keyboard events
	 */
  public boolean isIgnoreKeyboardEvents() {
    return ignoreKeyboardEvents;
  }

  /**
	 * Gets the nifty input consumer notify.
	 *
	 * @return the nifty input consumer notify
	 */
  public NiftyInputConsumerNotify getNiftyInputConsumerNotify() {
    return niftyInputConsumerNotify;
  }

  /**
	 * Sets the nifty input consumer notify.
	 *
	 * @param newNotify the new nifty input consumer notify
	 */
  public void setNiftyInputConsumerNotify(final NiftyInputConsumerNotify newNotify) {
    this.niftyInputConsumerNotify = newNotify;
  }
  /**
   * This method clip a rectangle area. This is meant to be used outside {@code nifty.render(true) } call, it
   * will clip an area like a camera . <b>Note:</b> Some elements with childClip=true could modify this clip within {@code nifty.render(true) } loop.
   * Tested on Java2d renderer.
   * @param x0 X coordinates of left-upper corner
   * @param y0 Y coordinates of left-upper corner
   * @param x1 X coordinates of right-bottom corner
   * @param y1 Y coordinates of right-bottom corner
   */
  public void setAbsoluteClip(int x0,int y0,int x1,int y1){
      this.renderEngine.setAbsoluteClip(x0, y0, x1, y1);
  }
  
  /**
	 * {@link #setAbsoluteClip(int, int, int, int) setAbsoluteClip} but this use
	 * position and size of a rectangle.
	 *
	 * @param x      the x
	 * @param y      the y
	 * @param width  the width
	 * @param height the height
	 */
  public void setAbsoluteClipRect(int x,int y,int width,int height){
      this.setAbsoluteClip(x, y, x+width, y+height);
  }

  /**
   * Disable absolute clipping.
   */
  public void disableAbsoluteClip() {
    renderEngine.disableAbsoluteClip();
  }

  /**
   * Implementation of {@link NiftyInputConsumerNotify} which will just ignore everything.
   *
   * @author void
   */
  private static class NiftyInputConsumerNotifyDefault implements NiftyInputConsumerNotify {
    
    /**
	 * Processed mouse event.
	 *
	 * @param mouseX     the mouse X
	 * @param mouseY     the mouse Y
	 * @param mouseWheel the mouse wheel
	 * @param button     the button
	 * @param buttonDown the button down
	 * @param processed  the processed
	 */
    @Override
    public void processedMouseEvent(
        int mouseX,
        int mouseY,
        int mouseWheel,
        int button,
        boolean buttonDown,
        boolean processed) {
    }

    /**
	 * Process keyboard event.
	 *
	 * @param keyEvent  the key event
	 * @param processed the processed
	 */
    @Override
    public void processKeyboardEvent(KeyboardInputEvent keyEvent, boolean processed) {
    }
  }

  /**
	 * Internal popup removed.
	 *
	 * @param id the id
	 */
  public void internalPopupRemoved(final String id) {
    closedPopups.remove(id);
  }

  /**
   * Set this to true to let the NiftyMethodInvoker not catch RuntimeException/Exception when calling any interact
   * on click method. This way when your on click handler crashes with an Exception it will crash the whole application.
   * This might be helpful when you develop your application. The default value is false which will not crash the
   * application but instead will only log the exception.
   *
   * @param debugEnabled set to true to not catch exceptions (default value is false)
   */
  public void setNiftyMethodInvokerDebugEnabled(final boolean debugEnabled) {
    this.niftyMethodInvokerDebugEnabled = debugEnabled;
  }

  /**
	 * Checks if is nifty method invoker debug enabled.
	 *
	 * @return true, if is nifty method invoker debug enabled
	 */
  public boolean isNiftyMethodInvokerDebugEnabled() {
    return niftyMethodInvokerDebugEnabled;
  }

  /**
	 * Sets the static default NiftyInputMapping used by all input event handlers.
	 * <b>Important note: this change will persist to all Nifty instances.</b>
	 *
	 * @param defaultInputMappingType the new default input mapping type
	 */
  public static void setDefaultInputMappingType (@Nonnull final Class<? extends NiftyInputMapping> defaultInputMappingType)
  {
    NiftyDefaults.setDefaultInputMapping(defaultInputMappingType);
  }
}
