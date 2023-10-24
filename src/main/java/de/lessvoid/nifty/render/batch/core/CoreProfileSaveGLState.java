package de.lessvoid.nifty.render.batch.core;

import java.nio.IntBuffer;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.render.batch.CheckGL;
import de.lessvoid.nifty.render.batch.spi.BufferFactory;
import de.lessvoid.nifty.render.batch.spi.core.CoreGL;

/**
 * The Class CoreProfileSaveGLState.
 *
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 * 
 *         Note: Requires OpenGL 3.2 or greater.
 */
public class CoreProfileSaveGLState {
  
  /** The gl. */
  @Nonnull
  private final CoreGL gl;
  
  /** The params. */
  @Nonnull
  private final IntBuffer params;
  
  /** The current program. */
  private int currentProgram;
  
  /** The texture binding. */
  private int textureBinding;
  
  /** The active texture. */
  private int activeTexture;
  
  /** The sampler binding tex 0. */
  private int samplerBindingTex0;
  
  /** The blending. */
  private boolean blending;
  
  /** The blending src factor. */
  private int blendingSrcFactor;
  
  /** The blending dst factor. */
  private int blendingDstFactor;
  
  /** The primitive restart. */
  private boolean primitiveRestart;
  
  /** The primitive restart index. */
  private int primitiveRestartIndex;

  /**
	 * Instantiates a new core profile save GL state.
	 *
	 * @param gl            the gl
	 * @param bufferFactory the buffer factory
	 */
  public CoreProfileSaveGLState(@Nonnull final CoreGL gl, @Nonnull final BufferFactory bufferFactory) {
    this.gl = gl;
    params = bufferFactory.createNativeOrderedIntBuffer(16);
  }

  /**
	 * Save core.
	 */
  public void saveCore() {
    params.clear();
    gl.glGetIntegerv(gl.GL_CURRENT_PROGRAM(), params);
    currentProgram = params.get(0);

    params.clear();
    gl.glGetIntegerv(gl.GL_ACTIVE_TEXTURE(), params);
    activeTexture = params.get(0);

    params.clear();
    // set active texture to zero for reading sampler binding
    gl.glActiveTexture(gl.GL_TEXTURE0 ());
    gl.glGetIntegerv(gl.GL_SAMPLER_BINDING(), params);
    samplerBindingTex0 = params.get(0);

    params.clear();
    gl.glGetIntegerv(gl.GL_TEXTURE_BINDING_2D(), params);
    textureBinding = params.get(0);

    blending = gl.glIsEnabled(gl.GL_BLEND());

    params.clear();
    gl.glGetIntegerv(gl.GL_BLEND_SRC(), params);
    blendingSrcFactor = params.get(0);

    params.clear();
    gl.glGetIntegerv(gl.GL_BLEND_DST(), params);
    blendingDstFactor = params.get(0);

    primitiveRestart = gl.glIsEnabled(gl.GL_PRIMITIVE_RESTART());

    params.clear();
    gl.glGetIntegerv(gl.GL_PRIMITIVE_RESTART_INDEX(), params);
    primitiveRestartIndex = params.get(0);

    CheckGL.checkGLError(gl, "Failed to save OpenGL Core Profile state!", true);
  }

  /**
	 * Restore core.
	 */
  public void restoreCore() {
    gl.glUseProgram(currentProgram);
    gl.glActiveTexture(activeTexture);
    gl.glBindSampler(0, samplerBindingTex0);
    gl.glBindTexture(gl.GL_TEXTURE_2D(), textureBinding);
    enable(gl.GL_BLEND(), blending);
    gl.glBlendFunc(blendingSrcFactor, blendingDstFactor);
    enable(gl.GL_PRIMITIVE_RESTART(), primitiveRestart);
    gl.glPrimitiveRestartIndex(primitiveRestartIndex);
    CheckGL.checkGLError(gl, "Failed to restore OpenGL Core Profile state!", true);
  }

  /**
	 * Enable.
	 *
	 * @param state the state
	 * @param value the value
	 */
  private void enable(final int state, final boolean value) {
    if (value) {
      gl.glEnable(state);
    } else {
      gl.glDisable(state);
    }
  }
}
