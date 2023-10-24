package org.jglfont.impl.format.angelcode;

import java.util.Hashtable;
import java.util.Map;

import org.jglfont.impl.format.angelcode.line.CharLine;
import org.jglfont.impl.format.angelcode.line.CharsLine;
import org.jglfont.impl.format.angelcode.line.CommonLine;
import org.jglfont.impl.format.angelcode.line.InfoLine;
import org.jglfont.impl.format.angelcode.line.KerningLine;
import org.jglfont.impl.format.angelcode.line.KerningsLine;
import org.jglfont.impl.format.angelcode.line.PageLine;


/**
 * The Class AngelCodeLineProcessors.
 */
public class AngelCodeLineProcessors {
  
  /** The line processors. */
  private Map<String, AngelCodeLine> lineProcessors = new Hashtable<String, AngelCodeLine>();

  /**
	 * Instantiates a new angel code line processors.
	 */
  public AngelCodeLineProcessors() {
    lineProcessors.put("char", new CharLine());
    lineProcessors.put("chars", new CharsLine());
    lineProcessors.put("common", new CommonLine());
    lineProcessors.put("info", new InfoLine());
    lineProcessors.put("kerning", new KerningLine());
    lineProcessors.put("kernings", new KerningsLine());
    lineProcessors.put("page", new PageLine());
  }

  /**
	 * Gets the.
	 *
	 * @param lineId the line id
	 * @return the angel code line
	 */
  public AngelCodeLine get(final String lineId) {
    return lineProcessors.get(lineId);
  }
}
