package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.tools.LinearInterpolator;
import de.lessvoid.xml.xpp3.Attributes;

public class EffectPropertiesValues {
  @Nonnull
  private final List<Attributes> values = new ArrayList<Attributes>();

  public void add(final Attributes p) {
    values.add(p);
  }

  @Nonnull
  public List<Attributes> getValues() {
    return values;
  }

  @Nullable
  public LinearInterpolator toLinearInterpolator() {
    if (values.isEmpty()) {
      return null;
    }
    if (!containsTimeValues()) {
      return null;
    }
    LinearInterpolator interpolator = new LinearInterpolator();
    for (Attributes p : values) {
      interpolator.addPoint(p.getAsFloat("time"), p.getAsFloat("value"));
    }
    return interpolator;
  }

  public boolean containsTimeValues() {
    if (values.isEmpty()) {
      return false;
    }
    for (Attributes p : values) {
      if (p.isSet("time")) {
        return true;
      }
    }
    return false;
  }
}
