package de.lessvoid.nifty.effects;

import java.util.Map;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.tools.LinearInterpolator;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class EffectProperties.
 */
public class EffectProperties extends Properties {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;
  
  /** The effect values. */
  private EffectPropertiesValues effectValues;

  /**
	 * Instantiates a new effect properties.
	 *
	 * @param createProperties the create properties
	 */
  public EffectProperties(@Nonnull final Properties createProperties) {
    super();

    for (Map.Entry<Object, Object> entry : createProperties.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  /**
	 * Adds the effect value.
	 *
	 * @param effectProperties the effect properties
	 */
  public void addEffectValue(final Attributes effectProperties) {
    getEffectPropertiesValueLazy().add(effectProperties);
  }

  /**
	 * Gets the effect values.
	 *
	 * @return the effect values
	 */
  public EffectPropertiesValues getEffectValues() {
    return getEffectPropertiesValueLazy();
  }

  /**
	 * Checks if is time interpolator.
	 *
	 * @return true, if is time interpolator
	 */
  public boolean isTimeInterpolator() {
    return getEffectPropertiesValueLazy().containsTimeValues();
  }

  /**
	 * Gets the interpolator.
	 *
	 * @return the interpolator
	 */
  @Nullable
  public LinearInterpolator getInterpolator() {
    if (effectValues == null) {
      return null;
    }
    LinearInterpolator interpolator = getEffectPropertiesValueLazy().toLinearInterpolator();
    if (interpolator == null) {
      return null;
    }
    interpolator.prepare();
    return interpolator;
  }

  /**
	 * Gets the effect properties value lazy.
	 *
	 * @return the effect properties value lazy
	 */
  private EffectPropertiesValues getEffectPropertiesValueLazy() {
    if (effectValues != null) {
      return effectValues;
    }
    effectValues = new EffectPropertiesValues();
    return effectValues;
  }
}
