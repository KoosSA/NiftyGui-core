package org.jglfont.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jglfont.spi.ResourceLoader;

/**
 * The Class ResourceLoaderImpl.
 */
public class ResourceLoaderImpl implements ResourceLoader {

  /**
	 * Load.
	 *
	 * @param filename the filename
	 * @return the input stream
	 */
  @Override
  public InputStream load(final String filename) {
    InputStream is = Thread.currentThread().getClass().getResourceAsStream("/" + filename);
    if (is == null) {
      File file = new File(filename);
      if (file.exists()) {
        try {
          is = new FileInputStream(file);
        } catch (FileNotFoundException ignore) {
        }
      }
    }
    return is;
  }
}
