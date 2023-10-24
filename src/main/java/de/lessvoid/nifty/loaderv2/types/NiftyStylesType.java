package de.lessvoid.nifty.loaderv2.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.helper.CollectionLogger;

/**
 * The Class NiftyStylesType.
 */
public class NiftyStylesType extends XmlBaseType {
  
  /** The registered mouse cursor. */
  @Nonnull
  private final Collection<RegisterMouseCursorType> registeredMouseCursor = new ArrayList<RegisterMouseCursorType>();
  
  /** The styles. */
  @Nonnull
  private final Collection<StyleType> styles = new ArrayList<StyleType>();
  
  /** The use styles. */
  @Nonnull
  private final Collection<UseStylesType> useStyles = new ArrayList<UseStylesType>();
  
  /** The registered sounds. */
  @Nonnull
  private final Collection<RegisterSoundType> registeredSounds = new ArrayList<RegisterSoundType>();
  
  /** The registered effect. */
  @Nonnull
  private final Collection<RegisterEffectType> registeredEffect = new ArrayList<RegisterEffectType>();
  
  /**
	 * Adds the register mouse cursor.
	 *
	 * @param registerMouseCursor the register mouse cursor
	 */
  public void addRegisterMouseCursor(final RegisterMouseCursorType registerMouseCursor) {
    registeredMouseCursor.add(registerMouseCursor);
  }

  /**
	 * Adds the style.
	 *
	 * @param newStyle the new style
	 */
  public void addStyle(final StyleType newStyle) {
    styles.add(newStyle);
  }

  /**
	 * Adds the use styles.
	 *
	 * @param newStyle the new style
	 */
  public void addUseStyles(final UseStylesType newStyle) {
    useStyles.add(newStyle);
  }
  
  /**
	 * Addregister sound.
	 *
	 * @param newSound the new sound
	 */
  public void addregisterSound(RegisterSoundType newSound){
      registeredSounds.add(newSound);
  }
  
  /**
	 * Addregister effect.
	 *
	 * @param newEffect the new effect
	 */
  public void addregisterEffect(RegisterEffectType newEffect){
      registeredEffect.add(newEffect);
  }
  
  /**
	 * Load styles.
	 *
	 * @param niftyLoader the nifty loader
	 * @param niftyType   the nifty type
	 * @param nifty       the nifty
	 * @param log         the log
	 * @throws Exception the exception
	 */
  public void loadStyles(
      @Nonnull final NiftyLoader niftyLoader,
      @Nonnull final NiftyType niftyType,
      @Nonnull final Nifty nifty,
      @Nonnull final Logger log) throws Exception {
    for (RegisterMouseCursorType registerMouseCursorType : registeredMouseCursor) {
      registerMouseCursorType.translateSpecialValues(nifty, null);
      registerMouseCursorType.materialize(nifty, log);
    }
    for (UseStylesType useStyle : useStyles) {
      useStyle.loadStyle(niftyLoader, niftyType, nifty);
    }
    for (StyleType style : styles) {
      niftyType.addStyle(style);
    }
    for(RegisterSoundType sound : registeredSounds){
        niftyType.addRegisterSound(sound);
    }
    for(RegisterEffectType effect : registeredEffect){
        niftyType.addRegisterEffect(effect);
    }
  }

  /**
	 * Output.
	 *
	 * @return the string
	 */
  @Nonnull
  public String output() {
    int offset = 1;
    return
        "\nNifty Data:\n" + CollectionLogger.out(offset, styles, "styles")
            + "\n" + CollectionLogger.out(offset, useStyles, "useStyles");
  }
}
