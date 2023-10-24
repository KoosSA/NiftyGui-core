package de.lessvoid.nifty.loaderv2.types;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.EnumStorage;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.nifty.tools.factories.CollectionFactory;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * EffectsType.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class EffectsType extends XmlBaseType {
  
  /** The effects. */
  @Nonnull
  private final EnumStorage<EffectEventId, Collection<EffectType>> effects;

  /**
	 * Instantiates a new effects type.
	 */
  public EffectsType() {
    effects = new EnumStorage<EffectEventId, Collection<EffectType>>(
        EffectEventId.class, CollectionFactory.<EffectType>getArrayListInstance());
  }

  /**
	 * Instantiates a new effects type.
	 *
	 * @param src the src
	 */
  public EffectsType(@Nonnull final EffectsType src) {
    super(src);
    effects = new EnumStorage<EffectEventId, Collection<EffectType>>(
        EffectEventId.class, CollectionFactory.<EffectType>getArrayListInstance());
    copyEffects(src);
  }

  /**
	 * Instantiates a new effects type.
	 *
	 * @param attributes the attributes
	 */
  public EffectsType(@Nonnull Attributes attributes) {
    super(attributes);
    effects = new EnumStorage<EffectEventId, Collection<EffectType>>(
        EffectEventId.class, CollectionFactory.<EffectType>getArrayListInstance());
  }

  /**
	 * Merge from effects type.
	 *
	 * @param src the src
	 */
  public void mergeFromEffectsType(@Nonnull final EffectsType src) {
    mergeFromAttributes(src.getAttributes());
    mergeEffects(src);
  }

  /**
	 * Copy effects.
	 *
	 * @param src the src
	 */
  private void copyEffects(@Nonnull final EffectsType src) {
    for (final EffectEventId event : EffectEventId.values()) {
      if (src.effects.isSet(event)) {
        copyCollection(effects.get(event), src.effects.get(event));
      }
    }
  }

  /**
	 * Merge effects.
	 *
	 * @param src the src
	 */
  private void mergeEffects(@Nonnull final EffectsType src) {
    for (final EffectEventId event : EffectEventId.values()) {
      if (src.effects.isSet(event)) {
        mergeCollection(effects.get(event), src.effects.get(event));
      }
    }
  }

  /**
	 * Copy collection.
	 *
	 * @param dst the dst
	 * @param src the src
	 * @return the collection
	 */
  @Nonnull
  private Collection<EffectType> copyCollection(
      @Nonnull final Collection<EffectType> dst,
      @Nonnull final Collection<EffectType> src) {
    dst.clear();
    copyEffects(dst, src);
    return dst;
  }

  /**
	 * Merge collection.
	 *
	 * @param dst the dst
	 * @param src the src
	 * @return the collection
	 */
  @Nonnull
  private Collection<EffectType> mergeCollection(
      @Nonnull final Collection<EffectType> dst,
      @Nonnull final Collection<EffectType> src) {
    copyEffects(dst, src);
    return dst;
  }

  /**
	 * Copy effects.
	 *
	 * @param dst the dst
	 * @param src the src
	 */
  void copyEffects(@Nonnull final Collection<EffectType> dst, @Nonnull final Collection<EffectType> src) {
    try {
      for (EffectType e : src) {
        dst.add(e.clone());
      }
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("Creating copy of effect data failed!", e);
    }
  }

  /**
	 * Translate special values.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 */
  @Override
  @SuppressWarnings("unchecked")
  public void translateSpecialValues(@Nonnull final Nifty nifty, @Nullable final Screen screen) {
    super.translateSpecialValues(nifty, screen);

    for (final EffectEventId event : EffectEventId.values()) {
      if (effects.isSet(event)) {
        for (EffectType effectType : effects.get(event)) {
          effectType.translateSpecialValues(nifty, screen);
        }
      }
    }
  }

  /**
	 * Adds the event effect.
	 *
	 * @param eventId     the event id
	 * @param effectParam the effect param
	 */
  public void addEventEffect(@Nonnull final EffectEventId eventId, @Nonnull final EffectType effectParam) {
    effects.get(eventId).add(effectParam);
  }

  /**
	 * Adds the on start screen.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnStartScreen(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onStartScreen, effectParam);
  }

  /**
	 * Adds the on end screen.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnEndScreen(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onEndScreen, effectParam);
  }

  /**
	 * Adds the on hover.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnHover(@Nonnull final EffectTypeOnHover effectParam) {
    addEventEffect(EffectEventId.onHover, effectParam);
  }

  /**
	 * Adds the on start hover.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnStartHover(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onStartHover, effectParam);
  }

  /**
	 * Adds the on end hover.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnEndHover(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onEndHover, effectParam);
  }

  /**
	 * Adds the on click.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnClick(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onClick, effectParam);
  }

  /**
	 * Adds the on focus.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnFocus(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onFocus, effectParam);
  }

  /**
	 * Adds the on lost focus.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnLostFocus(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onLostFocus, effectParam);
  }

  /**
	 * Adds the on get focus.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnGetFocus(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onGetFocus, effectParam);
  }

  /**
	 * Adds the on active.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnActive(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onActive, effectParam);
  }

  /**
	 * Adds the on show.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnShow(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onShow, effectParam);
  }

  /**
	 * Adds the on hide.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnHide(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onHide, effectParam);
  }

  /**
	 * Adds the on custom.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnCustom(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onCustom, effectParam);
  }

  /**
	 * Adds the on disabled.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnDisabled(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onDisabled, effectParam);
  }

  /**
	 * Adds the on enabled.
	 *
	 * @param effectParam the effect param
	 */
  public void addOnEnabled(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onEnabled, effectParam);
  }

  /**
	 * Gets the event effect types.
	 *
	 * @param id the id
	 * @return the event effect types
	 */
  public Collection<EffectType> getEventEffectTypes(@Nonnull final EffectEventId id) {
    if (effects.isSet(id)) {
      return Collections.unmodifiableCollection(effects.get(id));
    } else {
      return Collections.emptyList();
    }
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
    final StringBuilder builder = new StringBuilder();
    builder.append(StringHelper.whitespace(offset));
    builder.append("<effects> (").append(getAttributes().toString()).append(')');
    for (EffectEventId id : EffectEventId.values()) {
      builder.append(getCollectionString(id, offset + 1));
    }
    return builder.toString();
  }

  /**
	 * Gets the collection string.
	 *
	 * @param effectId the effect id
	 * @param offset   the offset
	 * @return the collection string
	 */
  @Nonnull
  private String getCollectionString(@Nonnull final EffectEventId effectId, final int offset) {
    if (!effects.isSet(effectId)) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (EffectType effect : effects.get(effectId)) {
      if (builder.length() > 0) {
        builder.append('\n');
      }
      builder.append(StringHelper.whitespace(offset)).append('<').append(effectId.name()).append("> ");
      builder.append(effect.output(offset));
      if (effect.getStyleId() != null) {
        builder.append(" [").append(effect.getStyleId()).append(']');
      }
    }
    return builder.toString();
  }

  /**
	 * Materialize.
	 *
	 * @param nifty       the nifty
	 * @param element     the element
	 * @param screen      the screen
	 * @param controllers the controllers
	 */
  public void materialize(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final Screen screen,
      @Nonnull final List<Object> controllers) {
    for (EffectEventId id : EffectEventId.values()) {
      initEffect(id, element, nifty, controllers);
    }
  }

  /**
	 * Inits the effect.
	 *
	 * @param eventId     the event id
	 * @param element     the element
	 * @param nifty       the nifty
	 * @param controllers the controllers
	 */
  private void initEffect(
      @Nonnull final EffectEventId eventId,
      @Nonnull final Element element,
      @Nonnull final Nifty nifty,
      @Nonnull final List<Object> controllers) {
    final Collection<EffectType> effectCollection = effects.get(eventId);
    final Attributes effectsTypeAttributes = getAttributes();

    for (EffectType effectType : effectCollection) {
      effectType.materialize(nifty, element, eventId, effectsTypeAttributes, controllers);
    }
  }

  /**
	 * Refresh from attributes.
	 *
	 * @param effects the effects
	 */
  public void refreshFromAttributes(@Nonnull final ControlEffectsAttributes effects) {
    effects.refreshEffectsType(this);
  }

  /**
	 * Apply.
	 *
	 * @param dstEffectType the dst effect type
	 * @param styleId       the style id
	 */
  public void apply(@Nonnull final EffectsType dstEffectType, @Nullable final String styleId) {
    for (EffectEventId id : EffectEventId.values()) {
      if (effects.isSet(id)) {
        applyEffectCollection(effects.get(id), dstEffectType.effects.get(id), styleId);
      }
    }
  }

  /**
	 * Apply effect collection.
	 *
	 * @param src     the src
	 * @param dst     the dst
	 * @param styleId the style id
	 */
  void applyEffectCollection(
      @Nonnull final Collection<EffectType> src,
      @Nonnull final Collection<EffectType> dst,
      @Nullable final String styleId) {
    try {
      for (EffectType effectType : src) {
        EffectType copy = effectType.clone();
        copy.setStyleId(styleId);
        dst.add(copy);
      }
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("Failure to apply a effect collection!", e);
    }
  }

  /**
	 * Resolve parameters.
	 *
	 * @param src the src
	 */
  public void resolveParameters(@Nonnull final Attributes src) {
    for (EffectEventId id : EffectEventId.values()) {
      if (effects.isSet(id)) {
        resolveParameterCollection(effects.get(id), src);
      }
    }
  }

  /**
	 * Resolve parameter collection.
	 *
	 * @param dst the dst
	 * @param src the src
	 */
  void resolveParameterCollection(@Nonnull final Collection<EffectType> dst, @Nonnull final Attributes src) {
    for (EffectType e : dst) {
      e.resolveParameters(src);
    }
  }

  /**
	 * Removes the with tag.
	 *
	 * @param styleId the style id
	 */
  public void removeWithTag(@Nonnull final String styleId) {
    getAttributes().removeWithTag(styleId);
    for (EffectEventId id : EffectEventId.values()) {
      if (effects.isSet(id)) {
        removeAllEffectsWithStyleId(effects.get(id), styleId);
      }
    }
  }

  /**
	 * Removes the all effects with style id.
	 *
	 * @param source  the source
	 * @param styleId the style id
	 */
  private void removeAllEffectsWithStyleId(
      @Nonnull final Collection<EffectType> source,
      @Nonnull final String styleId) {
    Iterator<EffectType> itr = source.iterator();
    while (itr.hasNext()) {
      EffectType current = itr.next();
      if (styleId.equals(current.getStyleId())) {
        itr.remove();
      }
    }
  }

  /**
	 * Checks for effect types.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
  public boolean hasEffectTypes(@Nonnull final EffectEventId id) {
    return effects.isSet(id);
  }

  /**
	 * Convert copy.
	 *
	 * @param <T>      the generic type
	 * @param effectId the effect id
	 * @param storage  the storage
	 */
  @SuppressWarnings("unchecked")
  public <T extends ControlEffectAttributes> void convertCopy(
      @Nonnull final EffectEventId effectId,
      @Nonnull final Collection<T> storage) {
    if (effects.isSet(effectId)) {
      for (EffectType e : effects.get(effectId)) {
        storage.add((T) e.convert());
      }
    }
  }
}
