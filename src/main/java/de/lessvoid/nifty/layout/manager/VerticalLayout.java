package de.lessvoid.nifty.layout.manager;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * A VerticalLayout implementation of the LayoutManager interface.
 * The children elements are arranged in a vertical form
 * in relation to the root element.
 *
 * @author void
 */
public class VerticalLayout implements LayoutManager {

  @Override
  public void layoutElements(@Nonnull final LayoutPart root, @Nonnull final List<LayoutPart> children) {
    if (isInvalid(root, children)) {
      return;
    }

    int rootBoxX = getRootBoxX(root);
    int rootBoxY = getRootBoxY(root);
    int rootBoxWidth = getRootBoxWidth(root);
    int rootBoxHeight = getRootBoxHeight(root);

    int y = rootBoxY;
    for (int i = 0; i < children.size(); i++) {
      Box currentBox = children.get(i).getBox();
      BoxConstraints currentBoxConstraints = children.get(i).getBoxConstraints();

      int elementHeight;

      if (hasHeightConstraint(currentBoxConstraints) && currentBoxConstraints.getHeight().hasWidthSuffix()) {
        int elementWidth = processWidthConstraints(rootBoxWidth, currentBoxConstraints, 0);
        currentBox.setWidth(elementWidth);

        elementHeight = calcElementHeight(children, rootBoxHeight, currentBoxConstraints, elementWidth);
        currentBox.setHeight(elementHeight);
      } else if (hasWidthConstraint(currentBoxConstraints) && currentBoxConstraints.getWidth().hasHeightSuffix()) {
        elementHeight = calcElementHeight(children, rootBoxHeight, currentBoxConstraints, 0);
        currentBox.setHeight(elementHeight);

        int elementWidth = processWidthConstraints(rootBoxWidth, currentBoxConstraints, elementHeight);
        currentBox.setWidth(elementWidth);
      } else {
        int elementWidth = processWidthConstraints(rootBoxWidth, currentBoxConstraints, 0);
        currentBox.setWidth(elementWidth);

        elementHeight = calcElementHeight(children, rootBoxHeight, currentBoxConstraints, 0);
        currentBox.setHeight(elementHeight);
      }

      int x = processHorizontalAlignment(rootBoxX, rootBoxWidth, currentBox.getWidth(), currentBoxConstraints);
      x = x + leftMargin(currentBoxConstraints, rootBoxWidth);
      currentBox.setX(x);

      y = y + topMargin(currentBoxConstraints, rootBoxHeight);
      currentBox.setY(y);

      y += elementHeight + bottomMargin(currentBoxConstraints, rootBoxHeight);
    }
  }

  private int leftMargin(@Nonnull final BoxConstraints boxConstraints, final int rootBoxWidth) {
    return boxConstraints.getMarginLeft().getValueAsInt(rootBoxWidth);
  }

  private int topMargin(@Nonnull final BoxConstraints boxConstraints, final int rootBoxHeight) {
    return boxConstraints.getMarginTop().getValueAsInt(rootBoxHeight);
  }

  private int bottomMargin(@Nonnull final BoxConstraints boxConstraints, final int rootBoxHeight) {
    return boxConstraints.getMarginBottom().getValueAsInt(rootBoxHeight);
  }

  @Nonnull
  @Override
  public SizeValue calculateConstraintWidth(@Nonnull final LayoutPart root, @Nonnull final List<LayoutPart> children) {
    return root.getMaxWidth(children);
  }

  @Nonnull
  @Override
  public SizeValue calculateConstraintHeight(@Nonnull final LayoutPart root, @Nonnull final List<LayoutPart> children) {
    return root.getSumHeight(children);
  }

  private int processWidthConstraints(
      final int rootBoxWidth,
      @Nonnull final BoxConstraints constraints,
      final int elementHeight) {
    if (hasWidthConstraint(constraints)) {
      if (constraints.getWidth().hasHeightSuffix()) {
        return constraints.getWidth().getValueAsInt(elementHeight);
      }
      return constraints.getWidth().getValueAsInt(rootBoxWidth);
    } else {
      return rootBoxWidth;
    }
  }

  private int processHorizontalAlignment(
      final int rootBoxX,
      final int rootBoxWidth,
      final int currentBoxWidth,
      @Nonnull final BoxConstraints constraints) {
    if (HorizontalAlign.center.equals(constraints.getHorizontalAlign())) {
      return rootBoxX + ((rootBoxWidth - currentBoxWidth) / 2);
    } else if (HorizontalAlign.right.equals(constraints.getHorizontalAlign())) {
      return rootBoxX + (rootBoxWidth - currentBoxWidth);
    } else if (HorizontalAlign.left.equals(constraints.getHorizontalAlign())) {
      return rootBoxX;
    } else {
      // default = same as left
      return rootBoxX;
    }
  }

  private int calcElementHeight(
      @Nonnull final List<LayoutPart> children,
      final int rootBoxHeight,
      @Nonnull final BoxConstraints boxConstraints,
      final int boxWidth) {
    if (hasHeightConstraint(boxConstraints)) {
      int h;
      if (boxConstraints.getHeight().hasWidthSuffix()) {
        h = boxConstraints.getHeight().getValueAsInt(boxWidth);
      } else {
        h = boxConstraints.getHeight().getValueAsInt(rootBoxHeight);
      }
      if (h != -1) {
        return h;
      }
    }
    return getMaxNonFixedHeight(children, rootBoxHeight);
  }

  private int getMaxNonFixedHeight(@Nonnull final List<LayoutPart> elements, final int parentHeight) {
    int maxFixedHeight = 0;
    int fixedCount = 0;

    for (int i = 0; i < elements.size(); i++) {
      LayoutPart p = elements.get(i);
      BoxConstraints original = p.getBoxConstraints();
      if (hasHeightConstraint(original)) {
        if (original.getHeight().hasValue()) {
          maxFixedHeight += original.getHeight().getValueAsInt(parentHeight);
          fixedCount++;
        }
      }
    }

    int notFixedCount = elements.size() - fixedCount;
    if (notFixedCount > 0) {
      return (parentHeight - maxFixedHeight) / notFixedCount;
    } else {
      return (parentHeight - maxFixedHeight);
    }
  }

  private boolean hasWidthConstraint(@Nullable final BoxConstraints constraint) {
    return constraint != null && constraint.getWidth().hasValue();
  }

  private boolean hasHeightConstraint(@Nullable final BoxConstraints boxConstraints) {
    return boxConstraints != null && boxConstraints.getHeight().hasValue();
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
