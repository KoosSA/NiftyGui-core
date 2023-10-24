package de.lessvoid.nifty.render.batch.spi;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * OpenGL compatibility (non-core-profile) abstraction to make it easy for internal OpenGL-based rendering classes to
 * make direct OpenGL calls, rather than forcing external rendering implementations to reinvent the wheel. For an
 * example of how this abstraction may be useful, see {@link de.lessvoid.nifty.render.batch.BatchRenderBackendInternal}.
 * This interface is not intended (at this time) to provide a complete OpenGL abstraction, but only access to the
 * methods (and constants) currently in use by internal OpenGL-based rendering classes.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface GL {
  
  /**
	 * Gl alpha.
	 *
	 * @return the int
	 */
  // OpenGL constants
  public int GL_ALPHA();
  
  /**
	 * Gl alpha test.
	 *
	 * @return the int
	 */
  public int GL_ALPHA_TEST();
  
  /**
	 * Gl blend.
	 *
	 * @return the int
	 */
  public int GL_BLEND();
  
  /**
	 * Gl blend dst.
	 *
	 * @return the int
	 */
  public int GL_BLEND_DST();
  
  /**
	 * Gl blend src.
	 *
	 * @return the int
	 */
  public int GL_BLEND_SRC();
  
  /**
	 * Gl byte.
	 *
	 * @return the int
	 */
  public int GL_BYTE();
  
  /**
	 * Gl color array.
	 *
	 * @return the int
	 */
  public int GL_COLOR_ARRAY();
  
  /**
	 * Gl color buffer bit.
	 *
	 * @return the int
	 */
  public int GL_COLOR_BUFFER_BIT();
  
  /**
	 * Gl cull face.
	 *
	 * @return the int
	 */
  public int GL_CULL_FACE();
  
  /**
	 * Gl depth test.
	 *
	 * @return the int
	 */
  public int GL_DEPTH_TEST();
  
  /**
	 * Gl dst color.
	 *
	 * @return the int
	 */
  public int GL_DST_COLOR();
  
  /**
	 * Gl false.
	 *
	 * @return the int
	 */
  public int GL_FALSE();
  
  /**
	 * Gl float.
	 *
	 * @return the int
	 */
  public int GL_FLOAT();
  
  /**
	 * Gl invalid enum.
	 *
	 * @return the int
	 */
  public int GL_INVALID_ENUM();
  
  /**
	 * Gl invalid operation.
	 *
	 * @return the int
	 */
  public int GL_INVALID_OPERATION();
  
  /**
	 * Gl invalid value.
	 *
	 * @return the int
	 */
  public int GL_INVALID_VALUE();
  
  /**
	 * Gl lighting.
	 *
	 * @return the int
	 */
  public int GL_LIGHTING();
  
  /**
	 * Gl linear.
	 *
	 * @return the int
	 */
  public int GL_LINEAR();
  
  /**
	 * Gl linear mipmap linear.
	 *
	 * @return the int
	 */
  public int GL_LINEAR_MIPMAP_LINEAR();
  
  /**
	 * Gl linear mipmap nearest.
	 *
	 * @return the int
	 */
  public int GL_LINEAR_MIPMAP_NEAREST();
  
  /**
	 * Gl luminance.
	 *
	 * @return the int
	 */
  public int GL_LUMINANCE();
  
  /**
	 * Gl luminance alpha.
	 *
	 * @return the int
	 */
  public int GL_LUMINANCE_ALPHA();
  
  /**
	 * Gl max texture size.
	 *
	 * @return the int
	 */
  public int GL_MAX_TEXTURE_SIZE();
  
  /**
	 * Gl modelview.
	 *
	 * @return the int
	 */
  public int GL_MODELVIEW();
  
  /**
	 * Gl nearest.
	 *
	 * @return the int
	 */
  public int GL_NEAREST();
  
  /**
	 * Gl nearest mipmap linear.
	 *
	 * @return the int
	 */
  public int GL_NEAREST_MIPMAP_LINEAR();
  
  /**
	 * Gl nearest mipmap nearest.
	 *
	 * @return the int
	 */
  public int GL_NEAREST_MIPMAP_NEAREST();
  
  /**
	 * Gl no error.
	 *
	 * @return the int
	 */
  public int GL_NO_ERROR();
  
  /**
	 * Gl notequal.
	 *
	 * @return the int
	 */
  public int GL_NOTEQUAL();
  
  /**
	 * Gl one minus src alpha.
	 *
	 * @return the int
	 */
  public int GL_ONE_MINUS_SRC_ALPHA();
  
  /**
	 * Gl out of memory.
	 *
	 * @return the int
	 */
  public int GL_OUT_OF_MEMORY();
  
  /**
	 * Gl points.
	 *
	 * @return the int
	 */
  public int GL_POINTS();
  
  /**
	 * Gl projection.
	 *
	 * @return the int
	 */
  public int GL_PROJECTION();
  
  /**
	 * Gl rgb.
	 *
	 * @return the int
	 */
  public int GL_RGB();
  
  /**
	 * Gl rgba.
	 *
	 * @return the int
	 */
  public int GL_RGBA();
  
  /**
	 * Gl short.
	 *
	 * @return the int
	 */
  public int GL_SHORT();
  
  /**
	 * Gl src alpha.
	 *
	 * @return the int
	 */
  public int GL_SRC_ALPHA();
  
  /**
	 * Gl stack overflow.
	 *
	 * @return the int
	 */
  public int GL_STACK_OVERFLOW();
  
  /**
	 * Gl stack underflow.
	 *
	 * @return the int
	 */
  public int GL_STACK_UNDERFLOW();
  
  /**
	 * Gl texture 2d.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE_2D();
  
  /**
	 * Gl texture binding 2d.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE_BINDING_2D();
  
  /**
	 * Gl texture coord array.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE_COORD_ARRAY();
  
  /**
	 * Gl texture mag filter.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE_MAG_FILTER();
  
  /**
	 * Gl texture min filter.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE_MIN_FILTER();
  
  /**
	 * Gl triangles.
	 *
	 * @return the int
	 */
  public int GL_TRIANGLES();
  
  /**
	 * Gl triangle strip.
	 *
	 * @return the int
	 */
  public int GL_TRIANGLE_STRIP();
  
  /**
	 * Gl triangle fan.
	 *
	 * @return the int
	 */
  public int GL_TRIANGLE_FAN();
  
  /**
	 * Gl true.
	 *
	 * @return the int
	 */
  public int GL_TRUE();
  
  /**
	 * Gl unsigned byte.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_BYTE();
  
  /**
	 * Gl unsigned short.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_SHORT();
  
  /**
	 * Gl unsigned short 4 4 4 4.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_SHORT_4_4_4_4();
  
  /**
	 * Gl unsigned short 5 5 5 1.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_SHORT_5_5_5_1();
  
  /**
	 * Gl unsigned short 5 6 5.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_SHORT_5_6_5();
  
  /**
	 * Gl vertex array.
	 *
	 * @return the int
	 */
  public int GL_VERTEX_ARRAY();
  
  /**
	 * Gl viewport.
	 *
	 * @return the int
	 */
  public int GL_VIEWPORT();
  
  /**
	 * Gl zero.
	 *
	 * @return the int
	 */
  public int GL_ZERO();

  /**
	 * Gl alpha func.
	 *
	 * @param func the func
	 * @param ref  the ref
	 */
  // OpenGL methods
  public void glAlphaFunc (int func, float ref);
  
  /**
	 * Gl bind texture.
	 *
	 * @param target  the target
	 * @param texture the texture
	 */
  public void glBindTexture (int target, int texture);
  
  /**
	 * Gl blend func.
	 *
	 * @param sfactor the sfactor
	 * @param dfactor the dfactor
	 */
  public void glBlendFunc (int sfactor, int dfactor);
  
  /**
	 * Gl clear.
	 *
	 * @param mask the mask
	 */
  public void glClear (int mask);
  
  /**
	 * Gl clear color.
	 *
	 * @param red   the red
	 * @param green the green
	 * @param blue  the blue
	 * @param alpha the alpha
	 */
  public void glClearColor (float red, float green, float blue, float alpha);
  
  /**
	 * Gl color pointer.
	 *
	 * @param size    the size
	 * @param type    the type
	 * @param stride  the stride
	 * @param pointer the pointer
	 */
  public void glColorPointer (int size, int type, int stride, FloatBuffer pointer);
  
  /**
	 * Gl delete textures.
	 *
	 * @param n        the n
	 * @param textures the textures
	 */
  public void glDeleteTextures (int n, IntBuffer textures);
  
  /**
	 * Gl disable.
	 *
	 * @param cap the cap
	 */
  public void glDisable (int cap);
  
  /**
	 * Gl disable client state.
	 *
	 * @param array the array
	 */
  public void glDisableClientState (int array);
  
  /**
	 * Gl draw arrays.
	 *
	 * @param mode  the mode
	 * @param first the first
	 * @param count the count
	 */
  public void glDrawArrays (int mode, int first, int count);
  
  /**
	 * Gl draw elements.
	 *
	 * @param mode    the mode
	 * @param count   the count
	 * @param type    the type
	 * @param indices the indices
	 */
  public void glDrawElements(int mode, int count, int type, int indices);
  
  /**
	 * Gl enable.
	 *
	 * @param cap the cap
	 */
  public void glEnable (int cap);
  
  /**
	 * Gl enable client state.
	 *
	 * @param array the array
	 */
  public void glEnableClientState (int array);
  
  /**
	 * Gl gen textures.
	 *
	 * @param n        the n
	 * @param textures the textures
	 */
  public void glGenTextures (int n, IntBuffer textures);
  
  /**
	 * Gl get error.
	 *
	 * @return the int
	 */
  public int glGetError();
  
  /**
	 * Gl get integerv.
	 *
	 * @param pname  the pname
	 * @param params the params
	 * @param offset the offset
	 */
  public void glGetIntegerv (int pname, int[] params, int offset);
  
  /**
	 * Gl get integerv.
	 *
	 * @param pname  the pname
	 * @param params the params
	 */
  public void glGetIntegerv (int pname, IntBuffer params);
  
  /**
	 * Gl is enabled.
	 *
	 * @param cap the cap
	 * @return true, if successful
	 */
  public boolean glIsEnabled(int cap);
  
  /**
	 * Gl load identity.
	 */
  public void glLoadIdentity();
  
  /**
	 * Gl matrix mode.
	 *
	 * @param mode the mode
	 */
  public void glMatrixMode (int mode);
  
  /**
	 * Gl orthof.
	 *
	 * @param left   the left
	 * @param right  the right
	 * @param bottom the bottom
	 * @param top    the top
	 * @param zNear  the z near
	 * @param zFar   the z far
	 */
  public void glOrthof (float left, float right, float bottom, float top, float zNear, float zFar);
  
  /**
	 * Gl tex coord pointer.
	 *
	 * @param size    the size
	 * @param type    the type
	 * @param stride  the stride
	 * @param pointer the pointer
	 */
  public void glTexCoordPointer (int size, int type, int stride, FloatBuffer pointer);
  
  /**
	 * Gl tex image 2 D.
	 *
	 * @param target         the target
	 * @param level          the level
	 * @param internalformat the internalformat
	 * @param width          the width
	 * @param height         the height
	 * @param border         the border
	 * @param format         the format
	 * @param type           the type
	 * @param pixels         the pixels
	 */
  public void glTexImage2D (int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels);
  
  /**
	 * Gl tex image 2 D.
	 *
	 * @param target         the target
	 * @param level          the level
	 * @param internalformat the internalformat
	 * @param width          the width
	 * @param height         the height
	 * @param border         the border
	 * @param format         the format
	 * @param type           the type
	 * @param pixels         the pixels
	 */
  public void glTexImage2D (int target, int level, int internalformat, int width, int height, int border, int format, int type, DoubleBuffer pixels);
  
  /**
	 * Gl tex image 2 D.
	 *
	 * @param target         the target
	 * @param level          the level
	 * @param internalformat the internalformat
	 * @param width          the width
	 * @param height         the height
	 * @param border         the border
	 * @param format         the format
	 * @param type           the type
	 * @param pixels         the pixels
	 */
  public void glTexImage2D (int target, int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels);
  
  /**
	 * Gl tex image 2 D.
	 *
	 * @param target         the target
	 * @param level          the level
	 * @param internalformat the internalformat
	 * @param width          the width
	 * @param height         the height
	 * @param border         the border
	 * @param format         the format
	 * @param type           the type
	 * @param pixels         the pixels
	 */
  public void glTexImage2D (int target, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels);
  
  /**
	 * Gl tex image 2 D.
	 *
	 * @param target         the target
	 * @param level          the level
	 * @param internalformat the internalformat
	 * @param width          the width
	 * @param height         the height
	 * @param border         the border
	 * @param format         the format
	 * @param type           the type
	 * @param pixels         the pixels
	 */
  public void glTexImage2D (int target, int level, int internalformat, int width, int height, int border, int format, int type, ShortBuffer pixels);
  
  /**
	 * Gl tex parameterf.
	 *
	 * @param target the target
	 * @param pname  the pname
	 * @param param  the param
	 */
  public void glTexParameterf (int target, int pname, float param);
  
  /**
	 * Gl tex parameteri.
	 *
	 * @param target the target
	 * @param pname  the pname
	 * @param param  the param
	 */
  public void glTexParameteri(int target, int pname, int param);
  
  /**
	 * Gl tex sub image 2 D.
	 *
	 * @param target  the target
	 * @param level   the level
	 * @param xoffset the xoffset
	 * @param yoffset the yoffset
	 * @param width   the width
	 * @param height  the height
	 * @param format  the format
	 * @param type    the type
	 * @param pixels  the pixels
	 */
  public void glTexSubImage2D (int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels);
  
  /**
	 * Gl translatef.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
  public void glTranslatef (float x, float y, float z);
  
  /**
	 * Gl vertex pointer.
	 *
	 * @param size    the size
	 * @param type    the type
	 * @param stride  the stride
	 * @param pointer the pointer
	 */
  public void glVertexPointer (int size, int type, int stride, FloatBuffer pointer);
  
  /**
	 * Gl viewport.
	 *
	 * @param x      the x
	 * @param y      the y
	 * @param width  the width
	 * @param height the height
	 */
  public void glViewport (int x, int y, int width, int height);
}
