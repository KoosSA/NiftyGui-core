package de.lessvoid.xml.lwxs.processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Nonnull;

import org.xmlpull.v1.XmlPullParserFactory;

import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

/**
 * The Class IncludeProcessor.
 */
public class IncludeProcessor implements XmlProcessor {
  
  /** The types. */
  @Nonnull
  private final Map < String, Type > types;
  
  /** The parser factory. */
  @Nonnull
  private final XmlPullParserFactory parserFactory;
  
  /** The resource loader. */
  @Nonnull
  private final NiftyResourceLoader resourceLoader;

  /**
	 * Instantiates a new include processor.
	 *
	 * @param parserFactory  the parser factory
	 * @param resourceLoader the resource loader
	 * @param typesParam     the types param
	 */
  public IncludeProcessor(
      @Nonnull final XmlPullParserFactory parserFactory,
      @Nonnull final NiftyResourceLoader resourceLoader,
      @Nonnull final Map<String, Type> typesParam) {
    this.resourceLoader = resourceLoader;
    this.types = typesParam;
    this.parserFactory = parserFactory;
  }

  /**
	 * Process.
	 *
	 * @param xmlParser  the xml parser
	 * @param attributes the attributes
	 * @throws Exception the exception
	 */
  @Override
  public void process(
      @Nonnull final XmlParser xmlParser,
      @Nonnull final Attributes attributes) throws Exception {
    String filename = attributes.get("filename");
    if (filename == null) {
      return;
    }

    Schema niftyXmlSchema = new Schema(parserFactory, resourceLoader);
    XmlParser parser = new XmlParser(parserFactory.newPullParser());
    InputStream stream = resourceLoader.getResourceAsStream(filename);
    if (stream != null) {
      try {
        parser.read(stream);
        parser.nextTag();
        parser.required("nxs", niftyXmlSchema);

        types.putAll(niftyXmlSchema.getTypes());
        xmlParser.nextTag();
      } finally {
        try {
          stream.close();
        } catch (IOException ignored) {}
      }
    }
  }
}
