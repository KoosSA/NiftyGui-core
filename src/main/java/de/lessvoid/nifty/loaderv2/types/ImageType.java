package de.lessvoid.nifty.loaderv2.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Class ImageType.
 */
public class ImageType extends ElementType {
  
  /**
	 * Instantiates a new image type.
	 */
  public ImageType() {
    super();
  }

  /**
	 * Instantiates a new image type.
	 *
	 * @param src the src
	 */
  public ImageType(@Nonnull final ImageType src) {
    super(src);
  }

  /**
	 * Copy.
	 *
	 * @return the element type
	 */
  @Override
  @Nonnull
  public ElementType copy() {
    return new ImageType(this);
  }

  /**
	 * Instantiates a new image type.
	 *
	 * @param attributes the attributes
	 */
  public ImageType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  /**
	 * Make flat.
	 */
  @Override
  protected void makeFlat() {
    super.makeFlat();
    setTagName("<image>");
    setElementRendererCreator(new ElementRendererCreator() {
      @Override
      @Nonnull
      public ElementRenderer[] createElementRenderer(@Nonnull final Nifty nifty) {
        ElementRenderer[] renderer = new ElementRenderer[1];
        //NiftyImage niftyImage = null;
        //        String filename = getFilename();
        //        if (filename != null) {
        //          niftyImage = nifty.getRenderEngine().createImage(filename, false); // FIXME filter
        //        }
        //        renderer[0] = new ImageRenderer(niftyImage);
        renderer[0] = new ImageRenderer();
        return renderer;
      }
    });
  }

  //  public String output(final int offset) {
  //    return StringHelper.whitespace(offset) + "<image> " + super.output(offset);
  //  }

  //  public ElementRendererCreator getElementRendererBuilder() {
  //    return new ElementRendererCreator() {
  //      public ElementRenderer[] createElementRenderer(final Nifty nifty) {
  //        ElementRenderer[] renderer = new ElementRenderer[1];
  //        NiftyImage niftyImage = null;
  //        String filename = getFilename();
  //        if (filename != null) {
  //          niftyImage = nifty.getRenderEngine().createImage(filename, false); // FIXME filter
  //        }
  //        renderer[0] = new ImageRenderer(niftyImage);
  //        return renderer;
  //      }
  //    };
  //  }

  /**
	 * Gets the filename.
	 *
	 * @return the filename
	 */
  @Nullable
  private String getFilename() {
    return getAttributes().get("filename");
  }
}
