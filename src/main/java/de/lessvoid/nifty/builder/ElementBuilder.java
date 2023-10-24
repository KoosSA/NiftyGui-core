package de.lessvoid.nifty.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlInteractAttributes;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.EnumStorage;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.factories.CollectionFactory;

/**
 * The Class ElementBuilder.
 */
public abstract class ElementBuilder {
  
  /** The attributes. */
  @Nonnull
  private final ControlAttributes attributes;
  
  /** The interact attributes. */
  @Nonnull
  private final ControlInteractAttributes interactAttributes;
  
  /** The element builders. */
  @Nonnull
  protected final List<ElementBuilder> elementBuilders;

  /** The effect builders. */
  @Nonnull
  private final EnumStorage<EffectEventId, Collection<EffectBuilder>> effectBuilders;

  /** The controller. */
  @Nullable
  private Controller controller;

  /**
	 * Instantiates a new element builder.
	 *
	 * @param attributes the attributes
	 */
  protected ElementBuilder(@Nonnull final ControlAttributes attributes) {
    elementBuilders = new ArrayList<ElementBuilder>();
    interactAttributes = new ControlInteractAttributes();
    effectBuilders = new EnumStorage<EffectEventId, Collection<EffectBuilder>>(
        EffectEventId.class, CollectionFactory.<EffectBuilder>getArrayListInstance());

    this.attributes = attributes;
    attributes.setInteract(interactAttributes);
  }

  /**
	 * The Enum ChildLayoutType.
	 */
  public enum ChildLayoutType {
    
    /** The Vertical. */
    Vertical("vertical"),
    
    /** The Horizontal. */
    Horizontal("horizontal"),
    
    /** The Center. */
    Center("center"),
    
    /** The Absolute. */
    Absolute("absolute"),
    
    /** The Absolute inside. */
    AbsoluteInside("absolute-inside"),
    
    /** The Overlay. */
    Overlay("overlay");

    /** The layout. */
    @Nonnull
    private final String layout;

    /**
	 * Instantiates a new child layout type.
	 *
	 * @param layout the layout
	 */
    private ChildLayoutType(@Nonnull final String layout) {
      this.layout = layout;
    }

    /**
	 * Gets the layout.
	 *
	 * @return the layout
	 */
    @Nonnull
    public String getLayout() {
      return layout;
    }
  }

  /**
	 * The Enum Align.
	 */
  public enum Align {
    
    /** The Left. */
    Left("left"), 
 /** The Right. */
 Right("right"), 
 /** The Center. */
 Center("center");

    /** The align. */
    @Nonnull
    private final String align;

    /**
	 * Instantiates a new align.
	 *
	 * @param align the align
	 */
    private Align(@Nonnull final String align) {
      this.align = align;
    }

    /**
	 * Gets the layout.
	 *
	 * @return the layout
	 */
    @Nonnull
    public String getLayout() {
      return align;
    }
  }

  /**
	 * The Enum VAlign.
	 */
  public enum VAlign {
    
    /** The Top. */
    Top("top"), 
 /** The Bottom. */
 Bottom("bottom"), 
 /** The Center. */
 Center("center");

    /** The valign. */
    @Nonnull
    private final String valign;

    /**
	 * Instantiates a new v align.
	 *
	 * @param valign the valign
	 */
    private VAlign(@Nonnull final String valign) {
      this.valign = valign;
    }

    /**
	 * Gets the layout.
	 *
	 * @return the layout
	 */
    @Nonnull
    public String getLayout() {
      return valign;
    }
  }

  /**
	 * Id.
	 *
	 * @param id the id
	 */
  public void id(@Nonnull final String id) {
    attributes.setId(id);
  }

  /**
	 * Gets the id.
	 *
	 * @return the id
	 */
  @Nullable
  public String getId() {
    return attributes.getId();
  }

  /**
	 * Checks if is auto id.
	 *
	 * @return true, if is auto id
	 */
  public boolean isAutoId() {
    return attributes.isAutoId();
  }

  /**
	 * Name.
	 *
	 * @param name the name
	 * @return the element builder
	 */
  public ElementBuilder name(@Nonnull final String name) {
    attributes.setName(name);
    return this;
  }

  /**
	 * Background color.
	 *
	 * @param backgroundColor the background color
	 * @return the element builder
	 */
  public ElementBuilder backgroundColor(@Nonnull final String backgroundColor) {
    attributes.setBackgroundColor(backgroundColor);
    return this;
  }

  /**
	 * Background color.
	 *
	 * @param backgroundColor the background color
	 * @return the element builder
	 */
  public ElementBuilder backgroundColor(@Nonnull final Color backgroundColor) {
    attributes.setBackgroundColor(backgroundColor.getColorString());
    return this;
  }

  /**
	 * Controller.
	 *
	 * @param controller the controller
	 * @return the element builder
	 */
  public ElementBuilder controller(@Nonnull final Controller controller) {
    attributes.set("controller", controller.getClass().getName());
    this.controller = controller;
    return this;
  }

  /**
	 * Controller.
	 *
	 * @param controllerClass the controller class
	 * @return the element builder
	 */
  public ElementBuilder controller(@Nonnull final String controllerClass) {
    attributes.set("controller", controllerClass);
    return this;
  }

  /**
	 * Color.
	 *
	 * @param color the color
	 * @return the element builder
	 */
  public ElementBuilder color(@Nonnull final String color) {
    attributes.setColor(color);
    return this;
  }

  /**
	 * Color.
	 *
	 * @param color the color
	 * @return the element builder
	 */
  public ElementBuilder color(@Nonnull final Color color) {
    attributes.setColor(color.getColorString());
    return this;
  }

  /**
	 * Selection color.
	 *
	 * @param color the color
	 * @return the element builder
	 */
  public ElementBuilder selectionColor(@Nonnull final String color) {
    attributes.setSelectionColor(color);
    return this;
  }

  /**
	 * Selection color.
	 *
	 * @param color the color
	 * @return the element builder
	 */
  public ElementBuilder selectionColor(@Nonnull final Color color) {
    attributes.setSelectionColor(color.getColorString());
    return this;
  }

  /**
	 * Text.
	 *
	 * @param text the text
	 * @return the element builder
	 */
  public ElementBuilder text(@Nonnull final String text) {
    attributes.setText(text);
    return this;
  }

  /**
	 * Background image.
	 *
	 * @param backgroundImage the background image
	 * @return the element builder
	 */
  public ElementBuilder backgroundImage(@Nonnull final String backgroundImage) {
    attributes.setBackgroundImage(backgroundImage);
    return this;
  }

  /**
	 * Image mode.
	 *
	 * @param imageMode the image mode
	 * @return the element builder
	 */
  public ElementBuilder imageMode(@Nonnull final String imageMode) {
    attributes.setImageMode(imageMode);
    return this;
  }

  /**
	 * Inset.
	 *
	 * @param inset the inset
	 * @return the element builder
	 */
  public ElementBuilder inset(@Nonnull final String inset) {
    attributes.setInset(inset);
    return this;
  }

  /**
	 * Input mapping.
	 *
	 * @param inputMapping the input mapping
	 * @return the element builder
	 */
  public ElementBuilder inputMapping(@Nonnull final String inputMapping) {
    attributes.setInputMapping(inputMapping);
    return this;
  }

  /**
	 * Style.
	 *
	 * @param style the style
	 * @return the element builder
	 */
  public ElementBuilder style(@Nonnull final String style) {
    attributes.setStyle(style);
    return this;
  }

  /**
	 * Child layout.
	 *
	 * @param childLayout the child layout
	 * @return the element builder
	 */
  public ElementBuilder childLayout(@Nonnull final ChildLayoutType childLayout) {
    attributes.setChildLayout(childLayout.getLayout());
    return this;
  }

  /**
	 * Child layout vertical.
	 *
	 * @return the element builder
	 */
  public ElementBuilder childLayoutVertical() {
    return childLayout(ChildLayoutType.Vertical);
  }

  /**
	 * Child layout horizontal.
	 *
	 * @return the element builder
	 */
  public ElementBuilder childLayoutHorizontal() {
    return childLayout(ChildLayoutType.Horizontal);
  }

  /**
	 * Child layout center.
	 *
	 * @return the element builder
	 */
  public ElementBuilder childLayoutCenter() {
    return childLayout(ChildLayoutType.Center);
  }

  /**
	 * Child layout absolute.
	 *
	 * @return the element builder
	 */
  public ElementBuilder childLayoutAbsolute() {
    return childLayout(ChildLayoutType.Absolute);
  }

  /**
	 * Child layout absolute inside.
	 *
	 * @return the element builder
	 */
  public ElementBuilder childLayoutAbsoluteInside() {
    return childLayout(ChildLayoutType.AbsoluteInside);
  }

  /**
	 * Child layout overlay.
	 *
	 * @return the element builder
	 */
  public ElementBuilder childLayoutOverlay() {
    return childLayout(ChildLayoutType.Overlay);
  }

  /**
	 * Height.
	 *
	 * @param height the height
	 * @return the element builder
	 */
  public ElementBuilder height(@Nonnull final String height) {
    attributes.setHeight(height);
    return this;
  }

  /**
	 * Width.
	 *
	 * @param width the width
	 * @return the element builder
	 */
  public ElementBuilder width(@Nonnull final String width) {
    attributes.setWidth(width);
    return this;
  }

  /**
	 * Height.
	 *
	 * @param height the height
	 * @return the element builder
	 */
  public ElementBuilder height(@Nonnull final SizeValue height) {
    attributes.setHeight(height.getValueAsString());
    return this;
  }

  /**
	 * Width.
	 *
	 * @param width the width
	 * @return the element builder
	 */
  public ElementBuilder width(@Nonnull final SizeValue width) {
    attributes.setWidth(width.getValueAsString());
    return this;
  }

  /**
	 * X.
	 *
	 * @param x the x
	 * @return the element builder
	 */
  public ElementBuilder x(@Nonnull final String x) {
    attributes.setX(x);
    return this;
  }

  /**
	 * Y.
	 *
	 * @param y the y
	 * @return the element builder
	 */
  public ElementBuilder y(@Nonnull final String y) {
    attributes.setY(y);
    return this;
  }

  /**
	 * X.
	 *
	 * @param x the x
	 * @return the element builder
	 */
  public ElementBuilder x(@Nonnull final SizeValue x) {
    attributes.setX(x.getValueAsString());
    return this;
  }

  /**
	 * Y.
	 *
	 * @param y the y
	 * @return the element builder
	 */
  public ElementBuilder y(@Nonnull final SizeValue y) {
    attributes.setY(y.getValueAsString());
    return this;
  }

  /**
	 * Child clip.
	 *
	 * @param childClip the child clip
	 * @return the element builder
	 */
  public ElementBuilder childClip(final boolean childClip) {
    attributes.setChildClip(String.valueOf(childClip));
    return this;
  }

  /**
	 * Render order.
	 *
	 * @param renderOrder the render order
	 * @return the element builder
	 */
  public ElementBuilder renderOrder(final int renderOrder) {
    attributes.setRenderOrder(renderOrder);
    return this;
  }

  /**
	 * Visible.
	 *
	 * @param visible the visible
	 * @return the element builder
	 */
  public ElementBuilder visible(final boolean visible) {
    attributes.setVisible(String.valueOf(visible));
    return this;
  }

  /**
	 * Focusable.
	 *
	 * @param focusable the focusable
	 * @return the element builder
	 */
  public ElementBuilder focusable(final boolean focusable) {
    attributes.setFocusable(String.valueOf(focusable));
    return this;
  }

  /**
	 * Focusable insert before element id.
	 *
	 * @param focusableInsertBeforeElementId the focusable insert before element id
	 * @return the element builder
	 */
  public ElementBuilder focusableInsertBeforeElementId(@Nonnull final String focusableInsertBeforeElementId) {
    attributes.setFocusableInsertBeforeElementId(focusableInsertBeforeElementId);
    return this;
  }

  /**
	 * Text H align.
	 *
	 * @param align the align
	 * @return the element builder
	 */
  public ElementBuilder textHAlign(@Nonnull final Align align) {
    attributes.set("textHAlign", align.getLayout());
    return this;
  }

  /**
	 * Text H align left.
	 *
	 * @return the element builder
	 */
  public ElementBuilder textHAlignLeft() {
    return textHAlign(Align.Left);
  }

  /**
	 * Text H align right.
	 *
	 * @return the element builder
	 */
  public ElementBuilder textHAlignRight() {
    return textHAlign(Align.Right);
  }

  /**
	 * Text H align center.
	 *
	 * @return the element builder
	 */
  public ElementBuilder textHAlignCenter() {
    return textHAlign(Align.Center);
  }

  /**
	 * Text V align.
	 *
	 * @param valign the valign
	 * @return the element builder
	 */
  public ElementBuilder textVAlign(@Nonnull final VAlign valign) {
    attributes.set("textVAlign", valign.getLayout());
    return this;
  }

  /**
	 * Text V align top.
	 *
	 * @return the element builder
	 */
  public ElementBuilder textVAlignTop() {
    return textVAlign(VAlign.Top);
  }

  /**
	 * Text V align bottom.
	 *
	 * @return the element builder
	 */
  public ElementBuilder textVAlignBottom() {
    return textVAlign(VAlign.Bottom);
  }

  /**
	 * Text V align center.
	 *
	 * @return the element builder
	 */
  public ElementBuilder textVAlignCenter() {
    return textVAlign(VAlign.Center);
  }

  /**
	 * Align.
	 *
	 * @param align the align
	 * @return the element builder
	 */
  public ElementBuilder align(@Nonnull final Align align) {
    attributes.setAlign(align.getLayout());
    return this;
  }

  /**
	 * Align left.
	 *
	 * @return the element builder
	 */
  public ElementBuilder alignLeft() {
    return align(Align.Left);
  }

  /**
	 * Align right.
	 *
	 * @return the element builder
	 */
  public ElementBuilder alignRight() {
    return align(Align.Right);
  }

  /**
	 * Align center.
	 *
	 * @return the element builder
	 */
  public ElementBuilder alignCenter() {
    return align(Align.Center);
  }

  /**
	 * Valign.
	 *
	 * @param valign the valign
	 * @return the element builder
	 */
  public ElementBuilder valign(@Nonnull final VAlign valign) {
    attributes.setVAlign(valign.getLayout());
    return this;
  }

  /**
	 * Valign top.
	 *
	 * @return the element builder
	 */
  public ElementBuilder valignTop() {
    return valign(VAlign.Top);
  }

  /**
	 * Valign bottom.
	 *
	 * @return the element builder
	 */
  public ElementBuilder valignBottom() {
    return valign(VAlign.Bottom);
  }

  /**
	 * Valign center.
	 *
	 * @return the element builder
	 */
  public ElementBuilder valignCenter() {
    return valign(VAlign.Center);
  }

  /**
	 * Visible to mouse.
	 *
	 * @param visibleToMouse the visible to mouse
	 * @return the element builder
	 */
  public ElementBuilder visibleToMouse(final boolean visibleToMouse) {
    attributes.setVisibleToMouse(String.valueOf(visibleToMouse));
    return this;
  }

  /**
	 * Visible to mouse.
	 *
	 * @return the element builder
	 */
  public ElementBuilder visibleToMouse() {
    return visibleToMouse(true);
  }

  /**
	 * Invisible to mouse.
	 *
	 * @return the element builder
	 */
  public ElementBuilder invisibleToMouse() {
    return visibleToMouse(false);
  }

  /**
	 * Font.
	 *
	 * @param font the font
	 * @return the element builder
	 */
  public ElementBuilder font(@Nonnull final String font) {
    attributes.setFont(font);
    return this;
  }

  /**
	 * Filename.
	 *
	 * @param filename the filename
	 * @return the element builder
	 */
  public ElementBuilder filename(@Nonnull final String filename) {
    attributes.setFilename(filename);
    return this;
  }

  /**
	 * Padding.
	 *
	 * @param padding the padding
	 * @return the element builder
	 */
  public ElementBuilder padding(@Nonnull final String padding) {
    attributes.setPadding(padding);
    return this;
  }

  /**
	 * Padding left.
	 *
	 * @param padding the padding
	 * @return the element builder
	 */
  public ElementBuilder paddingLeft(@Nonnull final String padding) {
    attributes.setPaddingLeft(padding);
    return this;
  }

  /**
	 * Padding right.
	 *
	 * @param padding the padding
	 * @return the element builder
	 */
  public ElementBuilder paddingRight(@Nonnull final String padding) {
    attributes.setPaddingRight(padding);
    return this;
  }

  /**
	 * Padding top.
	 *
	 * @param padding the padding
	 * @return the element builder
	 */
  public ElementBuilder paddingTop(@Nonnull final String padding) {
    attributes.setPaddingTop(padding);
    return this;
  }

  /**
	 * Padding bottom.
	 *
	 * @param padding the padding
	 * @return the element builder
	 */
  public ElementBuilder paddingBottom(@Nonnull final String padding) {
    attributes.setPaddingBottom(padding);
    return this;
  }

  /**
	 * Margin.
	 *
	 * @param margin the margin
	 * @return the element builder
	 */
  public ElementBuilder margin(@Nonnull final String margin) {
    attributes.setMargin(margin);
    return this;
  }

  /**
	 * Margin left.
	 *
	 * @param margin the margin
	 * @return the element builder
	 */
  public ElementBuilder marginLeft(@Nonnull final String margin) {
    attributes.setMarginLeft(margin);
    return this;
  }

  /**
	 * Margin right.
	 *
	 * @param margin the margin
	 * @return the element builder
	 */
  public ElementBuilder marginRight(@Nonnull final String margin) {
    attributes.setMarginRight(margin);
    return this;
  }

  /**
	 * Margin top.
	 *
	 * @param margin the margin
	 * @return the element builder
	 */
  public ElementBuilder marginTop(@Nonnull final String margin) {
    attributes.setMarginTop(margin);
    return this;
  }

  /**
	 * Margin bottom.
	 *
	 * @param margin the margin
	 * @return the element builder
	 */
  public ElementBuilder marginBottom(@Nonnull final String margin) {
    attributes.setMarginBottom(margin);
    return this;
  }

  /**
	 * Sets the.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the element builder
	 */
  public ElementBuilder set(@Nonnull final String key, @Nonnull final String value) {
    attributes.set(key, value);
    return this;
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
	 * Panel.
	 *
	 * @param panelBuilder the panel builder
	 * @return the element builder
	 */
  public ElementBuilder panel(@Nonnull final PanelBuilder panelBuilder) {
    elementBuilders.add(panelBuilder);
    return this;
  }

  /**
	 * Text.
	 *
	 * @param textBuilder the text builder
	 * @return the element builder
	 */
  public ElementBuilder text(@Nonnull final TextBuilder textBuilder) {
    elementBuilders.add(textBuilder);
    return this;
  }

  /**
	 * Image.
	 *
	 * @param imageBuilder the image builder
	 * @return the element builder
	 */
  public ElementBuilder image(@Nonnull final ImageBuilder imageBuilder) {
    elementBuilders.add(imageBuilder);
    return this;
  }

  /**
	 * Control.
	 *
	 * @param controlBuilder the control builder
	 * @return the element builder
	 */
  public ElementBuilder control(@Nonnull final ControlBuilder controlBuilder) {
    elementBuilders.add(controlBuilder);
    return this;
  }

  /**
	 * On effect.
	 *
	 * @param eventId the event id
	 * @param builder the builder
	 * @return the element builder
	 */
  public ElementBuilder onEffect(@Nonnull final EffectEventId eventId, @Nonnull final EffectBuilder builder) {
    effectBuilders.get(eventId).add(builder);
    return this;
  }

  /**
	 * On start screen effect.
	 *
	 * @param onStartScreenEffect the on start screen effect
	 * @return the element builder
	 */
  public ElementBuilder onStartScreenEffect(@Nonnull final EffectBuilder onStartScreenEffect) {
    return onEffect(EffectEventId.onStartScreen, onStartScreenEffect);
  }

  /**
	 * On end screen effect.
	 *
	 * @param onEndScreenEffect the on end screen effect
	 * @return the element builder
	 */
  public ElementBuilder onEndScreenEffect(@Nonnull final EffectBuilder onEndScreenEffect) {
    return onEffect(EffectEventId.onEndScreen, onEndScreenEffect);
  }

  /**
	 * On hover effect.
	 *
	 * @param onHoverEffect the on hover effect
	 * @return the element builder
	 */
  public ElementBuilder onHoverEffect(@Nonnull final HoverEffectBuilder onHoverEffect) {
    return onEffect(EffectEventId.onHover, onHoverEffect);
  }

  /**
	 * On start hover effect.
	 *
	 * @param onStartHoverEffect the on start hover effect
	 * @return the element builder
	 */
  public ElementBuilder onStartHoverEffect(@Nonnull final HoverEffectBuilder onStartHoverEffect) {
    return onEffect(EffectEventId.onStartHover, onStartHoverEffect);
  }

  /**
	 * On end hover effect.
	 *
	 * @param onEndHoverEffect the on end hover effect
	 * @return the element builder
	 */
  public ElementBuilder onEndHoverEffect(@Nonnull final HoverEffectBuilder onEndHoverEffect) {
    return onEffect(EffectEventId.onEndHover, onEndHoverEffect);
  }

  /**
	 * On click effect.
	 *
	 * @param onClickEffect the on click effect
	 * @return the element builder
	 */
  public ElementBuilder onClickEffect(@Nonnull final EffectBuilder onClickEffect) {
    return onEffect(EffectEventId.onClick, onClickEffect);
  }

  /**
	 * On focus effect.
	 *
	 * @param onFocusEffect the on focus effect
	 * @return the element builder
	 */
  public ElementBuilder onFocusEffect(@Nonnull final EffectBuilder onFocusEffect) {
    return onEffect(EffectEventId.onFocus, onFocusEffect);
  }

  /**
	 * On lost focus effect.
	 *
	 * @param onLostFocusEffect the on lost focus effect
	 * @return the element builder
	 */
  public ElementBuilder onLostFocusEffect(@Nonnull final EffectBuilder onLostFocusEffect) {
    return onEffect(EffectEventId.onLostFocus, onLostFocusEffect);
  }

  /**
	 * On get focus effect.
	 *
	 * @param onGetFocusEffect the on get focus effect
	 * @return the element builder
	 */
  public ElementBuilder onGetFocusEffect(@Nonnull final EffectBuilder onGetFocusEffect) {
    return onEffect(EffectEventId.onGetFocus, onGetFocusEffect);
  }

  /**
	 * On active effect.
	 *
	 * @param onActiveEffect the on active effect
	 * @return the element builder
	 */
  public ElementBuilder onActiveEffect(@Nonnull final EffectBuilder onActiveEffect) {
    return onEffect(EffectEventId.onActive, onActiveEffect);
  }

  /**
	 * On show effect.
	 *
	 * @param onShowEffect the on show effect
	 * @return the element builder
	 */
  public ElementBuilder onShowEffect(@Nonnull final EffectBuilder onShowEffect) {
    return onEffect(EffectEventId.onShow, onShowEffect);
  }

  /**
	 * On hide effect.
	 *
	 * @param onHideEffect the on hide effect
	 * @return the element builder
	 */
  public ElementBuilder onHideEffect(@Nonnull final EffectBuilder onHideEffect) {
    return onEffect(EffectEventId.onHide, onHideEffect);
  }

  /**
	 * On custom effect.
	 *
	 * @param onCustomEffect the on custom effect
	 * @return the element builder
	 */
  public ElementBuilder onCustomEffect(@Nonnull final EffectBuilder onCustomEffect) {
    return onEffect(EffectEventId.onCustom, onCustomEffect);
  }
  
  /**
	 * Interact on click.
	 *
	 * @param onClick the on click
	 * @return the element builder
	 */
  public ElementBuilder interactOnClick(@Nonnull String onClick) {
    interactAttributes.setOnClick(onClick);
    return this;
  }
  
  /**
	 * Interact on click repeat.
	 *
	 * @param onClickRepeat the on click repeat
	 * @return the element builder
	 */
  public ElementBuilder interactOnClickRepeat(@Nonnull final String onClickRepeat) {
    interactAttributes.setOnClickRepeat(onClickRepeat);
    return this;
  }
  
  /**
	 * Interact on release.
	 *
	 * @param onRelease the on release
	 * @return the element builder
	 */
  public ElementBuilder interactOnRelease(@Nonnull final String onRelease) {
    interactAttributes.setOnRelease(onRelease);
    return this;
  }
  
  /**
	 * Interact on click mouse move.
	 *
	 * @param onClickMouseMove the on click mouse move
	 * @return the element builder
	 */
  public ElementBuilder interactOnClickMouseMove(@Nonnull final String onClickMouseMove) {
    interactAttributes.setOnClickMouseMove(onClickMouseMove);
    return this;
  }
  
  /**
	 * Interact on multi click.
	 *
	 * @param onMultiClick the on multi click
	 * @return the element builder
	 */
  public ElementBuilder interactOnMultiClick(@Nonnull String onMultiClick){
    interactAttributes.setOnMultiClick(onMultiClick);
    return this;
  }
  
  /**
	 * Interact on primary click.
	 *
	 * @param onPrimaryClick the on primary click
	 * @return the element builder
	 */
  public ElementBuilder interactOnPrimaryClick(@Nonnull String onPrimaryClick) {
    interactAttributes.setOnPrimaryClick(onPrimaryClick);
    return this;
  }
  
  /**
	 * Interact on primary click repeat.
	 *
	 * @param onPrimaryClickRepeat the on primary click repeat
	 * @return the element builder
	 */
  public ElementBuilder interactOnPrimaryClickRepeat(@Nonnull final String onPrimaryClickRepeat) {
    interactAttributes.setOnPrimaryClickRepeat(onPrimaryClickRepeat);
    return this;
  }
  
  /**
	 * Interact on primary release.
	 *
	 * @param onPrimaryRelease the on primary release
	 * @return the element builder
	 */
  public ElementBuilder interactOnPrimaryRelease(@Nonnull final String onPrimaryRelease) {
    interactAttributes.setOnPrimaryRelease(onPrimaryRelease);
    return this;
  }
  
  /**
	 * Interact on primary click mouse move.
	 *
	 * @param onPrimaryClickMouseMove the on primary click mouse move
	 * @return the element builder
	 */
  public ElementBuilder interactOnPrimaryClickMouseMove(@Nonnull final String onPrimaryClickMouseMove) {
    interactAttributes.setOnPrimaryClickMouseMove(onPrimaryClickMouseMove);
    return this;
  }
  
  /**
	 * Interact on primary multi click.
	 *
	 * @param onPrimaryMultiClick the on primary multi click
	 * @return the element builder
	 */
  public ElementBuilder interactOnPrimaryMultiClick(@Nonnull String onPrimaryMultiClick){
    interactAttributes.setOnPrimaryMultiClick(onPrimaryMultiClick);
    return this;
  }
  
  /**
	 * Interact on secondary click.
	 *
	 * @param onSecondaryClick the on secondary click
	 * @return the element builder
	 */
  public ElementBuilder interactOnSecondaryClick(@Nonnull String onSecondaryClick) {
    interactAttributes.setOnSecondaryClick(onSecondaryClick);
    return this;
  }
  
  /**
	 * Interact on secondary click repeat.
	 *
	 * @param onSecondaryClickRepeat the on secondary click repeat
	 * @return the element builder
	 */
  public ElementBuilder interactOnSecondaryClickRepeat(@Nonnull final String onSecondaryClickRepeat) {
    interactAttributes.setOnSecondaryClickRepeat(onSecondaryClickRepeat);
    return this;
  }
  
  /**
	 * Interact on secondary release.
	 *
	 * @param onSecondaryRelease the on secondary release
	 * @return the element builder
	 */
  public ElementBuilder interactOnSecondaryRelease(@Nonnull final String onSecondaryRelease) {
    interactAttributes.setOnSecondaryRelease(onSecondaryRelease);
    return this;
  }
  
  /**
	 * Interact on secondary click mouse move.
	 *
	 * @param onSecondaryClickMouseMove the on secondary click mouse move
	 * @return the element builder
	 */
  public ElementBuilder interactOnSecondaryClickMouseMove(@Nonnull final String onSecondaryClickMouseMove) {
    interactAttributes.setOnSecondaryClickMouseMove(onSecondaryClickMouseMove);
    return this;
  }
  
  /**
	 * Interact on secondary multi click.
	 *
	 * @param onSecondaryMultiClick the on secondary multi click
	 * @return the element builder
	 */
  public ElementBuilder interactOnSecondaryMultiClick(@Nonnull String onSecondaryMultiClick){
    interactAttributes.setOnSecondaryMultiClick(onSecondaryMultiClick);
    return this;
  }

  /**
	 * Interact on tertiary click.
	 *
	 * @param onTertiaryClick the on tertiary click
	 * @return the element builder
	 */
  public ElementBuilder interactOnTertiaryClick(@Nonnull String onTertiaryClick) {
    interactAttributes.setOnTertiaryClick(onTertiaryClick);
    return this;
  }

  /**
	 * Interact on tertiary click repeat.
	 *
	 * @param onTertiaryClickRepeat the on tertiary click repeat
	 * @return the element builder
	 */
  public ElementBuilder interactOnTertiaryClickRepeat(@Nonnull final String onTertiaryClickRepeat) {
    interactAttributes.setOnTertiaryClickRepeat(onTertiaryClickRepeat);
    return this;
  }
  
  /**
	 * Interact on tertiary release.
	 *
	 * @param onTertiaryRelease the on tertiary release
	 * @return the element builder
	 */
  public ElementBuilder interactOnTertiaryRelease(@Nonnull final String onTertiaryRelease) {
    interactAttributes.setOnTertiaryRelease(onTertiaryRelease);
    return this;
  }

  /**
	 * Interact on tertiary click mouse move.
	 *
	 * @param onTertiaryClickMouseMove the on tertiary click mouse move
	 * @return the element builder
	 */
  public ElementBuilder interactOnTertiaryClickMouseMove(@Nonnull final String onTertiaryClickMouseMove) {
    interactAttributes.setOnTertiaryClickMouseMove(onTertiaryClickMouseMove);
    return this;
  }

  /**
	 * Interact on tertiary multi click.
	 *
	 * @param onTertiaryMultiClick the on tertiary multi click
	 * @return the element builder
	 */
  public ElementBuilder interactOnTertiaryMultiClick(@Nonnull String onTertiaryMultiClick){
    interactAttributes.setOnTertiaryMultiClick(onTertiaryMultiClick);
    return this;
  }
  
  /**
	 * Interact on mouse over.
	 *
	 * @param onMouseOver the on mouse over
	 * @return the element builder
	 */
  public ElementBuilder interactOnMouseOver(@Nonnull final String onMouseOver) {
    interactAttributes.setOnMouseOver(onMouseOver);
    return this;
  }

  /**
	 * Interact on mouse wheel.
	 *
	 * @param onMouseWheel the on mouse wheel
	 * @return the element builder
	 */
  public ElementBuilder interactOnMouseWheel(@Nonnull final String onMouseWheel) {
    interactAttributes.setOnMouseWheel(onMouseWheel);
    return this;
  }

  /**
	 * Interact on click alternate key.
	 *
	 * @param onClickAlternateKey the on click alternate key
	 * @return the element builder
	 */
  public ElementBuilder interactOnClickAlternateKey(@Nonnull final String onClickAlternateKey) {
    interactAttributes.setOnClickAlternateKey(onClickAlternateKey);
    return this;
  }

  /**
	 * Gets the element builders.
	 *
	 * @return the element builders
	 */
  public List<ElementBuilder> getElementBuilders() {
    return Collections.unmodifiableList(elementBuilders);
  }

  /**
	 * Build a element.
	 *
	 * @param parent the parent
	 * @return the element created
	 */
  public Element build(@Nonnull final Element parent) {
    ElementType type = buildElementType();
    Element result = parent.getNifty().createElementFromType(parent.getScreen(), parent, type);
    parent.layoutElements();
    return result;
  }

  /**
	 * Build an element in a specified position in parent element list.
	 *
	 * @param parent the parent
	 * @param index  the index
	 * @return the Element created
	 */
  @Nonnull
  public Element build(@Nonnull final Element parent, final int index) {
    Screen screen = parent.getScreen();
    ElementType type = buildElementType();
    Element result = parent.getNifty().createElementFromType(screen, parent, type, index);
    screen.layoutLayers();
    return result;
  }

  /**
	 * Build an element after a element in parent children list.
	 *
	 * @param parent the parent
	 * @param before the before
	 * @return the Element created
	 */
  @Nonnull
  public Element build(@Nonnull final Element parent, @Nullable final Element before) {
    //find the index of 'before' element
    List<Element> parentList = parent.getChildren();
    int index = parentList.size();
    for (int i = 0; i < parentList.size(); i++) {
      if (parentList.get(i).equals(before)) {
        index = i;
        break;
      }
    }

    return this.build(parent, index);
  }

  /**
	 * Build a element.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 * @param parent the parent
	 * @return the element created
	 */
  @Deprecated
  public Element build(@Nonnull final Nifty nifty, @Nonnull final Screen screen, @Nonnull final Element parent) {
    return build(parent);
  }

  /**
	 * Build an element in a specified position in parent element list.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 * @param parent the parent
	 * @param index  the index
	 * @return the Element created
	 */
  @Deprecated
  @Nonnull
  public Element build(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent,
      final int index) {
    return build(parent, index);
  }

  /**
	 * Build an element after a element in parent children list.
	 *
	 * @param nifty  the nifty
	 * @param screen the screen
	 * @param parent the parent
	 * @param before the before
	 * @return the Element created
	 */
  @Deprecated
  @Nonnull
  public Element build(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent,
      @Nullable final Element before) {
    return build(parent, before);
  }

  /**
   * This method is called whenever we need the ElementType instead of a real
   * Element instance. This is currently used for ControlDefinition and Popup
   * registering dynamically from Java using the Builder pattern.
   * <p/>
   * It is not used for the general Java builder call that generates real instances.
   *
   * @return the ElementType representation for this ElementBuilder
   */
  @Nullable
  public ElementType buildElementType() {
    connectAttributes();
    ElementType thisType = attributes.createType();
    if (thisType == null) {
      return null;
    }

    // this is quite complicated: when we use the builder stuff then all of the
    // builders will create automatically an id for the element it builds. this
    // is a required feature when the builders are used to create actual elements.
    //
    // in this case here we're creating a type and not an actual element instance.
    // this is used for instance in controldefinitions. therefore we need to make
    // sure that the automatically generated id is being removed again. otherwise
    // we would end up with controldefinitions with ids. when we later use these
    // control definitions in multiple controls these ids will be reused which
    // could cause trouble when we have the same id multiple times.
    //
    // so here we make sure that when we have an automatically generated id
    // that we remove it again.
    if (attributes.isAutoId()) {
      thisType.getAttributes().remove("id");
    }

    attributes.connect(thisType);
    thisType.attachController(controller);
    for (int i = 0; i < elementBuilders.size(); i++) {
      ElementType newType = elementBuilders.get(i).buildElementType();
      if (newType != null) {
        thisType.addElementType(newType);
      }
    }

    return thisType;
  }

  /**
	 * Connect attributes.
	 */
  private void connectAttributes() {
    attributes.setEffects(createEffects());
    for (EffectEventId eventId : EffectEventId.values()) {
      if (effectBuilders.isSet(eventId)) {
        Collection<EffectBuilder> builders = effectBuilders.get(eventId);
        for (EffectBuilder builder : builders) {
          attributes.addEffects(eventId, builder.getAttributes());
        }
      }
    }
  }

  /**
	 * Creates the effects.
	 *
	 * @return the control effects attributes
	 */
  @Nonnull
  private ControlEffectsAttributes createEffects() {
    return new ControlEffectsAttributes();
  }
}
