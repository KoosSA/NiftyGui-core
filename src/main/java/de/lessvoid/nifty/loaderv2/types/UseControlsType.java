package de.lessvoid.nifty.loaderv2.types;

import java.util.logging.Logger;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.tools.StringHelper;

/**
 * The Class UseControlsType.
 */
public class UseControlsType extends XmlBaseType {
  
  /** The Constant log. */
  private static final Logger log = Logger.getLogger(UseControlsType.class.getName());

  /**
	 * Output.
	 *
	 * @param offset the offset
	 * @return the string
	 */
  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<useControls> " + super.output(offset);
  }

  /**
	 * Load control.
	 *
	 * @param niftyLoader the nifty loader
	 * @param niftyType   the nifty type
	 * @throws Exception the exception
	 */
  public void loadControl(@Nonnull final NiftyLoader niftyLoader, @Nonnull final NiftyType niftyType) throws Exception {
    final String filename = getAttributes().get("filename");
    if (filename == null) {
      log.severe("Missing filename attribute for control");
    } else {
      niftyLoader.loadControlFile("nifty-controls.nxs", filename, niftyType);
    }
  }
}
