package de.lessvoid.nifty.layout.manager;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * A HorizontalLayout implementation of the LayoutManager interface.
 * The children elements are arranged in a horizontal form
 * in relation to the root element.
 *
 * @author void
 */
public class HorizontalLayout implements LayoutManager {

  /**
   * Layout the elements.
   *
   * @param root     the root element
   * @param children the children
   */
  @Override
  public final void layoutElements(@Nonnull final LayoutPart root, @Nonnull final List<LayoutPart> children) {
    if (isInvalid(root, children)) {
      return;
    }

    int rootBoxX = getRootBoxX(root);
    int rootBoxY = getRootBoxY(root);
    int rootBoxWidth = getRootBoxWidth(root);
    int rootBoxHeight = getRootBoxHeight(root);

    int x = rootBoxX;
    for (int i = 0; i < children.size(); i++) {
      LayoutPart current = children.get(i);
      Box box = current.getBox();
      BoxConstraints boxConstraints = current.getBoxConstraints();

      int elementWidth;
      if (boxConstraints.getWidth().hasHeightSuffix()) {
        int elementHeight = processHeightConstraint(rootBoxHeight, box, boxConstraints, 0);
        box.setHeight(elementHeight);

        elementWidth = calcElementWidth(children, rootBoxWidth, boxConstraints, elementHeight);
        box.setWidth(elementWidth);
      } else if (hasHeightConstraint(boxConstraints) && boxConstraints.getHeight().hasWidthSuffix()) {
        elementWidth = calcElementWidth(children, rootBoxWidth, boxConstraints, 0);
        box.setWidth(elementWidth);

        int elementHeight = processHeightConstraint(rootBoxHeight, box, boxConstraints, elementWidth);
        box.setHeight(elementHeight);
      } else {
        elementWidth = calcElementWidth(children, rootBoxWidth, boxConstraints, 0);
        box.setWidth(elementWidth);

        int elementHeight = processHeightConstraint(rootBoxHeight, box, boxConstraints, 0);
        box.setHeight(elementHeight);
      }

      int y = processVerticalAlignment(rootBoxY, rootBoxHeight, box, boxConstraints);
      y = y + topMargin(boxConstraints, rootBoxHeight);
      box.setY(y);

      x = x + leftMargin(boxConstraints, rootBoxWidth);
      box.setX(x);

      x += elementWidth + rightMargin(boxConstraints, rootBoxWidth);
    }
  }

  private int leftMargin(@Nonnull final BoxConstraints boxConstraints, final int rootBoxWidth) {
    return boxConstraints.getMarginLeft().getValueAsInt(rootBoxWidth);
  }

  private int rightMargin(@Nonnull final BoxConstraints boxConstraints, final int rootBoxWidth) {
    return boxConstraints.getMarginRight().getValueAsInt(rootBoxWidth);
  }

  private int topMargin(@Nonnull final BoxConstraints boxConstraints, final int rootBoxHeight) {
    return boxConstraints.getMarginTop().getValueAsInt(rootBoxHeight);
  }

  private int processHeightConstraint(
      final int rootBoxHeight,
      final Box box,
      @Nonnull final BoxConstraints constraint,
      final int elementWidth) {
    if (hasHeightConstraint(constraint)) {
      if (constraint.getHeight().hasWidthSuffix()) {
        return constraint.getHeight().getValueAsInt(elementWidth);
      }
      return constraint.getHeight().getValueAsInt(rootBoxHeight);
    } else {
      return rootBoxHeight;
    }
  }

  private boolean hasHeightConstraint(@Nullable final BoxConstraints constraint) {
    return constraint != null && constraint.getHeight().hasValue();
  }

  private int calcElementWidth(
      @Nonnull final List<LayoutPart> children,
      final int rootBoxWidth,
      @Nonnull final BoxConstraints boxConstraints,
      final int elementHeight) {
    if (boxConstraints.getWidth().hasValue()) {
      int h = boxConstraints.getWidth().getValueAsInt(rootBoxWidth);
      if (boxConstraints.getWidth().hasHeightSuffix()) {
        h = boxConstraints.getWidth().getValueAsInt(elementHeight);
      }
      if (h != -1) {
        return h;
      }
    }
    return getMaxNonFixedWidth(children, rootBoxWidth);
  }

  private int processVerticalAlignment(
      final int rootBoxY,
      final int rootBoxHeight,
      @Nonnull final Box box,
      @Nonnull final BoxConstraints boxConstraints) {
    if (VerticalAlign.center.equals(boxConstraints.getVerticalAlign())) {
      return rootBoxY + ((rootBoxHeight - box.getHeight()) / 2);
    } else if (VerticalAlign.top.equals(boxConstraints.getVerticalAlign())) {
      return rootBoxY;
    } else if (VerticalAlign.bottom.equals(boxConstraints.getVerticalAlign())) {
      return rootBoxY + (rootBoxHeight - box.getHeight());
    } else {
      // top is default in here
      return rootBoxY;
    }
  }

  /**
   * @param elements    the child elements the max width is going
   *                    to be calculated
   * @param parentWidth the width of the parent element
   * @return max non fixed width
   */
  private int getMaxNonFixedWidth(
      @Nonnull final List<LayoutPart> elements,
      final int parentWidth
  ) {
    int maxFixedWidth = 0;
    int fixedCount = 0;
    for (int i = 0; i < elements.size(); i++) {
      LayoutPart p = elements.get(i);
      BoxConstraints original = p.getBoxConstraints();

      if (original.getWidth().hasValue()) {
        maxFixedWidth += original.getWidth().getValueAsInt(parentWidth);
        fixedCount++;
      }

    }

    int notFixedCount = elements.size() - fixedCount;
    if (notFixedCount > 0) {
      return (parentWidth - maxFixedWidth) / notFixedCount;
    } else {
      return (parentWidth - maxFixedWidth);
    }
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  @Nonnull
  @Override
  public final SizeValue calculateConstraintWidth(
      @Nonnull final LayoutPart root,
      @Nonnull final List<LayoutPart> children) {
    return root.getSumWidth(children);
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  @Nonnull
  @Override
  public final SizeValue calculateConstraintHeight(
      @Nonnull final LayoutPart root,
      @Nonnull final List<LayoutPart> children) {
    return root.getMaxHeight(children);
  }

  private boolean isInvalid(@Nullable final LayoutPart root, @Nullable final List<LayoutPart> children) {
    return root == null || children == null || children.size() == 0;
  }

  private int getRootBoxX(@Nonnull final LayoutPart root) {
    return root.getBox().getX() + root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getBox().getWidth());
  }

  private int getRootBoxY(@Nonnull final LayoutPart root) {
    return root.getBox().getY() + root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight());
  }

  private int getRootBoxWidth(@Nonnull final LayoutPart root) {
    return root.getBox().getWidth() - root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getBox().getWidth
        ()) - root.getBoxConstraints().getPaddingRight().getValueAsInt(root.getBox().getWidth());
  }

  private int getRootBoxHeight(@Nonnull final LayoutPart root) {
    return root.getBox().getHeight() - root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight
        ()) - root.getBoxConstraints().getPaddingBottom().getValueAsInt(root.getBox().getHeight());
  }
}
