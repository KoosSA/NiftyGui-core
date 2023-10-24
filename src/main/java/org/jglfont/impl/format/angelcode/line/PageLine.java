package org.jglfont.impl.format.angelcode.line;

import org.jglfont.impl.format.JGLAbstractFontData;
import org.jglfont.impl.format.angelcode.AngelCodeLine;
import org.jglfont.impl.format.angelcode.AngelCodeLineData;

/**
 * PageLine.
 *
 * @author void
 */
public class PageLine implements AngelCodeLine {

  /**
	 * Process.
	 *
	 * @param line the line
	 * @param font the font
	 * @return true, if successful
	 */
  @Override
  public boolean process(final AngelCodeLineData line, final JGLAbstractFontData font) {
    if (!line.hasValue("id") ||
        !line.hasValue("file")) {
      return false;
    }
    font.addBitmap(line.getInt("id"), line.getString("file"));
    return true;
  }
}