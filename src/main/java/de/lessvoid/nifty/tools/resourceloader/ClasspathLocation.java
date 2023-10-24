package de.lessvoid.nifty.tools.resourceloader;

import java.io.InputStream;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;

/**
 * A resource location that searches the classpath.
 *
 * @author kevin
 */
public class ClasspathLocation implements ResourceLocation {
  
  /**
	 * Gets the resource.
	 *
	 * @param ref the ref
	 * @return the resource
	 */
  @Nullable
  @Override
  public URL getResource(@Nonnull final String ref) {
    String cpRef = ref.replace('\\', '/');
    return Thread.currentThread().getContextClassLoader().getResource(cpRef);
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
    String cpRef = ref.replace('\\', '/');
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(cpRef);
  }
}
