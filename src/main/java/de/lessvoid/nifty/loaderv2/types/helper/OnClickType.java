package de.lessvoid.nifty.loaderv2.types.helper;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;

/**
 * The Class OnClickType.
 */
public class OnClickType {
  
  /** The pattern used to check. */
  private static final Pattern VALID_PATTERN = Pattern.compile("\\w+\\((?:|\\w+(?:,\\s*\\w+)*)\\)");
  
  /** The value. */
  private final String value;

  /**
	 * Instantiates a new on click type.
	 *
	 * @param valueParam the value param
	 */
  public OnClickType(final String valueParam) {
    this.value = valueParam;
  }

  /**
	 * Checks if is valid.
	 *
	 * @return true, if is valid
	 */
  public boolean isValid() {
    if (value == null) {
      return false;
    }

    return VALID_PATTERN.matcher(value).matches();
  }

  /**
	 * Gets the method.
	 *
	 * @param nifty             the nifty
	 * @param controlController the control controller
	 * @return the method
	 */
  @Nonnull
  public NiftyMethodInvoker getMethod(final Nifty nifty, final Object... controlController) {
    return new NiftyMethodInvoker(nifty, value, controlController);
  }
}
