package de.lessvoid.xml.xpp3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;

//import org.xmlpull.v1.XmlPullParser;

import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.xml.tools.BundleInfo;
import de.lessvoid.xml.tools.SpecialValuesReplace;

/**
 * XPP Attributes in a nicer form.
 * @author void
 */
// TODO: This class should be modified to implement the Map<String, String> interface (renaming all concerned method),
// and should not expose its attributes member anymore.
public class Attributes {
  
  /** The Constant ORIGINAL_VALUE_MARKER. */
  private static final String ORIGINAL_VALUE_MARKER = "$$$originalValue->";

  /** The Constant controlParameter. */
  private final static ControlParameter controlParameter = new ControlParameter();
  
  /** The attributes. */
  @Nonnull
  private final Map<String, String> attributes;
  
  /** The tagged attributes. */
  @Nonnull
  private final Map<String, Set<String>> taggedAttributes;

  /**
	 * Instantiates a new attributes.
	 */
  public Attributes() {
    attributes = new HashMap<String, String>();
    taggedAttributes = new HashMap<String, Set<String>>();
  }

  /**
	 * Instantiates a new attributes.
	 *
	 * @param values the values
	 */
  public Attributes(@Nonnull final String ... values) {
    this();
    for (int i = 0; i < values.length / 2; i++) {
      attributes.put(values[i * 2], values[i * 2 + 1]);
    }
  }

  /**
   * Get Attributes from XmlParser.
   * @param xpp xpp
   */
  public Attributes(@Nonnull final XmlPullParser xpp) {
    this();
    final int count = xpp.getAttributeCount();
    for (int i = 0; i < count; i++) {
      String key = xpp.getAttributeName(i);
      String value = xpp.getAttributeValue(i);
      attributes.put(key, value);
    }
  }

  /**
   * copy constructor.
   * @param source source
   */
  public Attributes(@Nonnull final Attributes source) {
    this();
    attributes.putAll(source.attributes);
    taggedAttributes.putAll(source.taggedAttributes);
  }

  /**
	 * Translate special values.
	 *
	 * @param resourceBundle   the resource bundle
	 * @param screenController the screen controller
	 * @param globalProperties the global properties
	 * @param loc              the loc
	 */
  public void translateSpecialValues(
      @Nonnull final Map<String, BundleInfo> resourceBundle,
      @Nullable final ScreenController screenController,
      @Nullable final Properties globalProperties,
      @Nullable final Locale loc) {
    Map<String, String> replacedAttributes = new HashMap<String, String>();

    for (Map.Entry<String, String> entry : attributes.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();

      // skip original values - they don't need to be translated or added again
      if (key.startsWith(ORIGINAL_VALUE_MARKER)) {
        replacedAttributes.put(key, value);
        continue;
      }

      String replaced = SpecialValuesReplace.replace(value, resourceBundle, screenController, globalProperties, loc);
      replacedAttributes.put(key, replaced);
      replacedAttributes.put(ORIGINAL_VALUE_MARKER + key, value);
    }

    attributes.putAll(replacedAttributes);
  }

  /**
   * Check if a specific attribute is set.
   *
   * @param name the name of the attribute
   * @return {@code true} in case a attribute with the supplied name exists
   */
  public boolean isSet(@Nonnull final String name) {
    return get(name) != null;
  }

  /**
   * Return the attribute with the given name.
   * @param name name of attribute
   * @return value
   */
  @Nullable
  public String get(@Nonnull final String name) {
    return attributes.get(name);
  }

  /**
	 * Gets the with default.
	 *
	 * @param name         the name
	 * @param defaultValue the default value
	 * @return the with default
	 */
  @Nonnull
  public String getWithDefault(@Nonnull final String name, @Nonnull final String defaultValue) {
    String value = get(name);
    if (value == null) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * Create Properties instance from attributes.
   *
   * @return a filled Properties instance
   */
  @Nonnull
  public Properties createProperties() {
    Properties props = new Properties();
    props.putAll(attributes);
    return props;
  }

  /**
   * Fetch a value of the attributes and convert it to a boolean value. This method will return the default value in
   * case its not absolutely clear what boolean value is stored with the supplied attribute name.
   *
   * @param name name of attribute
   * @param defaultValue default value
   * @return the boolean value stored or the default value
   */
  public boolean getAsBoolean(@Nonnull final String name, final boolean defaultValue) {
    String value = get(name);
    if (value == null) {
      return defaultValue;
    }
    if (value.equals("true")) {
      return true;
    } else if (value.equals("false")) {
      return false;
    }
    return defaultValue;
  }

  /**
   * Fetch a value from the attributes and convert it to a integer.
   *
   * @param name the name of the attribute
   * @return the number stored with the supplied name
   * @throws IllegalArgumentException in case the name argument can't be resolved to a value
   * @throws NumberFormatException in case the value of the requested attribute does not contain a integer number
   */
  public int getAsInteger(@Nonnull final String name) {
    String value = get(name);
    if (value == null) {
      throw new IllegalArgumentException("Requested attribute \"" + name + "\" does not match a value.");
    }
    return Integer.parseInt(value);
  }

  /**
   * Fetch a value from the attributes and convert it to a integer. The default value is returned in case the
   * attribute name can't be mapped to a value or the value can't be converted to a integer.
   *
   * @param name the attribute name
   * @param defaultValue the value returned in case the attribute can't be converted to a integer value
   * @return the integer representation of the attribute value or the default value
   */
  public int getAsInteger(@Nonnull final String name, final int defaultValue) {
    String value = get(name);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * Fetch a value from the attributes and convert it to a float.
   *
   * @param name the name of the attribute
   * @return the number stored with the supplied name
   * @throws IllegalArgumentException in case the name argument can't be resolved to a value
   * @throws NumberFormatException in case the value of the requested attribute does not contain a floating point value
   */
  public float getAsFloat(@Nonnull final String name) {
    String value = get(name);
    if (value == null) {
      throw new IllegalArgumentException("Requested attribute \"" + name + "\" does not match a value.");
    }
    return Float.parseFloat(value);
  }

  /**
   * Fetch a value from the attributes and convert it to a float. The default value is returned in case the
   * attribute name can't be mapped to a value or the value can't be converted to a float.
   *
   * @param name the attribute name
   * @param defaultValue the value returned in case the attribute can't be converted to a float value
   * @return the float representation of the attribute value or the default value
   */
  public float getAsFloat(@Nonnull final String name, final float defaultValue) {
    String value = get(name);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Float.parseFloat(value);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
	 * Gets the as color.
	 *
	 * @param name the name
	 * @return the as color
	 */
  @Nullable
  public Color getAsColor(@Nonnull final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return new Color(value);
  }

  /**
   * Set an attribute.
   *
   * @param name name of the attribute to set
   * @param value value to set attribute to
   */
  public void set(@Nonnull final String name, @Nonnull final String value) {
    setAttribute(name, value);
  }

  /**
	 * Overwrite.
	 *
	 * @param src the src
	 */
  public void overwrite(@Nonnull final Attributes src) {
    attributes.clear();
    taggedAttributes.clear();
    attributes.putAll(src.attributes);
    taggedAttributes.putAll(src.taggedAttributes);
  }

  /**
	 * Merge.
	 *
	 * @param src the src
	 */
  public void merge(@Nonnull final Attributes src) {
    merge(src, false);
  }

  /**
	 * Merge and override.
	 *
	 * @param src the src
	 */
  public void mergeAndOverride(@Nonnull final Attributes src) {
    merge(src, true);
  }

  /**
	 * Merge.
	 *
	 * @param src      the src
	 * @param override the override
	 */
  private void merge(@Nonnull final Attributes src, boolean override) {
    for (Map.Entry<String, String> srcAttribute : src.attributes.entrySet()) {
      String srcKey = srcAttribute.getKey();
      if (override || !attributes.containsKey(srcKey)) {
        attributes.put(srcKey, srcAttribute.getValue());
        for (Map.Entry<String, Set<String>> tag : src.taggedAttributes.entrySet()) {
          if (tag.getValue().contains(srcKey)) {
            Set<String> attribForTag = taggedAttributes.get(tag.getKey());
            if (attribForTag == null) {
              attribForTag = new HashSet<String>();
              taggedAttributes.put(tag.getKey(), attribForTag);
            }
            attribForTag.add(srcKey);
          }
        }
      }
    }
  }

  /**
	 * Merge and tag.
	 *
	 * @param src the src
	 * @param tag the tag
	 */
  public void mergeAndTag(@Nonnull final Attributes src, @Nonnull final String tag) {
    for (Map.Entry<String, String> entry : src.attributes.entrySet()) {
      String srcKey = entry.getKey();

      // you can only overwrite keys when they don't exist yet
      if (!attributes.containsKey(srcKey)) {
        setAttribute(srcKey, entry.getValue());
        tagAttribute(srcKey, tag);
      }
    }
  }

  /**
	 * Tag attribute.
	 *
	 * @param srcKey the src key
	 * @param tag    the tag
	 */
  private void tagAttribute(@Nonnull final String srcKey, @Nonnull final String tag) {
    Set<String> attribForTag = taggedAttributes.get(tag);
    if (attribForTag == null) {
      attribForTag = new HashSet<String>();
      taggedAttributes.put(tag, attribForTag);
    }
    attribForTag.add(srcKey);
  }

  /**
	 * Refresh from attributes.
	 *
	 * @param src the src
	 */
  public void refreshFromAttributes(@Nonnull final Attributes src) {
    Map<String, String> srcAttributes = src.attributes;
    for (Map.Entry<String, String> srcAttribute : srcAttributes.entrySet()) {
      String srcKey = srcAttribute.getKey();
      String srcValue = srcAttribute.getValue();
      if (srcValue.equals("")) {
        // this key should be replaced
        attributes.remove(srcKey);
      } else {
        attributes.put(srcKey, srcAttributes.get(srcKey));
      }
    }
  }

  /**
	 * Sets the attribute.
	 *
	 * @param key   the key
	 * @param value the value
	 */
  private void setAttribute(final String key, final String value) {
    attributes.put(key, value);
  }

  /**
	 * To string.
	 *
	 * @return the string
	 */
  @Override
  @Nonnull
  public String toString() {
    StringBuilder result = new StringBuilder();
    boolean first = true;
    for (String key : attributes.keySet()) {
      if (!first) {
        result.append(", ");
      }
      if (first) {
        first = false;
      }
      result.append(key).append(" => ").append(attributes.get(key));

      String tag = resolveTag(key);
      if (tag != null) {
        result.append(" {").append(tag).append("}");
      }
    }
    return result.toString();
  }

  /**
	 * Resolve tag.
	 *
	 * @param key the key
	 * @return the string
	 */
  @Nullable
  private String resolveTag(final String key) {
    for (Map.Entry < String, Set < String >> tag : taggedAttributes.entrySet()) {
      if (tag.getValue().contains(key)) {
        return tag.getKey();
      }
    }
    return null;
  }

  /**
	 * The Class Parameter.
	 */
  public static class Parameter {
    
    /** The original value. */
    private final String originalValue;
    
    /** The key. */
    private final String key;
    
    /** The value. */
    private final String value;

    /**
	 * Instantiates a new parameter.
	 *
	 * @param originalValue the original value
	 * @param key           the key
	 * @param value         the value
	 */
    private Parameter(final String originalValue, final String key, final String value) {
      this.originalValue = originalValue;
      this.key = key;
      this.value = value;
    }

    /**
	 * Gets the original value.
	 *
	 * @return the original value
	 */
    public String getOriginalValue() {
      return originalValue;
    }

    /**
	 * Gets the key.
	 *
	 * @return the key
	 */
    public String getKey() {
      return key;
    }

    /**
	 * Gets the value.
	 *
	 * @return the value
	 */
    public String getValue() {
      return value;
    }
  }

  /**
	 * Extract parameters.
	 *
	 * @return the list
	 */
  @Nonnull
  public List<Parameter> extractParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();

    for (Map.Entry < String, String > entry : attributes.entrySet()) {
      String key = entry.getKey(); // like key="$value"
      String value = entry.getValue();
      if (isParameterDefinition(value)) {
        parameters.add(new Parameter(value, controlParameter.extractParameter(value), key));
      }
    }

    return parameters;
  }

  /**
	 * Checks if is parameter definition.
	 *
	 * @param value the value
	 * @return true, if is parameter definition
	 */
  private boolean isParameterDefinition(@Nonnull final String value) {
    return controlParameter.isParameter(value);
  }

  /**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
  @Nonnull
  public Map < String, String > getAttributes() {
    return attributes;
  }

  /**
	 * Removes the.
	 *
	 * @param key the key
	 */
  public void remove(final String key) {
    attributes.remove(key);
  }

  /**
	 * Gets the with tag.
	 *
	 * @param name the name
	 * @param tag  the tag
	 * @return the with tag
	 */
  @Nullable
  public String getWithTag(final String name, final String tag) {
    Set < String > attributesWithTag = taggedAttributes.get(tag);
    if (attributesWithTag == null) {
      return null;
    }
    if (!attributesWithTag.contains(name)) {
      return null;
    }
    return attributes.get(name);
  }

  /**
	 * Resolve parameters.
	 *
	 * @param attributes the attributes
	 */
  @SuppressWarnings("ConstantConditions")
  public void resolveParameters(@Nonnull final Attributes attributes) {
    List<Parameter> entrySet = getParameterSet();
    for (Parameter entry : entrySet) {
      String key = entry.getKey();
      String value = entry.getValue();

      // first check the given attributes and then check our own
      if (attributes.isSet(key)) {
        set(value, controlParameter.applyParameter(entry.getOriginalValue(), entry.getKey(), attributes.get(key)));
      } else if (isSet(key) && !isParameterDefinition(get(key))) {
        set(value, get(key));
      } else {
        remove(value);
      }
    }
  }

  /**
	 * Gets the parameter set.
	 *
	 * @return the parameter set
	 */
  @Nonnull
  List<Parameter> getParameterSet() {
    return extractParameters();
  }

  /**
	 * Removes the with tag.
	 *
	 * @param tag the tag
	 */
  public void removeWithTag(final String tag) {
    Set < String > tagged = taggedAttributes.get(tag);
    if (tagged != null) {
      for (String key : tagged) {
        remove(key);
      }
    }
  }

  /**
   * Get the original value (if possible) or the regular value if the original value does not exist.
   * @param key the key to return
   * @return the value of the key
   */
  public String getOriginalValue(final String key) {
    String value = get(Attributes.ORIGINAL_VALUE_MARKER + key);
    if (value != null) {
      return value;
    }
    return get(key);
  }
}
