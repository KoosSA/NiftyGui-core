package de.lessvoid.nifty.effects;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * The Interface EffectProcessor.
 */
public interface EffectProcessor {
  
  /**
	 * Register effect.
	 *
	 * @param e the e
	 */
  void registerEffect(@Nonnull Effect e);

  /**
	 * Render pre.
	 *
	 * @param renderDevice the render device
	 */
  void renderPre(@Nonnull NiftyRenderEngine renderDevice);

  /**
	 * Render post.
	 *
	 * @param renderDevice the render device
	 */
  void renderPost(@Nonnull NiftyRenderEngine renderDevice);

  /**
	 * Render overlay.
	 *
	 * @param renderDevice the render device
	 */
  void renderOverlay(@Nonnull NiftyRenderEngine renderDevice);

  /**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
  boolean isActive();

  /**
	 * Save active never stop rendering effects.
	 */
  void saveActiveNeverStopRenderingEffects();

  /**
	 * Restore never stop rendering effects.
	 */
  void restoreNeverStopRenderingEffects();

  /**
	 * Reset.
	 */
  void reset();

  /**
	 * Reset.
	 *
	 * @param customKey the custom key
	 */
  void reset(@Nonnull String customKey);

  /**
	 * Activate.
	 *
	 * @param newListener the new listener
	 * @param alternate   the alternate
	 * @param customKey   the custom key
	 */
  void activate(@Nullable EndNotify newListener, @Nullable String alternate, @Nullable String customKey);

  /**
	 * Gets the state string.
	 *
	 * @return the state string
	 */
  @Nullable
  String getStateString();

  /**
	 * Sets the active.
	 *
	 * @param newActive the new active
	 */
  void setActive(boolean newActive);

  /**
	 * Process hover.
	 *
	 * @param x the x
	 * @param y the y
	 */
  void processHover(int x, int y);

  /**
	 * Process start hover.
	 *
	 * @param x the x
	 * @param y the y
	 */
  void processStartHover(int x, int y);

  /**
	 * Process end hover.
	 *
	 * @param x the x
	 * @param y the y
	 */
  void processEndHover(int x, int y);

  /**
	 * Process hover deactivate.
	 *
	 * @param x the x
	 * @param y the y
	 */
  void processHoverDeactivate(int x, int y);

  /**
	 * Removes the all effects.
	 */
  void removeAllEffects();

  /**
	 * Gets the effects.
	 *
	 * @param <T>            the generic type
	 * @param requestedClass the requested class
	 * @return the effects
	 */
  @Nonnull
  <T extends EffectImpl> List<Effect> getEffects(@Nonnull Class<T> requestedClass);
}
