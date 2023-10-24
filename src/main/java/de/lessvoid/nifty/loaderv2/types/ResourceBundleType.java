package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.tools.StringHelper;

/**
 * The Class ResourceBundleType.
 */
public class ResourceBundleType extends XmlBaseType {
  
  /**
	 * Output.
	 *
	 * @param offset the offset
	 * @return the string
	 */
  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<resourceBundle> " + super.output(offset);
  }

  /**
	 * Materialize.
	 *
	 * @param nifty the nifty
	 */
  public void materialize(@Nonnull final Nifty nifty) {
    String id = getId();
    String fileName = getFilename();
    if (id != null && fileName != null) {
      nifty.addResourceBundle(id, fileName);
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
}
