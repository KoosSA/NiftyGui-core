package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.ElementInteractionClickHandler;
import de.lessvoid.nifty.loaderv2.types.helper.OnClickType;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class InteractType.
 */
public class InteractType extends XmlBaseType {
  
  /**
	 * Instantiates a new interact type.
	 */
  public InteractType() {
    super();
  }

  /**
	 * Instantiates a new interact type.
	 *
	 * @param src the src
	 */
  public InteractType(@Nonnull final InteractType src) {
    super(src);
  }

  /**
	 * Instantiates a new interact type.
	 *
	 * @param attributes the attributes
	 */
  public InteractType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  /**
	 * Merge from interact type.
	 *
	 * @param interact the interact
	 */
  public void mergeFromInteractType(@Nonnull final InteractType interact) {
    mergeFromAttributes(interact.getAttributes());
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
    return StringHelper.whitespace(offset) + "<interact> " + super.output(offset);
  }

  /**
	 * Materialize.
	 *
	 * @param nifty      the nifty
	 * @param element    the element
	 * @param controller the controller
	 */
  public void materialize(
      final Nifty nifty,
      @Nonnull final Element element,
      final Object... controller) {
    materializeMethods(nifty, element, element.getElementInteraction().getPrimary(),
        "onClick", "onClickRepeat", "onRelease", "onClickMouseMove","onMultiClick", controller);
    materializeMethods(nifty, element, element.getElementInteraction().getPrimary(),
        "onPrimaryClick", "onPrimaryClickRepeat", "onPrimaryRelease", "onPrimaryClickMouseMove","onPrimaryMultiClick", controller);
    materializeMethods(nifty, element, element.getElementInteraction().getSecondary(),
        "onSecondaryClick", "onSecondaryClickRepeat", "onSecondaryRelease", "onSecondaryClickMouseMove","onSecondaryMultiClick", controller);
    materializeMethods(nifty, element, element.getElementInteraction().getTertiary(),
        "onTertiaryClick", "onTertiaryClickRepeat", "onTertiaryRelease", "onTertiaryClickMouseMove","onTertiaryMultiClick", controller);

    OnClickType onMouseOver = getOnClickType("onMouseOver");
    if (onMouseOver != null) {
      element.setOnMouseOverMethod(onMouseOver.getMethod(nifty, controller));
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onMouseWheel = getOnClickType("onMouseWheel");
    if (onMouseWheel != null) {
      element.getElementInteraction().setOnMouseWheelMethod(onMouseWheel.getMethod(nifty, controller));
      element.setVisibleToMouseEvents(true);
    }
    String onClickAlternateKey = getAttributes().get("onClickAlternateKey");
    if (onClickAlternateKey != null) {
      element.setOnClickAlternateKey(onClickAlternateKey);
    }
  }

  /**
	 * Materialize methods.
	 *
	 * @param nifty                the nifty
	 * @param element              the element
	 * @param handler              the handler
	 * @param onClickName          the on click name
	 * @param onClickRepeatName    the on click repeat name
	 * @param onReleaseName        the on release name
	 * @param onClickMouseMoveName the on click mouse move name
	 * @param onMultiClickName     the on multi click name
	 * @param controller           the controller
	 */
  private void materializeMethods(
      final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final ElementInteractionClickHandler handler,
      @Nonnull final String onClickName,
      @Nonnull final String onClickRepeatName,
      @Nonnull final String onReleaseName,
      @Nonnull final String onClickMouseMoveName,
      @Nonnull final String onMultiClickName,
      final Object... controller) {
    OnClickType onClick = getOnClickType(onClickName);
    if (onClick != null) {
      handler.setOnClickMethod(onClick.getMethod(nifty, controller));
      handler.setOnClickRepeatEnabled(false);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onMultiClick = getOnClickType(onMultiClickName);
    if(onMultiClick != null){
      handler.setOnMultiClickMethod(onMultiClick.getMethod(nifty, controller));
      handler.setOnClickRepeatEnabled(false);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onClickRepeat = getOnClickType(onClickRepeatName);
    if (onClickRepeat != null) {
      handler.setOnClickMethod(onClickRepeat.getMethod(nifty, controller));
      handler.setOnClickRepeatEnabled(true);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onClickMouseMove = getOnClickType(onClickMouseMoveName);
    if (onClickMouseMove != null) {
      handler.setOnClickMouseMoveMethod(onClickMouseMove.getMethod(nifty, controller));
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onRelease = getOnClickType(onReleaseName);
    if (onRelease != null) {
      handler.setOnReleaseMethod(onRelease.getMethod(nifty, controller));
      element.setVisibleToMouseEvents(true);
    }
  }

  /**
	 * Gets the on click type.
	 *
	 * @param key the key
	 * @return the on click type
	 */
  @Nullable
  private OnClickType getOnClickType(@Nonnull final String key) {
    String onClick = getAttributes().get(key);
    if (onClick == null) {
      return null;
    }
    return new OnClickType(onClick);
  }

  /**
	 * Apply.
	 *
	 * @param interact the interact
	 * @param styleId  the style id
	 */
  public void apply(@Nonnull final InteractType interact, @Nonnull final String styleId) {
    interact.getAttributes().mergeAndTag(getAttributes(), styleId);
  }

  /**
	 * Resolve parameters.
	 *
	 * @param src the src
	 */
  public void resolveParameters(@Nonnull final Attributes src) {
    getAttributes().resolveParameters(src);
  }
}
