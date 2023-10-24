package de.lessvoid.nifty.loaderv2.types.apply;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class ApplyRenderText.
 */
public class ApplyRenderText implements ApplyRenderer {
  
  /** The convert. */
  private final Convert convert;

  /**
	 * Instantiates a new apply render text.
	 *
	 * @param convertParam the convert param
	 */
  public ApplyRenderText(final Convert convertParam) {
    convert = convertParam;
  }

  /**
	 * Apply.
	 *
	 * @param screen       the screen
	 * @param element      the element
	 * @param attributes   the attributes
	 * @param renderEngine the render engine
	 */
  @Override
  public void apply(
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Attributes attributes,
      @Nonnull final NiftyRenderEngine renderEngine) {
    TextRenderer textRenderer = element.getRenderer(TextRenderer.class);
    if (textRenderer == null) {
      return;
    }
    textRenderer.setFont(convert.font(renderEngine, attributes.get("font")));
    textRenderer.setTextHAlign(convert.textHorizontalAlign(attributes.get("textHAlign")));
    textRenderer.setTextVAlign(convert.textVerticalAlign(attributes.get("textVAlign")));
    textRenderer.setColor(convert.color(attributes.get("color"), textRenderer.getColor()));
    textRenderer.setTextSelectionColor(convert.color(attributes.get("selectionColor"),
        textRenderer.getTextSelectionColor()));
    textRenderer.setText(attributes.getOriginalValue("text"));
    textRenderer.setTextLineHeight(convert.sizeValue(attributes.get("textLineHeight")));
    textRenderer.setTextMinHeight(convert.sizeValue(attributes.get("textMinHeight")));
    boolean wrap = attributes.getAsBoolean("wrap", false);
    textRenderer.setLineWrapping(wrap);

    if (!wrap) {
      if (element.getConstraintWidth().hasDefault()) {
        element.setConstraintWidth(SizeValue.def(textRenderer.getTextWidth()));
      }
      if (element.getConstraintHeight().hasDefault()) {
        element.setConstraintHeight(SizeValue.def(textRenderer.getTextHeight()));
      }
    }
  }
}
