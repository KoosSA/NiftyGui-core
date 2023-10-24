package de.lessvoid.nifty.loaderv2.types.apply;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.layout.manager.AbsolutePositionLayout;
import de.lessvoid.nifty.layout.manager.CenterLayout;
import de.lessvoid.nifty.layout.manager.HorizontalLayout;
import de.lessvoid.nifty.layout.manager.LayoutManager;
import de.lessvoid.nifty.layout.manager.OverlayLayout;
import de.lessvoid.nifty.layout.manager.VerticalLayout;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.image.ImageMode;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * The Class Convert.
 */
public class Convert {
  
  /** The Constant log. */
  private static final Logger log = Logger.getLogger(Convert.class.getName());
  
  /** The Constant DEFAULT_PADDING. */
  public static final String DEFAULT_PADDING = "0px";
  
  /** The Constant DEFAULT_MARGIN. */
  public static final String DEFAULT_MARGIN = "0px";
  
  /** The Constant DEFAULT_HORIZONTAL_ALIGN. */
  public static final HorizontalAlign DEFAULT_HORIZONTAL_ALIGN = HorizontalAlign.horizontalDefault;
  
  /** The Constant DEFAULT_VERTICAL_ALIGN. */
  public static final VerticalAlign DEFAULT_VERTICAL_ALIGN = VerticalAlign.verticalDefault;
  
  /** The Constant DEFAULT_TEXT_HORIZONTAL_ALIGN. */
  public static final HorizontalAlign DEFAULT_TEXT_HORIZONTAL_ALIGN = HorizontalAlign.center;
  
  /** The Constant DEFAULT_TEXT_VERTICAL_ALIGN. */
  public static final VerticalAlign DEFAULT_TEXT_VERTICAL_ALIGN = VerticalAlign.center;
  
  /** The Constant DEFAULT_IMAGE_FILTER. */
  public static final boolean DEFAULT_IMAGE_FILTER = false;
  
  /** The Constant DEFAULT_FOCUSABLE. */
  public static final boolean DEFAULT_FOCUSABLE = false;
  
  /** The Constant DEFAULT_VISIBLE_TO_MOUSE. */
  public static final boolean DEFAULT_VISIBLE_TO_MOUSE = false;
  
  /** The Constant DEFAULT_VISIBLE. */
  public static final boolean DEFAULT_VISIBLE = true;
  
  /** The Constant DEFAULT_CHILD_CLIP. */
  public static final boolean DEFAULT_CHILD_CLIP = false;
  
  /** The Constant DEFAULT_RENDER_ORDER. */
  public static final int DEFAULT_RENDER_ORDER = 0;
  
  /** The Constant verticalLayout. */
  private static final VerticalLayout verticalLayout = new VerticalLayout();
  
  /** The Constant centerLayout. */
  private static final CenterLayout centerLayout = new CenterLayout();
  
  /** The Constant horizontalLayout. */
  private static final HorizontalLayout horizontalLayout = new HorizontalLayout();
  
  /** The Constant overlayLayout. */
  private static final OverlayLayout overlayLayout = new OverlayLayout();
  
  /** The Constant absolutePositionLayout. */
  private static final AbsolutePositionLayout absolutePositionLayout = new AbsolutePositionLayout();
  
  /** The Constant absolutePositionLayoutKeepInside. */
  private static final AbsolutePositionLayout absolutePositionLayoutKeepInside = new AbsolutePositionLayout(
      new AbsolutePositionLayout.KeepInsidePostProcess());

  /**
	 * Font.
	 *
	 * @param niftyRenderEngine the nifty render engine
	 * @param value             the value
	 * @return the render font
	 */
  @Nullable
  public RenderFont font(@Nonnull final NiftyRenderEngine niftyRenderEngine, @Nullable final String value) {
    if (value == null) {
      return null;
    }
    return niftyRenderEngine.createFont(value);
  }

  /**
	 * Size value.
	 *
	 * @param value the value
	 * @return the size value
	 */
  @Nonnull
  public SizeValue sizeValue(@Nullable final String value) {
    return new SizeValue(value);
  }

  /**
	 * Padding size value.
	 *
	 * @param value        the value
	 * @param defaultValue the default value
	 * @return the size value
	 */
  @Nonnull
  public SizeValue paddingSizeValue(@Nullable final String value, @Nonnull final String defaultValue) {
    if (value == null) {
      return new SizeValue(defaultValue);
    }
    return new SizeValue(value);
  }

  /**
	 * Horizontal align.
	 *
	 * @param value the value
	 * @return the horizontal align
	 */
  @Nonnull
  public HorizontalAlign horizontalAlign(@Nullable final String value) {
    if (value == null) {
      return DEFAULT_HORIZONTAL_ALIGN;
    }
    try {
      return HorizontalAlign.valueOf(value);
    } catch (IllegalArgumentException e) {
      log.warning("Illegal value for horizontal align: \"" + value + "\"");
      return DEFAULT_TEXT_HORIZONTAL_ALIGN;
    }
  }

  /**
	 * Text horizontal align.
	 *
	 * @param value the value
	 * @return the horizontal align
	 */
  @Nonnull
  public HorizontalAlign textHorizontalAlign(@Nullable final String value) {
    if (value == null) {
      return DEFAULT_TEXT_HORIZONTAL_ALIGN;
    }
    try {
      return HorizontalAlign.valueOf(value);
    } catch (IllegalArgumentException e) {
      log.warning("Illegal value for horizontal text align: \"" + value + "\"");
      return DEFAULT_TEXT_HORIZONTAL_ALIGN;
    }
  }

  /**
	 * Vertical align.
	 *
	 * @param value the value
	 * @return the vertical align
	 */
  @Nonnull
  public VerticalAlign verticalAlign(@Nullable final String value) {
    if (value == null) {
      return DEFAULT_VERTICAL_ALIGN;
    }
    try {
      return VerticalAlign.valueOf(value);
    } catch (IllegalArgumentException e) {
      log.warning("Illegal value for vertical align: \"" + value + "\"");
      return DEFAULT_VERTICAL_ALIGN;
    }
  }

  /**
	 * Text vertical align.
	 *
	 * @param value the value
	 * @return the vertical align
	 */
  @Nonnull
  public VerticalAlign textVerticalAlign(@Nullable final String value) {
    if (value == null) {
      return DEFAULT_TEXT_VERTICAL_ALIGN;
    }
    try {
      return VerticalAlign.valueOf(value);
    } catch (IllegalArgumentException e) {
      log.warning("Illegal value for vertical text align: \"" + value + "\"");
      return DEFAULT_TEXT_VERTICAL_ALIGN;
    }
  }

  /**
	 * Layout manager.
	 *
	 * @param type the type
	 * @return the layout manager
	 */
  @Nullable
  public LayoutManager layoutManager(@Nullable final String type) {
    if (type == null) {
      return null;
    }
    String typeCompare = type.toLowerCase();
    if (typeCompare.equals("vertical")) {
      return verticalLayout;
    } else if (typeCompare.equals("center")) {
      return centerLayout;
    } else if (typeCompare.equals("horizontal")) {
      return horizontalLayout;
    } else if (typeCompare.equals("overlay")) {
      return overlayLayout;
    } else if (typeCompare.equals("absolute")) {
      return absolutePositionLayout;
    } else if (typeCompare.equals("absolute-inside")) {
      return absolutePositionLayoutKeepInside;
    }

    return null;
  }

  /**
	 * Color.
	 *
	 * @param value the value
	 * @return the color
	 */
  @Nullable
  public Color color(@Nullable final String value) {
    if (value == null) {
      return null;
    }
    return new Color(value);
  }

  /**
	 * Color.
	 *
	 * @param value        the value
	 * @param defaultColor the default color
	 * @return the color
	 */
  @Nonnull
  public Color color(@Nullable final String value, @Nonnull final Color defaultColor) {
    if (value == null) {
      return defaultColor;
    }
    return new Color(value);
  }

  /**
	 * Image mode.
	 *
	 * @param areaProviderProperty   the area provider property
	 * @param renderStrategyProperty the render strategy property
	 * @return the image mode
	 */
  @Nonnull
  public ImageMode imageMode(
      @Nullable final String areaProviderProperty,
      @Nullable final String renderStrategyProperty) {
    return ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty, renderStrategyProperty);
  }

  /**
	 * Inset size value.
	 *
	 * @param value       the value
	 * @param imageHeight the image height
	 * @return the int
	 */
  public int insetSizeValue(@Nullable final String value, final int imageHeight) {
    if (value == null) {
      return 0;
    }
    SizeValue sizeValue = new SizeValue(value);
    return sizeValue.getValueAsInt(imageHeight);
  }
}
