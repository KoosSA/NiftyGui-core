package de.lessvoid.nifty.loaderv2.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.LinearInterpolator;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class EffectType.
 */
public class EffectType extends XmlBaseType implements Cloneable {
  
  /** The Constant logger. */
  private static final Logger logger = Logger.getLogger(EffectType.class.getName());

  /** The Constant DEFAULT_INHERIT. */
  private static final boolean DEFAULT_INHERIT = false;
  
  /** The Constant DEFAULT_POST. */
  private static final boolean DEFAULT_POST = false;
  
  /** The Constant DEFAULT_OVERLAY. */
  private static final boolean DEFAULT_OVERLAY = false;

  /** The effect values. */
  @Nonnull
  protected List<EffectValueType> effectValues;
  
  /** The style id. */
  @Nullable
  private String styleId;

  /**
	 * Instantiates a new effect type.
	 */
  public EffectType() {
    effectValues = new ArrayList<EffectValueType>();
  }

  /**
	 * Instantiates a new effect type.
	 *
	 * @param attributes the attributes
	 */
  public EffectType(@Nonnull final Attributes attributes) {
    super(attributes);
    effectValues = new ArrayList<EffectValueType>();
  }

  /**
	 * Clone.
	 *
	 * @return the effect type
	 * @throws CloneNotSupportedException the clone not supported exception
	 */
  @Nonnull
  @Override
  public EffectType clone() throws CloneNotSupportedException {
    try {
      final EffectType newObject = (EffectType) super.clone();

      newObject.effectValues = new ArrayList<EffectValueType>(effectValues.size());
      for (EffectValueType effectValue : effectValues) {
        newObject.effectValues.add(effectValue.clone());
      }

      return newObject;
    } catch (ClassCastException e) {
      throw new CloneNotSupportedException("Cloning failed because the clone method created the wrong object.");
    }
  }

  /**
	 * This supports creating CustomControlCreator.
	 *
	 * @return the control effect attributes
	 */
  @Nonnull
  public ControlEffectAttributes convert() {
    return new ControlEffectAttributes(getAttributes(), effectValues);
  }

  /**
	 * Materialize.
	 *
	 * @param nifty                 the nifty
	 * @param element               the element
	 * @param effectEventId         the effect event id
	 * @param effectsTypeAttributes the effects type attributes
	 * @param controllers           the controllers
	 */
  public void materialize(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectEventId effectEventId,
      @Nonnull final Attributes effectsTypeAttributes,
      @Nonnull final List<Object> controllers) {
    Attributes effectAttributes = new Attributes(getAttributes());
    effectAttributes.merge(effectsTypeAttributes);

    RegisterEffectType registerEffectType = getRegisteredEffectType(nifty, effectAttributes);
    if (registerEffectType == null) {
      return;
    }

    Class<?> effectClass = registerEffectType.getEffectClass();
    if (effectClass == null) {
      return;
    }

    EffectProperties effectProperties = new EffectProperties(effectAttributes.createProperties());
    applyEffectValues(effectProperties);

    EffectImpl effectImpl = createEffectImpl(effectClass);
    if (effectImpl != null) {
      Effect effect = new Effect(
          nifty,
          getInherit(effectAttributes),
          getPost(effectAttributes),
          getOverlay(effectAttributes),
          getAlternateEnable(effectAttributes),
          getAlternateDisable(effectAttributes),
          getCustomKey(effectAttributes),
          getNeverStopRendering(effectAttributes),
          effectEventId,
          element,
          effectImpl,
          effectProperties,
          nifty.getTimeProvider(),
          controllers);
      initializeEffect(effect, effectEventId);

      element.registerEffect(effectEventId, effect);
    }
  }

  /**
	 * Gets the registered effect type.
	 *
	 * @param nifty      the nifty
	 * @param attributes the attributes
	 * @return the registered effect type
	 */
  @Nullable
  private RegisterEffectType getRegisteredEffectType(@Nonnull final Nifty nifty, @Nonnull final Attributes attributes) {
    String name = getEffectName(attributes);
    RegisterEffectType registerEffectType = nifty.resolveRegisteredEffect(name);
    if (registerEffectType == null) {
      logger.warning("unable to convert effect [" + name + "] because no effect with this name has been registered.");
      return null;
    }
    return registerEffectType;
  }

  /**
	 * Gets the inherit.
	 *
	 * @param attributes the attributes
	 * @return the inherit
	 */
  private boolean getInherit(@Nonnull final Attributes attributes) {
    return attributes.getAsBoolean("inherit", DEFAULT_INHERIT);
  }

  /**
	 * Gets the post.
	 *
	 * @param attributes the attributes
	 * @return the post
	 */
  private boolean getPost(@Nonnull final Attributes attributes) {
    return attributes.getAsBoolean("post", DEFAULT_POST);
  }

  /**
	 * Gets the overlay.
	 *
	 * @param attributes the attributes
	 * @return the overlay
	 */
  private boolean getOverlay(@Nonnull final Attributes attributes) {
    return attributes.getAsBoolean("overlay", DEFAULT_OVERLAY);
  }

  /**
	 * Gets the alternate enable.
	 *
	 * @param attributes the attributes
	 * @return the alternate enable
	 */
  @Nullable
  private String getAlternateEnable(@Nonnull final Attributes attributes) {
    return attributes.get("alternateEnable");
  }

  /**
	 * Gets the alternate disable.
	 *
	 * @param attributes the attributes
	 * @return the alternate disable
	 */
  @Nullable
  private String getAlternateDisable(@Nonnull final Attributes attributes) {
    return attributes.get("alternateDisable");
  }

  /**
	 * Gets the custom key.
	 *
	 * @param attributes the attributes
	 * @return the custom key
	 */
  @Nullable
  private String getCustomKey(@Nonnull final Attributes attributes) {
    return attributes.get("customKey");
  }

  /**
	 * Gets the never stop rendering.
	 *
	 * @param attributes the attributes
	 * @return the never stop rendering
	 */
  private boolean getNeverStopRendering(@Nonnull final Attributes attributes) {
    return attributes.getAsBoolean("neverStopRendering", false);
  }

  /**
	 * Initialize effect.
	 *
	 * @param effect        the effect
	 * @param effectEventId the effect event id
	 */
  protected void initializeEffect(@Nonnull final Effect effect, final EffectEventId effectEventId) {
    if (EffectEventId.onFocus.equals(effectEventId) ||
        EffectEventId.onActive.equals(effectEventId) ||
        EffectEventId.onEnabled.equals(effectEventId) ||
        EffectEventId.onDisabled.equals(effectEventId)) {
      effect.enableInfinite();
    }
    if (EffectEventId.onClick.equals(effectEventId)) {
      effect.setVisibleToMouse(true);
    }
  }

  /**
	 * Creates the effect impl.
	 *
	 * @param effectClass the effect class
	 * @return the effect impl
	 */
  @Nullable
  private EffectImpl createEffectImpl(@Nonnull final Class<?> effectClass) {
    try {
      if (EffectImpl.class.isAssignableFrom(effectClass)) {
        return (EffectImpl) effectClass.newInstance();
      } else {
        logger.warning("given effect class ["
            + effectClass.getName()
            + "] does not implement ["
            + EffectImpl.class.getName() + "]");
      }
    } catch (Exception e) {
      logger.warning("class [" + effectClass.getName() + "] could not be instanziated");
    }
    return null;
  }

  /**
	 * Gets the effect name.
	 *
	 * @param attributes the attributes
	 * @return the effect name
	 */
  @Nullable
  private String getEffectName(@Nonnull final Attributes attributes) {
    return attributes.get("name");
  }

  /**
	 * Resolve parameters.
	 *
	 * @param src the src
	 */
  public void resolveParameters(@Nonnull final Attributes src) {
    getAttributes().resolveParameters(src);

    for (EffectValueType e : effectValues) {
      e.getAttributes().resolveParameters(src);
    }
  }

  /**
	 * Adds the value.
	 *
	 * @param elementValueType the element value type
	 */
  public void addValue(@Nonnull final EffectValueType elementValueType) {
    effectValues.add(elementValueType);
  }

  /**
	 * Adds the values.
	 *
	 * @param effectValueTypes the effect value types
	 */
  public void addValues(@Nonnull final Collection<EffectValueType> effectValueTypes) {
    effectValues.addAll(effectValueTypes);
  }

  /**
	 * Apply effect values.
	 *
	 * @param effectProperties the effect properties
	 */
  void applyEffectValues(@Nonnull final EffectProperties effectProperties) {
    if (!effectValues.isEmpty()) {
      for (EffectValueType effectValueType : effectValues) {
        effectProperties.addEffectValue(effectValueType.getAttributes());
      }
      if (effectProperties.isTimeInterpolator()) {
        LinearInterpolator interpolator = effectProperties.getInterpolator();
        if (interpolator != null) {
          effectProperties.setProperty("length", String.valueOf((long) interpolator.getMaxX()));
        }
      }
    }
  }

  /**
	 * Sets the style id.
	 *
	 * @param styleId the new style id
	 */
  public void setStyleId(@Nullable final String styleId) {
    this.styleId = styleId;
  }

  /**
	 * Gets the style id.
	 *
	 * @return the style id
	 */
  @Nullable
  public String getStyleId() {
    return styleId;
  }
}
