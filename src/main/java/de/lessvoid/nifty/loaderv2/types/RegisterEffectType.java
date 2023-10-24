package de.lessvoid.nifty.loaderv2.types;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.tools.ClassHelper;

/**
 * The Class RegisterEffectType.
 */
public class RegisterEffectType extends XmlBaseType {
  
  /** The Constant logger. */
  private static final Logger logger = Logger.getLogger(RegisterEffectType.class.getName());

  /**
	 * Instantiates a new register effect type.
	 */
  public RegisterEffectType() {
  }

  /**
	 * Instantiates a new register effect type.
	 *
	 * @param nameParam  the name param
	 * @param classParam the class param
	 */
  public RegisterEffectType(@Nonnull final String nameParam, @Nonnull final String classParam) {
    getAttributes().set("name", nameParam);
    getAttributes().set("class", classParam);
  }

  /**
	 * Output.
	 *
	 * @param offset the offset
	 * @return the string
	 */
  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<registerEffect> " + super.output(offset);
  }

  /**
	 * Gets the effect class.
	 *
	 * @return the effect class
	 */
  @Nullable
  public Class<?> getEffectClass() {
    String className = getClassName();
    if (className == null) {
      return null;
    }
    return ClassHelper.loadClass(className);
  }

  /**
	 * Gets the name.
	 *
	 * @return the name
	 */
  @Nullable
  public String getName() {
    return getAttributes().get("name");
  }

  /**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
  @Nullable
  private String getClassName() {
    return getAttributes().get("class");
  }
}
