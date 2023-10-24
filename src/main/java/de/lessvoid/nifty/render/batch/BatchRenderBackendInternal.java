package de.lessvoid.nifty.render.batch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.render.batch.spi.Batch;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.render.batch.spi.BufferFactory;
import de.lessvoid.nifty.render.batch.spi.GL;
import de.lessvoid.nifty.render.batch.spi.ImageFactory;
import de.lessvoid.nifty.render.batch.spi.MouseCursorFactory;
import de.lessvoid.nifty.render.io.ImageLoader;
import de.lessvoid.nifty.render.io.ImageLoaderFactory;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.Factory;
import de.lessvoid.nifty.tools.ObjectPool;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * Internal {@link de.lessvoid.nifty.render.batch.spi.BatchRenderBackend} implementation for OpenGL batch rendering that
 * provides OpenGL (& OpenGL ES) - based {@link de.lessvoid.nifty.render.batch.spi.BatchRenderBackend} implementations some
 * default functionality to avoid having to reinvent the wheel and to prevent unnecessary code duplication. Fully
 * OpenGL ES compatible - this class doesn't require the implementation of any OpenGL methods that are not available in
 * OpenGL ES. This implementation will be the most backwards-compatible because it doesn't use any functions beyond
 * OpenGL 1.1. It is suitable for both mobile & desktop devices.
 *
 * {@inheritDoc}
 *
 * @author void256
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class BatchRenderBackendInternal implements BatchRenderBackend {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(BatchRenderBackendInternal.class.getName());
  
  /** The Constant INVALID_TEXTURE_ID. */
  private static final int INVALID_TEXTURE_ID = -1;
  
  /** The gl. */
  @Nonnull
  private final GL gl;
  
  /** The buffer factory. */
  @Nonnull
  private final BufferFactory bufferFactory;
  
  /** The image factory. */
  @Nonnull
  private final ImageFactory imageFactory;
  
  /** The mouse cursor factory. */
  @Nonnull
  private final MouseCursorFactory mouseCursorFactory;
  
  /** The viewport buffer. */
  @Nonnull
  private final IntBuffer viewportBuffer;
  
  /** The single texture id buffer. */
  @Nonnull
  private final IntBuffer singleTextureIdBuffer;
  
  /** The batch pool. */
  @Nonnull
  private final ObjectPool<Batch> batchPool;
  
  /** The batches. */
  @Nonnull
  private final List<Batch> batches = new ArrayList<Batch>();
  
  /** The non atlas texture ids. */
  @Nonnull
  private final ArrayList<Integer> nonAtlasTextureIds = new ArrayList<Integer>();
  
  /** The atlas widths. */
  @Nonnull
  private final Map<Integer, Integer> atlasWidths = new HashMap<Integer, Integer>();
  
  /** The atlas heights. */
  @Nonnull
  private final Map<Integer, Integer> atlasHeights = new HashMap<Integer, Integer>();
  
  /** The cursor cache. */
  @Nonnull
  private final Map<String, MouseCursor> cursorCache = new HashMap<String, MouseCursor>();
  
  /** The mouse cursor. */
  @Nullable
  private MouseCursor mouseCursor;
  
  /** The resource loader. */
  @Nullable
  private NiftyResourceLoader resourceLoader;
  
  /** The current batch. */
  @Nullable
  private Batch currentBatch;
  
  /** The viewport width. */
  private int viewportWidth;
  
  /** The viewport height. */
  private int viewportHeight;
  
  /** The should use high quality textures. */
  private boolean shouldUseHighQualityTextures = false;
  
  /** The should fill removed images in atlas. */
  private boolean shouldFillRemovedImagesInAtlas = false;

  /**
	 * Instantiates a new batch render backend internal.
	 *
	 * @param gl                 the gl
	 * @param bufferFactory      the buffer factory
	 * @param imageFactory       the image factory
	 * @param mouseCursorFactory the mouse cursor factory
	 */
  public BatchRenderBackendInternal(
          @Nonnull final GL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final ImageFactory imageFactory,
          @Nonnull final MouseCursorFactory mouseCursorFactory) {
	  log.setLevel(Level.WARNING);
    this.gl = gl;
    this.bufferFactory = bufferFactory;
    this.imageFactory = imageFactory;
    this.mouseCursorFactory = mouseCursorFactory;
    viewportBuffer = bufferFactory.createNativeOrderedIntBuffer(16);
    singleTextureIdBuffer = bufferFactory.createNativeOrderedIntBuffer(1);
    batchPool = new ObjectPool<Batch>(new Factory<Batch>() {
      @Nonnull
      @Override
      public Batch createNew() {
        return new BatchInternal(gl, bufferFactory);
      }
    });
    initializeOpenGL();
  }

  /**
	 * Sets the resource loader.
	 *
	 * @param resourceLoader the new resource loader
	 */
  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
    log.fine("setResourceLoader()");
    this.resourceLoader = resourceLoader;
  }

  /**
	 * Gets the width.
	 *
	 * @return the width
	 */
  @Override
  public int getWidth() {
    log.fine("getWidth()");
    updateViewport();
    return viewportWidth;
  }

  /**
	 * Gets the height.
	 *
	 * @return the height
	 */
  @Override
  public int getHeight() {
    log.fine("getHeight()");
    updateViewport();
    return viewportHeight;
  }

  /**
	 * Begin frame.
	 */
  @Override
  public void beginFrame() {
    log.fine("beginFrame()");
    deleteBatches();
  }

  /**
	 * End frame.
	 */
  @Override
  public void endFrame() {
    log.fine("endFrame()");
    CheckGL.checkGLError(gl);
  }

  /**
	 * Clear.
	 */
  @Override
  public void clear() {
    log.fine("clear()");
    clearGlColorBufferWithBlack();
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
  @Nullable
  @Override
  public MouseCursor createMouseCursor(@Nonnull final String filename, final int hotspotX, final int hotspotY)
          throws IOException {
    log.fine("createMouseCursor()");
    return existsCursor(filename) ? getCursor(filename) : createCursor(filename, hotspotX, hotspotY);
  }

  /**
	 * Enable mouse cursor.
	 *
	 * @param mouseCursor the mouse cursor
	 */
  @Override
  public void enableMouseCursor(@Nonnull final MouseCursor mouseCursor) {
    log.fine("enableMouseCursor()");
    this.mouseCursor = mouseCursor;
    mouseCursor.enable();
  }

  /**
	 * Disable mouse cursor.
	 */
  @Override
  public void disableMouseCursor() {
    if (mouseCursor != null) {
      log.fine("disableMouseCursor()");
      mouseCursor.disable();
    }
  }

  /**
	 * Creates the texture atlas.
	 *
	 * @param atlasWidth  the atlas width
	 * @param atlasHeight the atlas height
	 * @return the int
	 */
  @Override
  public int createTextureAtlas(final int atlasWidth, final int atlasHeight) {
    log.fine("createTextureAtlas()");
    try {
      return createAtlasTextureInternal(atlasWidth, atlasHeight);
    } catch (Exception e) {
      textureCreationFailed(atlasWidth, atlasHeight, e);
      return INVALID_TEXTURE_ID;
    }
  }

  /**
	 * Clear texture atlas.
	 *
	 * @param atlasTextureId the atlas texture id
	 */
  @Override
  public void clearTextureAtlas(final int atlasTextureId) {
    log.fine("clearTextureAtlas()");
    bindGlTexture(atlasTextureId);
    updateCurrentlyBoundGlTexture(
            createBlankImageDataForAtlas(atlasTextureId),
            getAtlasWidth(atlasTextureId),
            getAtlasHeight(atlasTextureId));
  }

  /**
	 * Load image.
	 *
	 * @param filename the filename
	 * @return the image
	 */
  @Nonnull
  @Override
  public Image loadImage(@Nonnull final String filename) {
    log.fine("loadImage()");
    return createImageFromFile(filename);
  }

  /**
	 * Load image.
	 *
	 * @param imageData   the image data
	 * @param imageWidth  the image width
	 * @param imageHeight the image height
	 * @return the image
	 */
  @Nullable
  @Override
  public Image loadImage(@Nonnull final ByteBuffer imageData, final int imageWidth, final int imageHeight) {
    log.fine("loadImage2()");
    return imageFactory.create(imageData, imageWidth, imageHeight);
  }

  /**
	 * Adds the image to atlas.
	 *
	 * @param image          the image
	 * @param atlasX         the atlas X
	 * @param atlasY         the atlas Y
	 * @param atlasTextureId the atlas texture id
	 */
  @Override
  public void addImageToAtlas(
          @Nonnull final Image image,
          final int atlasX,
          final int atlasY,
          final int atlasTextureId) {
    log.fine("addImageToAtlas()");
    bindGlTexture(atlasTextureId);
    updateCurrentlyBoundGlTexture(
            imageFactory.asByteBuffer(image),
            atlasX,
            atlasY,
            image.getWidth(),
            image.getHeight());
  }

  /**
	 * Creates the non atlas texture.
	 *
	 * @param image the image
	 * @return the int
	 */
  @Override
  public int createNonAtlasTexture(@Nonnull final Image image) {
    log.fine("createNonAtlasTexture()");
    try {
      return createNonAtlasTextureInternal(imageFactory.asByteBuffer(image), image.getWidth(), image.getHeight());
    } catch (Exception e) {
      textureCreationFailed(image.getWidth(), image.getHeight(), e);
      return INVALID_TEXTURE_ID;
    }
  }

  /**
	 * Delete non atlas texture.
	 *
	 * @param textureId the texture id
	 */
  @Override
  public void deleteNonAtlasTexture(final int textureId) {
    log.fine("deleteNonAtlasTexture()");
    try {
      deleteNonAtlasTextureInternal(textureId);
    } catch (Exception e) {
      textureDeletionFailed(textureId, e);
    }
  }

  /**
	 * Exists non atlas texture.
	 *
	 * @param textureId the texture id
	 * @return true, if successful
	 */
  @Override
  public boolean existsNonAtlasTexture(final int textureId) {
    log.fine("existsNonAtlasTexture()");
    return nonAtlasTextureIds.contains(textureId);
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
  @Override
  public void addQuad(
          final float x,
          final float y,
          final float width,
          final float height,
          @Nonnull final Color color1,
          @Nonnull final Color color2,
          @Nonnull final Color color3,
          @Nonnull final Color color4,
          final float textureX,
          final float textureY,
          final float textureWidth,
          final float textureHeight,
          final int textureId) {
    log.fine("addQuad()");
    updateCurrentBatch(textureId);
    addQuadToCurrentBatch(
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
            textureHeight);
  }

  /**
	 * Begin batch.
	 *
	 * @param blendMode the blend mode
	 * @param textureId the texture id
	 */
  @Override
  public void beginBatch(@Nonnull final BlendMode blendMode, final int textureId) {
    log.fine("beginBatch()");
    currentBatch = createNewBatch();
    addBatch(currentBatch);
    currentBatch.begin(blendMode, textureId);
  }

  /**
	 * Render.
	 *
	 * @return the int
	 */
  @Override
  public int render() {
    log.fine("render()");
    beginRendering();
    renderBatches();
    endRendering();
    return getTotalBatchesRendered();
  }

  /**
	 * Removes the image from atlas.
	 *
	 * @param image          the image
	 * @param atlasX         the atlas X
	 * @param atlasY         the atlas Y
	 * @param imageWidth     the image width
	 * @param imageHeight    the image height
	 * @param atlasTextureId the atlas texture id
	 */
  @Override
  public void removeImageFromAtlas(
          @Nonnull final Image image,
          final int atlasX,
          final int atlasY,
          final int imageWidth,
          final int imageHeight,
          final int atlasTextureId) {
    // Since we clear the whole texture when we switch screens it's not really necessary to remove data from the
    // texture atlas when individual textures are removed.
    if (! shouldFillRemovedImagesInAtlas) {
      return;
    }
    log.fine("removeImageFromAtlas()");
    bindGlTexture(atlasTextureId);
    updateCurrentlyBoundGlTexture(
            createBlankImageData(imageWidth, imageHeight),
            atlasX,
            atlasY,
            imageWidth,
            imageHeight);
  }

  /**
	 * Use high quality textures.
	 *
	 * @param shouldUseHighQualityTextures the should use high quality textures
	 */
  @Override
  public void useHighQualityTextures(final boolean shouldUseHighQualityTextures) {
    log.fine("useHighQualityTextures()");
    log.info(shouldUseHighQualityTextures ? "Using high quality textures (near & far bilinear filtering, " +
            "mipmapping not supported in this implementation." : "Using low quality textures (no filtering).");
    this.shouldUseHighQualityTextures = shouldUseHighQualityTextures;
  }

  /**
	 * Fill removed images in atlas.
	 *
	 * @param shouldFill the should fill
	 */
  @Override
  public void fillRemovedImagesInAtlas(final boolean shouldFill) {
    log.fine("fillRemovedImagesInAtlas()");
    log.info(shouldFill ? "Filling in removed images in atlas." : "Not filling in removed images in atlas.");
    shouldFillRemovedImagesInAtlas = shouldFill;
  }

  // Internal implementations

  /**
	 * Initialize open GL.
	 */
  private void initializeOpenGL() {
    gl.glViewport(0, 0, getWidth(), getHeight());
    gl.glMatrixMode(gl.GL_PROJECTION());
    gl.glLoadIdentity();
    gl.glOrthof(0.0f, (float) getWidth(), (float) getHeight(), 0.0f, -9999.0f, 9999.0f);
    gl.glMatrixMode(gl.GL_MODELVIEW());
    gl.glLoadIdentity();
    gl.glDisable(gl.GL_DEPTH_TEST());
    gl.glDisable(gl.GL_CULL_FACE());
    gl.glDisable(gl.GL_LIGHTING());
    gl.glEnable(gl.GL_ALPHA_TEST());
    gl.glEnable(gl.GL_BLEND());
    gl.glEnable(gl.GL_TEXTURE_2D());
    gl.glAlphaFunc(gl.GL_NOTEQUAL(), 0);
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glClear(gl.GL_COLOR_BUFFER_BIT());
    // Enable exact pixelization for 2D rendering
    // See: http://www.opengl.org/archives/resources/faq/technical/transformations.htm#tran0030
    gl.glTranslatef(0.375f, 0.375f, 0.0f);
  }

  /**
	 * Update viewport.
	 */
  private void updateViewport() {
    viewportBuffer.clear();
    gl.glGetIntegerv(gl.GL_VIEWPORT(), viewportBuffer);
    viewportWidth = viewportBuffer.get(2);
    viewportHeight = viewportBuffer.get(3);
    log.fine("Updated viewport: width: " + viewportWidth + ", height: " + viewportHeight);
  }

  /**
	 * Creates the blank image data for atlas.
	 *
	 * @param atlasTextureId the atlas texture id
	 * @return the byte buffer
	 */
  @Nonnull
  private ByteBuffer createBlankImageDataForAtlas(final int atlasTextureId) {
    return createBlankImageData(getAtlasWidth(atlasTextureId), getAtlasHeight(atlasTextureId));
  }

  /**
	 * Creates the blank image data.
	 *
	 * @param textureWidth  the texture width
	 * @param textureHeight the texture height
	 * @return the byte buffer
	 */
  @Nonnull
  private ByteBuffer createBlankImageData(final int textureWidth, final int textureHeight) {
    return this.bufferFactory.createNativeOrderedByteBuffer(textureWidth * textureHeight * 4);
  }

  /**
	 * Gets the atlas width.
	 *
	 * @param atlasTextureId the atlas texture id
	 * @return the atlas width
	 */
  private int getAtlasWidth(final int atlasTextureId) {
    return atlasWidths.get(atlasTextureId);
  }

  /**
	 * Gets the atlas height.
	 *
	 * @param atlasTextureId the atlas texture id
	 * @return the atlas height
	 */
  private int getAtlasHeight(final int atlasTextureId) {
    return atlasHeights.get(atlasTextureId);
  }

  /**
	 * Delete batches.
	 */
  private void deleteBatches() {
    for (Batch batch : batches) {
      batchPool.free(batch);
    }
    batches.clear();
  }

  /**
	 * Clear gl color buffer with black.
	 */
  private void clearGlColorBufferWithBlack() {
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glClear(gl.GL_COLOR_BUFFER_BIT());
  }

  /**
	 * Creates the atlas texture internal.
	 *
	 * @param width  the width
	 * @param height the height
	 * @return the int
	 * @throws Exception the exception
	 */
  private int createAtlasTextureInternal(final int width, final int height) throws Exception {
    int atlasTextureId = createGlTexture(createBlankImageData(width, height), width, height);
    saveAtlasSize(atlasTextureId, width, height);
    return atlasTextureId;
  }

  /**
	 * Texture creation failed.
	 *
	 * @param textureWidth  the texture width
	 * @param textureHeight the texture height
	 * @param exception     the exception
	 */
  private void textureCreationFailed(
          final int textureWidth,
          final int textureHeight,
          @Nonnull final Exception exception) {
    log.log(Level.WARNING, "Failed to create texture of width: " + textureWidth + " & height: " + textureHeight + ".",
            exception);
  }

  /**
	 * Creates the gl texture.
	 *
	 * @param imageData     the image data
	 * @param textureWidth  the texture width
	 * @param textureHeight the texture height
	 * @return the int
	 * @throws Exception the exception
	 */
  private int createGlTexture(
          @Nullable final ByteBuffer imageData,
          final int textureWidth,
          final int textureHeight) throws Exception {
    CheckGL.checkGLTextureSize(gl, textureWidth, textureHeight);
    int glTextureId = createTextureId();
    bindGlTexture(glTextureId);
    updateCurrentlyBoundGlTexture(imageData, textureWidth, textureHeight);
    setCurrentlyBoundGlTextureFilteringQuality(shouldUseHighQualityTextures);
    return glTextureId;
  }

  /**
	 * Bind gl texture.
	 *
	 * @param textureId the texture id
	 */
  private void bindGlTexture(final int textureId) {
    gl.glBindTexture(gl.GL_TEXTURE_2D(), textureId);
  }

  /**
	 * Update currently bound gl texture.
	 *
	 * @param imageData the image data
	 * @param width     the width
	 * @param height    the height
	 */
  private void updateCurrentlyBoundGlTexture(@Nullable final ByteBuffer imageData, final int width, final int height) {
    if (imageData == null) {
      log.warning("Attempted to update currently bound OpenGL texture with null image data!");
      return;
    }
    gl.glTexImage2D(
            gl.GL_TEXTURE_2D(),
            0,
            gl.GL_RGBA(),
            width,
            height,
            0,
            gl.GL_RGBA(),
            gl.GL_UNSIGNED_BYTE(),
            imageData);
    CheckGL.checkGLError(gl);
  }

  /**
	 * Update currently bound gl texture.
	 *
	 * @param imageData        the image data
	 * @param subTextureX      the sub texture X
	 * @param subTextureY      the sub texture Y
	 * @param subTextureWidth  the sub texture width
	 * @param subTextureHeight the sub texture height
	 */
  private void updateCurrentlyBoundGlTexture(
          @Nullable final ByteBuffer imageData,
          final int subTextureX,
          final int subTextureY,
          final int subTextureWidth,
          final int subTextureHeight) {
    if (imageData == null) {
      log.warning("Attempted to update sub-texture of currently bound OpenGL texture with null image data!");
      return;
    }
    gl.glTexSubImage2D(
            gl.GL_TEXTURE_2D(),
            0,
            subTextureX,
            subTextureY,
            subTextureWidth,
            subTextureHeight,
            gl.GL_RGBA(),
            gl.GL_UNSIGNED_BYTE(),
            imageData);
    CheckGL.checkGLError(gl);
  }

  /**
	 * Sets the currently bound gl texture filtering quality.
	 *
	 * @param isHighQuality the new currently bound gl texture filtering quality
	 */
  private void setCurrentlyBoundGlTextureFilteringQuality(final boolean isHighQuality) {
    if (isHighQuality) {
      gl.glTexParameterf(gl.GL_TEXTURE_2D(), gl.GL_TEXTURE_MIN_FILTER(), gl.GL_LINEAR());
      gl.glTexParameterf(gl.GL_TEXTURE_2D(), gl.GL_TEXTURE_MAG_FILTER(), gl.GL_LINEAR());
    } else {
      gl.glTexParameterf(gl.GL_TEXTURE_2D(), gl.GL_TEXTURE_MIN_FILTER(), gl.GL_NEAREST());
      gl.glTexParameterf(gl.GL_TEXTURE_2D(), gl.GL_TEXTURE_MAG_FILTER(), gl.GL_NEAREST());
    }
    CheckGL.checkGLError(gl);
  }

  /**
	 * Save atlas size.
	 *
	 * @param atlasTextureId the atlas texture id
	 * @param atlasWidth     the atlas width
	 * @param atlasHeight    the atlas height
	 */
  private void saveAtlasSize(final int atlasTextureId, final int atlasWidth, final int atlasHeight) {
    atlasWidths.put(atlasTextureId, atlasWidth);
    atlasHeights.put(atlasTextureId, atlasHeight);
  }

  /**
	 * Creates the texture id.
	 *
	 * @return the int
	 */
  private int createTextureId() {
    singleTextureIdBuffer.clear();
    gl.glGenTextures(1, singleTextureIdBuffer);
    CheckGL.checkGLError(gl);
    return singleTextureIdBuffer.get(0);
  }

  /**
	 * Creates the non atlas texture internal.
	 *
	 * @param imageData the image data
	 * @param width     the width
	 * @param height    the height
	 * @return the int
	 * @throws Exception the exception
	 */
  private int createNonAtlasTextureInternal(@Nullable final ByteBuffer imageData, final int width, final int height)
          throws Exception {
    int textureId = createGlTexture(imageData, width, height);
    nonAtlasTextureIds.add(textureId);
    return textureId;
  }

  /**
	 * Delete non atlas texture internal.
	 *
	 * @param nonAtlasTextureId the non atlas texture id
	 */
  private void deleteNonAtlasTextureInternal(final int nonAtlasTextureId) {
    singleTextureIdBuffer.clear();
    singleTextureIdBuffer.put(0, nonAtlasTextureId);
    gl.glDeleteTextures(1, singleTextureIdBuffer);
    CheckGL.checkGLError(gl);
    nonAtlasTextureIds.remove(nonAtlasTextureId);
  }

  /**
	 * Texture deletion failed.
	 *
	 * @param textureId the texture id
	 * @param exception the exception
	 */
  private void textureDeletionFailed(final int textureId, final Exception exception) {
    log.log(Level.WARNING, "Failed to delete texture width id: " + textureId + ".", exception);
  }

  /**
	 * Adds the quad to current batch.
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
	 */
  private void addQuadToCurrentBatch(
          final float x,
          final float y,
          final float width,
          final float height,
          @Nonnull final Color color1,
          @Nonnull final Color color2,
          @Nonnull final Color color3,
          @Nonnull final Color color4,
          final float textureX,
          final float textureY,
          final float textureWidth,
          final float textureHeight) {
    assert currentBatch != null;
    currentBatch.addQuad(
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
            textureHeight);
  }

  /**
	 * Update current batch.
	 *
	 * @param textureId the texture id
	 */
  private void updateCurrentBatch(final int textureId) {
    if (shouldBeginBatch()) {
      beginBatch(getCurrentBlendMode(), textureId);
    }
  }

  /**
	 * Gets the current blend mode.
	 *
	 * @return the current blend mode
	 */
  @Nonnull
  private BlendMode getCurrentBlendMode() {
    assert currentBatch != null;
    return currentBatch.getBlendMode();
  }

  /**
	 * Should begin batch.
	 *
	 * @return true, if successful
	 */
  private boolean shouldBeginBatch() {
    assert currentBatch != null;
    return !currentBatch.canAddQuad();
  }

  /**
	 * Creates the new batch.
	 *
	 * @return the batch
	 */
  @Nonnull
  private Batch createNewBatch() {
    return batchPool.allocate();
  }

  /**
	 * Adds the batch.
	 *
	 * @param batch the batch
	 */
  private void addBatch (@Nonnull final Batch batch) {
    batches.add(batch);
  }

  /**
	 * Render batches.
	 */
  private void renderBatches() {
    for (Batch batch : batches) {
      batch.render();
    }
  }

  /**
	 * Begin rendering.
	 */
  private void beginRendering() {
    gl.glEnable(gl.GL_TEXTURE_2D());
    gl.glEnable(gl.GL_BLEND());
    gl.glEnableClientState(gl.GL_VERTEX_ARRAY());
    gl.glEnableClientState(gl.GL_COLOR_ARRAY());
    gl.glEnableClientState(gl.GL_TEXTURE_COORD_ARRAY());
  }

  /**
	 * End rendering.
	 */
  private void endRendering() {
    gl.glDisableClientState(gl.GL_TEXTURE_COORD_ARRAY());
    gl.glDisableClientState(gl.GL_COLOR_ARRAY());
    gl.glDisableClientState(gl.GL_VERTEX_ARRAY());
    gl.glDisable(gl.GL_BLEND());
    gl.glDisable(gl.GL_TEXTURE_2D());
  }

  /**
	 * Gets the total batches rendered.
	 *
	 * @return the total batches rendered
	 */
  private int getTotalBatchesRendered() {
    return batches.size();
  }

  /**
	 * Exists cursor.
	 *
	 * @param filename the filename
	 * @return true, if successful
	 */
  private boolean existsCursor(@Nonnull final String filename) {
    return cursorCache.containsKey(filename);
  }

  /**
	 * Gets the cursor.
	 *
	 * @param filename the filename
	 * @return the cursor
	 */
  @Nonnull
  private MouseCursor getCursor(@Nonnull final String filename) {
    assert cursorCache.containsKey(filename);
    return cursorCache.get(filename);
  }

  /**
	 * Creates the cursor.
	 *
	 * @param filename the filename
	 * @param hotspotX the hotspot X
	 * @param hotspotY the hotspot Y
	 * @return the mouse cursor
	 */
  @Nullable
  private MouseCursor createCursor (final String filename, final int hotspotX, final int hotspotY) {
    try {
      assert resourceLoader != null;
      cursorCache.put(filename, mouseCursorFactory.create(filename, hotspotX, hotspotY, resourceLoader));
      return cursorCache.get(filename);
    } catch (Exception e) {
      log.log(Level.WARNING, "Could not create mouse cursor [" + filename + "]", e);
      return null;
    }
  }

  /**
	 * Creates the image from file.
	 *
	 * @param filename the filename
	 * @return the image
	 */
  @Nonnull
  private Image createImageFromFile(@Nonnull final String filename) {
    ImageLoader loader = ImageLoaderFactory.createImageLoader(filename);
    InputStream imageStream = null;
    try {
      assert resourceLoader != null;
      imageStream = resourceLoader.getResourceAsStream(filename);
      if (imageStream != null) {
        ByteBuffer image = loader.loadAsByteBufferRGBA(imageStream);
        image.rewind();
        int width = loader.getImageWidth();
        int height = loader.getImageHeight();
        return imageFactory.create(image, width, height);
      }
    } catch (Exception e) {
      log.log(Level.WARNING, "Could not load image from file: [" + filename + "]", e);
    } finally {
      if (imageStream != null) {
        try {
          imageStream.close();
        } catch (IOException ignored) {
        }
      }
    }
    return imageFactory.create(null, 0, 0);
  }
}
