package de.lessvoid.nifty.controls.dynamic.attributes;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.types.AttributesType;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.EffectsType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.ImageType;
import de.lessvoid.nifty.loaderv2.types.InteractType;
import de.lessvoid.nifty.loaderv2.types.LayerType;
import de.lessvoid.nifty.loaderv2.types.PanelType;
import de.lessvoid.nifty.loaderv2.types.PopupType;
import de.lessvoid.nifty.loaderv2.types.StyleType;
import de.lessvoid.nifty.loaderv2.types.TextType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class ControlAttributes.
 */
public class ControlAttributes {
  
  /** The attributes. */
  @Nonnull
  private final Attributes attributes;
  
  /** The interact. */
  @Nullable
  private ControlInteractAttributes interact;
  
  /** The effects. */
  @Nullable
  private ControlEffectsAttributes effects;

  /** The is auto id. */
  private boolean isAutoId = false;

  /**
	 * Instantiates a new control attributes.
	 */
  public ControlAttributes() {
    attributes = new Attributes();
  }

  /**
	 * Instantiates a new control attributes.
	 *
	 * @param type the type
	 */
  public ControlAttributes(@Nonnull final ElementType type) {
    attributes = new Attributes(type.getAttributes());
    final InteractType interactType = type.getInteract();
    interact = new ControlInteractAttributes(interactType);
    final EffectsType effectsType = type.getEffects();
    effects = new ControlEffectsAttributes(effectsType);
  }

  /**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
  @Nonnull
  public Attributes getAttributes() {
    return attributes;
  }

  /**
	 * Gets the interact.
	 *
	 * @return the interact
	 */
  @Nonnull
  protected ControlInteractAttributes getInteract() {
    if (interact == null) {
      interact = new ControlInteractAttributes();
    }
    return interact;
  }

  /**
	 * Gets the effects.
	 *
	 * @return the effects
	 */
  @Nonnull
  protected ControlEffectsAttributes getEffects() {
    if (effects == null) {
      effects = new ControlEffectsAttributes();
    }
    return effects;
  }

  /**
	 * Sets the interact.
	 *
	 * @param controlInteract the new interact
	 */
  public void setInteract(@Nullable final ControlInteractAttributes controlInteract) {
    interact = controlInteract;
  }

  /**
	 * Sets the effects.
	 *
	 * @param controlEffects the new effects
	 */
  public void setEffects(@Nullable final ControlEffectsAttributes controlEffects) {
    effects = controlEffects;
  }

  /**
	 * Sets the.
	 *
	 * @param key   the key
	 * @param value the value
	 */
  public void set(@Nonnull final String key, @Nonnull final String value) {
    attributes.set(key, value);
  }

  /**
	 * Gets the.
	 *
	 * @param key the key
	 * @return the string
	 */
  @Nullable
  public String get(@Nonnull final String key) {
    return attributes.get(key);
  }

  /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
  public void setId(@Nonnull final String id) {
    isAutoId = false;
    set("id", id);
  }

  /**
	 * Gets the id.
	 *
	 * @return the id
	 */
  @Nullable
  public String getId() {
    return get("id");
  }

  /**
	 * Sets the auto id.
	 */
  public void setAutoId() {
    setAutoId(NiftyIdCreator.generate());
  }

  /**
	 * Sets the auto id.
	 *
	 * @param id the new auto id
	 */
  public void setAutoId(@Nonnull final String id) {
    isAutoId = true;
    set("id", id);
  }

  /**
	 * Checks if is auto id.
	 *
	 * @return true, if is auto id
	 */
  public boolean isAutoId() {
    return isAutoId;
  }

  /**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
  public void setName(@Nonnull final String name) {
    set("name", name);
  }

  /**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
  public void setHeight(@Nonnull final String height) {
    set("height", height);
  }

  /**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
  public void setWidth(@Nonnull final String width) {
    set("width", width);
  }

  /**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
  public void setX(@Nonnull final String x) {
    set("x", x);
  }

  /**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
  public void setY(@Nonnull final String y) {
    set("y", y);
  }

  /**
	 * Sets the align.
	 *
	 * @param align the new align
	 */
  public void setAlign(@Nonnull final String align) {
    set("align", align);
  }

  /**
	 * Sets the v align.
	 *
	 * @param valign the new v align
	 */
  public void setVAlign(@Nonnull final String valign) {
    set("valign", valign);
  }

  /**
	 * Sets the padding.
	 *
	 * @param padding the new padding
	 */
  public void setPadding(@Nonnull final String padding) {
    set("padding", padding);
  }

  /**
	 * Sets the padding left.
	 *
	 * @param paddingLeft the new padding left
	 */
  public void setPaddingLeft(@Nonnull final String paddingLeft) {
    set("paddingLeft", paddingLeft);
  }

  /**
	 * Sets the padding right.
	 *
	 * @param paddingRight the new padding right
	 */
  public void setPaddingRight(@Nonnull final String paddingRight) {
    set("paddingRight", paddingRight);
  }

  /**
	 * Sets the padding top.
	 *
	 * @param paddingTop the new padding top
	 */
  public void setPaddingTop(@Nonnull final String paddingTop) {
    set("paddingTop", paddingTop);
  }

  /**
	 * Sets the padding bottom.
	 *
	 * @param paddingBottom the new padding bottom
	 */
  public void setPaddingBottom(@Nonnull final String paddingBottom) {
    set("paddingBottom", paddingBottom);
  }

  /**
	 * Sets the margin.
	 *
	 * @param margin the new margin
	 */
  public void setMargin(@Nonnull final String margin) {
    set("margin", margin);
  }

  /**
	 * Sets the margin left.
	 *
	 * @param marginLeft the new margin left
	 */
  public void setMarginLeft(@Nonnull final String marginLeft) {
    set("marginLeft", marginLeft);
  }

  /**
	 * Sets the margin right.
	 *
	 * @param marginRight the new margin right
	 */
  public void setMarginRight(@Nonnull final String marginRight) {
    set("marginRight", marginRight);
  }

  /**
	 * Sets the margin top.
	 *
	 * @param marginTop the new margin top
	 */
  public void setMarginTop(@Nonnull final String marginTop) {
    set("marginTop", marginTop);
  }

  /**
	 * Sets the margin bottom.
	 *
	 * @param marginBottom the new margin bottom
	 */
  public void setMarginBottom(@Nonnull final String marginBottom) {
    set("marginBottom", marginBottom);
  }

  /**
	 * Sets the child clip.
	 *
	 * @param childClip the new child clip
	 */
  public void setChildClip(@Nonnull final String childClip) {
    set("childClip", childClip);
  }

  /**
	 * Sets the render order.
	 *
	 * @param renderOrder the new render order
	 */
  public void setRenderOrder(final int renderOrder) {
    set("renderOrder", String.valueOf(renderOrder));
  }

  /**
	 * Sets the visible.
	 *
	 * @param visible the new visible
	 */
  public void setVisible(@Nonnull final String visible) {
    set("visible", visible);
  }

  /**
	 * Sets the visible to mouse.
	 *
	 * @param visibleToMouse the new visible to mouse
	 */
  public void setVisibleToMouse(@Nonnull final String visibleToMouse) {
    set("visibleToMouse", visibleToMouse);
  }

  /**
	 * Sets the child layout.
	 *
	 * @param childLayout the new child layout
	 */
  public void setChildLayout(@Nonnull final String childLayout) {
    set("childLayout", childLayout);
  }

  /**
	 * Sets the focusable.
	 *
	 * @param focusable the new focusable
	 */
  public void setFocusable(@Nonnull final String focusable) {
    set("focusable", focusable);
  }

  /**
	 * Sets the focusable insert before element id.
	 *
	 * @param focusableInsertBeforeElementId the new focusable insert before element
	 *                                       id
	 */
  public void setFocusableInsertBeforeElementId(@Nonnull final String focusableInsertBeforeElementId) {
    set("focusableInsertBeforeElementId", focusableInsertBeforeElementId);
  }

  /**
	 * Sets the font.
	 *
	 * @param font the new font
	 */
  public void setFont(@Nonnull final String font) {
    set("font", font);
  }

  /**
	 * Sets the text H align.
	 *
	 * @param textHAlign the new text H align
	 */
  public void setTextHAlign(@Nonnull final String textHAlign) {
    set("textHAlign", textHAlign);
  }

  /**
	 * Sets the text V align.
	 *
	 * @param textVAlign the new text V align
	 */
  public void setTextVAlign(@Nonnull final String textVAlign) {
    set("textVAlign", textVAlign);
  }

  /**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
  public void setColor(@Nonnull final String color) {
    set("color", color);
  }

  /**
	 * Sets the selection color.
	 *
	 * @param selectionColor the new selection color
	 */
  public void setSelectionColor(@Nonnull final String selectionColor) {
    set("selectionColor", selectionColor);
  }

  /**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
  public void setText(@Nonnull final String text) {
    set("text", text);
  }

  /**
	 * Sets the background color.
	 *
	 * @param backgroundColor the new background color
	 */
  public void setBackgroundColor(@Nonnull final String backgroundColor) {
    set("backgroundColor", backgroundColor);
  }

  /**
	 * Sets the background image.
	 *
	 * @param backgroundImage the new background image
	 */
  public void setBackgroundImage(@Nonnull final String backgroundImage) {
    set("backgroundImage", backgroundImage);
  }

  /**
	 * Sets the image mode.
	 *
	 * @param imageMode the new image mode
	 */
  public void setImageMode(@Nonnull final String imageMode) {
    set("imageMode", imageMode);
  }

  /**
	 * Sets the filename.
	 *
	 * @param filename the new filename
	 */
  public void setFilename(@Nonnull final String filename) {
    set("filename", filename);
  }

  /**
	 * Sets the inset.
	 *
	 * @param inset the new inset
	 */
  public void setInset(@Nonnull final String inset) {
    set("inset", inset);
  }

  /**
	 * Sets the controller.
	 *
	 * @param controller the new controller
	 */
  public void setController(@Nonnull final String controller) {
    set("controller", controller);
  }

  /**
	 * Sets the input mapping.
	 *
	 * @param inputMapping the new input mapping
	 */
  public void setInputMapping(@Nonnull final String inputMapping) {
    set("inputMapping", inputMapping);
  }

  /**
	 * Sets the style.
	 *
	 * @param style the new style
	 */
  public void setStyle(@Nonnull final String style) {
    set("style", style);
  }

  /**
	 * Sets the interact attribute.
	 *
	 * @param name  the name
	 * @param value the value
	 */
  public void setInteractAttribute(@Nonnull final String name, @Nonnull final String value) {
    getInteract().setAttribute(name, value);
  }

  /**
	 * Sets the interact on click.
	 *
	 * @param onClick the new interact on click
	 */
  public void setInteractOnClick(@Nonnull final String onClick) {
    getInteract().setOnClick(onClick);
  }
  
  /**
	 * Sets the ineract on multi click.
	 *
	 * @param onClick the new ineract on multi click
	 */
  public void setIneractOnMultiClick(@Nonnull final String onClick){
    getInteract().setOnMultiClick(onClick);
  }
  
  /**
	 * Sets the interact on release.
	 *
	 * @param onRelease the new interact on release
	 */
  public void setInteractOnRelease(@Nonnull final String onRelease) {
    getInteract().setOnRelease(onRelease);
  }

  /**
	 * Sets the interact on mouse over.
	 *
	 * @param onMouseOver the new interact on mouse over
	 */
  public void setInteractOnMouseOver(@Nonnull final String onMouseOver) {
    getInteract().setOnMouseOver(onMouseOver);
  }

  /**
	 * Sets the interact on click repeat.
	 *
	 * @param onClickRepeat the new interact on click repeat
	 */
  public void setInteractOnClickRepeat(@Nonnull final String onClickRepeat) {
    getInteract().setOnClickRepeat(onClickRepeat);
  }

  /**
	 * Sets the interact on click mouse move.
	 *
	 * @param onClickMouseMove the new interact on click mouse move
	 */
  public void setInteractOnClickMouseMove(@Nonnull final String onClickMouseMove) {
    getInteract().setOnClickMouseMove(onClickMouseMove);
  }

  /**
	 * Sets the interact on click alternate key.
	 *
	 * @param onClickAlternateKey the new interact on click alternate key
	 */
  public void setInteractOnClickAlternateKey(@Nonnull final String onClickAlternateKey) {
    getInteract().setOnClickAlternateKey(onClickAlternateKey);
  }

  /**
	 * Sets the effects attribute.
	 *
	 * @param name  the name
	 * @param value the value
	 */
  public void setEffectsAttribute(@Nonnull final String name, @Nonnull final String value) {
    getEffects().setAttribute(name, value);
  }

  /**
	 * Sets the effects overlay.
	 *
	 * @param overlay the new effects overlay
	 */
  public void setEffectsOverlay(@Nonnull final String overlay) {
    getEffects().setOverlay(overlay);
  }

  /**
	 * Adds the effects.
	 *
	 * @param eventId     the event id
	 * @param effectParam the effect param
	 */
  public void addEffects(@Nonnull final EffectEventId eventId, @Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addEffectAttribute(eventId, effectParam);
  }

  /**
	 * Adds the effects on start screen.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnStartScreen(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnStartScreen(effectParam);
  }

  /**
	 * Adds the effects on end screen.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnEndScreen(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnEndScreen(effectParam);
  }

  /**
	 * Adds the effects on hover.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    getEffects().addOnHover(effectParam);
  }

  /**
	 * Adds the effects on start hover.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnStartHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    getEffects().addOnStartHover(effectParam);
  }

  /**
	 * Adds the effects on end hover.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnEndHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    getEffects().addOnEndHover(effectParam);
  }

  /**
	 * Adds the effects on click.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnClick(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnClick(effectParam);
  }

  /**
	 * Adds the effects on focus.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnFocus(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnFocus(effectParam);
  }

  /**
	 * Adds the effects on lost focus.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnLostFocus(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnLostFocus(effectParam);
  }

  /**
	 * Adds the effects on get focus.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnGetFocus(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnGetFocus(effectParam);
  }

  /**
	 * Adds the effects on active.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnActive(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnActive(effectParam);
  }

  /**
	 * Adds the effects on show.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnShow(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnShow(effectParam);
  }

  /**
	 * Adds the effects on hide.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnHide(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnHide(effectParam);
  }

  /**
	 * Adds the effects on custom.
	 *
	 * @param effectParam the effect param
	 */
  public void addEffectsOnCustom(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnCustom(effectParam);
  }

  /**
	 * Creates the control internal.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 * @param parent the parent
	 * @return the element
	 */
  @Nonnull
  protected Element createControlInternal(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    ControlType controlType = new ControlType(attributes);
    return buildControl(nifty, screen, parent, controlType, new LayoutPart());
  }

  /**
	 * Creates the text.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 * @param parent the parent
	 * @return the element
	 */
  @Nonnull
  protected Element createText(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    TextType textType = new TextType(attributes);
    return buildControl(nifty, screen, parent, textType, new LayoutPart());
  }

  /**
	 * Creates the panel.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 * @param parent the parent
	 * @return the element
	 */
  @Nonnull
  protected Element createPanel(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    PanelType panelType = new PanelType(attributes);
    return buildControl(nifty, screen, parent, panelType, new LayoutPart());
  }

  /**
	 * Register popup.
	 *
	 * @param nifty the nifty
	 */
  protected void registerPopup(@Nonnull final Nifty nifty) {
    PopupType popupType = new PopupType(attributes);
    popupType.translateSpecialValues(nifty, null);
    nifty.registerPopup(popupType);
  }

  /**
	 * Creates the layer.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 * @param parent the parent
	 * @return the element
	 */
  @Nonnull
  protected Element createLayer(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    LayerType layerType = new LayerType(attributes);
    return buildControl(nifty, screen, parent, layerType, nifty.getRootLayerFactory().createRootLayerLayoutPart(nifty));
  }

  /**
	 * Creates the image.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 * @param parent the parent
	 * @return the element
	 */
  @Nonnull
  protected Element createImage(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    ImageType imageType = new ImageType(attributes);
    return buildControl(nifty, screen, parent, imageType, new LayoutPart());
  }

  /**
	 * Builds the control.
	 *
	 * @param nifty       the nifty
	 * @param screen      the screen
	 * @param parent      the parent
	 * @param elementType the element type
	 * @param layoutPart  the layout part
	 * @return the element
	 */
  @Nonnull
  private Element buildControl(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent,
      @Nonnull final ElementType elementType,
      @Nonnull final LayoutPart layoutPart) {
    if (effects != null) {
      elementType.setEffect(effects.create());
    }
    if (interact != null) {
      elementType.setInteract(interact.create());
    }
    elementType.prepare(nifty, screen, parent.getElementType());
    elementType.connectParentControls(parent);
    Element element = elementType.create(
        parent,
        nifty,
        screen,
        layoutPart);

    parent.layoutElements();
    return element;
  }

  /**
	 * Gets the standard control.
	 *
	 * @return the standard control
	 */
  @Nonnull
  protected StandardControl getStandardControl() {
    return new StandardControl() {
      @Nonnull
      @Override
      public Element createControl(
          @Nonnull final Nifty nifty,
          @Nonnull final Screen screen,
          @Nonnull final Element parent) {
        return createControlInternal(nifty, screen, parent);
      }
    };
  }

  /**
	 * Refresh attributes.
	 *
	 * @param attrib the attrib
	 */
  public void refreshAttributes(@Nonnull final Attributes attrib) {
    attrib.refreshFromAttributes(attributes);
  }

  /**
	 * Refresh effects.
	 *
	 * @param effects the effects
	 */
  public void refreshEffects(@Nonnull final EffectsType effects) {
    if (this.effects != null) {
      effects.refreshFromAttributes(this.effects);
    }
  }

  /**
	 * Creates the type.
	 *
	 * @return the element type
	 */
  @Nullable
  public ElementType createType() {
    // you'll need to implement this in a sub class
    return null;
  }

  /**
	 * Connect.
	 *
	 * @param e the e
	 */
  public void connect(@Nonnull final ElementType e) {
    if (effects != null) {
      e.setEffect(effects.create());
    }
    if (interact != null) {
      e.setInteract(interact.create());
    }
  }

  /**
	 * Creates the style type.
	 *
	 * @param styleAttributes the style attributes
	 * @return the style type
	 */
  @Nonnull
  public StyleType createStyleType(@Nonnull final Attributes styleAttributes) {
    StyleType styleType = new StyleType(styleAttributes);
    styleType.setAttributes(new AttributesType(attributes));
    if (effects != null) {
      styleType.setEffect(effects.create());
    }
    if (interact != null) {
      styleType.setInteract(interact.create());
    }
    return styleType;
  }
}
