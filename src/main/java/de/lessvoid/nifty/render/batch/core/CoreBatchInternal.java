package de.lessvoid.nifty.render.batch.core;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.render.batch.spi.BufferFactory;
import de.lessvoid.nifty.render.batch.spi.core.CoreBatch;
import de.lessvoid.nifty.render.batch.spi.core.CoreGL;
import de.lessvoid.nifty.tools.Color;

/**
 * Internal implementation for OpenGL Core Profile batch management that gives OpenGL-based
 * {@link de.lessvoid.nifty.render.batch.spi.BatchRenderBackend} implementations some default functionality to avoid having to
 * reinvent the wheel and to prevent unnecessary code duplication. Suitable for desktop devices.
 *
 * Note: Requires OpenGL 3.2 or higher. Mobiles devices & OpenGL ES are not officially supported yet with this class.
 *
 * {@inheritDoc}
 *
 * @author void256
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class CoreBatchInternal implements CoreBatch {
  // 4 vertices per quad and 8 vertex attributes per vertex:
  // - 2 x pos
  // - 2 x texture
  /** The Constant PRIMITIVE_SIZE. */
  // - 4 x color
  private static final int PRIMITIVE_SIZE = 4 * 8;
  
  /** The Constant SIZE. */
  private static final int SIZE = 64 * 1024; // 64k
  
  /** The gl. */
  @Nonnull
  private final CoreGL gl;
  
  /** The primitive buffer. */
  @Nonnull
  private float[] primitiveBuffer = new float[PRIMITIVE_SIZE];
  
  /** The element index buffer. */
  @Nonnull
  private int[] elementIndexBuffer = new int[5];
  
  /** The blend mode. */
  @Nonnull
  private BlendMode blendMode = BlendMode.BLEND;
  
  /** The vao. */
  @Nonnull
  private final CoreVAO vao;
  
  /** The vbo. */
  @Nonnull
  private final CoreVBO vbo;
  
  /** The element vbo. */
  @Nonnull
  private final CoreElementVBO elementVbo;
  
  /** The primitive restart index. */
  private final int primitiveRestartIndex;
  
  /** The texture. */
  private CoreTexture2D texture;
  
  /** The primitive count. */
  private int primitiveCount;
  
  /** The index count. */
  private int indexCount;
  
  /** The global index. */
  private int globalIndex;

  /**
	 * Instantiates a new core batch internal.
	 *
	 * @param gl                    the gl
	 * @param shader                the shader
	 * @param bufferFactory         the buffer factory
	 * @param primitiveRestartIndex the primitive restart index
	 */
  public CoreBatchInternal(
          @Nonnull final CoreGL gl,
          @Nonnull final CoreShader shader,
          @Nonnull final BufferFactory bufferFactory,
          final int primitiveRestartIndex) {
    this.gl = gl;
    this.primitiveRestartIndex = primitiveRestartIndex;
    vao = new CoreVAO(gl, bufferFactory);
    vao.bind();

    elementVbo = CoreElementVBO.createStreamVBO(gl, bufferFactory, new int[SIZE]);
    elementVbo.bind();

    vbo = CoreVBO.createStreamVBO(gl, bufferFactory, new float[SIZE]);
    vbo.bind();

    vao.enableVertexAttributef(shader.getAttribLocation("aVertex"), 2, 8, 0);
    vao.enableVertexAttributef(shader.getAttribLocation("aColor"), 4, 8, 2);
    vao.enableVertexAttributef(shader.getAttribLocation("aTexture"), 2, 8, 6);

    primitiveCount = 0;
    globalIndex = 0;
    indexCount = 0;
    vao.unbind();
  }

  /**
	 * Begin.
	 *
	 * @param blendMode the blend mode
	 * @param texture   the texture
	 */
  @Override
  public void begin(@Nonnull BlendMode blendMode, CoreTexture2D texture) {
    this.texture = texture;
    vao.bind();
    vbo.bind();
    vbo.getBuffer().clear();
    elementVbo.bind();
    elementVbo.getBuffer().clear();
    primitiveCount = 0;
    globalIndex = 0;
    indexCount = 0;
    vao.unbind();
  }

  /**
	 * Gets the blend mode.
	 *
	 * @return the blend mode
	 */
  @Nonnull
  @Override
  public BlendMode getBlendMode() {
    return blendMode;
  }

  /**
	 * Render.
	 */
  @Override
  public void render() {
    if (primitiveCount == 0) {
      return; // Attempting to render with an empty vertex buffer crashes the program.
    }

    texture.bind();

    if (blendMode.equals(BlendMode.BLEND)) {
      gl.glBlendFunc(gl.GL_SRC_ALPHA(), gl.GL_ONE_MINUS_SRC_ALPHA());
    } else if (blendMode.equals(BlendMode.MULIPLY)) {
      gl.glBlendFunc(gl.GL_DST_COLOR(), gl.GL_ZERO());
    }

    vao.bind();
    vbo.getBuffer().flip();
    vbo.bind();
    vbo.send();
    elementVbo.getBuffer().flip();
    elementVbo.bind();
    elementVbo.send();
    CoreRender.renderTriangleStripIndexed(gl, indexCount);
  }

  /**
	 * Can add quad.
	 *
	 * @return true, if successful
	 */
  @Override
  public boolean canAddQuad() {
    return ((primitiveCount + 1) * PRIMITIVE_SIZE) < SIZE;
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
	 */
  @Override
  public void addQuad(
          final float x,
          final float y,
          final float width,
          final float height,
          final @Nonnull Color color1,
          final @Nonnull Color color2,
          final @Nonnull Color color3,
          final @Nonnull Color color4,
          final float textureX,
          final float textureY,
          final float textureWidth,
          final float textureHeight) {
    int bufferIndex = 0;
    int elementIndexBufferIndex = 0;

    primitiveBuffer[bufferIndex++] = x;
    primitiveBuffer[bufferIndex++] = y + height;
    primitiveBuffer[bufferIndex++] = color3.getRed();
    primitiveBuffer[bufferIndex++] = color3.getGreen();
    primitiveBuffer[bufferIndex++] = color3.getBlue();
    primitiveBuffer[bufferIndex++] = color3.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX;
    primitiveBuffer[bufferIndex++] = textureY + textureHeight;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;

    primitiveBuffer[bufferIndex++] = x + width;
    primitiveBuffer[bufferIndex++] = y + height;
    primitiveBuffer[bufferIndex++] = color4.getRed();
    primitiveBuffer[bufferIndex++] = color4.getGreen();
    primitiveBuffer[bufferIndex++] = color4.getBlue();
    primitiveBuffer[bufferIndex++] = color4.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX + textureWidth;
    primitiveBuffer[bufferIndex++] = textureY + textureHeight;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;

    primitiveBuffer[bufferIndex++] = x;
    primitiveBuffer[bufferIndex++] = y;
    primitiveBuffer[bufferIndex++] = color1.getRed();
    primitiveBuffer[bufferIndex++] = color1.getGreen();
    primitiveBuffer[bufferIndex++] = color1.getBlue();
    primitiveBuffer[bufferIndex++] = color1.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX;
    primitiveBuffer[bufferIndex++] = textureY;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;

    primitiveBuffer[bufferIndex++] = x + width;
    primitiveBuffer[bufferIndex++] = y;
    primitiveBuffer[bufferIndex++] = color2.getRed();
    primitiveBuffer[bufferIndex++] = color2.getGreen();
    primitiveBuffer[bufferIndex++] = color2.getBlue();
    primitiveBuffer[bufferIndex++] = color2.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX + textureWidth;
    primitiveBuffer[bufferIndex] = textureY;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;
    elementIndexBuffer[elementIndexBufferIndex] = primitiveRestartIndex;

    indexCount += 5;

    vbo.getBuffer().put(primitiveBuffer);
    elementVbo.getBuffer().put(elementIndexBuffer);
    primitiveCount++;
  }
}
