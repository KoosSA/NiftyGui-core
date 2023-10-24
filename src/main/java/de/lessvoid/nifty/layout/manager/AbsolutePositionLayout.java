package de.lessvoid.nifty.layout.manager;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * AbsolutePositionLayout doesn't layout things. It just
 * absolute position it according to the constraints.
 *
 * @author void
 */
public class AbsolutePositionLayout implements LayoutManager {
  
  /** The Constant defaultPostProcess. */
  @Nonnull
  private static final DefaultPostProcess defaultPostProcess = new DefaultPostProcess();
  
  /** The post. */
  @Nonnull
  private final PostProcess post;

  /**
	 * Instantiates a new absolute position layout.
	 */
  public AbsolutePositionLayout() {
    this.post = defaultPostProcess;
  }

  /**
	 * Instantiates a new absolute position layout.
	 *
	 * @param post the post
	 */
  public AbsolutePositionLayout(@Nonnull final PostProcess post) {
    this.post = post;
  }

  /**
   * layoutElements.
   *
   * @param rootElement @see {@link LayoutManager}
   * @param elements    @see {@link LayoutManager}
   */
  @Override
  public void layoutElements(
      @Nullable final LayoutPart rootElement,
      @Nullable final List<LayoutPart> elements) {

    // make the params any sense?
    if (rootElement == null || elements == null || elements.size() == 0) {
      return;
    }

    // get the root box
    int rootBoxX = getRootBoxX(rootElement);
    int rootBoxY = getRootBoxY(rootElement);
    int rootBoxWidth = getRootBoxWidth(rootElement);
    int rootBoxHeight = getRootBoxHeight(rootElement);

    // now do the layout
    for (int i = 0; i < elements.size(); i++) {
      LayoutPart p = elements.get(i);
      Box box = p.getBox();
      BoxConstraints cons = p.getBoxConstraints();

      // makes only sense with constraints given
      if (cons.getX().hasValue()) {
        box.setX(rootBoxX + cons.getX().getValueAsInt(rootBoxWidth));
      }else{
      	box.setX(0);//to handle when a previous setX was done and you want to set back to default
      }
      if (cons.getY().hasValue()) {
        box.setY(rootBoxY + cons.getY().getValueAsInt(rootBoxHeight));
      }else{
      	box.setY(0); //to handle when a previous setY was done and you want to set back to default
      }

      if (cons.getWidth().hasHeightSuffix()) {
        if (cons.getHeight().hasValue()) {
          box.setHeight(cons.getHeight().getValueAsInt(rootBoxHeight));
        }
        box.setWidth(cons.getWidth().getValueAsInt(box.getHeight()));
      } else if (cons.getHeight().hasWidthSuffix()) {
        if (cons.getWidth().hasValue()) {
          box.setWidth(cons.getWidth().getValueAsInt(rootBoxWidth));
        }
        box.setHeight(cons.getHeight().getValueAsInt(box.getWidth()));
      } else {
        if (cons.getWidth().hasValue()) {
          box.setWidth(cons.getWidth().getValueAsInt(rootBoxWidth));
        }else{
      	  box.setWidth(0);
        }
      	  
        if (cons.getHeight().hasValue()) {
          box.setHeight(cons.getHeight().getValueAsInt(rootBoxHeight));
        }else{
      	 box.setHeight(0);
        }
      }

      post.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    }
  }

  /**
	 * Calculate constraint width.
	 *
	 * @param root     the root
	 * @param children children elements of the root element
	 * @return new calculated SizeValue
	 */
  @Nonnull
  @Override
  public final SizeValue calculateConstraintWidth(
      @Nonnull final LayoutPart root,
      @Nonnull final List<LayoutPart> children) {
    return SizeValue.def();
  }

  /**
	 * Calculate constraint height.
	 *
	 * @param root     the root
	 * @param children children elements of the root element
	 * @return new calculated SizeValue
	 */
  @Nonnull
  @Override
  public final SizeValue calculateConstraintHeight(
      @Nonnull final LayoutPart root,
      @Nonnull final List<LayoutPart> children) {
    return SizeValue.def();
  }

  /**
	 * Gets the root box X.
	 *
	 * @param root the root
	 * @return the root box X
	 */
  private int getRootBoxX(@Nonnull final LayoutPart root) {
    return root.getBox().getX() + root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getBox().getWidth());
  }

  /**
	 * Gets the root box Y.
	 *
	 * @param root the root
	 * @return the root box Y
	 */
  private int getRootBoxY(@Nonnull final LayoutPart root) {
    return root.getBox().getY() + root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight());
  }

  /**
	 * Gets the root box width.
	 *
	 * @param root the root
	 * @return the root box width
	 */
  private int getRootBoxWidth(@Nonnull final LayoutPart root) {
    return root.getBox().getWidth() - root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getBox().getWidth
        ()) - root.getBoxConstraints().getPaddingRight().getValueAsInt(root.getBox().getWidth());
  }

  /**
	 * Gets the root box height.
	 *
	 * @param root the root
	 * @return the root box height
	 */
  private int getRootBoxHeight(@Nonnull final LayoutPart root) {
    return root.getBox().getHeight() - root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight
        ()) - root.getBoxConstraints().getPaddingBottom().getValueAsInt(root.getBox().getHeight());
  }

  /**
	 * The Interface PostProcess.
	 */
  public interface PostProcess {
    
    /**
	 * Process.
	 *
	 * @param rootBoxX      the root box X
	 * @param rootBoxY      the root box Y
	 * @param rootBoxWidth  the root box width
	 * @param rootBoxHeight the root box height
	 * @param box           the box
	 */
    void process(int rootBoxX, int rootBoxY, int rootBoxWidth, int rootBoxHeight, @Nonnull Box box);
  }

  /**
	 * The Class DefaultPostProcess.
	 */
  public static class DefaultPostProcess implements PostProcess {
    
    /**
	 * Process.
	 *
	 * @param rootBoxX      the root box X
	 * @param rootBoxY      the root box Y
	 * @param rootBoxWidth  the root box width
	 * @param rootBoxHeight the root box height
	 * @param box           the box
	 */
    @Override
    public void process(
        final int rootBoxX,
        final int rootBoxY,
        final int rootBoxWidth,
        final int rootBoxHeight,
        @Nonnull final Box box) {
    }
  }

  /**
	 * The Class KeepInsidePostProcess.
	 */
  public static class KeepInsidePostProcess implements PostProcess {
    
    /**
	 * Process.
	 *
	 * @param rootBoxX      the root box X
	 * @param rootBoxY      the root box Y
	 * @param rootBoxWidth  the root box width
	 * @param rootBoxHeight the root box height
	 * @param box           the box
	 */
    @Override
    public void process(
        final int rootBoxX,
        final int rootBoxY,
        final int rootBoxWidth,
        final int rootBoxHeight,
        @Nonnull final Box box) {
      // first make sure width and height fit into the root box
      if (box.getWidth() > rootBoxWidth) {
        box.setWidth(rootBoxWidth);
      }
      if (box.getHeight() > rootBoxHeight) {
        box.setHeight(rootBoxHeight);
      }

      // and now make sure the box fits the root box
      if (box.getX() < rootBoxX) {
        box.setX(rootBoxX);
      }
      if (box.getY() < rootBoxY) {
        box.setY(rootBoxY);
      }
      if ((box.getX() + box.getWidth()) > (rootBoxX + rootBoxWidth)) {
        box.setX(rootBoxX + rootBoxWidth - box.getWidth());
      }
      if ((box.getY() + box.getHeight()) > (rootBoxY + rootBoxHeight)) {
        box.setY(rootBoxY + rootBoxHeight - box.getHeight());
      }
    }
  }
}
