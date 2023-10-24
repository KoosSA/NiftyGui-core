package de.lessvoid.nifty.render.batch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jglfont.JGLFontFactory;
import org.jglfont.spi.JGLFontRenderer;
import org.jglfont.spi.ResourceLoader;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.ColorValueParser;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * BatchRenderDevice will try to reduce state changes by storing as many textures as possible into texture atlases, and
 * will try to render the whole GUI in as few draw calls as possible. In the best case scenario, the whole GUI will be
 * rendered in a single draw call.
 *
 * It will, however, create non-atlas textures for images that are impractical to store in the atlas because they would
 * take up too large of a percentage of area in the atlas. A good example would be fullscreen, high-res background
 * images, where a single 1920 x 1080 sized background image with padding would take up more than 50% of the space of
 * an empty texture atlas sized 2048 x 2048. These relatively large textures will each require an extra draw call, so
 * use them as sparingly as possible. Custom mouse cursors also currently each require a separate texture and therefore
 * a separate draw call, so use them as sparingly as possible.
 *
 * You can control which size of textures are added to an atlas by specifying a custom atlas size, padding, and
 * tolerance - see {@link #BatchRenderDevice(de.lessvoid.nifty.render.batch.spi.BatchRenderBackend, BatchRenderConfiguration)}.
 * Also, if you want to allow any size of texture to be put into an atlas just set atlas tolerance to 1.0f.
 *
 * You can also control whether the images for your {@link de.lessvoid.nifty.screen.Screen}'s will be disposed of when
 * the screen ends by setting {@code disposeImagesBetweenScreens} in {@link BatchRenderConfiguration}. The default is
 * {@code true}. This has a significant impact on performance during screen transitions. If you need lightning-fast
 * screen transitions, you may want to set this to {@code false}. The tradeoff is that not disposing of the previous
 * screen's images when transitioning to a new screen will take up more atlas space.
 *
 * If a texture atlas fills up to the point that textures within atlas tolerance are not fitting, another texture atlas
 * will be created to hold the overflow. As many texture atlases as are needed will be created, but it will start with
 * just one. There is a performance penalty for creating and using extra texture atlases, so use them as sparingly as
 * possible.
 *
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class BatchRenderDevice implements RenderDevice {
  
  /** The log. */
  @Nonnull
  private static Logger log = Logger.getLogger(BatchRenderDevice.class.getName());
  
  /** The render backend. */
  @Nonnull
  private final BatchRenderBackend renderBackend;
  
  /** The time provider. */
  @Nonnull
  private final TimeProvider timeProvider = new AccurateTimeProvider();
  
  /** The viewport width. */
  private int viewportWidth = -1;
  
  /** The viewport height. */
  private int viewportHeight = -1;
  
  /** The time. */
  private long time;
  
  /** The frames. */
  private long frames = 0;
  
  /** The glyph count. */
  private int glyphCount = 0;
  
  /** The quad count. */
  private int quadCount = 0;
  
  /** The current texture id. */
  private int currentTextureId = -1;
  
  /** The display FPS. */
  private boolean displayFPS = false;
  
  /** The log FPS. */
  private boolean logFPS = false;
  
  /** The should start new batch. */
  private boolean shouldStartNewBatch = true;
  
  /** The fps font. */
  @Nullable
  private RenderFont fpsFont = null;
  
  /** The plain image. */
  @Nullable
  private BatchRenderImage thePlainImage = null;
  
  /** The current blend mode. */
  @Nonnull
  private BlendMode currentBlendMode = BlendMode.BLEND;
  
  /** The buffer. */
  @Nonnull
  private StringBuilder buffer = new StringBuilder();
  
  /** The font color. */
  @Nonnull
  private Color fontColor = new Color("#f00");
  
  /** The font cache. */
  @Nonnull
  private final Set<BatchRenderFont> fontCache = new HashSet<BatchRenderFont>();
  
  /** The clipping. */
  @Nonnull
  private final Clipping clipping = new Clipping (0, 0, 0, 0, false);
  
  /** The original quad. */
  // 3 re-usable Rects used to temporarily store the results of clipping calculations.
  @Nonnull
  private Rect originalQuad = new Rect(0, 0, 0, 0);
  
  /** The clipped quad. */
  @Nonnull
  private Rect clippedQuad = new Rect(0, 0, 0, 0);
  
  /** The clipped quad texture. */
  @Nonnull
  private Rect clippedQuadTexture = new Rect(0, 0, 0, 0);
  
  /** The texture atlas generators. */
  @Nonnull
  private final Map<Integer, TextureAtlasGenerator> textureAtlasGenerators = new HashMap<Integer, TextureAtlasGenerator>();
  
  /** The image cache. */
  @Nonnull
  private final Map<String, BatchRenderImage> imageCache = new HashMap<String, BatchRenderImage>();
  
  /** The current texture size. */
  @Nullable
  private BatchRenderImage.TextureSize currentTextureSize = null;
  
  /** The font renderer. */
  @Nonnull
  private final FontRenderer fontRenderer;
  
  /** The factory. */
  @Nonnull
  private final JGLFontFactory factory;
  
  /** The current atlas texture id. */
  private int currentAtlasTextureId;
  
  /** The resource loader. */
  @Nonnull
  private NiftyResourceLoader resourceLoader;
  
  /** The render config. */
  @Nonnull
  private BatchRenderConfiguration renderConfig;
  
  /** The atlas texture ids. */
  @Nonnull
  private List<Integer> atlasTextureIds = new ArrayList<Integer>();
  
  /** The atlas texture id iterator. */
  @Nonnull
  private ListIterator<Integer> atlasTextureIdIterator = atlasTextureIds.listIterator();

  /**
	 * This is a convenience constructor that creates a BatchRenderDevice using the
	 * default values specified in
	 * {@link de.lessvoid.nifty.render.batch.BatchRenderConfiguration}, which should
	 * be fine for simple uses (including the Nifty examples) where you don't want
	 * to have to worry about fine tuning advanced settings.
	 *
	 * @param renderBackend the render backend
	 */
  public BatchRenderDevice(@Nonnull final BatchRenderBackend renderBackend) {
    this(renderBackend, new BatchRenderConfiguration());
  }

  /**
	 * This constructor is for more advanced users who wish to manually tune batch
	 * rendering for absolute maximum performance.
	 *
	 * @param renderBackend the render backend
	 * @param renderConfig  the render config
	 */
  public BatchRenderDevice(@Nonnull final BatchRenderBackend renderBackend, @Nonnull final BatchRenderConfiguration renderConfig) {
    log.setLevel(Level.WARNING);
	  this.renderBackend = renderBackend;
    renderBackend.useHighQualityTextures(renderConfig.useHighQualityTextures);
    renderBackend.fillRemovedImagesInAtlas(renderConfig.fillRemovedImagesInAtlas);
    this.renderConfig = renderConfig;
    time = timeProvider.getMsTime();
    fontRenderer = new FontRenderer(this);
    factory = new JGLFontFactory(fontRenderer, new ResourceLoader() {
      @Override
      public InputStream load(String path) {
        if (resourceLoader == null) {
          return null;
        }
        return resourceLoader.getResourceAsStream(path);
      }
    });
    createInitialTextureAtlases();
  }

  /**
   * Enable the logging of frames per second. This will impact performance. Don't use in production code.
   */
  public void enableLogFPS() {
    log.finest("enableLogFPS()");
    logFPS = true;
  }

  /**
	 * Enable the drawing of frames per second on the display surface. This will
	 * impact performance. Don't use in production code.
	 *
	 * @param newValue the new display FPS
	 */
  public void setDisplayFPS(final boolean newValue) {
    if (newValue != displayFPS) {
      displayFPS = newValue;
      if (resourceLoader != null && fpsFont == null && displayFPS) {
        fpsFont = createFont("fps.fnt");
      }
    }
  }

  /**
	 * Sets the resource loader.
	 *
	 * @param resourceLoader the new resource loader
	 */
  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
    log.finest("setResourceLoader()");
    this.resourceLoader = resourceLoader;

    if (this.displayFPS) {
      fpsFont = createFont("fps.fnt");
    }

    renderBackend.setResourceLoader(resourceLoader);
  }

  /**
	 * Gets the width.
	 *
	 * @return the width
	 */
  @Override
  public int getWidth() {
    log.finest("getWidth()");
    if (viewportWidth == -1) {
      getViewport();
    }
    return viewportWidth;
  }

  /**
	 * Gets the height.
	 *
	 * @return the height
	 */
  @Override
  public int getHeight() {
    log.finest("getHeight()");
    if (viewportHeight == -1) {
      getViewport();
    }
    return viewportHeight;
  }

  /**
	 * Gets the viewport.
	 *
	 * @return the viewport
	 */
  private void getViewport() {
    viewportWidth = renderBackend.getWidth();
    viewportHeight = renderBackend.getHeight();
  }

  /**
	 * Begin frame.
	 */
  @Override
  public void beginFrame() {
    log.finest("beginFrame()");
    renderBackend.beginFrame();
    currentBlendMode = BlendMode.BLEND;
    clipping.setEnabled(false);
    clipping.setToViewport();
    clipping.resetDiscardCount();
    shouldStartNewBatch = true;
    quadCount = 0;
    glyphCount = 0;
  }

  /**
	 * End frame.
	 */
  @Override
  public void endFrame() {
    log.finest("endFrame");
    log.fine("completely clipped elements: " + clipping.getDiscardCount());

    if (displayFPS && fpsFont != null) {
      renderFont(fpsFont, buffer.toString(), 10, getHeight() - fpsFont.getHeight() - 10, fontColor, 1.0f, 1.0f);
    }

    int batches = renderBackend.render();
    renderBackend.endFrame();

    frames++;
    long diff = timeProvider.getMsTime() - time;
    if (diff >= 1000) {
      time += diff;
      long lastFrames = frames;

      buffer.setLength(0);
      buffer.append("FPS: ");
      buffer.append(lastFrames);
      buffer.append(" (");
      buffer.append(String.format("%f", 1000.f / lastFrames));
      buffer.append(" ms)");
      buffer.append(", Total Tri: ");
      buffer.append(quadCount * 2);
      buffer.append(" (Text: ");
      buffer.append(glyphCount * 2);
      buffer.append(")");
      buffer.append(", Total Vert: ");
      buffer.append(quadCount * 4);
      buffer.append(" (Text: ");
      buffer.append(glyphCount * 4);
      buffer.append("), Batches: ");
      buffer.append(batches);

      if (logFPS) {
        System.out.println(buffer.toString());
      }

      frames = 0;
    }

    // currently the RenderDevice interface does not support a way to be notified when the resolution is changed
    // so we reset the viewportWidth and viewportHeight here so that we only call getViewport() once per frame and
    // not each time someone calls getWidth() or getHeight().
    viewportWidth = -1;
    viewportHeight = -1;
  }

  /**
	 * Clear.
	 */
  @Override
  public void clear() {
    log.finest("clear()");
    renderBackend.clear();
  }

  /**
	 * Creates the image.
	 *
	 * @param filename     the filename
	 * @param filterLinear the filter linear
	 * @return the render image
	 */
  @Nullable
  @Override
  public RenderImage createImage(@Nonnull final String filename, final boolean filterLinear) {
    if(!renderConfig.disposeImagesBetweenScreens && imageCache.containsKey(filename)) {
      return imageCache.get(filename);
    }
    log.finest("createImage()");
    BatchRenderImage batchRenderImage = new BatchRenderImage(
            renderBackend.loadImage(filename),
            filename,
            renderBackend,
            getCurrentTextureAtlasGenerator(),
            getCurrentAtlasTextureId(),
            renderConfig.disposeImagesBetweenScreens);
    if (!renderConfig.disposeImagesBetweenScreens) {
      imageCache.put(filename, batchRenderImage);
    }
    return batchRenderImage;
  }

  /**
	 * Creates the font.
	 *
	 * @param filename the filename
	 * @return the render font
	 */
  @Nonnull
  @Override
  public RenderFont createFont(@Nonnull final String filename) {
    log.finest("createFont()");
    if (resourceLoader == null) {
      throw new RuntimeException("Can't create font without ResourceLoader instance.");
    }
    try {
      BatchRenderFont batchRenderFont = new BatchRenderFont(this, filename, factory, resourceLoader);
      fontCache.add(batchRenderFont);
      return batchRenderFont;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
	 * Dispose font.
	 *
	 * @param batchRenderFont the batch render font
	 */
  public void disposeFont(final BatchRenderFont batchRenderFont) {
    fontCache.remove(batchRenderFont);
  }

  /**
	 * Render quad.
	 *
	 * @param x      the x
	 * @param y      the y
	 * @param width  the width
	 * @param height the height
	 * @param color  the color
	 */
  @Override
  public void renderQuad(
      final int x,
      final int y,
      final int width,
      final int height,
      @Nonnull final Color color) {
    log.finest("renderQuad()");
    BatchRenderImage plainImage = getPlainImage();
    addQuad(x,
            y,
            width,
            height,
            color,
            color,
            color,
            color,
            plainImage.getX(),
            plainImage.getY(),
            plainImage.getWidth(),
            plainImage.getHeight(),
            plainImage.getTextureId());
  }

  /**
	 * Render quad.
	 *
	 * @param x           the x
	 * @param y           the y
	 * @param width       the width
	 * @param height      the height
	 * @param topLeft     the top left
	 * @param topRight    the top right
	 * @param bottomRight the bottom right
	 * @param bottomLeft  the bottom left
	 */
  @Override
  public void renderQuad(
      final int x,
      final int y,
      final int width,
      final int height,
      @Nonnull final Color topLeft,
      @Nonnull final Color topRight,
      @Nonnull final Color bottomRight,
      @Nonnull final Color bottomLeft) {
    log.finest("renderQuad2()");
    BatchRenderImage plainImage = getPlainImage();
    addQuad(
        x,
        y,
        width,
        height,
        topLeft,
        topRight,
        bottomLeft,
        bottomRight,
        plainImage.getX(),
        plainImage.getY(),
        plainImage.getWidth(),
        plainImage.getHeight(),
        plainImage.getTextureId());
  }

  /**
	 * Render image.
	 *
	 * @param image  the image
	 * @param x      the x
	 * @param y      the y
	 * @param width  the width
	 * @param height the height
	 * @param c      the c
	 * @param scale  the scale
	 */
  @Override
  public void renderImage(
      @Nonnull final RenderImage image,
      final int x,
      final int y,
      final int width,
      final int height,
      @Nonnull final Color c,
      final float scale) {
    log.finest("renderImage()");
    if (width < 0) {
      log.warning("Attempted to render image with negative width");
      return;
    }
    if (height < 0) {
      log.warning("Attempted to render image with negative height");
      return;
    }
    BatchRenderImage img = (BatchRenderImage) image;
    uploadImageInternal(img);
    float centerX = x + width / 2.f;
    float centerY = y + height / 2.f;
    int ix = Math.round(centerX - (width * scale) / 2.f);
    int iy = Math.round(centerY - (height * scale) / 2.f);
    int iw = Math.round(width * scale);
    int ih = Math.round(height * scale);
    addQuad(ix, iy, iw, ih, c, c, c, c, img.getX(), img.getY(), img.getWidth(), img.getHeight(), img.getTextureId());
  }

  /**
	 * Render image.
	 *
	 * @param image   the image
	 * @param x       the x
	 * @param y       the y
	 * @param w       the w
	 * @param h       the h
	 * @param srcX    the src X
	 * @param srcY    the src Y
	 * @param srcW    the src W
	 * @param srcH    the src H
	 * @param c       the c
	 * @param scale   the scale
	 * @param centerX the center X
	 * @param centerY the center Y
	 */
  @Override
  public void renderImage(
      @Nonnull final RenderImage image,
      final int x,
      final int y,
      final int w,
      final int h,
      final int srcX,
      final int srcY,
      final int srcW,
      final int srcH,
      @Nonnull final Color c,
      final float scale,
      final int centerX,
      final int centerY) {
    log.finest("renderImage2()");
    if (w < 0) {
      log.warning("Attempted to render image with negative width");
      return;
    }
    if (h < 0) {
      log.warning("Attempted to render image with negative height");
      return;
    }
    int ix = Math.round(-scale * centerX + scale * x + centerX);
    int iy = Math.round(-scale * centerY + scale * y + centerY);
    int iw = Math.round(w * scale);
    int ih = Math.round(h * scale);
    BatchRenderImage img = (BatchRenderImage) image;
    uploadImageInternal(img);
    addQuad(ix, iy, iw, ih, c, c, c, c, img.getX() + srcX, img.getY() + srcY, srcW, srcH, img.getTextureId());
  }

  /**
	 * Render font.
	 *
	 * @param font  the font
	 * @param text  the text
	 * @param x     the x
	 * @param y     the y
	 * @param color the color
	 * @param sizeX the size X
	 * @param sizeY the size Y
	 */
  @Override
  public void renderFont(
      @Nonnull final RenderFont font,
      @Nonnull final String text,
      final int x,
      final int y,
      @Nonnull final Color color,
      final float sizeX,
      final float sizeY) {
    log.finest("renderFont()");
    BatchRenderFont renderFont = (BatchRenderFont) font;
    renderFont.getBitmapFont().renderText(
        x,
        y,
        text,
        sizeX,
        sizeY,
        color.getRed(),
        color.getGreen(),
        color.getBlue(),
        color.getAlpha());
  }

  /**
	 * Enable clip.
	 *
	 * @param x0 the x 0
	 * @param y0 the y 0
	 * @param x1 the x 1
	 * @param y1 the y 1
	 */
  @Override
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    log.finest("enableClip()");
    clipping.setEnabled(true);
    clipping.setBounds(x0, y0, x1, y1);
  }

  /**
	 * Disable clip.
	 */
  @Override
  public void disableClip() {
    log.finest("disableClip()");
    if (!clipping.isEnabled()) return;
    clipping.setEnabled(false);
    clipping.setToViewport();
  }

  /**
	 * Sets the blend mode.
	 *
	 * @param renderMode the new blend mode
	 */
  @Override
  public void setBlendMode(@Nonnull final BlendMode renderMode) {
    log.finest("setBlendMode()");
    if (renderMode.equals(currentBlendMode)) {
      return;
    }
    currentBlendMode = renderMode;
    shouldStartNewBatch = true;
  }

  /**
	 * Creates the mouse cursor.
	 *
	 * @param filename the filename
	 * @param hotspotX the hotspot X
	 * @param hotspotY the hotspot Y
	 * @return the mouse cursor
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  @Override
  public MouseCursor createMouseCursor(
      @Nonnull final String filename,
      final int hotspotX,
      final int hotspotY) throws IOException {
    log.finest("createMouseCursor()");
    return renderBackend.createMouseCursor(filename, hotspotX, hotspotY);
  }

  /**
	 * Enable mouse cursor.
	 *
	 * @param mouseCursor the mouse cursor
	 */
  @Override
  public void enableMouseCursor(@Nonnull final MouseCursor mouseCursor) {
    log.finest("enableMouseCursor()");
    renderBackend.enableMouseCursor(mouseCursor);
  }

  /**
	 * Disable mouse cursor.
	 */
  @Override
  public void disableMouseCursor() {
    log.finest("disableMouseCursor()");
    renderBackend.disableMouseCursor();
  }

  /**
	 * Reset texture atlases.
	 */
  public void resetTextureAtlases() {
    if (! renderConfig.disposeImagesBetweenScreens) {
      return;
    }
    log.finest("resetTextureAtlases()");
    if (thePlainImage != null) {
      thePlainImage.unload();
    }
    resetTextureAtlasGenerators();
    clearTextureAtlases();
    fontRenderer.unload();
  }

  // Internal implementations

  /**
	 * Creates the initial texture atlases.
	 */
  private void createInitialTextureAtlases() {
    for (int i = 0; i < renderConfig.initialAtlasCount; ++i) {
      createTextureAtlasGenerator(createTextureAtlas());
    }
    resetCurrentTextureAtlas();
  }

  /**
	 * Creates the texture atlas.
	 *
	 * @return the int
	 */
  private int createTextureAtlas() {
    int atlasTextureId = renderBackend.createTextureAtlas(renderConfig.atlasWidth, renderConfig.atlasHeight);
    log.info("Created a new texture atlas (atlas texture id: " + atlasTextureId + ").");
    atlasTextureIdIterator.add(atlasTextureId);
    createTextureAtlasGenerator(atlasTextureId);
    return atlasTextureId;
  }

  /**
	 * Creates the texture atlas generator.
	 *
	 * @param atlasTextureId the atlas texture id
	 */
  private void createTextureAtlasGenerator(final int atlasTextureId) {
    textureAtlasGenerators.put(atlasTextureId, new TextureAtlasGenerator(renderConfig.atlasWidth,
            renderConfig.atlasHeight, renderConfig.atlasPadding, renderConfig.atlasTolerance));
  }

  /**
	 * Reset current texture atlas.
	 */
  private void resetCurrentTextureAtlas() {
    rewindAtlasTextureIdIterator();
    currentAtlasTextureId = nextTextureAtlas();
  }

  /**
	 * Rewind atlas texture id iterator.
	 */
  private void rewindAtlasTextureIdIterator() {
    while(atlasTextureIdIterator.hasPrevious()) {
      atlasTextureIdIterator.previous();
    }
  }

  /**
	 * Gets the current texture atlas generator.
	 *
	 * @return the current texture atlas generator
	 */
  private TextureAtlasGenerator getCurrentTextureAtlasGenerator() {
    return textureAtlasGenerators.get(currentAtlasTextureId);
  }

  /**
	 * Gets the current atlas texture id.
	 *
	 * @return the current atlas texture id
	 */
  private int getCurrentAtlasTextureId() {
    return currentAtlasTextureId;
  }

  /**
	 * Next texture atlas.
	 *
	 * @return the int
	 */
  private int nextTextureAtlas() {
    currentAtlasTextureId = atlasTextureIdIterator.hasNext() ? atlasTextureIdIterator.next() : createTextureAtlas();
    log.info("Switched atlases to atlas texture with id: " + currentAtlasTextureId + ".");
    return currentAtlasTextureId;
  }

  /**
	 * Gets the plain image.
	 *
	 * @return the plain image
	 */
  @Nonnull
  private BatchRenderImage getPlainImage() {
    if (thePlainImage == null) {
      thePlainImage = (BatchRenderImage) createImage("de/lessvoid/nifty/render/batch/nifty.png", true);
      if (thePlainImage == null) {
        throw new RuntimeException("The batch renderer requires the plain image in the resources, but its not there.");
      }
    }
    uploadImageInternal(thePlainImage);
    return thePlainImage;
  }

  /**
	 * Upload image internal.
	 *
	 * @param image the image
	 */
  private void uploadImageInternal(final BatchRenderImage image) {
    if (image.isUploaded()) {
      return;
    }

    // First attempt
    image.upload();

    // Next attempt
    if (! image.isUploaded()) {
      reattemptUpload(image);
    }
  }

  /**
	 * Reattempt upload.
	 *
	 * @param image the image
	 */
  private void reattemptUpload(final BatchRenderImage image) {
    while (!image.isUploaded() && !image.uploadFailedPermanently()) {
      // We're still not uploaded, yet the upload has not failed permanently - there's hope!
      // Keep trying new texture atlases & re-attempting upload, until we either have success or permanent failure.
      nextTextureAtlas();
      image.reUpload(getCurrentAtlasTextureId(), getCurrentTextureAtlasGenerator());
    }

    // Go back to the first texture atlas for next time. Just because the last image may not have fit, doesn't mean a
    // smaller one won't come along and fill in any remaining gaps in a (mostly full) atlas...
    resetCurrentTextureAtlas();
  }

  /**
	 * Reset texture atlas generators.
	 */
  private void resetTextureAtlasGenerators() {
    for (TextureAtlasGenerator generator : textureAtlasGenerators.values()) {
      generator.reset();
    }
  }

  /**
	 * Clear texture atlases.
	 */
  private void clearTextureAtlases() {
    for (int atlasTextureId : atlasTextureIds) {
      renderBackend.clearTextureAtlas(atlasTextureId);
    }
    resetCurrentTextureAtlas();
  }

  /**
	 * Adds the quad.
	 *
	 * @param x             the x
	 * @param y             the y
	 * @param width         the width
	 * @param height        the height
	 * @param color1        the color 1
	 * @param color2        the color 2
	 * @param color3        the color 3
	 * @param color4        the color 4
	 * @param textureX      the texture X
	 * @param textureY      the texture Y
	 * @param textureWidth  the texture width
	 * @param textureHeight the texture height
	 * @param textureId     the texture id
	 */
  private void addQuad(
          final float x,
          final float y,
          final float width,
          final float height,
          @Nonnull final Color color1,
          @Nonnull final Color color2,
          @Nonnull final Color color3,
          @Nonnull final Color color4,
          final int textureX,
          final int textureY,
          final int textureWidth,
          final int textureHeight,
          final int textureId) {
    // if this quad is completely outside the clipping area we don't need to render it at all
    if (clipping.isCompletelyOutside((int) x, (int) y, (int) width, (int) height)) {
      clipping.incrementDiscardCounter();
      return;
    }

    // if this quad is completely inside the clipping area we can simply render the quad
    if (clipping.isCompletelyInside((int) x, (int) y, (int) width, (int) height)) {
      addQuadInternal(
          x,
          y,
          width,
          height,
          color1,
          color2,
          color3,
          color4,
          textureX,
          textureY,
          textureWidth,
          textureHeight,
          textureId);
      return;
    }

    // we need to clip
    originalQuad.set((int) x, (int) y, (int) width, (int) height);
    clippedQuad.set(clipping.clipQuad(originalQuad));
    clippedQuadTexture.set(clipping.clipQuadTexture(textureX, textureY, textureWidth, textureHeight, originalQuad, clippedQuad));

    addQuadInternal(
        (float) clippedQuad.x0,
        (float) clippedQuad.y0,
        (float) clippedQuad.getWidth(),
        (float) clippedQuad.getHeight(),
        color1,
        color2,
        color3,
        color4,
        clippedQuadTexture.x0,
        clippedQuadTexture.y0,
        clippedQuadTexture.getWidth(),
        clippedQuadTexture.getHeight(),
        textureId);
  }

  /**
	 * Adds the quad internal.
	 *
	 * @param x             the x
	 * @param y             the y
	 * @param width         the width
	 * @param height        the height
	 * @param color1        the color 1
	 * @param color2        the color 2
	 * @param color3        the color 3
	 * @param color4        the color 4
	 * @param textureX      the texture X
	 * @param textureY      the texture Y
	 * @param textureWidth  the texture width
	 * @param textureHeight the texture height
	 * @param textureId     the texture id
	 */
  private void addQuadInternal(
          final float x,
          final float y,
          final float width,
          final float height,
          @Nonnull final Color color1,
          @Nonnull final Color color2,
          @Nonnull final Color color3,
          @Nonnull final Color color4,
          final int textureX,
          final int textureY,
          final int textureWidth,
          final int textureHeight,
          final int textureId) {
    checkIfTextureChanged(textureId);
    beginNewBatchIfRequired();
    renderBackend.addQuad(
            x,
            y,
            width,
            height,
            color1,
            color2,
            color3,
            color4,
            calcU(textureX, getFullWidthOfCurrentTexture()),
            calcU(textureY, getFullHeightOfCurrentTexture()),
            calcU(textureWidth - 1, getFullWidthOfCurrentTexture()),
            calcU(textureHeight - 1, getFullHeightOfCurrentTexture()),
            textureId);
    quadCount++;
  }

  /**
	 * Check if texture changed.
	 *
	 * @param textureId the texture id
	 */
  private void checkIfTextureChanged(final int textureId) {
    if (!isCurrentTexture(textureId)) {
      updateCurrentTexture(textureId);
      shouldStartNewBatch = true;
    }
  }

  /**
	 * Begin new batch if required.
	 */
  private void beginNewBatchIfRequired() {
    if (shouldStartNewBatch) {
      renderBackend.beginBatch(currentBlendMode, currentTextureId);
      shouldStartNewBatch = false;
    }
  }

  /**
	 * Checks if is current texture.
	 *
	 * @param textureId the texture id
	 * @return true, if is current texture
	 */
  private boolean isCurrentTexture(final int textureId) {
    return textureId == currentTextureId;
  }

  /**
	 * Update current texture.
	 *
	 * @param textureId the texture id
	 */
  private void updateCurrentTexture(final int textureId) {
    currentTextureId = textureId;
    currentTextureSize = BatchRenderImage.getTextureSize(textureId);
    if (currentTextureSize == null) {
      log.severe("cannot get texture size of texture with id: " + textureId + "  because that texture id is not registered!");
    }
  }

  /**
	 * Calc U.
	 *
	 * @param value the value
	 * @param max   the max
	 * @return the float
	 */
  private float calcU(final int value, final int max) {
    return (0.5f / (float) max) + (value / (float) max);
  }

  /**
	 * Gets the full width of current texture.
	 *
	 * @return the full width of current texture
	 */
  private int getFullWidthOfCurrentTexture() {
    return currentTextureSize != null ? currentTextureSize.getWidth() : 0;
  }

  /**
	 * Gets the full height of current texture.
	 *
	 * @return the full height of current texture
	 */
  private int getFullHeightOfCurrentTexture() {
    return currentTextureSize != null ? currentTextureSize.getHeight() : 0;
  }

  /**
	 * The Class Clipping.
	 */
  private class Clipping {
    
    /** The bounding box. */
    private Rect boundingBox;
    
    /** The clipped rect. */
    private Rect clippedRect;
    
    /** The is enabled. */
    private boolean isEnabled;
    
    /** The discard count. */
    private int discardCount;

    /**
	 * Instantiates a new clipping.
	 *
	 * @param x0        the x 0
	 * @param y0        the y 0
	 * @param x1        the x 1
	 * @param y1        the y 1
	 * @param isEnabled the is enabled
	 */
    public Clipping(final int x0, final int y0, final int x1, final int y1, final boolean isEnabled) {
      boundingBox = new Rect (x0, y0, x1, y1);
      clippedRect = new Rect (0, 0, 0, 0);
      this.isEnabled = isEnabled;
      discardCount = 0;
    }

    /**
	 * Sets the bounds.
	 *
	 * @param x0 the x 0
	 * @param y0 the y 0
	 * @param x1 the x 1
	 * @param y1 the y 1
	 */
    public void setBounds(final int x0, final int y0, final int x1, final int y1) {
      boundingBox.x0 = x0;
      boundingBox.y0 = y0;
      boundingBox.x1 = x1;
      boundingBox.y1 = y1;
    }

    /**
	 * Sets the enabled.
	 *
	 * @param isEnabled the new enabled
	 */
    public void setEnabled(boolean isEnabled) {
      this.isEnabled = isEnabled;
    }

    /**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
    public boolean isEnabled() {
      return isEnabled;
    }

    /**
	 * Sets the to viewport.
	 */
    public void setToViewport() {
      setBounds(0, 0, getWidth() - 1, getHeight() - 1);
    }

    /**
	 * Clip quad.
	 *
	 * @param quad the quad
	 * @return the rect
	 */
    public Rect clipQuad(final Rect quad) {
      return clipQuad(quad.x0, quad.y0, quad.getWidth(), quad.getHeight());
    }

    /**
	 * Clip quad.
	 *
	 * @param x0     the x 0
	 * @param y0     the y 0
	 * @param width  the width
	 * @param height the height
	 * @return the rect
	 */
    public Rect clipQuad(final int x0, final int y0, final int width, final int height) {
      return clipQuadInternal(x0, y0, x0 + width - 1, y0 + height - 1);
    }

    /**
	 * Clip quad texture.
	 *
	 * @param textureX0     the texture X 0
	 * @param textureY0     the texture Y 0
	 * @param textureWidth  the texture width
	 * @param textureHeight the texture height
	 * @param originalQuad  the original quad
	 * @param clippedQuad   the clipped quad
	 * @return the rect
	 */
    public Rect clipQuadTexture(final int textureX0,
                                final int textureY0,
                                final int textureWidth,
                                final int textureHeight,
                                final Rect originalQuad,
                                final Rect clippedQuad) {
      return clipQuadTextureInternal(textureX0,
                                     textureY0,
                                     textureX0 + textureWidth - 1,
                                     textureY0 + textureHeight - 1,
                                     textureWidth,
                                     textureHeight,
                                     originalQuad,
                                     clippedQuad);
    }

    /**
	 * Checks if is completely outside.
	 *
	 * @param x0     the x 0
	 * @param y0     the y 0
	 * @param width  the width
	 * @param height the height
	 * @return true, if is completely outside
	 */
    public boolean isCompletelyOutside(final int x0, final int y0, final int width, final int height) {
      return isCompletelyOutsideInternal(x0, y0, x0 + width - 1, y0 + height - 1);
    }

    /**
	 * Checks if is completely inside.
	 *
	 * @param x0     the x 0
	 * @param y0     the y 0
	 * @param width  the width
	 * @param height the height
	 * @return true, if is completely inside
	 */
    public boolean isCompletelyInside(final int x0, final int y0, final int width, final int height) {
      return isCompletelyInsideInternal(x0, y0, x0 + width - 1, y0 + height - 1);
    }

    /**
	 * Increment discard counter.
	 */
    public void incrementDiscardCounter() {
      ++discardCount;
    }

    /**
	 * Gets the discard count.
	 *
	 * @return the discard count
	 */
    public int getDiscardCount() {
      return discardCount;
    }

    /**
	 * Reset discard count.
	 */
    public void resetDiscardCount() {
      discardCount = 0;
    }

    // Internal implementations

    /**
	 * Clip quad internal.
	 *
	 * @param x0 the x 0
	 * @param y0 the y 0
	 * @param x1 the x 1
	 * @param y1 the y 1
	 * @return the rect
	 */
    private Rect clipQuadInternal(final int x0, final int y0, final int x1, final int y1) {
      if (x0 >= boundingBox.x0) {
        clippedRect.x0 = x0;
      } else {
        clippedRect.x0 = boundingBox.x0;
      }

      if (x1 <= boundingBox.x1) {
        clippedRect.x1 = x1;
      } else {
        clippedRect.x1 = boundingBox.x1;
      }

      if (y0 >= boundingBox.y0) {
        clippedRect.y0 = y0;
      } else {
        clippedRect.y0 = boundingBox.y0;
      }

      if (y1 <= boundingBox.y1) {
        clippedRect.y1 = y1;
      } else {
        clippedRect.y1 = boundingBox.y1;
      }

      return clippedRect;
    }

    /**
	 * Clip quad texture internal.
	 *
	 * @param textureX0     the texture X 0
	 * @param textureY0     the texture Y 0
	 * @param textureX1     the texture X 1
	 * @param textureY1     the texture Y 1
	 * @param textureWidth  the texture width
	 * @param textureHeight the texture height
	 * @param originalQuad  the original quad
	 * @param clippedQuad   the clipped quad
	 * @return the rect
	 */
    private Rect clipQuadTextureInternal(final int textureX0,
                                         final int textureY0,
                                         final int textureX1,
                                         final int textureY1,
                                         final int textureWidth,
                                         final int textureHeight,
                                         final Rect originalQuad,
                                         final Rect clippedQuad) {
      if (clippedQuad.x0 == originalQuad.x0) {
        clippedRect.x0 = textureX0;
      } else {
        clippedRect.x0 = (int) (textureX0 +
                ((clippedQuad.x0 - originalQuad.x0) / (float) originalQuad.getWidth()) * textureWidth);
      }

      if (clippedQuad.y0 == originalQuad.y0) {
        clippedRect.y0 = textureY0;
      } else {
        clippedRect.y0 = (int) (textureY0 +
                ((clippedQuad.y0 - originalQuad.y0) / (float) originalQuad.getHeight()) * textureHeight);
      }

      if (clippedQuad.x1 == originalQuad.x1) {
        clippedRect.x1 = textureX1;
      } else {
        clippedRect.x1 = (int) (textureX1 +
                ((clippedQuad.x1 - originalQuad.x1) / (float) originalQuad.getWidth()) * textureWidth);
      }

      if (clippedQuad.y1 == originalQuad.y1) {
        clippedRect.y1 = textureY1;
      } else {
        clippedRect.y1 = (int) (textureY1 +
                ((clippedQuad.y1 - originalQuad.y1) / (float) originalQuad.getHeight()) * textureHeight);
      }
      return this.clippedRect;
    }

    /**
	 * Checks if is completely outside internal.
	 *
	 * @param x0 the x 0
	 * @param y0 the y 0
	 * @param x1 the x 1
	 * @param y1 the y 1
	 * @return true, if is completely outside internal
	 */
    private boolean isCompletelyOutsideInternal(final int x0, final int y0, final int x1, final int y1) {
      return (x0 > boundingBox.x1) || (x1 < boundingBox.x0) || (y0 > boundingBox.y1) || (y1 < boundingBox.y0);
    }

    /**
	 * Checks if is completely inside internal.
	 *
	 * @param x0 the x 0
	 * @param y0 the y 0
	 * @param x1 the x 1
	 * @param y1 the y 1
	 * @return true, if is completely inside internal
	 */
    private boolean isCompletelyInsideInternal(final int x0, final int y0, final int x1, final int y1) {
      return x0 >= boundingBox.x0 && x0 <= boundingBox.x1 &&
              x1 >= boundingBox.x0 && x1 <= boundingBox.x1 &&
              y0 >= boundingBox.y0 && y0 <= boundingBox.y1 &&
              y1 >= boundingBox.y0 && y1 <= boundingBox.y1;
    }
  }

  /**
	 * The Class Rect.
	 */
  private class Rect {
    
    /** The x 0. */
    public int x0;
    
    /** The y 0. */
    public int y0;
    
    /** The x 1. */
    public int x1;
    
    /** The y 1. */
    public int y1;

    /**
	 * Instantiates a new rect.
	 *
	 * @param x0 the x 0
	 * @param y0 the y 0
	 * @param x1 the x 1
	 * @param y1 the y 1
	 */
    public Rect (final int x0, final int y0, final int x1, final int y1) {
      this.x0 = x0;
      this.y0 = y0;
      this.x1 = x1;
      this.y1 = y1;
    }

    /**
	 * Gets the width.
	 *
	 * @return the width
	 */
    public int getWidth() {
      return x1 - x0 + 1;
    }

    /**
	 * Gets the height.
	 *
	 * @return the height
	 */
    public int getHeight() {
      return y1 - y0 + 1;
    }

    /**
	 * Sets the.
	 *
	 * @param x0     the x 0
	 * @param y0     the y 0
	 * @param width  the width
	 * @param height the height
	 */
    public void set(final int x0, final int y0, final int width, final int height) {
      this.x0 = x0;
      this.y0 = y0;
      this.x1 = x0 + width - 1;
      this.y1 = y0 + height - 1;
    }

    /**
	 * Sets the.
	 *
	 * @param rect the rect
	 */
    public void set(Rect rect) {
      this.x0 = rect.x0;
      this.y0 = rect.y0;
      this.x1 = rect.x1;
      this.y1 = rect.y1;
    }
  }

  /**
	 * The Class FontRenderer.
	 */
  private class FontRenderer implements JGLFontRenderer {
    
    /** The texture infos. */
    private final Map<String, BitmapInfo> textureInfos = new HashMap<String, BitmapInfo>();
    
    /** The color value parser. */
    private final ColorValueParser colorValueParser = new ColorValueParser();
    
    /** The batch render device. */
    private final BatchRenderDevice batchRenderDevice;
    
    /** The text color. */
    private final Color textColor = Color.BLACK;
    
    /** The has color. */
    private boolean hasColor;

    /**
	 * Instantiates a new font renderer.
	 *
	 * @param batchRenderDevice the batch render device
	 */
    public FontRenderer(final BatchRenderDevice batchRenderDevice) {
      this.batchRenderDevice = batchRenderDevice;
    }

    /**
	 * Unload.
	 */
    public void unload() {
      for (BitmapInfo info : textureInfos.values()) {
        info.unload();
      }
    }

    /**
	 * Register bitmap.
	 *
	 * @param bitmapId the bitmap id
	 * @param data     the data
	 * @param filename the filename
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Override
    public void registerBitmap(
        @Nonnull final String bitmapId,
        final InputStream data,
        @Nonnull final String filename) throws IOException {
      textureInfos.put(bitmapId, new BitmapInfo((BatchRenderImage) batchRenderDevice.createImage(filename, true)));
    }

    /**
	 * Register bitmap.
	 *
	 * @param bitmapId the bitmap id
	 * @param data     the data
	 * @param width    the width
	 * @param height   the height
	 * @param filename the filename
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Override
    public void registerBitmap(
            @Nonnull final String bitmapId,
            @Nonnull final ByteBuffer data,
            final int width,
            final int height,
            @Nonnull final String filename
    ) throws IOException {
      BatchRenderImage batchRenderImage = null;
      BatchRenderBackend.Image image = renderBackend.loadImage(data, width, height);
      if (image != null) {
        batchRenderImage = new BatchRenderImage(
                image,
                filename,
                renderBackend,
                getCurrentTextureAtlasGenerator(),
                getCurrentAtlasTextureId(),
                renderConfig.disposeImagesBetweenScreens);
      }
      textureInfos.put(bitmapId, new BitmapInfo(batchRenderImage));
    }

    /**
	 * Register glyph.
	 *
	 * @param bitmapId the bitmap id
	 * @param c        the c
	 * @param xoff     the xoff
	 * @param yoff     the yoff
	 * @param w        the w
	 * @param h        the h
	 * @param u0       the u 0
	 * @param v0       the v 0
	 * @param u1       the u 1
	 * @param v1       the v 1
	 */
    @Override
    public void registerGlyph(
        final String bitmapId,
        final int c,
        final int xoff,
        final int yoff,
        final int w,
        final int h,
        final float u0,
        final float v0,
        final float u1,
        final float v1) {
      BitmapInfo textureInfo = textureInfos.get(bitmapId);
      textureInfo.addCharRenderInfo(c, new CharRenderInfo(xoff, yoff, w, h, u0, v0));
    }

    /**
	 * Prepare.
	 */
    @Override
    public void prepare() {
    }

    /**
	 * Before render.
	 *
	 * @param customRenderState the custom render state
	 */
    @Override
    public void beforeRender (final Object customRenderState) {
      hasColor = false;
      for (BitmapInfo info : textureInfos.values()) {
        info.upload();
      }
    }

    /**
	 * Pre process.
	 *
	 * @param text   the text
	 * @param offset the offset
	 * @return the int
	 */
    @Override
    public int preProcess(@Nonnull final String text, final int offset) {
      int index = offset;
      colorValueParser.isColor(text, index);
      while (colorValueParser.isColor()) {
        final Color color = colorValueParser.getColor();
        assert color != null;
        textColor.setRed(color.getRed());
        textColor.setGreen(color.getGreen());
        textColor.setBlue(color.getBlue());
        textColor.setAlpha(color.getAlpha());
        hasColor = true;
        index = colorValueParser.getNextIndex();
        if (index >= text.length()) {
          return index;
        }
        colorValueParser.isColor(text, index);
      }
      return index;
    }

    /**
	 * Render.
	 *
	 * @param bitmapId the bitmap id
	 * @param x        the x
	 * @param y        the y
	 * @param c        the c
	 * @param sx       the sx
	 * @param sy       the sy
	 * @param r        the r
	 * @param g        the g
	 * @param b        the b
	 * @param a        the a
	 */
    @Override
    public void render(
        final String bitmapId,
        final int x,
        final int y,
        final int c,
        final float sx,
        final float sy,
        final float r,
        final float g,
        final float b,
        final float a) {
      if (!hasColor) {
        textColor.setRed(r);
        textColor.setGreen(g);
        textColor.setBlue(b);
      }
      textColor.setAlpha(a);
      textureInfos.get(bitmapId).renderCharacter(c, x, y, sx, sy, textColor);
    }

    /**
	 * After render.
	 */
    @Override
    public void afterRender() {
    }

    /**
	 * Pre process for length.
	 *
	 * @param text   the text
	 * @param offset the offset
	 * @return the int
	 */
    @Override
    public int preProcessForLength(@Nonnull final String text, final int offset) {
      int index = offset;
      colorValueParser.isColor(text, index);
      while (colorValueParser.isColor()) {
        index = colorValueParser.getNextIndex();
        if (index >= text.length()) {
          return index;
        }
        colorValueParser.isColor(text, index);
      }
      return index;
    }
  }

  /**
	 * The Class CharRenderInfo.
	 */
  private class CharRenderInfo {
    
    /** The xoff. */
    final int xoff;
    
    /** The yoff. */
    final int yoff;
    
    /** The w. */
    final int w;
    
    /** The h. */
    final int h;
    
    /** The u 0. */
    final float u0;
    
    /** The v 0. */
    final float v0;

    /**
	 * Instantiates a new char render info.
	 *
	 * @param xoff the xoff
	 * @param yoff the yoff
	 * @param w    the w
	 * @param h    the h
	 * @param u0   the u 0
	 * @param v0   the v 0
	 */
    public CharRenderInfo(
        final int xoff,
        final int yoff,
        final int w,
        final int h,
        final float u0,
        final float v0) {
      this.xoff = xoff;
      this.yoff = yoff;
      this.w = w;
      this.h = h;
      this.u0 = u0;
      this.v0 = v0;
    }

    /**
	 * Render quad.
	 *
	 * @param x             the x
	 * @param y             the y
	 * @param sx            the sx
	 * @param sy            the sy
	 * @param textColor     the text color
	 * @param textureX      the texture X
	 * @param textureY      the texture Y
	 * @param textureWidth  the texture width
	 * @param textureHeight the texture height
	 * @param textureId     the texture id
	 */
    public void renderQuad(
        final int x,
        final int y,
        final float sx,
        final float sy,
        @Nonnull final Color textColor,
        final int textureX,
        final int textureY,
        final int textureWidth,
        final int textureHeight,
        final int textureId) {
      glyphCount++;
      addQuad(
          x + (float) Math.floor(xoff * sx),
          y + (float) Math.floor(yoff * sy),
          w * sx,
          h * sy,
          textColor,
          textColor,
          textColor,
          textColor,
          (int) (textureX + u0 * textureWidth),
          (int) (textureY + v0 * textureHeight),
          w,
          h,
          textureId);
    }
  }

  /**
	 * The Class BitmapInfo.
	 */
  private class BitmapInfo {
    
    /** The image. */
    private final BatchRenderImage image;
    
    /** The character indices. */
    private final Map<Integer, CharRenderInfo> characterIndices = new HashMap<Integer, CharRenderInfo>();

    /**
	 * Instantiates a new bitmap info.
	 *
	 * @param image the image
	 */
    public BitmapInfo(final BatchRenderImage image) {
      this.image = image;
    }

    /**
	 * Upload.
	 */
    private void upload() {
      uploadImageInternal(image);
    }

    /**
	 * Unload.
	 */
    private void unload() {
      image.markAsUnloaded();
    }

    /**
	 * Render character.
	 *
	 * @param c         the c
	 * @param x         the x
	 * @param y         the y
	 * @param sx        the sx
	 * @param sy        the sy
	 * @param textColor the text color
	 */
    public void renderCharacter(int c, int x, int y, float sx, float sy, @Nonnull Color textColor) {
      characterIndices.get(c).renderQuad(
          x,
          y,
          sx,
          sy,
          textColor,
          image.getX(),
          image.getY(),
          image.getWidth(),
          image.getHeight(),
          image.getTextureId());
    }

    /**
	 * Adds the char render info.
	 *
	 * @param c          the c
	 * @param renderInfo the render info
	 */
    public void addCharRenderInfo(final Integer c, final CharRenderInfo renderInfo) {
      this.characterIndices.put(c, renderInfo);
    }
  }
}
