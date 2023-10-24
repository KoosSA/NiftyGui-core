package de.lessvoid.nifty.builder;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.loaderv2.types.StyleType;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class StyleBuilder.
 */
public class StyleBuilder {
  
  /** The style attributes. */
  @Nonnull
  private final Attributes styleAttributes = new Attributes();
  
  /** The attributes. */
  @Nonnull
  private final ControlAttributes attributes = new ControlAttributes();
  
  /** The on start screen. */
  @Nonnull
  private final Collection<EffectBuilder> onStartScreen = new ArrayList<EffectBuilder>();
  
  /** The on end screen. */
  @Nonnull
  private final Collection<EffectBuilder> onEndScreen = new ArrayList<EffectBuilder>();
  
  /** The on hover. */
  @Nonnull
  private final Collection<HoverEffectBuilder> onHover = new ArrayList<HoverEffectBuilder>();
  
  /** The on start hover. */
  @Nonnull
  private final Collection<HoverEffectBuilder> onStartHover = new ArrayList<HoverEffectBuilder>();
  
  /** The on end hover. */
  @Nonnull
  private final Collection<HoverEffectBuilder> onEndHover = new ArrayList<HoverEffectBuilder>();
  
  /** The on click. */
  @Nonnull
  private final Collection<EffectBuilder> onClick = new ArrayList<EffectBuilder>();
  
  /** The on focus. */
  @Nonnull
  private final Collection<EffectBuilder> onFocus = new ArrayList<EffectBuilder>();
  
  /** The on lost focus. */
  @Nonnull
  private final Collection<EffectBuilder> onLostFocus = new ArrayList<EffectBuilder>();
  
  /** The on get focus. */
  @Nonnull
  private final Collection<EffectBuilder> onGetFocus = new ArrayList<EffectBuilder>();
  
  /** The on active. */
  @Nonnull
  private final Collection<EffectBuilder> onActive = new ArrayList<EffectBuilder>();
  
  /** The on custom. */
  @Nonnull
  private final Collection<EffectBuilder> onCustom = new ArrayList<EffectBuilder>();
  
  /** The on show. */
  @Nonnull
  private final Collection<EffectBuilder> onShow = new ArrayList<EffectBuilder>();
  
  /** The on hide. */
  @Nonnull
  private final Collection<EffectBuilder> onHide = new ArrayList<EffectBuilder>();

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
    private final String layout;

    /**
	 * Instantiates a new child layout type.
	 *
	 * @param layout the layout
	 */
    private ChildLayoutType(final String layout) {
      this.layout = layout;
    }

    /**
	 * Gets the layout.
	 *
	 * @return the layout
	 */
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
    private final String align;

    /**
	 * Instantiates a new align.
	 *
	 * @param align the align
	 */
    private Align(final String align) {
      this.align = align;
    }

    /**
	 * Gets the layout.
	 *
	 * @return the layout
	 */
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
    private final String valign;

    /**
	 * Instantiates a new v align.
	 *
	 * @param valign the valign
	 */
    private VAlign(final String valign) {
      this.valign = valign;
    }

    /**
	 * Gets the layout.
	 *
	 * @return the layout
	 */
    public String getLayout() {
      return valign;
    }
  }

  // these two attributes are applied directly to the style (<style id="this" base="another">)

  /**
	 * Id.
	 *
	 * @param id the id
	 * @return the style builder
	 */
  public StyleBuilder id(@Nonnull final String id) {
    styleAttributes.set("id", id);
    return this;
  }

  /**
	 * Base.
	 *
	 * @param baseStyle the base style
	 * @return the style builder
	 */
  public StyleBuilder base(@Nonnull final String baseStyle) {
    styleAttributes.set("base", baseStyle);
    return this;
  }

  // all other attributes are applied to the attribute tag (<style><attributes .../></style>)

  /**
	 * Name.
	 *
	 * @param name the name
	 * @return the style builder
	 */
  public StyleBuilder name(@Nonnull final String name) {
    attributes.setName(name);
    return this;
  }

  /**
	 * Background color.
	 *
	 * @param backgroundColor the background color
	 * @return the style builder
	 */
  public StyleBuilder backgroundColor(@Nonnull final String backgroundColor) {
    attributes.setBackgroundColor(backgroundColor);
    return this;
  }

  /**
	 * Background color.
	 *
	 * @param backgroundColor the background color
	 * @return the style builder
	 */
  public StyleBuilder backgroundColor(@Nonnull final Color backgroundColor) {
    attributes.setBackgroundColor(backgroundColor.getColorString());
    return this;
  }

  /**
	 * Color.
	 *
	 * @param color the color
	 * @return the style builder
	 */
  public StyleBuilder color(@Nonnull final String color) {
    attributes.setColor(color);
    return this;
  }

  /**
	 * Color.
	 *
	 * @param color the color
	 * @return the style builder
	 */
  public StyleBuilder color(@Nonnull final Color color) {
    attributes.setColor(color.getColorString());
    return this;
  }

  /**
	 * Selection color.
	 *
	 * @param color the color
	 * @return the style builder
	 */
  public StyleBuilder selectionColor(@Nonnull final String color) {
    attributes.setSelectionColor(color);
    return this;
  }

  /**
	 * Selection color.
	 *
	 * @param color the color
	 * @return the style builder
	 */
  public StyleBuilder selectionColor(@Nonnull final Color color) {
    attributes.setSelectionColor(color.getColorString());
    return this;
  }

  /**
	 * Text.
	 *
	 * @param text the text
	 * @return the style builder
	 */
  public StyleBuilder text(@Nonnull final String text) {
    attributes.setText(text);
    return this;
  }

  /**
	 * Background image.
	 *
	 * @param backgroundImage the background image
	 * @return the style builder
	 */
  public StyleBuilder backgroundImage(@Nonnull final String backgroundImage) {
    attributes.setBackgroundImage(backgroundImage);
    return this;
  }

  /**
	 * Image mode.
	 *
	 * @param imageMode the image mode
	 * @return the style builder
	 */
  public StyleBuilder imageMode(@Nonnull final String imageMode) {
    attributes.setImageMode(imageMode);
    return this;
  }

  /**
	 * Inset.
	 *
	 * @param inset the inset
	 * @return the style builder
	 */
  public StyleBuilder inset(@Nonnull final String inset) {
    attributes.setInset(inset);
    return this;
  }

  /**
	 * Input mapping.
	 *
	 * @param inputMapping the input mapping
	 * @return the style builder
	 */
  public StyleBuilder inputMapping(@Nonnull final String inputMapping) {
    attributes.setInputMapping(inputMapping);
    return this;
  }

  /**
	 * Style.
	 *
	 * @param style the style
	 * @return the style builder
	 */
  public StyleBuilder style(@Nonnull final String style) {
    attributes.setStyle(style);
    return this;
  }

  /**
	 * Child layout.
	 *
	 * @param childLayout the child layout
	 * @return the style builder
	 */
  public StyleBuilder childLayout(@Nonnull final ChildLayoutType childLayout) {
    attributes.setChildLayout(childLayout.getLayout());
    return this;
  }

  /**
	 * Child layout vertical.
	 *
	 * @return the style builder
	 */
  public StyleBuilder childLayoutVertical() {
    return childLayout(ChildLayoutType.Vertical);
  }

  /**
	 * Child layout horizontal.
	 *
	 * @return the style builder
	 */
  public StyleBuilder childLayoutHorizontal() {
    return childLayout(ChildLayoutType.Horizontal);
  }

  /**
	 * Child layout center.
	 *
	 * @return the style builder
	 */
  public StyleBuilder childLayoutCenter() {
    return childLayout(ChildLayoutType.Center);
  }

  /**
	 * Child layout absolute.
	 *
	 * @return the style builder
	 */
  public StyleBuilder childLayoutAbsolute() {
    return childLayout(ChildLayoutType.Absolute);
  }

  /**
	 * Child layout overlay.
	 *
	 * @return the style builder
	 */
  public StyleBuilder childLayoutOverlay() {
    return childLayout(ChildLayoutType.Overlay);
  }

  /**
	 * Height.
	 *
	 * @param height the height
	 * @return the style builder
	 */
  public StyleBuilder height(@Nonnull final String height) {
    attributes.setHeight(height);
    return this;
  }

  /**
	 * Width.
	 *
	 * @param width the width
	 * @return the style builder
	 */
  public StyleBuilder width(@Nonnull final String width) {
    attributes.setWidth(width);
    return this;
  }

  /**
	 * X.
	 *
	 * @param x the x
	 * @return the style builder
	 */
  public StyleBuilder x(@Nonnull final String x) {
    attributes.setX(x);
    return this;
  }

  /**
	 * Y.
	 *
	 * @param y the y
	 * @return the style builder
	 */
  public StyleBuilder y(@Nonnull final String y) {
    attributes.setY(y);
    return this;
  }

  /**
	 * Child clip.
	 *
	 * @param childClip the child clip
	 * @return the style builder
	 */
  public StyleBuilder childClip(final boolean childClip) {
    attributes.setChildClip(String.valueOf(childClip));
    return this;
  }

  /**
	 * Render order.
	 *
	 * @param renderOrder the render order
	 * @return the style builder
	 */
  public StyleBuilder renderOrder(final int renderOrder) {
    attributes.setRenderOrder(renderOrder);
    return this;
  }

  /**
	 * Visible.
	 *
	 * @param visible the visible
	 * @return the style builder
	 */
  public StyleBuilder visible(final boolean visible) {
    attributes.setVisible(String.valueOf(visible));
    return this;
  }

  /**
	 * Focusable.
	 *
	 * @param focusable the focusable
	 * @return the style builder
	 */
  public StyleBuilder focusable(final boolean focusable) {
    attributes.setFocusable(String.valueOf(focusable));
    return this;
  }

  /**
	 * Text H align.
	 *
	 * @param align the align
	 * @return the style builder
	 */
  public StyleBuilder textHAlign(@Nonnull final Align align) {
    attributes.set("textHAlign", align.getLayout());
    return this;
  }

  /**
	 * Text H align left.
	 *
	 * @return the style builder
	 */
  public StyleBuilder textHAlignLeft() {
    return textHAlign(Align.Left);
  }

  /**
	 * Text H align right.
	 *
	 * @return the style builder
	 */
  public StyleBuilder textHAlignRight() {
    return textHAlign(Align.Right);
  }

  /**
	 * Text H align center.
	 *
	 * @return the style builder
	 */
  public StyleBuilder textHAlignCenter() {
    return textHAlign(Align.Center);
  }

  /**
	 * Text V align.
	 *
	 * @param valign the valign
	 * @return the style builder
	 */
  public StyleBuilder textVAlign(@Nonnull final VAlign valign) {
    attributes.set("textVAlign", valign.getLayout());
    return this;
  }

  /**
	 * Text V align top.
	 *
	 * @return the style builder
	 */
  public StyleBuilder textVAlignTop() {
    return textVAlign(VAlign.Top);
  }

  /**
	 * Text V align bottom.
	 *
	 * @return the style builder
	 */
  public StyleBuilder textVAlignBottom() {
    return textVAlign(VAlign.Bottom);
  }

  /**
	 * Text V align center.
	 *
	 * @return the style builder
	 */
  public StyleBuilder textVAlignCenter() {
    return textVAlign(VAlign.Center);
  }

  /**
	 * Align.
	 *
	 * @param align the align
	 * @return the style builder
	 */
  public StyleBuilder align(@Nonnull final Align align) {
    attributes.setAlign(align.getLayout());
    return this;
  }

  /**
	 * Align left.
	 *
	 * @return the style builder
	 */
  public StyleBuilder alignLeft() {
    return align(Align.Left);
  }

  /**
	 * Align right.
	 *
	 * @return the style builder
	 */
  public StyleBuilder alignRight() {
    return align(Align.Right);
  }

  /**
	 * Align center.
	 *
	 * @return the style builder
	 */
  public StyleBuilder alignCenter() {
    return align(Align.Center);
  }

  /**
	 * Valign.
	 *
	 * @param valign the valign
	 * @return the style builder
	 */
  public StyleBuilder valign(@Nonnull final VAlign valign) {
    attributes.setVAlign(valign.getLayout());
    return this;
  }

  /**
	 * Valign top.
	 *
	 * @return the style builder
	 */
  public StyleBuilder valignTop() {
    return valign(VAlign.Top);
  }

  /**
	 * Valign bottom.
	 *
	 * @return the style builder
	 */
  public StyleBuilder valignBottom() {
    return valign(VAlign.Bottom);
  }

  /**
	 * Valign center.
	 *
	 * @return the style builder
	 */
  public StyleBuilder valignCenter() {
    return valign(VAlign.Center);
  }

  /**
	 * Visible to mouse.
	 *
	 * @param visibleToMouse the visible to mouse
	 * @return the style builder
	 */
  public StyleBuilder visibleToMouse(final boolean visibleToMouse) {
    attributes.setVisibleToMouse(String.valueOf(visibleToMouse));
    return this;
  }

  /**
	 * Visible to mouse.
	 *
	 * @return the style builder
	 */
  public StyleBuilder visibleToMouse() {
    return visibleToMouse(true);
  }

  /**
	 * Invisible to mouse.
	 *
	 * @return the style builder
	 */
  public StyleBuilder invisibleToMouse() {
    return visibleToMouse(false);
  }

  /**
	 * Font.
	 *
	 * @param font the font
	 * @return the style builder
	 */
  public StyleBuilder font(@Nonnull final String font) {
    attributes.setFont(font);
    return this;
  }

  /**
	 * Filename.
	 *
	 * @param filename the filename
	 * @return the style builder
	 */
  public StyleBuilder filename(@Nonnull final String filename) {
    attributes.setFilename(filename);
    return this;
  }

  /**
	 * Padding.
	 *
	 * @param padding the padding
	 * @return the style builder
	 */
  public StyleBuilder padding(@Nonnull final String padding) {
    attributes.setPadding(padding);
    return this;
  }

  /**
	 * Padding left.
	 *
	 * @param padding the padding
	 * @return the style builder
	 */
  public StyleBuilder paddingLeft(@Nonnull final String padding) {
    attributes.setPaddingLeft(padding);
    return this;
  }

  /**
	 * Padding right.
	 *
	 * @param padding the padding
	 * @return the style builder
	 */
  public StyleBuilder paddingRight(@Nonnull final String padding) {
    attributes.setPaddingRight(padding);
    return this;
  }

  /**
	 * Padding top.
	 *
	 * @param padding the padding
	 * @return the style builder
	 */
  public StyleBuilder paddingTop(@Nonnull final String padding) {
    attributes.setPaddingTop(padding);
    return this;
  }

  /**
	 * Padding bottom.
	 *
	 * @param padding the padding
	 * @return the style builder
	 */
  public StyleBuilder paddingBottom(@Nonnull final String padding) {
    attributes.setPaddingBottom(padding);
    return this;
  }

  /**
	 * Margin.
	 *
	 * @param margin the margin
	 * @return the style builder
	 */
  public StyleBuilder margin(@Nonnull final String margin) {
    attributes.setMargin(margin);
    return this;
  }

  /**
	 * Margin left.
	 *
	 * @param margin the margin
	 * @return the style builder
	 */
  public StyleBuilder marginLeft(@Nonnull final String margin) {
    attributes.setMarginLeft(margin);
    return this;
  }

  /**
	 * Margin right.
	 *
	 * @param margin the margin
	 * @return the style builder
	 */
  public StyleBuilder marginRight(@Nonnull final String margin) {
    attributes.setMarginRight(margin);
    return this;
  }

  /**
	 * Margin top.
	 *
	 * @param margin the margin
	 * @return the style builder
	 */
  public StyleBuilder marginTop(@Nonnull final String margin) {
    attributes.setMarginTop(margin);
    return this;
  }

  /**
	 * Margin bottom.
	 *
	 * @param margin the margin
	 * @return the style builder
	 */
  public StyleBuilder marginBottom(@Nonnull final String margin) {
    attributes.setMarginBottom(margin);
    return this;
  }

  /**
	 * Sets the.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the style builder
	 */
  public StyleBuilder set(@Nonnull final String key, @Nonnull final String value) {
    attributes.set(key, value);
    return this;
  }

  /**
	 * On start screen effect.
	 *
	 * @param onStartScreenEffect the on start screen effect
	 * @return the style builder
	 */
  public StyleBuilder onStartScreenEffect(final EffectBuilder onStartScreenEffect) {
    onStartScreen.add(onStartScreenEffect);
    return this;
  }

  /**
	 * On end screen effect.
	 *
	 * @param onEndScreenEffect the on end screen effect
	 * @return the style builder
	 */
  public StyleBuilder onEndScreenEffect(final EffectBuilder onEndScreenEffect) {
    onEndScreen.add(onEndScreenEffect);
    return this;
  }

  /**
	 * On hover effect.
	 *
	 * @param onHoverEffect the on hover effect
	 * @return the style builder
	 */
  public StyleBuilder onHoverEffect(final HoverEffectBuilder onHoverEffect) {
    onHover.add(onHoverEffect);
    return this;
  }

  /**
	 * On start hover effect.
	 *
	 * @param onStartHoverEffect the on start hover effect
	 * @return the style builder
	 */
  public StyleBuilder onStartHoverEffect(final HoverEffectBuilder onStartHoverEffect) {
    onStartHover.add(onStartHoverEffect);
    return this;
  }

  /**
	 * On end hover effect.
	 *
	 * @param onEndHoverEffect the on end hover effect
	 * @return the style builder
	 */
  public StyleBuilder onEndHoverEffect(final HoverEffectBuilder onEndHoverEffect) {
    onEndHover.add(onEndHoverEffect);
    return this;
  }

  /**
	 * On click effect.
	 *
	 * @param onClickEffect the on click effect
	 * @return the style builder
	 */
  public StyleBuilder onClickEffect(final EffectBuilder onClickEffect) {
    onClick.add(onClickEffect);
    return this;
  }

  /**
	 * On focus effect.
	 *
	 * @param onFocusEffect the on focus effect
	 * @return the style builder
	 */
  public StyleBuilder onFocusEffect(final EffectBuilder onFocusEffect) {
    onFocus.add(onFocusEffect);
    return this;
  }

  /**
	 * On lost focus effect.
	 *
	 * @param onLostFocusEffect the on lost focus effect
	 * @return the style builder
	 */
  public StyleBuilder onLostFocusEffect(final EffectBuilder onLostFocusEffect) {
    onLostFocus.add(onLostFocusEffect);
    return this;
  }

  /**
	 * On get focus effect.
	 *
	 * @param onGetFocusEffect the on get focus effect
	 * @return the style builder
	 */
  public StyleBuilder onGetFocusEffect(final EffectBuilder onGetFocusEffect) {
    onGetFocus.add(onGetFocusEffect);
    return this;
  }

  /**
	 * On active effect.
	 *
	 * @param onActiveEffect the on active effect
	 * @return the style builder
	 */
  public StyleBuilder onActiveEffect(final EffectBuilder onActiveEffect) {
    onActive.add(onActiveEffect);
    return this;
  }

  /**
	 * On show effect.
	 *
	 * @param onShowEffect the on show effect
	 * @return the style builder
	 */
  public StyleBuilder onShowEffect(final EffectBuilder onShowEffect) {
    onShow.add(onShowEffect);
    return this;
  }

  /**
	 * On hide effect.
	 *
	 * @param onHideEffect the on hide effect
	 * @return the style builder
	 */
  public StyleBuilder onHideEffect(final EffectBuilder onHideEffect) {
    onHide.add(onHideEffect);
    return this;
  }

  /**
	 * On custom effect.
	 *
	 * @param onCustomEffect the on custom effect
	 * @return the style builder
	 */
  public StyleBuilder onCustomEffect(final EffectBuilder onCustomEffect) {
    onCustom.add(onCustomEffect);
    return this;
  }

  /**
	 * Interact on click.
	 *
	 * @param method the method
	 * @return the style builder
	 */
  public StyleBuilder interactOnClick(@Nonnull String method) {
    attributes.setInteractOnClick(method);
    return this;
  }
  
  /**
	 * Interact on multi click.
	 *
	 * @param method the method
	 * @return the style builder
	 */
  public StyleBuilder interactOnMultiClick(@Nonnull String method){
    attributes.setIneractOnMultiClick(method);
    return this;
  }
  
  /**
	 * Interact on release.
	 *
	 * @param onRelease the on release
	 * @return the style builder
	 */
  public StyleBuilder interactOnRelease(@Nonnull final String onRelease) {
    attributes.setInteractOnRelease(onRelease);
    return this;
  }

  /**
	 * Interact on mouse over.
	 *
	 * @param onMouseOver the on mouse over
	 * @return the style builder
	 */
  public StyleBuilder interactOnMouseOver(@Nonnull final String onMouseOver) {
    attributes.setInteractOnMouseOver(onMouseOver);
    return this;
  }

  /**
	 * Interact on click repeat.
	 *
	 * @param onClickRepeat the on click repeat
	 * @return the style builder
	 */
  public StyleBuilder interactOnClickRepeat(@Nonnull final String onClickRepeat) {
    attributes.setInteractOnClickRepeat(onClickRepeat);
    return this;
  }

  /**
	 * Interact on click mouse move.
	 *
	 * @param onClickMouseMove the on click mouse move
	 * @return the style builder
	 */
  public StyleBuilder interactOnClickMouseMove(@Nonnull final String onClickMouseMove) {
    attributes.setInteractOnClickMouseMove(onClickMouseMove);
    return this;
  }

  /**
	 * Interact on click alternate key.
	 *
	 * @param onClickAlternateKey the on click alternate key
	 * @return the style builder
	 */
  public StyleBuilder interactOnClickAlternateKey(@Nonnull final String onClickAlternateKey) {
    attributes.setInteractOnClickAlternateKey(onClickAlternateKey);
    return this;
  }

  /**
	 * Percentage.
	 *
	 * @param percentage the percentage
	 * @return the string
	 */
  @Nonnull
  public String percentage(final int percentage) {
    return Integer.toString(percentage) + "%";
  }

  /**
	 * Pixels.
	 *
	 * @param px the px
	 * @return the string
	 */
  @Nonnull
  public String pixels(final int px) {
    return Integer.toString(px) + "px";
  }

  /**
	 * Connect attributes.
	 */
  private void connectAttributes() {
    attributes.setEffects(createEffects());
    for (EffectBuilder effectBuild : onStartScreen) {
      attributes.addEffectsOnStartScreen(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onEndScreen) {
      attributes.addEffectsOnEndScreen(effectBuild.getAttributes());
    }
    for (HoverEffectBuilder effectBuild : onHover) {
      attributes.addEffectsOnHover(effectBuild.getAttributes());
    }
    for (HoverEffectBuilder effectBuild : onStartHover) {
      attributes.addEffectsOnStartHover(effectBuild.getAttributes());
    }
    for (HoverEffectBuilder effectBuild : onEndHover) {
      attributes.addEffectsOnEndHover(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onClick) {
      attributes.addEffectsOnClick(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onFocus) {
      attributes.addEffectsOnFocus(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onLostFocus) {
      attributes.addEffectsOnLostFocus(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onGetFocus) {
      attributes.addEffectsOnGetFocus(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onActive) {
      attributes.addEffectsOnActive(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onCustom) {
      attributes.addEffectsOnCustom(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onShow) {
      attributes.addEffectsOnShow(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onHide) {
      attributes.addEffectsOnHide(effectBuild.getAttributes());
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

  /**
	 * Builds the.
	 *
	 * @param nifty the nifty
	 */
  public void build(@Nonnull final Nifty nifty) {
    connectAttributes();

    StyleType style = attributes.createStyleType(styleAttributes);
    nifty.registerStyle(style);
  }
}
