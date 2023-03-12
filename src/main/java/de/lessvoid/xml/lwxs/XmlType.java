package de.lessvoid.xml.lwxs;

import javax.annotation.Nonnull;

import de.lessvoid.xml.xpp3.Attributes;

public interface XmlType {
  void applyAttributes(@Nonnull Attributes attributes);
}
