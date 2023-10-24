package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.tools.StringHelper;

/**
 * The Class RegisterMusicType.
 */
public class RegisterMusicType extends XmlBaseType {
  
  /**
	 * Output.
	 *
	 * @param offset the offset
	 * @return the string
	 */
  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<registerMusic> " + super.output(offset);
  }

  /**
	 * Materialize.
	 *
	 * @param soundSystem the sound system
	 */
  public void materialize(@Nonnull final SoundSystem soundSystem) {
    String fileName = getFilename();
    if (fileName == null) {
      return;
    }
    soundSystem.addMusic(getId(), fileName);
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
