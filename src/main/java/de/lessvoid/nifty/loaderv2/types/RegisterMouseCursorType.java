package de.lessvoid.nifty.loaderv2.types;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.tools.StringHelper;

/**
 * The Class RegisterMouseCursorType.
 */
public class RegisterMouseCursorType extends XmlBaseType {
  
  /**
	 * Output.
	 *
	 * @param offset the offset
	 * @return the string
	 */
  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<registerMouseCursor> " + super.output(offset);
  }

  /**
	 * Materialize.
	 *
	 * @param nifty the nifty
	 * @param log   the log
	 */
  public void materialize(@Nonnull final Nifty nifty, @Nonnull final Logger log) {
    try {
      nifty.getNiftyMouse().registerMouseCursor(getId(), getFilename(), getHotspotX(), getHotspotY());
      log.fine("Registering mouseCursor with id [" + getId() + "]");
    } catch (IOException e) {
      log.warning("Error registering mouse cursor: " + e.getMessage());
    }
  }

  /**
	 * Gets the id.
	 *
	 * @return the id
	 */
  @Nullable
  private String getId() {
    return getAttributes().get("id");
  }

  /**
	 * Gets the filename.
	 *
	 * @return the filename
	 */
  @Nullable
  private String getFilename() {
    return getAttributes().get("filename");
  }

  /**
	 * Gets the hotspot X.
	 *
	 * @return the hotspot X
	 */
  private int getHotspotX() {
    return getAttributes().getAsInteger("hotspotX", 0);
  }

  /**
	 * Gets the hotspot Y.
	 *
	 * @return the hotspot Y
	 */
  private int getHotspotY() {
    return getAttributes().getAsInteger("hotspotY", 0);
  }
}
