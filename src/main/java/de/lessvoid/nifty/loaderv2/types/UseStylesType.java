package de.lessvoid.nifty.loaderv2.types;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.tools.StringHelper;

/**
 * The Class UseStylesType.
 */
public class UseStylesType extends XmlBaseType {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(UseStylesType.class.getName());

  /**
	 * Output.
	 *
	 * @param offset the offset
	 * @return the string
	 */
  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<useStyle> " + super.output(offset);
  }

  /**
	 * Load style.
	 *
	 * @param niftyLoader the nifty loader
	 * @param niftyType   the nifty type
	 * @param nifty       the nifty
	 * @throws Exception the exception
	 */
  public void loadStyle(
      @Nonnull final NiftyLoader niftyLoader,
      @Nonnull final NiftyType niftyType,
      @Nonnull final Nifty nifty) throws Exception {
    final String filename = getAttributes().get("filename");
    if (filename == null) {
      log.log(Level.SEVERE, "Missing filename attribute for style!");
    } else {
      niftyLoader.loadStyleFile("nifty-styles.nxs", filename, niftyType, nifty);
    }
  }
}
