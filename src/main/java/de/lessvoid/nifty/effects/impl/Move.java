package de.lessvoid.nifty.effects.impl;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.TargetElementResolver;

/**
 * Move - move stuff around.
 *
 * @author void
 */
public class Move implements EffectImpl {

  /** The Constant log. */
  private static final Logger log = Logger.getLogger(Move.class.getName());

  /** The Constant LEFT. */
  private static final String LEFT = "left";
  
  /** The Constant RIGHT. */
  private static final String RIGHT = "right";
  
  /** The Constant TOP. */
  private static final String TOP = "top";
  
  /** The Constant BOTTOM. */
  private static final String BOTTOM = "bottom";

  /** The direction. */
  private String direction;
  
  /** The offset. */
  private long offset = 0;
  
  /** The start offset. */
  private long startOffset = 0;
  
  /** The offset dir. */
  private int offsetDir = 0;
  
  /** The offset Y. */
  private float offsetY;
  
  /** The start offset Y. */
  private float startOffsetY;
  
  /** The start offset X. */
  private int startOffsetX;
  
  /** The offset X. */
  private float offsetX;
  
  /** The with target. */
  private boolean withTarget = false;
  
  /** The from offset. */
  private boolean fromOffset = false;
  
  /** The to offset. */
  private boolean toOffset = false;

  /**
	 * Activate.
	 *
	 * @param nifty     the nifty
	 * @param element   the element
	 * @param parameter the parameter
	 */
  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    String mode = parameter.getProperty("mode");
    direction = parameter.getProperty("direction");
    if (LEFT.equals(direction)) {
      offset = element.getX() + element.getWidth();
    } else if (RIGHT.equals(direction)) {
      offset = nifty.getRenderEngine().getWidth() - element.getX();
    } else if (TOP.equals(direction)) {
      offset = element.getY() + element.getHeight();
    } else if (BOTTOM.equals(direction)) {
      offset = nifty.getRenderEngine().getHeight() - element.getY();
    } else {
      offset = 0;
    }

    if ("out".equals(mode)) {
      startOffset = 0;
      offsetDir = -1;
      withTarget = false;
    } else if ("in".equals(mode)) {
      startOffset = offset;
      offsetDir = 1;
      withTarget = false;
    } else if ("fromPosition".equals(mode)) {
      withTarget = true;
    } else if ("toPosition".equals(mode)) {
      withTarget = true;
    } else if ("fromOffset".equals(mode)) {
      fromOffset = true;
      startOffsetX = Integer.valueOf(parameter.getProperty("offsetX", "0"));
      startOffsetY = Integer.valueOf(parameter.getProperty("offsetY", "0"));
      offsetX = startOffsetX * -1;
      offsetY = startOffsetY * -1;
    } else if ("toOffset".equals(mode)) {
      toOffset = true;
      startOffsetX = 0;
      startOffsetY = 0;
      offsetX = Integer.valueOf(parameter.getProperty("offsetX", "0"));
      offsetY = Integer.valueOf(parameter.getProperty("offsetY", "0"));
    }

    String target = parameter.getProperty("targetElement");
    Screen screen = nifty.getCurrentScreen();
    if (target != null && screen != null) {
      TargetElementResolver resolver = new TargetElementResolver(screen, element);
      Element targetElement = resolver.resolve(target);
      if (targetElement == null) {
        log.warning("move effect for element [" + element.getId() + "] was unable to find target element [" + target
            + "] at screen [" + screen.getScreenId() + "]");
        return;
      }

      if ("fromPosition".equals(mode)) {
        startOffsetX = targetElement.getX() - element.getX();
        startOffsetY = targetElement.getY() - element.getY();
        offsetX = -(targetElement.getX() - element.getX());
        offsetY = -(targetElement.getY() - element.getY());
      } else if ("toPosition".equals(mode)) {
        startOffsetX = 0;
        startOffsetY = 0;
        offsetX = (targetElement.getX() - element.getX());
        offsetY = (targetElement.getY() - element.getY());
      }
    }
  }

  /**
	 * Execute.
	 *
	 * @param element        the element
	 * @param normalizedTime the normalized time
	 * @param falloff        the falloff
	 * @param r              the r
	 */
  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (fromOffset || toOffset) {
      float moveToX = startOffsetX + normalizedTime * offsetX;
      float moveToY = startOffsetY + normalizedTime * offsetY;
      r.moveTo(moveToX, moveToY);
    } else if (withTarget) {
      float moveToX = startOffsetX + normalizedTime * offsetX;
      float moveToY = startOffsetY + normalizedTime * offsetY;
      r.moveTo(moveToX, moveToY);
    } else {
      if (LEFT.equals(direction)) {
        r.moveTo(-startOffset + offsetDir * normalizedTime * offset, 0);
      } else if (RIGHT.equals(direction)) {
        r.moveTo(startOffset - offsetDir * normalizedTime * offset, 0);
      } else if (TOP.equals(direction)) {
        r.moveTo(0, -startOffset + offsetDir * normalizedTime * offset);
      } else if (BOTTOM.equals(direction)) {
        r.moveTo(0, startOffset - offsetDir * normalizedTime * offset);
      }
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
