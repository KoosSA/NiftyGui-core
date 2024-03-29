package org.jglfont.impl.format.angelcode.line;

import org.jglfont.impl.format.JGLAbstractFontData;
import org.jglfont.impl.format.angelcode.AngelCodeLine;
import org.jglfont.impl.format.angelcode.AngelCodeLineData;

/**
 * InfoLine.
 *
 * @author void
 */
public class InfoLine implements AngelCodeLine {

  /**
	 * Process.
	 *
	 * @param line the line
	 * @param font the font
	 * @return true, if successful
	 */
  @Override
  public boolean process(final AngelCodeLineData line, final JGLAbstractFontData font) {
    if (!line.hasValue("face")) {
      return false;
    }
    font.setName(line.getString("face"));
    return true;
  }
}