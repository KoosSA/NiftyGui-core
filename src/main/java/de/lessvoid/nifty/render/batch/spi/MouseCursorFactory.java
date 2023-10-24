package de.lessvoid.nifty.render.batch.spi;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * A factory for creating MouseCursor objects.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface MouseCursorFactory {
  
  /**
	 * Creates the.
	 *
	 * @param filename       the filename
	 * @param hotspotX       the hotspot X
	 * @param hotspotY       the hotspot Y
	 * @param resourceLoader the resource loader
	 * @return the mouse cursor
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  @Nullable
  public MouseCursor create(
          @Nonnull final String filename,
          final int hotspotX,
          final int hotspotY,
          @Nonnull final NiftyResourceLoader resourceLoader) throws IOException;
}
