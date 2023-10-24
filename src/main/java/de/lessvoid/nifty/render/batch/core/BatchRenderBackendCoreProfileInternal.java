package de.lessvoid.nifty.render.batch.core;

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
import de.lessvoid.nifty.render.batch.BatchRenderBackendInternal;
import de.lessvoid.nifty.render.batch.CheckGL;
import de.lessvoid.nifty.render.batch.core.CoreTexture2D.ColorFormat;
import de.lessvoid.nifty.render.batch.core.CoreTexture2D.ResizeFilter;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.render.batch.spi.BufferFactory;
import de.lessvoid.nifty.render.batch.spi.ImageFactory;
import de.lessvoid.nifty.render.batch.spi.MouseCursorFactory;
import de.lessvoid.nifty.render.batch.spi.core.CoreBatch;
import de.lessvoid.nifty.render.batch.spi.core.CoreGL;
import de.lessvoid.nifty.render.batch.spi.core.CoreMatrixFactory;
import de.lessvoid.nifty.render.io.ImageLoader;
import de.lessvoid.nifty.render.io.ImageLoaderFactory;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.Factory;
import de.lessvoid.nifty.tools.ObjectPool;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * Internal {@link de.lessvoid.nifty.render.batch.spi.BatchRenderBackend} implementation for OpenGL Core Profile batch
 * rendering that provides OpenGL-based {@link de.lessvoid.nifty.render.batch.spi.BatchRenderBackend}
 * implementations some default functionality to avoid having to reinvent the wheel and to prevent unnecessary code
 * duplication. Suitable for desktop devices.
 *
 * Note: Requires OpenGL 3.2 or higher. Mobiles devices & OpenGL ES are not officially supported yet with this class.
 *
 * {@inheritDoc}
 *
 * @author void256
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class BatchRenderBackendCoreProfileInternal implements BatchRenderBackend {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(BatchRenderBackendInternal.class.getName());
  
  /** The Constant PRIMITIVE_RESTART_INDEX. */
  private static final int PRIMITIVE_RESTART_INDEX = 0xFFFF;
  
  /** The Constant INVALID_TEXTURE_ID. */
  private static final int INVALID_TEXTURE_ID = -1;
  
  /** The gl. */
  @Nonnull
  private final CoreGL gl;
  
  /** The buffer factory. */
  @Nonnull
  private final BufferFactory bufferFactory;
  
  /** The image factory. */
  @Nonnull
  private final ImageFactory imageFactory;
  
  /** The mouse cursor factory. */
  @Nonnull
  private final MouseCursorFactory mouseCursorFactory;
  
  /** The shader. */
  @Nonnull
  private final CoreShader shader;
  
  /** The viewport buffer. */
  @Nonnull
  private final IntBuffer viewportBuffer;
  
  /** The batch pool. */
  @Nonnull
  private final ObjectPool<CoreBatch> batchPool;
  
  /** The save GL state. */
  @Nonnull
  private final CoreProfileSaveGLState saveGLState;
  
  /** The batches. */
  @Nonnull
  private final List<CoreBatch> batches = new ArrayList<CoreBatch>();
  
  /** The atlas textures. */
  @Nonnull
  private final Map<Integer, CoreTexture2D> atlasTextures = new HashMap<Integer, CoreTexture2D>();
  
  /** The non atlas textures. */
  @Nonnull
  private final Map<Integer, CoreTexture2D> nonAtlasTextures = new HashMap<Integer, CoreTexture2D>();
  
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
  private CoreBatch currentBatch;
  
  /** The viewport width. */
  private int viewportWidth;
  
  /** The viewport height. */
  private int viewportHeight;
  
  /** The should use high quality textures. */
  private boolean shouldUseHighQualityTextures = false;
  
  /** The should fill removed images in atlas. */
  private boolean shouldFillRemovedImagesInAtlas = false;

  /**
	 * Instantiates a new batch render backend core profile internal.
	 *
	 * @param gl                 the gl
	 * @param bufferFactory      the buffer factory
	 * @param imageFactory       the image factory
	 * @param mouseCursorFactory the mouse cursor factory
	 */
  public BatchRenderBackendCoreProfileInternal(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final ImageFactory imageFactory,
          @Nonnull final MouseCursorFactory mouseCursorFactory) {
    this.gl = gl;
    this.bufferFactory = bufferFactory;
    this.imageFactory = imageFactory;
    this.mouseCursorFactory = mouseCursorFactory;
    this.saveGLState = new CoreProfileSaveGLState(gl, bufferFactory);
    viewportBuffer = bufferFactory.createNativeOrderedIntBuffer(16);
    shader = CoreShader.createShaderWithVertexAttributes(gl, bufferFactory, "aVertex", "aColor", "aTexture");
    shader.fragmentShader("nifty.fs");
    shader.vertexShader("nifty.vs");
    shader.link();
    shader.activate();
    shader.setUniformi("uTex", 0);
    batchPool = new ObjectPool<CoreBatch>(new Factory<CoreBatch>() {
      @Nonnull
      @Override
      public CoreBatch createNew() {
        return new CoreBatchInternal(gl, shader, bufferFactory, PRIMITIVE_RESTART_INDEX);
      }
    });
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
    saveGLState.saveCore();
    shader.activate();
    shader.setUniformMatrix4f("uModelViewProjectionMatrix", CoreMatrixFactory.createOrthoMatrix(0, getWidth(), getHeight(), 0));
    deleteBatches();
  }

  /**
	 * End frame.
	 */
  @Override
  public void endFrame() {
    log.fine("endFrame()");
    saveGLState.restoreCore();
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
    updateAtlasTexture(atlasTextureId, createBlankImageDataForAtlas(atlasTextureId));
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
    log.fine("addImageToAtlas with atlas texture id: " + atlasTextureId);
    updateAtlasTextureSection(
            atlasTextureId,
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
      if (imageFactory.asByteBuffer(image) == null) {
        log.severe("Attempted to create a non atlas texture with null image data!");
        return INVALID_TEXTURE_ID;
      }
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
    return nonAtlasTextures.containsKey(textureId);
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
    currentBatch.begin(blendMode, findTexture(textureId));
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
    updateAtlasTextureSection(
            atlasTextureId,
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
    log.info(shouldUseHighQualityTextures ? "Using high quality textures (near & far trilinear filtering with " +
            "mipmaps)." : "Using low quality textures (no filtering).");
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
    return getAtlasTexture(atlasTextureId).getWidth();
  }

  /**
	 * Gets the atlas height.
	 *
	 * @param atlasTextureId the atlas texture id
	 * @return the atlas height
	 */
  private int getAtlasHeight(final int atlasTextureId) {
    return getAtlasTexture(atlasTextureId).getHeight();
  }

  /**
	 * Delete batches.
	 */
  private void deleteBatches() {
    for (CoreBatch batch : batches) {
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
    CoreTexture2D atlasTexture = createTexture(createBlankImageData(width, height), width, height);
    log.fine("createAtlasTextureInternal with atlas texture id: " + atlasTexture.getId());
    atlasTextures.put(atlasTexture.getId(), atlasTexture);
    return atlasTexture.getId();
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
    log.log(Level.SEVERE, "Failed to create texture of width: " + textureWidth + " & height: " + textureHeight +
            ".", exception);
  }

  /**
	 * Creates the texture.
	 *
	 * @param imageData     the image data
	 * @param textureWidth  the texture width
	 * @param textureHeight the texture height
	 * @return the core texture 2 D
	 * @throws Exception the exception
	 */
  @Nonnull
  private CoreTexture2D createTexture(
          @Nonnull final ByteBuffer imageData,
          final int textureWidth,
          final int textureHeight) throws Exception {
    return new CoreTexture2D(
            gl,
            bufferFactory,
            ColorFormat.RGBA,
            textureWidth,
            textureHeight,
            imageData,
            getTextureQuality());
  }

  /**
	 * Gets the texture quality.
	 *
	 * @return the texture quality
	 */
  @Nonnull
  private ResizeFilter getTextureQuality() {
    return shouldUseHighQualityTextures ? ResizeFilter.LinearMipMapLinear : ResizeFilter.Nearest;
  }

  /**
	 * Update atlas texture.
	 *
	 * @param atlasTextureId the atlas texture id
	 * @param imageData      the image data
	 */
  private void updateAtlasTexture(final int atlasTextureId, @Nullable final ByteBuffer imageData) {
    bindAtlasTexture(atlasTextureId);
    getAtlasTexture(atlasTextureId).updateTextureData(imageData);
  }

  /**
	 * Update atlas texture section.
	 *
	 * @param atlasTextureId     the atlas texture id
	 * @param imageData          the image data
	 * @param atlasSectionX      the atlas section X
	 * @param atlasSectionY      the atlas section Y
	 * @param atlasSectionWidth  the atlas section width
	 * @param atlasSectionHeight the atlas section height
	 */
  private void updateAtlasTextureSection(
          final int atlasTextureId,
          @Nullable final ByteBuffer imageData,
          final int atlasSectionX,
          final int atlasSectionY,
          final int atlasSectionWidth,
          final int atlasSectionHeight) {
    if (imageData == null) {
      log.severe("Attempted to update section of atlas texture (id: " + atlasTextureId + ") with null image data!");
      return;
    }
    log.fine("updateAtlasTextureSection with atlas texture id: " + atlasTextureId);
    bindAtlasTexture(atlasTextureId);
    // TODO Move this OpenGL call and error check to CoreTexture2D!
    gl.glTexSubImage2D(
            gl.GL_TEXTURE_2D(),
            0,
            atlasSectionX,
            atlasSectionY,
            atlasSectionWidth,
            atlasSectionHeight,
            gl.GL_RGBA(),
            gl.GL_UNSIGNED_BYTE(),
            imageData);
    CheckGL.checkGLError(gl, "Failed to update section [x, y, w, h]: [" + atlasSectionX + ", " + atlasSectionY + ", " +
            atlasSectionWidth + ", " + atlasSectionHeight + "] of atlas texture with id: " + atlasTextureId + ".");
  }

  /**
	 * Bind atlas texture.
	 *
	 * @param atlasTextureId the atlas texture id
	 */
  private void bindAtlasTexture(final int atlasTextureId) {
    log.fine("bindAtlasTexture with atlas texture id: " + atlasTextureId);
    log.fine("bindAtlasTexture: available atlas texture ids:");
    for (int textureId : atlasTextures.keySet()) {
      log.fine("bindAtlasTexture: available atlas texture id: " + textureId);
    }
    getAtlasTexture(atlasTextureId).bind();
  }

  /**
	 * Gets the atlas texture.
	 *
	 * @param atlasTextureId the atlas texture id
	 * @return the atlas texture
	 */
  private CoreTexture2D getAtlasTexture(final int atlasTextureId) {
    return atlasTextures.get(atlasTextureId);
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
  private int createNonAtlasTextureInternal(@Nonnull final ByteBuffer imageData, final int width, final int height)
          throws Exception {
    CoreTexture2D nonAtlasTexture = createTexture(imageData, width, height);
    nonAtlasTextures.put(nonAtlasTexture.getId(), nonAtlasTexture);
    return nonAtlasTexture.getId();
  }

  /**
	 * Delete non atlas texture internal.
	 *
	 * @param nonAtlasTextureId the non atlas texture id
	 */
  private void deleteNonAtlasTextureInternal(final int nonAtlasTextureId) {
    getNonAtlasTexture(nonAtlasTextureId).dispose();
    nonAtlasTextures.remove(nonAtlasTextureId);
  }

  /**
	 * Gets the non atlas texture.
	 *
	 * @param nonAtlasTextureId the non atlas texture id
	 * @return the non atlas texture
	 */
  private CoreTexture2D getNonAtlasTexture(final int nonAtlasTextureId) {
    return nonAtlasTextures.get(nonAtlasTextureId);
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
	 * Find texture.
	 *
	 * @param textureId the texture id
	 * @return the core texture 2 D
	 */
  private CoreTexture2D findTexture(final int textureId) {
    if (atlasTextures.containsKey(textureId)) {
      return atlasTextures.get(textureId);
    } else if (nonAtlasTextures.containsKey(textureId)) {
      return nonAtlasTextures.get(textureId);
    } else {
      throw new IllegalStateException("Cannot find texture with id: " + textureId);
    }
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
	 * @return the core batch
	 */
  @Nonnull
  private CoreBatch createNewBatch() {
    return batchPool.allocate();
  }

  /**
	 * Adds the batch.
	 *
	 * @param batch the batch
	 */
  private void addBatch (@Nonnull final CoreBatch batch) {
    batches.add(batch);
  }

  /**
	 * Render batches.
	 */
  private void renderBatches() {
    for (CoreBatch batch : batches) {
      batch.render();
    }
  }

  /**
	 * Begin rendering.
	 */
  private void beginRendering() {
    gl.glActiveTexture(gl.GL_TEXTURE0());
    gl.glBindSampler(0, 0); // make sure default tex unit and sampler are bound
    gl.glEnable(gl.GL_BLEND());
    gl.glEnable(gl.GL_PRIMITIVE_RESTART());
    gl.glPrimitiveRestartIndex(PRIMITIVE_RESTART_INDEX);
  }

  /**
	 * End rendering.
	 */
  private void endRendering() {
    gl.glDisable(gl.GL_PRIMITIVE_RESTART());
    gl.glDisable(gl.GL_BLEND());
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
