package de.lessvoid.nifty.render.batch.spi;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface MouseCursorFactory {
  @Nullable
  public MouseCursor create(
          @Nonnull final String filename,
          final int hotspotX,
          final int hotspotY,
          @Nonnull final NiftyResourceLoader resourceLoader) throws IOException;
}
