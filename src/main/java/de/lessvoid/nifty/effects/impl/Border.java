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
 * Border - border overlay.
 *
 * @author void
 */
public class Border implements EffectImpl {
  
  /** The Constant log. */
  private static final Logger log = Logger.getLogger(Border.class.getName());
  
  /** The color left. */
  @Nonnull
  private Color colorLeft = Color.WHITE;
  
  /** The color right. */
  @Nonnull
  private Color colorRight = Color.WHITE;
  
  /** The color top. */
  @Nonnull
  private Color colorTop = Color.WHITE;
  
  /** The color bottom. */
  @Nonnull
  private Color colorBottom = Color.WHITE;
  
  /** The border left. */
  @Nonnull
  private SizeValue borderLeft = SizeValue.px(1);
  
  /** The border right. */
  @Nonnull
  private SizeValue borderRight = SizeValue.px(1);
  
  /** The border top. */
  @Nonnull
  private SizeValue borderTop = SizeValue.px(1);
  
  /** The border bottom. */
  @Nonnull
  private SizeValue borderBottom = SizeValue.px(1);
  
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
    try {
      PaddingAttributeParser parser = new PaddingAttributeParser(parameter.getProperty("border", "1px"));
      borderLeft = new SizeValue(parser.getLeft());
      borderRight = new SizeValue(parser.getRight());
      borderTop = new SizeValue(parser.getTop());
      borderBottom = new SizeValue(parser.getBottom());

      parser = new PaddingAttributeParser(parameter.getProperty("color", "#ffff"));
      colorLeft = new Color(parser.getLeft());
      colorRight = new Color(parser.getRight());
      colorTop = new Color(parser.getTop());
      colorBottom = new Color(parser.getBottom());

      parser = new PaddingAttributeParser(parameter.getProperty("inset", "0px"));
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
    int left = getBorder(element, borderLeft);
    int right = getBorder(element, borderRight);
    int top = getBorder(element, borderTop);
    int bottom = getBorder(element, borderBottom);
    int insetOffsetLeft = insetLeft.getValueAsInt(element.getWidth());
    int insetOffsetRight = insetRight.getValueAsInt(element.getWidth());
    int insetOffsetTop = insetTop.getValueAsInt(element.getHeight());
    int insetOffsetBottom = insetBottom.getValueAsInt(element.getHeight());

    if (left > 0) {
      setAlphaSaveColor(r, colorLeft);
      r.renderQuad(
          element.getX() - left + insetOffsetLeft,
          element.getY() - top + insetOffsetTop,
          left,
          element.getHeight() + top + bottom - insetOffsetTop - insetOffsetBottom);
    }
    if (right > 0) {
      setAlphaSaveColor(r, colorRight);
      r.renderQuad(
          element.getX() + element.getWidth() - insetOffsetRight,
          element.getY() - top + insetOffsetTop,
          right,
          element.getHeight() + top + bottom - insetOffsetTop - insetOffsetBottom);
    }
    if (top > 0) {
      setAlphaSaveColor(r, colorTop);
      r.renderQuad(
          element.getX() - left + insetOffsetLeft,
          element.getY() - top + insetOffsetTop,
          element.getWidth() + left + right - insetOffsetLeft - insetOffsetRight,
          top);
    }
    if (bottom > 0) {
      setAlphaSaveColor(r, colorBottom);
      r.renderQuad(
          element.getX() - left + insetOffsetLeft,
          element.getY() + element.getHeight() - insetOffsetBottom,
          element.getWidth() + left + right - insetOffsetLeft - insetOffsetRight,
          bottom);
    }
    r.restoreStates();
  }

  /**
	 * Sets the alpha save color.
	 *
	 * @param r     the r
	 * @param color the color
	 */
  private void setAlphaSaveColor(@Nonnull final NiftyRenderEngine r, @Nonnull final Color color) {
    if (r.isColorAlphaChanged()) {
      r.setColorIgnoreAlpha(color);
    } else {
      r.setColor(color);
    }
  }

  /**
	 * Gets the border.
	 *
	 * @param element   the element
	 * @param sizeValue the size value
	 * @return the border
	 */
  private int getBorder(@Nonnull final Element element, @Nonnull final SizeValue sizeValue) {
    if (!element.hasParent()) {
      return 0;
    } else {
      final int parentWidth = sizeValue.getValueAsInt(element.getParent().getWidth());
      if (parentWidth < 0) {
        return 0;
      }
      return parentWidth;
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
