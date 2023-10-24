package de.lessvoid.nifty.render.image;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.ParameterizedObjectFactory;

/**
 * The Class ImageModeHelper.
 */
public class ImageModeHelper {
  
  /** The Constant log. */
  private static final Logger log = Logger.getLogger(ParameterizedObjectFactory.class.getName());

  /**
	 * Instantiates a new image mode helper.
	 */
  private ImageModeHelper() {
  }

  /**
	 * Gets the area provider property.
	 *
	 * @param properties the properties
	 * @return the area provider property
	 */
  @Nullable
  public static String getAreaProviderProperty(final Properties properties) {
    return getAreaProviderProperty(new StringPropertyAdapter(properties));
  }

  /**
	 * Gets the area provider property.
	 *
	 * @param properties the properties
	 * @return the area provider property
	 */
  @Nullable
  public static String getAreaProviderProperty(@Nonnull final Map<String, String> properties) {
    final String imageModeProperty = properties.get("imageMode");
    String property = getAreaProviderProperty(imageModeProperty);
    if (property != null) {
      log.fine("imageMode property converted to imageArea property : " + imageModeProperty + " -> " + property);
      return property;
    }

    return properties.get("imageArea");
  }

  /**
	 * Gets the area provider property.
	 *
	 * @param imageModeProperty the image mode property
	 * @return the area provider property
	 */
  @Nullable
  public static String getAreaProviderProperty(@Nullable final String imageModeProperty) {
    if (imageModeProperty == null) {
      return null;
    }

    final String[] imageMode = imageModeProperty.split(":");
    final String imageModeName = imageMode[0];

    if (imageModeName.equals("normal") || imageModeName.equals("resize")) {
      return "fullimage";
    } else if (imageModeName.equals("subImage") || imageModeName.equals("subImageDirect") || imageModeName.equals
        ("repeat")) {
      return "subimage:" + getImageModeParameters(imageMode);
    } else if (imageModeName.equals("sprite")) {
      return "sprite:" + getImageModeParameters(imageMode);
    } else if (imageModeName.equals("sprite-resize")) {
      String imageModeParameters = imageMode[1];
      return "sprite:" + imageModeParameters.replace("," + getNinePartParameters(imageModeParameters), "");
    }else if (imageModeName.equals("subImage-resize")) {
      return "subimage:" + getImageModeParameters(imageMode).replaceFirst("(,+\\d*){12}$", "");
    } else {
      log.warning("imageMode property could not be converted to imageArea property : " + imageModeProperty);
      return null;
    }
  }

  /**
	 * Gets the render strategy property.
	 *
	 * @param properties the properties
	 * @return the render strategy property
	 */
  @Nullable
  public static String getRenderStrategyProperty(final Properties properties) {
    return getRenderStrategyProperty(new StringPropertyAdapter(properties));
  }

  /**
	 * Gets the render strategy property.
	 *
	 * @param properties the properties
	 * @return the render strategy property
	 */
  @Nullable
  public static String getRenderStrategyProperty(@Nonnull final Map<String, String> properties) {
    final String imageModeProperty = properties.get("imageMode");
    String property = getRenderStrategyProperty(imageModeProperty);
    if (property != null) {
      log.fine("imageMode property converted to renderStrategy property : " + imageModeProperty + " -> "
          + property);
      return property;
    }

    return properties.get("renderStrategy");
  }

  /**
	 * Gets the render strategy property.
	 *
	 * @param imageModeProperty the image mode property
	 * @return the render strategy property
	 */
  @Nullable
  public static String getRenderStrategyProperty(@Nullable final String imageModeProperty) {
    if (imageModeProperty == null) {
      return null;
    }

    final String[] imageMode = imageModeProperty.split(":");
    final String imageModeName = imageMode[0];

    if (imageModeName.equals("normal") || imageModeName.equals("subImage") || imageModeName.equals("sprite")) {
      return "resize";
    } else if (imageModeName.equals("subImageDirect")) {
      return "direct";
    } else if (imageModeName.equals("resize")) {
      return "nine-part:" + getImageModeParameters(imageMode);
    } else if (imageModeName.equals("sprite-resize")) {
      return "nine-part:" + getNinePartParameters(getImageModeParameters(imageMode));
    }else if (imageModeName.equals("subImage-resize")) {
      return "nine-part:" + getSubNinePartParameters(getImageModeParameters(imageMode));
    } else if (imageModeName.equals("repeat")) {
      return "repeat";
    } else {
      log.warning("imageMode property could not be converted to renderStrategy property : "
          + imageModeProperty);
      return null;
    }
  }

  /**
	 * Gets the image mode parameters.
	 *
	 * @param imageMode the image mode
	 * @return the image mode parameters
	 */
  private static String getImageModeParameters(@Nonnull final String[] imageMode) {
    if (imageMode.length > 1) {
      return imageMode[1];
    }

    return "";
  }

  /**
	 * Gets the nine part parameters.
	 *
	 * @param imageMode the image mode
	 * @return the nine part parameters
	 */
  private static String getNinePartParameters(@Nonnull final String imageMode) {
    String[] split = imageMode.split("(\\d+,){3}", 2);
    if (split.length > 1) {
      return split[1];
    }

    return "";
  }
  
  /**
	 * Get parameters from subImagemode.
	 *
	 * @param imageMode the image mode
	 * @return the sub nine part parameters
	 */
  private static String getSubNinePartParameters(@Nonnull final String imageMode) {
    String[] split = imageMode.split("(\\d+,){4}", 2);
    if (split.length > 1) {
      return split[1];
    }

    return "";
  }

  /**
   * Hack to circumvent a JRE oddity : Properties class is a Map<Object, Object> that can (actually should) only
   * contain <String, String> pairs ! Thus we cannot share a single get*Property taking a Map<String, String>
   * for code that calls it with Attributes and code that calls it with Properties.
   */
  private static class StringPropertyAdapter implements Map<String, String> {
    
    /** The m properties. */
    private final Properties m_properties;

    /**
	 * Instantiates a new string property adapter.
	 *
	 * @param properties the properties
	 */
    public StringPropertyAdapter(Properties properties) {
      m_properties = properties;
    }

    /**
	 * Clear.
	 */
    @Override
    public void clear() {
      throw new UnsupportedOperationException();
    }

    /**
	 * Contains key.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
    @Override
    public boolean containsKey(Object key) {
      throw new UnsupportedOperationException();
    }

    /**
	 * Contains value.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
    @Override
    public boolean containsValue(Object value) {
      throw new UnsupportedOperationException();
    }

    /**
	 * Entry set.
	 *
	 * @return the sets the
	 */
    @Nonnull
    @Override
    public Set<java.util.Map.Entry<String, String>> entrySet() {
      throw new UnsupportedOperationException();
    }

    /**
	 * Gets the.
	 *
	 * @param key the key
	 * @return the string
	 */
    @Override
    public String get(Object key) {
      return m_properties.getProperty((String) key);
    }

    /**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
    @Override
    public boolean isEmpty() {
      throw new UnsupportedOperationException();
    }

    /**
	 * Key set.
	 *
	 * @return the sets the
	 */
    @Nonnull
    @Override
    public Set<String> keySet() {
      throw new UnsupportedOperationException();
    }

    /**
	 * Put.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the string
	 */
    @Nonnull
    @Override
    public String put(String key, String value) {
      throw new UnsupportedOperationException();
    }

    /**
	 * Put all.
	 *
	 * @param m the m
	 */
    @Override
    public void putAll(@Nonnull Map<? extends String, ? extends String> m) {
      throw new UnsupportedOperationException();
    }

    /**
	 * Removes the.
	 *
	 * @param key the key
	 * @return the string
	 */
    @Nonnull
    @Override
    public String remove(Object key) {
      throw new UnsupportedOperationException();
    }

    /**
	 * Size.
	 *
	 * @return the int
	 */
    @Override
    public int size() {
      throw new UnsupportedOperationException();
    }

    /**
	 * Values.
	 *
	 * @return the collection
	 */
    @Nonnull
    @Override
    public Collection<String> values() {
      throw new UnsupportedOperationException();
    }
  }
}
