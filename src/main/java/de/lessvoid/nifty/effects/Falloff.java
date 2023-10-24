package de.lessvoid.nifty.effects;

import java.util.Properties;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * The Class Falloff.
 *
 * @author void
 */
public class Falloff {
  
  /** The Constant HOVER_FALLOFF_TYPE. */
  @Nonnull
  public static final String HOVER_FALLOFF_TYPE = "hoverFalloffType";
  
  /** The Constant HOVER_FALLOFF_CONSTRAINT. */
  @Nonnull
  public static final String HOVER_FALLOFF_CONSTRAINT = "hoverFalloffConstraint";
  
  /** The Constant HOVER_WIDTH. */
  @Nonnull
  public static final String HOVER_WIDTH = "hoverWidth";
  
  /** The Constant HOVER_HEIGHT. */
  @Nonnull
  public static final String HOVER_HEIGHT = "hoverHeight";

  /**
	 * The Enum HoverFalloffType.
	 *
	 * @author void
	 */
  public enum HoverFalloffType {
    
    /** The none. */
    none, 
 /** The linear. */
 // default
    linear
  }

  /**
	 * The Enum HoverFalloffConstraint.
	 *
	 * @author void
	 */
  public enum HoverFalloffConstraint {
    
    /** The none. */
    none, 
 /** The vertical. */
 // default
    vertical,
    
    /** The horizontal. */
    horizontal,
    
    /** The both. */
    both
  }

  /** The falloff constraint. */
  @Nonnull
  private final HoverFalloffConstraint falloffConstraint;
  
  /** The falloff type. */
  @Nonnull
  private final HoverFalloffType falloffType;
  
  /** The hover width. */
  @Nonnull
  private final SizeValue hoverWidth;
  
  /** The hover height. */
  @Nonnull
  private final SizeValue hoverHeight;
  
  /** The falloff value. */
  private float falloffValue;

  /**
	 * Instantiates a new falloff.
	 *
	 * @param properties the properties
	 */
  public Falloff(@Nonnull final Properties properties) {
    String falloffTypeString = properties.getProperty(Falloff.HOVER_FALLOFF_TYPE);
    if (falloffTypeString != null) {
      falloffType = HoverFalloffType.valueOf(falloffTypeString);
    } else {
      falloffType = HoverFalloffType.none;
    }

    String falloffConstraintString = properties.getProperty(Falloff.HOVER_FALLOFF_CONSTRAINT);
    if (falloffConstraintString != null) {
      falloffConstraint = HoverFalloffConstraint.valueOf(falloffConstraintString);
    } else {
      falloffConstraint = HoverFalloffConstraint.none;
    }

    hoverWidth = new SizeValue(properties.getProperty(Falloff.HOVER_WIDTH));
    hoverHeight = new SizeValue(properties.getProperty(Falloff.HOVER_HEIGHT));
  }

  /**
	 * Apply properties.
	 *
	 * @param properties the properties
	 */
  public void applyProperties(final Properties properties) {
  }

  /**
	 * Checks if is inside.
	 *
	 * @param element the element
	 * @param x       the x
	 * @param y       the y
	 * @return true, if is inside
	 */
  public final boolean isInside(@Nonnull final Element element, final int x, final int y) {
    int centerX = element.getX() + element.getWidth() / 2;
    int centerY = element.getY() + element.getHeight() / 2;

    int horizontalHover = getHorizontalHover(element);
    int verticalHover = getVerticalHover(element);

    return x > (centerX - horizontalHover / 2) &&
        x <= (centerX + horizontalHover / 2) &&
        y > (centerY - verticalHover / 2) &&
        y <= (centerY + verticalHover / 2);
  }

  /**
	 * Gets the vertical hover.
	 *
	 * @param element the element
	 * @return the vertical hover
	 */
  private int getVerticalHover(@Nonnull Element element) {
    return hoverHeight.hasValue() ? hoverHeight.getValueAsInt(element.getHeight()) : element.getHeight();
  }

  /**
	 * Gets the horizontal hover.
	 *
	 * @param element the element
	 * @return the horizontal hover
	 */
  private int getHorizontalHover(@Nonnull Element element) {
    return hoverWidth.hasValue() ? hoverWidth.getValueAsInt(element.getWidth()) : element.getWidth();
  }

  /**
	 * Update falloff value.
	 *
	 * @param element the element
	 * @param mouseX  the mouse X
	 * @param mouseY  the mouse Y
	 */
  public void updateFalloffValue(@Nonnull final Element element, final int mouseX, final int mouseY) {
    if (falloffConstraint == HoverFalloffConstraint.none || falloffType == HoverFalloffType.none) {
      falloffValue = 1.0f;
      return;
    }

    int centerX = element.getX() + element.getWidth() / 2;
    int centerY = element.getY() + element.getHeight() / 2;
    float dx = mouseX - centerX;
    float dy = mouseY - centerY;
    float falloff = 0.0f;

    if (falloffConstraint == HoverFalloffConstraint.vertical) {
      dx = 0.0f;
      falloff = getVerticalHover(element) / 2;
    }

    if (falloffConstraint == HoverFalloffConstraint.horizontal) {
      dy = 0.0f;
      falloff = getHorizontalHover(element) / 2;
    }

    if (falloffConstraint == HoverFalloffConstraint.both) {
      /* determine the angle from center of element to current mouse position.
         NOTE: if dy and dy are zero it is not possible to determine the angle.
         Assume an angle of zero degrees if dy AND dx are 0 */
      double dA;
      if (dy == 0.0 && dx == 0.0) {
        dA = 0.0;
      } else {
        dA = Math.abs(Math.atan((dy / dx)));
      }
      // determine the angle from the center of the object to one of its corners to find the flip / cutoff angle
      float elA = (float) (getHorizontalHover(element) / 2);
      float elB = (float) (getVerticalHover(element) / 2);
      double dB;
      dB = Math.abs(Math.atan((elB / elA)));
      if ((Math.abs(Math.toDegrees(dA)) >= 0.0) && (Math.abs(Math.toDegrees(dA)) <= Math.abs(Math.toDegrees(dB)))) {
        //use cos(dA) = adj / hyp: so hyp = adj / cos(dA)
        falloff = (float) (elA / Math.cos(dA));
      }
      if ((Math.abs(Math.toDegrees(dA)) > Math.abs(Math.toDegrees(dB))) && (Math.abs(Math.toDegrees(dA)) <= 90.0)) {
        //use sin(dA) = opp / hyp: so hyp = opp / sin(dA)
        falloff = (float) (elB / Math.sin(dA));
      }
    }

    float d = (float) Math.hypot(dx, dy);
    if (d > falloff) {
      falloffValue = 0.0f;
    }

    falloffValue = Math.abs(1.0f - d / falloff);
  }

  /**
	 * Gets the falloff value.
	 *
	 * @return the falloff value
	 */
  public float getFalloffValue() {
    return falloffValue;
  }

  /**
	 * Gets the falloff constraint.
	 *
	 * @return the falloff constraint
	 */
  @Nonnull
  public HoverFalloffConstraint getFalloffConstraint() {
    return falloffConstraint;
  }
}
