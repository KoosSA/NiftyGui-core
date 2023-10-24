package de.lessvoid.nifty.loaderv2.types.helper;

import javax.annotation.Nonnull;

/**
 * one value: [applied to all] two values: [top and bottom], [left and right]
 * three values: [top], [left and right], [bottom] four values: [top], [right],
 * [bottom], [left].
 *
 * @author void
 */
public class PaddingAttributeParser {
  
  /** The left. */
  @Nonnull
  private final String left;
  
  /** The right. */
  @Nonnull
  private final String right;
  
  /** The top. */
  @Nonnull
  private final String top;
  
  /** The bottom. */
  @Nonnull
  private final String bottom;

  /**
	 * Instantiates a new padding attribute parser.
	 *
	 * @param input the input
	 * @throws Exception the exception
	 */
  public PaddingAttributeParser(@Nonnull final String input) throws Exception {

    String[] values = input.split(",");
    if (values.length == 0) {
      throw new Exception("parsing error, paddingString is empty");
    }

    int valueCount = values.length;
    if (valueCount == 1) {
      if (values[0].length() == 0) {
        throw new Exception("parsing error, paddingString is empty");
      }
      left = values[0];
      right = values[0];
      top = values[0];
      bottom = values[0];
    } else if (valueCount == 2) {
      left = values[1];
      right = values[1];
      top = values[0];
      bottom = values[0];
    } else if (valueCount == 3) {
      left = values[1];
      right = values[1];
      top = values[0];
      bottom = values[2];
    } else if (valueCount == 4) {
      left = values[3];
      right = values[1];
      top = values[0];
      bottom = values[2];
    } else {
      throw new Exception("parsing error, paddingString count error (" + valueCount + ") expecting value from 1 to 4");
    }
  }

  /**
	 * Gets the left.
	 *
	 * @return the left
	 */
  @Nonnull
  public String getLeft() {
    return left;
  }

  /**
	 * Gets the top.
	 *
	 * @return the top
	 */
  @Nonnull
  public String getTop() {
    return top;
  }

  /**
	 * Gets the right.
	 *
	 * @return the right
	 */
  @Nonnull
  public String getRight() {
    return right;
  }

  /**
	 * Gets the bottom.
	 *
	 * @return the bottom
	 */
  @Nonnull
  public String getBottom() {
    return bottom;
  }
}
