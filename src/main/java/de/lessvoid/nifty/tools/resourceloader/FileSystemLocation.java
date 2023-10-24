package de.lessvoid.nifty.tools.resourceloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;

/**
 * A resource loading location that searches somewhere on the classpath.
 *
 * @author kevin
 */
public class FileSystemLocation implements ResourceLocation {
  
  /** The root of the file system to search. */
  @Nonnull
  private final File root;

  /**
	 * Create a new resource location based on the file system.
	 *
	 * @param root The root of the file system to search
	 */
  public FileSystemLocation(@Nonnull final File root) {
    this.root = root;
  }

  /**
	 * Gets the resource.
	 *
	 * @param ref the ref
	 * @return the resource
	 */
  @Nullable
  @Override
  public URL getResource(@Nonnull final String ref) {
    try {
      File file = new File(root, ref);
      if (!file.exists()) {
        file = new File(ref);
      }
      if (!file.exists()) {
        return null;
      }

      return file.toURI().toURL();
    } catch (IOException e) {
      return null;
    }
  }

  /**
	 * Gets the resource as stream.
	 *
	 * @param ref the ref
	 * @return the resource as stream
	 */
  @Nullable
  @Override
  @WillNotClose
  public InputStream getResourceAsStream(@Nonnull final String ref) {
    try {
      File file = new File(root, ref);
      if (!file.exists()) {
        file = new File(ref);
      }
      return new FileInputStream(file);
    } catch (IOException e) {
      return null;
    }
  }

}
