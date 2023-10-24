package de.lessvoid.nifty.render;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;

/**
 * The Class NiftyMouseImpl.
 */
public class NiftyMouseImpl implements NiftyMouse {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(NiftyMouseImpl.class.getName());
  
  /** The render device. */
  @Nonnull
  private final RenderDevice renderDevice;
  
  /** The input system. */
  @Nonnull
  private final InputSystem inputSystem;
  
  /** The registered mouse cursors. */
  @Nonnull
  private final Map<String, MouseCursor> registeredMouseCursors;
  
  /** The current id. */
  @Nullable
  private String currentId;
  
  /** The mouse X. */
  private int mouseX;
  
  /** The mouse Y. */
  private int mouseY;
  
  /** The time provider. */
  @Nonnull
  private final TimeProvider timeProvider;
  
  /** The last mouse move event time. */
  private long lastMouseMoveEventTime;

  /**
	 * Instantiates a new nifty mouse impl.
	 *
	 * @param renderDevice the render device
	 * @param inputSystem  the input system
	 * @param timeProvider the time provider
	 */
  public NiftyMouseImpl(
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final InputSystem inputSystem,
      @Nonnull final TimeProvider timeProvider) {
    this.renderDevice = renderDevice;
    this.inputSystem = inputSystem;
    this.timeProvider = timeProvider;
    lastMouseMoveEventTime = timeProvider.getMsTime();
    registeredMouseCursors = new HashMap<String, MouseCursor>();
  }

  /**
	 * Register mouse cursor.
	 *
	 * @param id       the id
	 * @param filename the filename
	 * @param hotspotX the hotspot X
	 * @param hotspotY the hotspot Y
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  @Override
  public void registerMouseCursor(
      @Nonnull final String id,
      @Nonnull final String filename,
      final int hotspotX,
      final int hotspotY) throws IOException {
    MouseCursor mouseCursor = renderDevice.createMouseCursor(filename, hotspotX, hotspotY);
    if (mouseCursor == null) {
      log.warning("Your RenderDevice does not support the createMouseCursor() method. Mouse cursors can't be changed.");
      return;
    }
    registeredMouseCursors.put(id, mouseCursor);
  }

  /**
	 * Gets the current id.
	 *
	 * @return the current id
	 */
  @Nullable
  @Override
  public String getCurrentId() {
    return currentId;
  }

  /**
	 * Unregister all.
	 */
  @Override
  public void unregisterAll() {
    for (MouseCursor cursor : registeredMouseCursors.values()) {
      cursor.dispose();
    }
    registeredMouseCursors.clear();
  }

  /**
	 * Reset mouse cursor.
	 */
  @Override
  public void resetMouseCursor() {
    currentId = null;
    renderDevice.disableMouseCursor();
  }

  /**
	 * Enable mouse cursor.
	 *
	 * @param id the id
	 */
  @Override
  public void enableMouseCursor(@Nullable final String id) {
    if (id == null) {
      resetMouseCursor();
      return;
    }
    if (id.equals(currentId)) {
      return;
    }
    renderDevice.enableMouseCursor(registeredMouseCursors.get(id));
    currentId = id;
  }

  /**
	 * Sets the mouse position.
	 *
	 * @param x the x
	 * @param y the y
	 */
  @Override
  public void setMousePosition(final int x, final int y) {
    inputSystem.setMousePosition(x, y);
    updateMousePosition(x, y);
  }

  /**
	 * Gets the x.
	 *
	 * @return the x
	 */
  @Override
  public int getX() {
    return mouseX;
  }

  /**
	 * Gets the y.
	 *
	 * @return the y
	 */
  @Override
  public int getY() {
    return mouseY;
  }

  /**
	 * Gets the no mouse movement time.
	 *
	 * @return the no mouse movement time
	 */
  @Override
  public long getNoMouseMovementTime() {
    long now = timeProvider.getMsTime();
    return now - lastMouseMoveEventTime;
  }

  /**
	 * Update mouse position.
	 *
	 * @param x the x
	 * @param y the y
	 */
  public void updateMousePosition(final int x, final int y) {
    if (positionChanged(x, y)) {
      lastMouseMoveEventTime = timeProvider.getMsTime();
    }
    mouseX = x;
    mouseY = y;
  }

  /**
	 * Position changed.
	 *
	 * @param x the x
	 * @param y the y
	 * @return true, if successful
	 */
  private boolean positionChanged(final int x, final int y) {
    return x != mouseX || y != mouseY;
  }
}
