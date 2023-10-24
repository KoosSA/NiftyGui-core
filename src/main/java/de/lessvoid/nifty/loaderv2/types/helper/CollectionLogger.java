package de.lessvoid.nifty.loaderv2.types.helper;

import java.util.Collection;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.loaderv2.types.XmlBaseType;
import de.lessvoid.nifty.tools.StringHelper;

/**
 * The Class CollectionLogger.
 */
public final class CollectionLogger {
  
  /**
	 * Instantiates a new collection logger.
	 */
  private CollectionLogger() {
  }

  /**
	 * Out.
	 *
	 * @param offset     the offset
	 * @param collection the collection
	 * @param message    the message
	 * @return the string
	 */
  @Nonnull
  public static String out(
      final int offset,
      @Nonnull final Collection<? extends XmlBaseType> collection,
      final String message) {
    if (collection.isEmpty()) {
      return StringHelper.whitespace(offset) + "no children " + message;
    }
    StringBuilder result = new StringBuilder();
    result.append(StringHelper.whitespace(offset));
    result.append("children ");
    result.append(message);
    result.append(": ");
    result.append(collection.size());
    for (XmlBaseType type : collection) {
      result.append("\n");
      result.append(type.output(offset + 1));
    }
    return result.toString();
  }
}
