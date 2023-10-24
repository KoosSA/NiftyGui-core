package de.lessvoid.nifty.tools;

import java.util.ArrayList;

import javax.annotation.Nonnull;

/**
 * A value computed based on linear interpolation between a set of points.
 *
 * @author void
 */
public class LinearInterpolator {
  
  /** The curve. */
  @Nonnull
  private final ArrayList<Point> curve = new ArrayList<Point>();
  
  /** The max X. */
  private float maxX = 0;

  /**
	 * Adds the point.
	 *
	 * @param x the x
	 * @param y the y
	 */
  public void addPoint(final float x, final float y) {
    curve.add(new Point(x, y));
  }

  /**
	 * Prepare.
	 */
  public void prepare() {
    maxX = calcMaxX(curve);
    for (Point p : curve) {
      p.x = p.x / maxX;
    }
  }

  /**
	 * Gets the max X.
	 *
	 * @return the max X
	 */
  public float getMaxX() {
    return maxX;
  }

  /**
	 * Gets the value.
	 *
	 * @param x the x
	 * @return the value
	 */
  public float getValue(final float x) {
    Point p0 = curve.get(0);
    for (int i = 1; i < curve.size(); i++) {
      Point p1 = curve.get(i);
      if (isInInterval(x, p0, p1)) {
        return calcValue(x, p0, p1);
      }
      p0 = p1;
    }
    if (x > curve.get(curve.size() - 1).x) {
      return curve.get(curve.size() - 1).y;
    } else if (x < curve.get(0).x) {
      return curve.get(0).y;
    } else {
      return 0.0f;
    }
  }

  /**
	 * Checks if is in interval.
	 *
	 * @param x  the x
	 * @param p0 the p 0
	 * @param p1 the p 1
	 * @return true, if is in interval
	 */
  private boolean isInInterval(final float x, @Nonnull final Point p0, @Nonnull final Point p1) {
    return x >= p0.x && x <= p1.x;
  }

  /**
	 * Calc value.
	 *
	 * @param x  the x
	 * @param p0 the p 0
	 * @param p1 the p 1
	 * @return the float
	 */
  private float calcValue(final float x, @Nonnull final Point p0, @Nonnull final Point p1) {
    float st = (x - p0.x) / (p1.x - p0.x);
    return p0.y + st * (p1.y - p0.y);
  }

  /**
	 * Calc max X.
	 *
	 * @param curve the curve
	 * @return the float
	 */
  private float calcMaxX(@Nonnull final ArrayList<Point> curve) {
    float maxX = -1.0f;
    for (Point p : curve) {
      if (p.x > maxX) {
        maxX = p.x;
      }
    }
    return maxX;
  }

  /**
	 * The Class Point.
	 */
  public static class Point {
    
    /** The x. */
    public float x;
    
    /** The y. */
    public final float y;

    /**
	 * Instantiates a new point.
	 *
	 * @param x the x
	 * @param y the y
	 */
    public Point(final float x, final float y) {
      this.x = x;
      this.y = y;
    }
  }
}
