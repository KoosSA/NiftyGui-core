package de.lessvoid.nifty.elements.render;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

/**
 * The ElementRenderer for a Panel.
 *
 * @author void
 */
public class PanelRenderer implements ElementRenderer {
  /**
   * the background color when used otherwise null.
   */
  @Nullable
  private Color backgroundColor;

  /** The debug color. */
  @Nullable
  private Color debugColor;

  /**
   * Default constructor.
   */
  public PanelRenderer() {
  }

  /**
   * render it.
   *
   * @param element the widget we're connected to
   * @param r       the renderDevice we should use
   */
  @Override
  public void render(@Nonnull final Element element, @Nonnull final NiftyRenderEngine r) {
    if (element.getNifty().isDebugOptionPanelColors()) {
      r.saveStates();
      r.setColor(getDebugColor());
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
      r.restoreStates();
      return;
    }

    if (backgroundColor != null) {
      r.saveStates();
      if (!r.isColorChanged()) {
        if (r.isColorAlphaChanged()) {
          r.setColorIgnoreAlpha(backgroundColor);
        } else {
          r.setColor(backgroundColor);
        }
      }
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
      r.restoreStates();
    }
  }

  /**
	 * Gets the debug color.
	 *
	 * @return the debug color
	 */
  @Nonnull
  private Color getDebugColor() {
    if (debugColor != null) {
      return debugColor;
    }
    final Random rnd = new Random();
    debugColor = new Color(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat(), .5f);
    return debugColor;
  }

  /**
	 * Sets the background color.
	 *
	 * @param backgroundColor the new background color
	 */
  public void setBackgroundColor(@Nullable final Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  /**
	 * Gets the background color.
	 *
	 * @return the background color
	 */
  @Nullable
  public Color getBackgroundColor() {
    return backgroundColor;
  }
}
