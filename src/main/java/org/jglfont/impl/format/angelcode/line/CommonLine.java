package org.jglfont.impl.format.angelcode.line;

import org.jglfont.impl.format.JGLAbstractFontData;
import org.jglfont.impl.format.angelcode.AngelCodeLine;
import org.jglfont.impl.format.angelcode.AngelCodeLineData;

/**
 * CommonLine.
 *
 * @author void
 */
public class CommonLine implements AngelCodeLine {

  /**
	 * Process.
	 *
	 * @param line the line
	 * @param font the font
	 * @return true, if successful
	 */
  @Override
  public boolean process(final AngelCodeLineData line, final JGLAbstractFontData font) {
    if (!line.hasValue("scaleW") &&
        !line.hasValue("scaleH")) {
      return false;
    }
    font.setBitmapWidth(line.getInt("scaleW"));
    font.setBitmapHeight(line.getInt("scaleH"));
    return true;
  }
}