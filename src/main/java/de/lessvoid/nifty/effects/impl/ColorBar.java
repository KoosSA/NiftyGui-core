package de.lessvoid.nifty.effects.impl;


import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.helper.PaddingAttributeParser;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Color - color overlay.
 *
 * @author void
 */
public class ColorBar implements EffectImpl {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(ColorBar.class.getName());
  
  /** The color. */
  @Nullable
  private Color color;
  
  /** The temp color. */
  @Nonnull
  private final Color tempColor = new Color("#000f");
  
  /** The width. */
  @Nonnull
  private SizeValue width = SizeValue.def();
  
  /** The inset left. */
  @Nonnull
  private SizeValue insetLeft = SizeValue.px(0);
  
  /** The inset right. */
  @Nonnull
  private SizeValue insetRight = SizeValue.px(0);
  
  /** The inset top. */
  @Nonnull
  private SizeValue insetTop = SizeValue.px(0);
  
  /** The inset bottom. */
  @Nonnull
  private SizeValue insetBottom = SizeValue.px(0);

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
    color = new Color(parameter.getProperty("color", "#ffffffff"));
    width = new SizeValue(parameter.getProperty("width"));
    try {
      PaddingAttributeParser parser = new PaddingAttributeParser(parameter.getProperty("inset", "0px"));
      insetLeft = new SizeValue(parser.getLeft());
      insetRight = new SizeValue(parser.getRight());
      insetTop = new SizeValue(parser.getTop());
      insetBottom = new SizeValue(parser.getBottom());
    } catch (Exception e) {
      log.warning(e.getMessage());
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
    r.saveStates();
    if (color != null) {
      if (r.isColorAlphaChanged()) {
        if (falloff == null) {
          r.setColorIgnoreAlpha(color);
        } else {
          tempColor.multiply(color, falloff.getFalloffValue());
          r.setColorIgnoreAlpha(tempColor);
        }
      } else {
        if (falloff == null) {
          r.setColor(color);
        } else {
          tempColor.multiply(color, falloff.getFalloffValue());
          r.setColor(tempColor);
        }
      }
    }

    int insetOffsetLeft = insetLeft.getValueAsInt(element.getWidth());
    int insetOffsetRight = insetRight.getValueAsInt(element.getWidth());
    int insetOffsetTop = insetTop.getValueAsInt(element.getHeight());
    int insetOffsetBottom = insetBottom.getValueAsInt(element.getHeight());

    final int size;
    if (!element.hasParent()) {
      size = -1;
    } else {
      size = width.getValueAsInt(element.getParent().getWidth());
    }
    if (size == -1) {
      r.renderQuad(
          element.getX() + insetOffsetLeft,
          element.getY() + insetOffsetTop,
          element.getWidth() - insetOffsetLeft - insetOffsetRight,
          element.getHeight() - insetOffsetTop - insetOffsetBottom);
    } else {
      r.renderQuad(
          (element.getX() + element.getWidth() / 2) - size / 2 + insetOffsetLeft,
          element.getY() + insetOffsetTop,
          size - insetOffsetLeft - insetOffsetRight,
          element.getHeight() - insetOffsetTop - insetOffsetBottom);
    }
    r.restoreStates();
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
