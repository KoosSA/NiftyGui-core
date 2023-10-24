package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * This stores all active effects and is used by the EffectProcessor. It will
 * classify effects on add (overlay, pre, post).
 *
 * @author void
 */
public class ActiveEffects {
  
  /** The all. */
  @Nonnull
  private final List<Effect> all = new ArrayList<Effect>();
  
  /** The post. */
  @Nonnull
  private final List<Effect> post = new ArrayList<Effect>();
  
  /** The pre. */
  @Nonnull
  private final List<Effect> pre = new ArrayList<Effect>();
  
  /** The overlay. */
  @Nonnull
  private final List<Effect> overlay = new ArrayList<Effect>();

  /**
	 * Clear.
	 */
  public void clear() {
    all.clear();
    post.clear();
    pre.clear();
    overlay.clear();
  }

  /**
	 * Adds the.
	 *
	 * @param e the e
	 */
  public void add(@Nonnull final Effect e) {
    all.add(e);

    if (e.isOverlay()) {
      overlay.add(e);
    } else if (e.isPost()) {
      post.add(e);
    } else {
      pre.add(e);
    }
  }

  /**
	 * Removes the.
	 *
	 * @param e the e
	 */
  public void remove(final Effect e) {
    all.remove(e);
    post.remove(e);
    pre.remove(e);
    overlay.remove(e);
  }

  /**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
  public boolean isEmpty() {
    return all.isEmpty();
  }

  /**
	 * Contains.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
  public boolean contains(final Effect e) {
    return all.contains(e);
  }

  /**
	 * Size.
	 *
	 * @return the int
	 */
  public int size() {
    return all.size();
  }

  /**
	 * Contains active effects.
	 *
	 * @return true, if successful
	 */
  public boolean containsActiveEffects() {
    for (int i = 0; i < all.size(); i++) {
      Effect e = all.get(i);
      if (e.isActive()) {
        return true;
      }
    }
    return false;
  }

  /**
	 * Gets the active.
	 *
	 * @return the active
	 */
  @Nonnull
  public List<Effect> getActive() {
    return all;
  }

  /**
	 * Gets the active post.
	 *
	 * @return the active post
	 */
  @Nonnull
  public List<Effect> getActivePost() {
    return post;
  }

  /**
	 * Gets the active pre.
	 *
	 * @return the active pre
	 */
  @Nonnull
  public List<Effect> getActivePre() {
    return pre;
  }

  /**
	 * Gets the active overlay.
	 *
	 * @return the active overlay
	 */
  @Nonnull
  public List<Effect> getActiveOverlay() {
    return overlay;
  }
}
