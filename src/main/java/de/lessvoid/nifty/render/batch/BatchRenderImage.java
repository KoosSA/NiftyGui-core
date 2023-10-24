package de.lessvoid.nifty.render.batch;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.render.batch.TextureAtlasGenerator.Result;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend.Image;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * The Class BatchRenderImage.
 *
 * @author void
 */
public class BatchRenderImage implements RenderImage {
	
	/** The Constant log. */
	@Nonnull
	private static final Logger log = Logger.getLogger(BatchRenderImage.class.getName());
	
	/** The texture sizes. */
	@Nonnull
	private static Map<Integer, TextureSize> textureSizes = new HashMap<Integer, TextureSize>(); // provides the size of
																									// a texture
																									// represented by a
																									// specific texture
																									/** The image. */
																									// id
	@Nonnull
	private final Image image; // the image in the format needed by the rendering backend
	
	/** The filename. */
	@Nonnull
	private final String filename; // the filename associated with this image
	
	/** The render backend. */
	@Nonnull
	private final BatchRenderBackend renderBackend; // the rendering backend to delegate low level texture handling to
	
	/** The generator. */
	@Nonnull
	private TextureAtlasGenerator generator; // mainly used to determine whether the image will fit in the specified
												
												/** The x. */
												// atlas (the "brain" of the texture atlas)
	private int x; // the x location of the image in an atlas, or 0 for non-atlas images
	
	/** The y. */
	private int y; // the y location of the image in an atlas, or 0 for non-atlas images
	
	/** The texture id. */
	private int textureId; // the texture id of the atlas texture this image part of, or the texture id of
							
							/** The is uploaded. */
							// the non-atlas texture representing this image
	private boolean isUploaded; // whether this image was uploaded (created as a texture) yet
	
	/** The should unload. */
	private boolean shouldUnload; // whether this image should be unloaded when unload() is called on it
	
	/** The upload failed. */
	private boolean uploadFailed; // will be set to true if this image already failed at an attempted upload
	
	/** The result. */
	@Nullable
	private Result result; // the result returned by processing this image with a TextureAtlasGenerator

	/**
	 * Instantiates a new batch render image.
	 *
	 * @param image          The image in the format needed by the rendering backend
	 * @param filename       The filename associated with this image
	 * @param renderBackend  The rendering backend to delegate low level texture
	 *                       handling to
	 * @param generator      Mainly used to determine whether the image will fit in
	 *                       the specified atlas (the "brain" of the texture atlas)
	 * @param atlasTextureId The texture id of the atlas to use to attempt to upload
	 *                       the image to.
	 * @param shouldUnload   Whether or not to unload the image between screens.
	 */
	public BatchRenderImage(@Nonnull final Image image, @Nonnull final String filename,
			@Nonnull final BatchRenderBackend renderBackend, @Nonnull final TextureAtlasGenerator generator,
			final int atlasTextureId, final boolean shouldUnload) {
		log.setLevel(Level.WARNING);
		this.image = image;
		this.filename = filename;
		this.generator = generator;
		this.renderBackend = renderBackend;
		this.shouldUnload = shouldUnload;
		// the final x, y, and textureId will be calculated in the upload() method
		textureId = atlasTextureId;
		x = 0;
		y = 0;
		isUploaded = false;
		uploadFailed = false;
	}

	/**
	 * The Class TextureSize.
	 */
	public static class TextureSize {
		
		/** The width. */
		private final int width;
		
		/** The height. */
		private final int height;

		/**
		 * Instantiates a new texture size.
		 *
		 * @param width  the width
		 * @param height the height
		 */
		public TextureSize(final int width, final int height) {
			this.width = width;
			this.height = height;
		}

		/**
		 * Gets the width.
		 *
		 * @return the width
		 */
		public int getWidth() {
			return width;
		}

		/**
		 * Gets the height.
		 *
		 * @return the height
		 */
		public int getHeight() {
			return height;
		}
	}

	/**
	 * Gets the texture size.
	 *
	 * @param textureId the texture id
	 * @return the texture size
	 */
	@Nullable
	public static TextureSize getTextureSize(final int textureId) {
		return textureSizes.get(textureId);
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	@Override
	public int getWidth() {
		return image.getWidth();
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	@Override
	public int getHeight() {
		return image.getHeight();
	}

	/**
	 * Dispose.
	 */
	@Override
	public void dispose() {
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the texture id.
	 *
	 * @return the texture id
	 */
	public int getTextureId() {
		return textureId;
	}

	/**
	 * Checks if is uploaded.
	 *
	 * @return true, if is uploaded
	 */
	public boolean isUploaded() {
		return isUploaded;
	}

	/**
	 * Mark as unloaded.
	 */
	public void markAsUnloaded() {
		if (shouldUnload) {
			isUploaded = false;
			uploadFailed = false;
			log.fine("image [" + filename + "] marked as unloaded");
		}
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		return super.toString() + " {" + filename + "}";
	}

	// You can only call this once. After that, the image will either be uploaded or
	// will have failed to upload.
	// In either case, calling it again has no effect. Try the reUpload method if
	/**
	 * Upload.
	 */
	// the upload fails.
	public void upload() {
		if (isUploaded || uploadFailed) {
			return;
		}

		preProcessImageUpload();

		if (imageWillFitInAtlas()) {
			uploadImageToAtlas();
		} else if (imageExceedsAtlasTolerance()) {
			uploadNonAtlasImage();
		} else {
			uploadFailedBecauseAtlasIsFull();
		}
	}

	// Reattempts uploading with the specified atlas texture id and texture atlas
	// generator.
	/**
	 * Re upload.
	 *
	 * @param atlasTextureId the atlas texture id
	 * @param generator      the generator
	 */
	// Don't bother calling this if the uploadFailedPermanently method returns true.
	public void reUpload(final int atlasTextureId, @Nonnull final TextureAtlasGenerator generator) {
		textureId = atlasTextureId;
		this.generator = generator;
		isUploaded = false;
		uploadFailed = false;
		upload();
	}

	/**
	 * Upload failed permanently.
	 *
	 * @return true, if successful
	 */
	public boolean uploadFailedPermanently() {
		// If we've already failed once before, and if the image exceeds atlas
		// tolerance, we'll never succeed.
		// (The reason why is that currently all atlases have the same tolerance, so if
		// you fail uploading to one atlas due
		// to tolerance, you fail uploading to all atlases due to tolerance, regardless
		// of how empty they might be).
		return uploadFailed && imageExceedsAtlasTolerance();
	}

	/**
	 * Unload.
	 */
	public void unload() {
		if (!isUploaded || !shouldUnload) {
			return;
		}

		preProcessImageUnloading();

		if (imageExistsInAtlas()) {
			unloadImageFromAtlas();
		} else if (existsAsNonAtlasImage()) {
			unloadNonAtlasImage();
		} else {
			unloadFailed();
		}

		isUploaded = false;
	}

	// Internal implementations

	/**
	 * Pre process image upload.
	 */
	private void preProcessImageUpload() {
		result = generator.addImage(image.getWidth(), image.getHeight(), filename);
	}

	/**
	 * Image will fit in atlas.
	 *
	 * @return true, if successful
	 */
	private boolean imageWillFitInAtlas() {
		return result != null;
	}

	/**
	 * Upload image to atlas.
	 */
	private void uploadImageToAtlas() {
		assert result != null;
		renderBackend.addImageToAtlas(image, result.getX(), result.getY(), textureId);
		BatchRenderImage.registerTextureSize(textureId, generator.getAtlasWidth(), generator.getAtlasHeight());
		x = result.getX();
		y = result.getY();
		isUploaded = true;
		log.info("Image [" + filename + "] uploaded to atlas (atlas texture id: " + textureId + ").");
	}

	/**
	 * Register texture size.
	 *
	 * @param textureId     the texture id
	 * @param textureWidth  the texture width
	 * @param textureHeight the texture height
	 */
	private static void registerTextureSize(final int textureId, final int textureWidth, final int textureHeight) {
		textureSizes.put(textureId, new TextureSize(textureWidth, textureHeight));
	}

	/**
	 * Image exceeds atlas tolerance.
	 *
	 * @return true, if successful
	 */
	private boolean imageExceedsAtlasTolerance() {
		return !generator.shouldAddImage(image.getWidth(), image.getHeight());
	}

	/**
	 * Upload non atlas image.
	 */
	private void uploadNonAtlasImage() {
		int textureId = createNonAtlasTexture();

		if (isCreatedNonAtlasTexture(textureId)) {
			uploadNonAtlasImageSuccessful(textureId);
		} else {
			uploadNonAtlasImageFailed();
		}
	}

	/**
	 * Creates the non atlas texture.
	 *
	 * @return the int
	 */
	private int createNonAtlasTexture() {
		return renderBackend.createNonAtlasTexture(image);
	}

	/**
	 * Checks if is created non atlas texture.
	 *
	 * @param textureId the texture id
	 * @return true, if is created non atlas texture
	 */
	private boolean isCreatedNonAtlasTexture(final int textureId) {
		return renderBackend.existsNonAtlasTexture(textureId);
	}

	/**
	 * Upload non atlas image successful.
	 *
	 * @param textureId the texture id
	 */
	private void uploadNonAtlasImageSuccessful(final int textureId) {
		this.textureId = textureId;
		BatchRenderImage.registerTextureSize(textureId, getWidth(), getHeight());
		isUploaded = true;
		log.info("Image [" + filename + "] is not within atlas tolerance and has been created as a non-atlas texture "
				+ "(texture id: " + textureId + ").");
	}

	/**
	 * Upload non atlas image failed.
	 */
	private void uploadNonAtlasImageFailed() {
		log.severe("Image [" + filename + "] is not within atlas tolerance but could not be created as a non-atlas "
				+ "texture.\nThis image will be missing from your screen. Some of the possible causes could be:\n"
				+ "1) Your BatchRenderBackend doesn't support non-atlas textures.\n"
				+ "2) There is a compatibility issue between your gpu and the image (size, format, file type, etc).\n"
				+ "3) You don't have enough memory to create the image.\n\n"
				+ "For support, please create a new issue at https://github.com/void256/nifty-gui");
		uploadFailed = true;
		textureId = -1;
	}

	/**
	 * Upload failed because atlas is full.
	 */
	private void uploadFailedBecauseAtlasIsFull() {
		log.info("Image [" + filename + "] did not fit into the texture atlas, yet it is within atlas tolerance.\n"
				+ "The current atlas (atlas texture id: " + textureId + ") is too full to hold this image.");
		uploadFailed = true;
		textureId = -1;
	}

	/**
	 * Pre process image unloading.
	 */
	private void preProcessImageUnloading() {
		result = generator.removeImage(filename);
	}

	/**
	 * Image exists in atlas.
	 *
	 * @return true, if successful
	 */
	private boolean imageExistsInAtlas() {
		return result != null;
	}

	/**
	 * Unload image from atlas.
	 */
	private void unloadImageFromAtlas() {
		assert result != null;
		renderBackend.removeImageFromAtlas(image, result.getX(), result.getY(), result.getOriginalImageWidth(),
				result.getOriginalImageHeight(), textureId);
		log.info("Image [" + filename + "] unloaded from texture atlas (atlas texture id: " + textureId + ").");
	}

	/**
	 * Deregister texture size.
	 *
	 * @param textureId the texture id
	 */
	private static void deregisterTextureSize(final int textureId) {
		textureSizes.remove(textureId);
	}

	/**
	 * Exists as non atlas image.
	 *
	 * @return true, if successful
	 */
	private boolean existsAsNonAtlasImage() {
		return renderBackend.existsNonAtlasTexture(textureId);
	}

	/**
	 * Unload non atlas image.
	 */
	private void unloadNonAtlasImage() {
		renderBackend.deleteNonAtlasTexture(textureId);
		deregisterTextureSize(textureId);
		log.info("Image [" + filename + "] unloaded (non-atlas texture, texture id: " + textureId + ")");
	}

	/**
	 * Unload failed.
	 */
	private void unloadFailed() {
		log.warning("Failed to unload image [" + filename + "] because its associated texture (texture id: " + textureId
				+ ") could not be found.");
	}
}
