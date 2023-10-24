package de.lessvoid.nifty.tools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Checks a String if it contains a Nifty inline color. This class is used while
 * parsing text strings for colors.
 * <p/>
 * This has now been changed to not return an Result instance anymore but simply
 * remembers the last result in instance variables. This makes the class not
 * thread-safe but it was not shared between threads anyway.
 *
 * @author void
 */
public class ColorValueParser {
  
  /** The is color. */
  private boolean isColor;
  
  /** The next index. */
  private int nextIndex;
  
  /** The color. */
  @Nullable
  private Color color;

  /**
	 * Instantiates a new color value parser.
	 */
  public ColorValueParser() {
    setNoResult();
  }

  /**
	 * Checks if is color.
	 *
	 * @return true, if is color
	 */
  public boolean isColor() {
    return isColor;
  }

  /**
	 * Gets the next index.
	 *
	 * @return the next index
	 */
  public int getNextIndex() {
    return nextIndex;
  }

  /**
	 * Gets the color.
	 *
	 * @return the color
	 */
  @Nullable
  public Color getColor() {
    return color;
  }

  /**
	 * Checks if is color.
	 *
	 * @param text     the text
	 * @param startIdx the start idx
	 * @return true, if is color
	 */
  public boolean isColor(@Nonnull final String text, final int startIdx) {
    if (text.startsWith("\\#", startIdx)) {
      int endIdx = text.indexOf('#', startIdx + 2);
      if (endIdx != -1) {
        setResult(text.substring(startIdx + 1, endIdx), endIdx + 1);
        return isColor;
      }
    }
    setNoResult();
    return false;
  }

  /**
	 * Sets the no result.
	 */
  private void setNoResult() {
    nextIndex = -1;
    color = null;
    isColor = false;
  }

  /**
	 * Sets the result.
	 *
	 * @param value  the value
	 * @param endIdx the end idx
	 */
  private void setResult(@Nonnull final String value, final int endIdx) {
    nextIndex = -1;
    color = null;
    isColor = Color.check(value);
    if (isColor) {
      color = new Color(value);
      nextIndex = endIdx;
    }
  }
}
