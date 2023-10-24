package de.lessvoid.nifty;

import javax.annotation.Nullable;

/**
 * The Class Size.
 */
public class Size {
	
	/** The m width. */
	private final int m_width;
	
	/** The m height. */
	private final int m_height;

	/**
	 * Instantiates a new size.
	 */
	public Size() {
		this(0, 0);
	}

	/**
	 * Instantiates a new size.
	 *
	 * @param width  the width
	 * @param height the height
	 */
	public Size(int width, int height) {
		m_width = width;
		m_height = height;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return m_width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return m_height;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + m_height;
		result = prime * result + m_width;
		return result;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(@Nullable Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Size other = (Size) obj;
		if (m_height != other.m_height)
			return false;
		if (m_width != other.m_width)
			return false;
		return true;
	}
}
