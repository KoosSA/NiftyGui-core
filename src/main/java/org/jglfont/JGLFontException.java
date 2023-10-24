package org.jglfont;


/**
 * The Class JGLFontException.
 */
public class JGLFontException extends RuntimeException {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
	 * Instantiates a new JGL font exception.
	 *
	 * @param message the message
	 */
  public JGLFontException(final String message) {
    super(message);
  }

  /**
	 * Instantiates a new JGL font exception.
	 *
	 * @param e the e
	 */
  public JGLFontException(final Exception e) {
    super (e);
  }
}
