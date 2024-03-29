package de.lessvoid.xml.tools;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Helper to get Class from class name.
 * @author void
 */
public final class ClassHelper {

  /**
   * logger.
   */
  private static final Logger log = Logger.getLogger(ClassHelper.class.getName());

  /**
   * You can't initialize this class.
   */
  private ClassHelper() {
  }

  /**
   * Load Class with the given full qualified name.
   * @param className name of class to load
   * @return Class object or null
   */
  @Nullable
  public static Class<?> loadClass(@Nonnull final String className) {
    try {
		return Thread.currentThread().getContextClassLoader().loadClass(className);
//      return Class.forName(className);
    } catch (Exception e) {
      log.warning("class [" + className + "] could not be found (" + e.getMessage() + ")");
    }
    return null;
  }

  /**
	 * dynamically load the given class, create and return a new instance.
	 *
	 * @param <T>       class
	 * @param className className
	 * @param type      type
	 * @return new ScreenController instance or null
	 */
  @Nullable
  public static <T> T getInstance(@Nonnull final String className, @Nonnull final Class<T> type) {
    try {
		Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
//	  Class<?> cls = Class.forName(className);
	  if (type.isAssignableFrom(cls)) {
	    return type.cast(cls.newInstance());
	  } else {
	    log.warning("given class [" + className + "] does not implement [" + type.getName() + "]");
	  }
    } catch (Exception e) {
      log.warning("class [" + className + "] could not be instantiated (" + e.toString() + ")");
    }
    return null;
  }

  /**
	 * Gets the single instance of ClassHelper.
	 *
	 * @param <T>   the generic type
	 * @param clazz the clazz
	 * @return single instance of ClassHelper
	 */
  @Nullable
  public static <T> T getInstance(@Nonnull final Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      log.warning("class [" + clazz.getName() + "] could not be instantiated");
    }
    return null;
  }
}

