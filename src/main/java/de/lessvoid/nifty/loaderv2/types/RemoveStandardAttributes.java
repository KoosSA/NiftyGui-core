package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;

import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class RemoveStandardAttributes.
 */
public class RemoveStandardAttributes {
  
  /** The control attributes. */
  private final Attributes controlAttributes;

  /**
	 * Instantiates a new removes the standard attributes.
	 *
	 * @param controlAttributesParam the control attributes param
	 */
  public RemoveStandardAttributes(final Attributes controlAttributesParam) {
    controlAttributes = controlAttributesParam;
  }

  /**
	 * Removes the standard.
	 *
	 * @return the attributes
	 */
  @Nonnull
  public Attributes removeStandard() {
    Attributes attributes = new Attributes(controlAttributes);
    attributes.remove("id");
    attributes.remove("height");
    attributes.remove("width");
    attributes.remove("x");
    attributes.remove("y");
    attributes.remove("align");
    attributes.remove("valign");
    attributes.remove("paddingLeft");
    attributes.remove("paddingRight");
    attributes.remove("paddingTop");
    attributes.remove("paddingBottom");
    attributes.remove("padding");
    attributes.remove("marginLeft");
    attributes.remove("marginRight");
    attributes.remove("marginTop");
    attributes.remove("marginBottom");
    attributes.remove("margin");
    attributes.remove("childClip");
    attributes.remove("visible");
    attributes.remove("visibleToMouse");
    attributes.remove("childLayout");
    attributes.remove("focusable");
    attributes.remove("filename");
    attributes.remove("filter");
    attributes.remove("imageMode");
    attributes.remove("inset");
    attributes.remove("backgroundColor");
    attributes.remove("backgroundImage");
    attributes.remove("font");
    attributes.remove("textHAlign");
    attributes.remove("textVAlign");
    attributes.remove("color");
    attributes.remove("selectionColor");
    attributes.remove("name");
    attributes.remove("renderOrder");
    return attributes;
  }
}
