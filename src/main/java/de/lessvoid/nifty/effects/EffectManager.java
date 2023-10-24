package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.time.TimeProvider;

/**
 * manage all effects of an element.
 *
 * @author void
 */
public class EffectManager {
  
  /** The Constant effectsRenderOrder. */
  // define the order of effects as they are rendered later
  private static final EffectEventId[] effectsRenderOrder = new EffectEventId[] {
      EffectEventId.onShow,
      EffectEventId.onHide,
      EffectEventId.onStartScreen,
      EffectEventId.onEndScreen,
      EffectEventId.onCustom,
      EffectEventId.onActive,
      EffectEventId.onHover,
      EffectEventId.onStartHover,
      EffectEventId.onEndHover,
      EffectEventId.onFocus,
      EffectEventId.onLostFocus,
      EffectEventId.onGetFocus,
      EffectEventId.onClick,
      EffectEventId.onEnabled,
      EffectEventId.onDisabled
  };

  /** The Constant effectsHideShowOrder. */
  // define the order of effects as they are called for hide/show/reset things
  private static final EffectEventId[] effectsHideShowOrder = new EffectEventId[] {
      EffectEventId.onStartScreen,
      EffectEventId.onEndScreen,
      EffectEventId.onShow,
      EffectEventId.onHide,
      EffectEventId.onCustom,
      EffectEventId.onHover,
      EffectEventId.onStartHover,
      EffectEventId.onEndHover,
      // onActive is currently used by the nifty-panel style. when we reset that effect here
      // we would not be able to use the nifty-panel in popups. when a popup is being closed
      // all effects will be reset. which makes sense but probably not for the onActive effect.
      // we need to check later if this uncommenting has any bad influence on other controls.
      //
      //  EffectEventId.onActive
      EffectEventId.onFocus,
      EffectEventId.onLostFocus,
      EffectEventId.onGetFocus,
      EffectEventId.onClick
  };

  /** The effect processor. */
  @Nonnull
  private final Map<EffectEventId, EffectProcessor> effectProcessor = new EnumMap<EffectEventId,
      EffectProcessor>(EffectEventId.class);
  
  /** The effect processor list. */
  @Nonnull
  private final List<EffectProcessor> effectProcessorList = new ArrayList<EffectProcessor>(0);
  
  /** The hover falloff. */
  @Nullable
  private Falloff hoverFalloff;
  
  /** The alternate key. */
  @Nullable
  private String alternateKey;
  
  /** The is empty. */
  private boolean isEmpty = true;
  
  /** The notify. */
  @Nonnull
  private final Notify notify;

  /** The Constant renderPhasePre. */
  // we're not multi-threaded so we can use static in here to save memory allocation when creating lots of elements
  @Nonnull
  private static final RenderPhase renderPhasePre = new RenderPhasePre();
  
  /** The Constant renderPhasePost. */
  @Nonnull
  private static final RenderPhase renderPhasePost = new RenderPhasePost();
  
  /** The Constant renderPhaseOverlay. */
  @Nonnull
  private static final RenderPhase renderPhaseOverlay = new RenderPhaseOverlay();

  /**
	 * create a new effectManager with the given listener.
	 *
	 * @param notify the notify
	 */
  public EffectManager(@Nonnull final Notify notify) {
    this.alternateKey = null;
    this.notify = notify;
  }

  /**
   * register an effect.
   *
   * @param id the id
   * @param e  the effect
   */
  public void registerEffect(@Nonnull final EffectEventId id, @Nonnull final Effect e) {
    EffectProcessor processor = effectProcessor.get(id);
    if (processor == null) {
      processor = id.createEffectProcessor(new NotifyAdapter(id, notify));
      effectProcessor.put(id, processor);
      effectProcessorList.add(processor);
    }
    processor.registerEffect(e);
    isEmpty = false;
  }

  /**
   * start all effects with the given id for the given element.
   *
   * @param id       the effect id to start
   * @param w        the element
   * @param time     TimeProvider
   * @param listener the {@link EndNotify} to use.
   */
  public void startEffect(
      @Nonnull final EffectEventId id,
      @Nonnull final Element w,
      @Nonnull final TimeProvider time,
      @Nullable final EndNotify listener) {
    startEffect(id, w, time, listener, null);
  }

  /**
	 * Start effect.
	 *
	 * @param id        the id
	 * @param w         the w
	 * @param time      the time
	 * @param listener  the listener
	 * @param customKey the custom key
	 */
  public void startEffect(
      @Nonnull final EffectEventId id,
      @Nonnull final Element w,
      @Nonnull final TimeProvider time,
      @Nullable final EndNotify listener,
      @Nullable final String customKey) {
    stopEffect(id);
    EffectProcessor processor = getEffectProcessor(id);
    if (processor != null) {
      processor.activate(listener, alternateKey, customKey);
    }
  }

  /**
	 * Stop effect.
	 *
	 * @param effectId the effect id
	 */
  public void stopEffect(@Nonnull final EffectEventId effectId) {
    EffectProcessor processor = getEffectProcessor(effectId);
    if (processor != null) {
      processor.setActive(false);
    }
  }

  /**
	 * Render pre.
	 *
	 * @param renderEngine the render engine
	 * @param element      the element
	 */
  public void renderPre(@Nonnull final NiftyRenderEngine renderEngine, final Element element) {
    renderInternal(renderEngine, renderPhasePre);
  }

  /**
	 * Render post.
	 *
	 * @param renderEngine the render engine
	 * @param element      the element
	 */
  public void renderPost(@Nonnull final NiftyRenderEngine renderEngine, final Element element) {
    renderInternal(renderEngine, renderPhasePost);
  }

  /**
	 * Render overlay.
	 *
	 * @param renderEngine the render engine
	 * @param element      the element
	 */
  public void renderOverlay(@Nonnull final NiftyRenderEngine renderEngine, final Element element) {
    renderInternal(renderEngine, renderPhaseOverlay);
  }

  /**
	 * Render internal.
	 *
	 * @param renderEngine the render engine
	 * @param phase        the phase
	 */
  private void renderInternal(
      @Nonnull final NiftyRenderEngine renderEngine,
      @Nonnull final RenderPhase phase) {
    for (int i = 0; i < effectsRenderOrder.length; i++) {
      EffectProcessor processor = getEffectProcessor(effectsRenderOrder[i]);
      if (processor != null) {
        phase.render(processor, renderEngine);
      }
    }
  }

  /**
   * handle mouse hover effects.
   *
   * @param element the current element
   * @param x       mouse x position
   * @param y       mouse y position
   */
  public void handleHover(final Element element, final int x, final int y) {
    EffectProcessor processor = getEffectProcessor(EffectEventId.onHover);
    if (processor != null) {
      processor.processHover(x, y);
    }
  }

  /**
	 * Handle hover start and end.
	 *
	 * @param element the element
	 * @param x       the x
	 * @param y       the y
	 */
  public void handleHoverStartAndEnd(final Element element, final int x, final int y) {
    EffectProcessor processor = getEffectProcessor(EffectEventId.onStartHover);
    if (processor != null) {
      processor.processStartHover(x, y);
    }

    processor = getEffectProcessor(EffectEventId.onEndHover);
    if (processor != null) {
      processor.processEndHover(x, y);
    }
  }

  /**
	 * Handle hover deactivate.
	 *
	 * @param element the element
	 * @param x       the x
	 * @param y       the y
	 */
  public void handleHoverDeactivate(final Element element, final int x, final int y) {
    EffectProcessor processor = getEffectProcessor(EffectEventId.onHover);
    if (processor != null) {
      processor.processHoverDeactivate(x, y);
    }
  }

  /**
   * checks if a certain effect is active.
   *
   * @param effectEventId the effectEventId to check
   * @return true, if active, false otherwise
   */
  public final boolean isActive(@Nonnull final EffectEventId effectEventId) {
    EffectProcessor processor = getEffectProcessor(effectEventId);
    if (processor == null) {
      return false;
    }
    return processor.isActive();
  }

  /**
	 * Reset.
	 */
  public void reset() {
    // onHover should stay active and is not reset
    // onActive should stay active and is not reset
    // onFocus should stay active and is not reset
    // onLostFocus should stay active and is not reset
    // onClick should stay active and is not reset
    resetSingleEffect(EffectEventId.onStartScreen);
    resetSingleEffect(EffectEventId.onEndScreen);
    resetSingleEffect(EffectEventId.onShow);
    resetSingleEffect(EffectEventId.onHide);
    //  effectProcessor.get(EffectEventId.onCustom).reset();
  }

  /**
	 * Reset all.
	 */
  public void resetAll() {
    for (int i = 0; i < effectsHideShowOrder.length; i++) {
      resetSingleEffect(effectsHideShowOrder[i]);
    }
  }

  /**
	 * Reset for hide.
	 */
  public void resetForHide() {
    for (int i = 0; i < effectsHideShowOrder.length; i++) {
      EffectProcessor processor = getEffectProcessor(effectsHideShowOrder[i]);
      if (processor != null) {
        processor.saveActiveNeverStopRenderingEffects();
      }
    }
  }

  /**
	 * Restore for show.
	 */
  public void restoreForShow() {
    for (int i = 0; i < effectsHideShowOrder.length; i++) {
      EffectProcessor processor = getEffectProcessor(effectsHideShowOrder[i]);
      if (processor != null) {
        processor.restoreNeverStopRenderingEffects();
      }
    }
  }

  /**
	 * Reset single effect.
	 *
	 * @param effectEventId the effect event id
	 */
  public void resetSingleEffect(@Nonnull final EffectEventId effectEventId) {
    EffectProcessor processor = getEffectProcessor(effectEventId);
    if (processor != null) {
      processor.reset();
    }
  }

  /**
	 * Reset single effect.
	 *
	 * @param effectEventId the effect event id
	 * @param customKey     the custom key
	 */
  public void resetSingleEffect(@Nonnull final EffectEventId effectEventId, @Nonnull final String customKey) {
    EffectProcessor processor = getEffectProcessor(effectEventId);
    if (processor != null) {
      processor.reset(customKey);
    }
  }

  /**
   * set the alternate key.
   *
   * @param newAlternateKey alternate key
   */
  public void setAlternateKey(@Nullable final String newAlternateKey) {
    this.alternateKey = newAlternateKey;
  }

  /**
   * get state string.
   *
   * @param offset offset
   * @return String with state information
   */
  @Nonnull
  public String getStateString(final String offset) {
    StringBuilder data = new StringBuilder();

    int activeProcessors = 0;
    for (EffectEventId eventId : effectProcessor.keySet()) {
      EffectProcessor processor = getEffectProcessor(eventId);
      if (processor != null && processor.isActive()) {
        activeProcessors++;

        data.append(offset);
        data.append("  {").append(eventId.toString()).append("} ");
        data.append(processor.getStateString());
      }
    }

    if (activeProcessors == 0) {
      return offset + "{}";
    } else {
      return data.toString();
    }
  }

  /**
	 * Sets the falloff.
	 *
	 * @param newFalloff the new falloff
	 */
  public void setFalloff(final Falloff newFalloff) {
    hoverFalloff = newFalloff;
  }

  /**
	 * Gets the falloff.
	 *
	 * @return the falloff
	 */
  @Nullable
  public Falloff getFalloff() {
    return hoverFalloff;
  }

  /**
	 * Removes the all effects.
	 */
  public void removeAllEffects() {
    for (int i = 0; i < effectProcessorList.size(); i++) {
      effectProcessorList.get(i).removeAllEffects();
    }
    isEmpty = true;
  }

  /**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
  public boolean isEmpty() {
    return isEmpty;
  }

  /**
	 * Gets the effects.
	 *
	 * @param <T>            the generic type
	 * @param effectEventId  the effect event id
	 * @param requestedClass the requested class
	 * @return the effects
	 */
  @Nonnull
  public <T extends EffectImpl> List<Effect> getEffects(
      @Nonnull final EffectEventId effectEventId,
      @Nonnull final Class<T> requestedClass) {
    EffectProcessor processor = getEffectProcessor(effectEventId);
    if (processor == null) {
      return Collections.emptyList();
    }
    return processor.getEffects(requestedClass);
  }

  /**
	 * The Interface RenderPhase.
	 */
  interface RenderPhase {
    
    /**
	 * Render.
	 *
	 * @param effectProcessor the effect processor
	 * @param renderEngine    the render engine
	 */
    public void render(@Nonnull EffectProcessor effectProcessor, @Nonnull NiftyRenderEngine renderEngine);
  }

  /**
	 * The Class RenderPhasePre.
	 */
  private final static class RenderPhasePre implements RenderPhase {
    
    /**
	 * Render.
	 *
	 * @param processor    the processor
	 * @param renderEngine the render engine
	 */
    @Override
    public void render(@Nonnull final EffectProcessor processor, @Nonnull final NiftyRenderEngine renderEngine) {
      processor.renderPre(renderEngine);
    }
  }

  /**
	 * The Class RenderPhasePost.
	 */
  private final static class RenderPhasePost implements RenderPhase {
    
    /**
	 * Render.
	 *
	 * @param processor    the processor
	 * @param renderEngine the render engine
	 */
    @Override
    public void render(@Nonnull final EffectProcessor processor, @Nonnull final NiftyRenderEngine renderEngine) {
      processor.renderPost(renderEngine);
    }
  }

  /**
	 * The Class RenderPhaseOverlay.
	 */
  private final static class RenderPhaseOverlay implements RenderPhase {
    
    /**
	 * Render.
	 *
	 * @param processor    the processor
	 * @param renderEngine the render engine
	 */
    @Override
    public void render(@Nonnull final EffectProcessor processor, @Nonnull final NiftyRenderEngine renderEngine) {
      processor.renderOverlay(renderEngine);
    }
  }

  /**
	 * The Interface Notify.
	 */
  public interface Notify {
    
    /**
	 * Effect state changed.
	 *
	 * @param eventId the event id
	 * @param active  the active
	 */
    void effectStateChanged(@Nonnull EffectEventId eventId, boolean active);
  }

  /**
	 * The Class NotifyAdapter.
	 */
  private static class NotifyAdapter implements EffectProcessorImpl.Notify {
    
    /** The notify. */
    @Nonnull
    private final Notify notify;
    
    /** The event id. */
    @Nonnull
    private final EffectEventId eventId;

    /**
	 * Instantiates a new notify adapter.
	 *
	 * @param eventId the event id
	 * @param notify  the notify
	 */
    public NotifyAdapter(@Nonnull final EffectEventId eventId, @Nonnull final Notify notify) {
      this.eventId = eventId;
      this.notify = notify;
    }

    /**
	 * Effect processor state changed.
	 *
	 * @param active the active
	 */
    @Override
    public void effectProcessorStateChanged(final boolean active) {
      notify.effectStateChanged(eventId, active);
    }
  }

  /**
	 * Gets the effect processor.
	 *
	 * @param id the id
	 * @return the effect processor
	 */
  @Nullable
  private EffectProcessor getEffectProcessor(@Nonnull final EffectEventId id) {
    EffectProcessor processor = effectProcessor.get(id);
    if (processor == null) {
      return null;
    }
    return processor;
  }
}
