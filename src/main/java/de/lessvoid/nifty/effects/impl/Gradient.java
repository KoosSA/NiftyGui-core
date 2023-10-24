package de.lessvoid.nifty.effects.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * Color - color overlay.
 *
 * @author void
 */
public class Gradient implements EffectImpl {
  
  /** The entries. */
  @Nonnull
  private final List<Entry> entries = new ArrayList<Entry>();
  
  /** The horizontal. */
  private boolean horizontal = false;

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
    entries.clear();
    for (Attributes entry : parameter.getEffectValues().getValues()) {
      SizeValue offset = new SizeValue(entry.get("offset"));
      Color color = entry.getAsColor("color");
      if (color != null) {
        entries.add(new Entry(offset, color));
      }
    }
    horizontal = "horizontal".equals(parameter.getProperty("direction", "vertical"));
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
    if (normalizedTime > 0.0f) {
      if (horizontal) {
        for (int i = 1; i < entries.size(); i++) {
          Entry entry1 = entries.get(i - 1);
          Entry entry2 = entries.get(i);
          r.renderQuad(
              element.getX() + entry1.offset.getValueAsInt(element.getWidth()),
              element.getY(),
              entry2.offset.getValueAsInt(element.getWidth()) - entry1.offset.getValueAsInt(element.getWidth()),
              element.getHeight(),
              entry1.color,
              entry2.color,
              entry2.color,
              entry1.color);
        }
      } else {
        for (int i = 1; i < entries.size(); i++) {
          Entry entry1 = entries.get(i - 1);
          Entry entry2 = entries.get(i);
          int yStart = element.getY() + entry1.offset.getValueAsInt(element.getHeight());
          int yEnd = element.getY() + entry2.offset.getValueAsInt(element.getHeight());
          r.renderQuad(
              element.getX(),
              yStart,
              element.getWidth(),
              yEnd - yStart,
              entry1.color,
              entry1.color,
              entry2.color,
              entry2.color);
        }
      }
    }
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }

  /**
	 * The Class Entry.
	 */
  private static class Entry {
    
    /** The offset. */
    @Nonnull
    public final SizeValue offset;
    
    /** The color. */
    @Nonnull
    public final Color color;

    /**
	 * Instantiates a new entry.
	 *
	 * @param offset the offset
	 * @param color  the color
	 */
    public Entry(@Nonnull final SizeValue offset, @Nonnull final Color color) {
      this.offset = offset;
      this.color = color;
    }
  }
}
