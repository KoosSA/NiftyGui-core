package de.lessvoid.nifty.nulldevice;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * This is a sound device that does not play or create any music. In case the GUI is not supposed to play music,
 * this device works.
 */
public class NullSoundDevice implements SoundDevice {
  
  /**
	 * Sets the resource loader.
	 *
	 * @param niftyResourceLoader the new resource loader
	 */
  @Override
  public void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader) {
  }

  /**
	 * Load sound.
	 *
	 * @param soundSystem the sound system
	 * @param filename    the filename
	 * @return the sound handle
	 */
  @Nullable
  @Override
  public SoundHandle loadSound(@Nonnull SoundSystem soundSystem, @Nonnull String filename) {
    return null;
  }

  /**
	 * Load music.
	 *
	 * @param soundSystem the sound system
	 * @param filename    the filename
	 * @return the sound handle
	 */
  @Nullable
  @Override
  public SoundHandle loadMusic(@Nonnull SoundSystem soundSystem, @Nonnull String filename) {
    return null;
  }

  /**
	 * Update.
	 *
	 * @param delta the delta
	 */
  @Override
  public void update(int delta) {
  }
}
