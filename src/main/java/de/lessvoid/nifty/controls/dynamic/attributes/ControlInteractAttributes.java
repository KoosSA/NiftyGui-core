package de.lessvoid.nifty.controls.dynamic.attributes;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.loaderv2.types.InteractType;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class ControlInteractAttributes.
 */
public class ControlInteractAttributes {
  
  /** The attributes. */
  @Nonnull
  protected Attributes attributes = new Attributes();

  /**
	 * Instantiates a new control interact attributes.
	 */
  public ControlInteractAttributes() {
  }

  /**
	 * Support for CustomControlCreator.
	 *
	 * @param interact the interact
	 */
  public ControlInteractAttributes(@Nonnull final InteractType interact) {
    this.attributes = new Attributes(interact.getAttributes());
  }

  /**
	 * Sets the attribute.
	 *
	 * @param name  the name
	 * @param value the value
	 */
  public void setAttribute(@Nonnull final String name, @Nonnull final String value) {
    attributes.set(name, value);
  }
  
  /**
	 * Sets the on click.
	 *
	 * @param onClick the new on click
	 */
  public void setOnClick(@Nonnull final String onClick) {
    setAttribute("onClick", onClick);
  }
  
  /**
	 * Sets the on click repeat.
	 *
	 * @param onClickRepeat the new on click repeat
	 */
  public void setOnClickRepeat(@Nonnull final String onClickRepeat) {
    setAttribute("onClickRepeat", onClickRepeat);
  }
  
  /**
	 * Sets the on release.
	 *
	 * @param onRelease the new on release
	 */
  public void setOnRelease(@Nonnull final String onRelease) {
    setAttribute("onRelease", onRelease);
  }
  
  /**
	 * Sets the on click mouse move.
	 *
	 * @param onClickMouseMove the new on click mouse move
	 */
  public void setOnClickMouseMove(@Nonnull final String onClickMouseMove) {
    setAttribute("onClickMouseMove", onClickMouseMove);
  }
  
  /**
	 * Sets the on multi click.
	 *
	 * @param onOnMultiClick the new on multi click
	 */
  public void setOnMultiClick(@Nonnull final String onOnMultiClick) {
    setAttribute("onMultiClick", onOnMultiClick);
  }
  
  /**
	 * Sets the on primary click.
	 *
	 * @param onPrimaryClick the new on primary click
	 */
  public void setOnPrimaryClick(@Nonnull final String onPrimaryClick) {
    setAttribute("onPrimaryClick", onPrimaryClick);
  }
  
  /**
	 * Sets the on primary click repeat.
	 *
	 * @param onPrimaryClickRepeat the new on primary click repeat
	 */
  public void setOnPrimaryClickRepeat(@Nonnull final String onPrimaryClickRepeat) {
    setAttribute("onPrimaryClickRepeat", onPrimaryClickRepeat);
  }
  
  /**
	 * Sets the on primary release.
	 *
	 * @param onPrimaryRelease the new on primary release
	 */
  public void setOnPrimaryRelease(@Nonnull final String onPrimaryRelease) {
    setAttribute("onPrimaryRelease", onPrimaryRelease);
  }
  
  /**
	 * Sets the on primary click mouse move.
	 *
	 * @param onPrimaryClickMouseMove the new on primary click mouse move
	 */
  public void setOnPrimaryClickMouseMove(@Nonnull final String onPrimaryClickMouseMove) {
    setAttribute("onPrimaryClickMouseMove", onPrimaryClickMouseMove);
  }
  
  /**
	 * Sets the on primary multi click.
	 *
	 * @param onPrimaryMultiClick the new on primary multi click
	 */
  public void setOnPrimaryMultiClick(@Nonnull final String onPrimaryMultiClick) {
    setAttribute("onPrimaryMultiClick", onPrimaryMultiClick);
  }
  
  /**
	 * Sets the on secondary click.
	 *
	 * @param onSecondaryClick the new on secondary click
	 */
  public void setOnSecondaryClick(@Nonnull final String onSecondaryClick) {
    setAttribute("onSecondaryClick", onSecondaryClick);
  }
  
  /**
	 * Sets the on secondary click repeat.
	 *
	 * @param onSecondaryClickRepeat the new on secondary click repeat
	 */
  public void setOnSecondaryClickRepeat(@Nonnull final String onSecondaryClickRepeat) {
    setAttribute("onSecondaryClickRepeat", onSecondaryClickRepeat);
  }
  
  /**
	 * Sets the on secondary release.
	 *
	 * @param onSecondaryRelease the new on secondary release
	 */
  public void setOnSecondaryRelease(@Nonnull final String onSecondaryRelease) {
    setAttribute("onSecondaryRelease", onSecondaryRelease);
  }
  
  /**
	 * Sets the on secondary click mouse move.
	 *
	 * @param onSecondaryClickMouseMove the new on secondary click mouse move
	 */
  public void setOnSecondaryClickMouseMove(@Nonnull final String onSecondaryClickMouseMove) {
    setAttribute("onSecondaryClickMouseMove", onSecondaryClickMouseMove);
  }
  
  /**
	 * Sets the on secondary multi click.
	 *
	 * @param onSecondaryMultiClick the new on secondary multi click
	 */
  public void setOnSecondaryMultiClick(@Nonnull final String onSecondaryMultiClick) {
    setAttribute("onSecondaryMultiClick", onSecondaryMultiClick);
  }
  
  /**
	 * Sets the on tertiary click.
	 *
	 * @param onTertiaryClick the new on tertiary click
	 */
  public void setOnTertiaryClick(@Nonnull final String onTertiaryClick) {
    setAttribute("onTertiaryClick", onTertiaryClick);
  }
  
  /**
	 * Sets the on tertiary click repeat.
	 *
	 * @param onTertiaryClickRepeat the new on tertiary click repeat
	 */
  public void setOnTertiaryClickRepeat(@Nonnull final String onTertiaryClickRepeat) {
    setAttribute("onTertiaryClickRepeat", onTertiaryClickRepeat);
  }
  
  /**
	 * Sets the on tertiary release.
	 *
	 * @param onTertiaryRelease the new on tertiary release
	 */
  public void setOnTertiaryRelease(@Nonnull final String onTertiaryRelease) {
    setAttribute("onTertiaryRelease", onTertiaryRelease);
  }
  
  /**
	 * Sets the on tertiary click mouse move.
	 *
	 * @param onTertiaryClickMouseMove the new on tertiary click mouse move
	 */
  public void setOnTertiaryClickMouseMove(@Nonnull final String onTertiaryClickMouseMove) {
    setAttribute("onTertiaryClickMouseMove", onTertiaryClickMouseMove);
  }
  
  /**
	 * Sets the on tertiary multi click.
	 *
	 * @param onTertiaryMultiClick the new on tertiary multi click
	 */
  public void setOnTertiaryMultiClick(@Nonnull final String onTertiaryMultiClick) {
    setAttribute("onTertiaryMultiClick", onTertiaryMultiClick);
  }
  
  /**
	 * Sets the on mouse over.
	 *
	 * @param onMouseOver the new on mouse over
	 */
  public void setOnMouseOver(@Nonnull final String onMouseOver) {
    setAttribute("onMouseOver", onMouseOver);
  }
  
  /**
	 * Sets the on mouse wheel.
	 *
	 * @param onMouseWheel the new on mouse wheel
	 */
  public void setOnMouseWheel(@Nonnull final String onMouseWheel) {
    setAttribute("onMouseWheel", onMouseWheel);
  }

  /**
	 * Sets the on click alternate key.
	 *
	 * @param onClickAlternateKey the new on click alternate key
	 */
  public void setOnClickAlternateKey(@Nonnull final String onClickAlternateKey) {
    setAttribute("onClickAlternateKey", onClickAlternateKey);
  }

  /**
	 * Creates the.
	 *
	 * @return the interact type
	 */
  @Nonnull
  public InteractType create() {
    return new InteractType(attributes);
  }
}
