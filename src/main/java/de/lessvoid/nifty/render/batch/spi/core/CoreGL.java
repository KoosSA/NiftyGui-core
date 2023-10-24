package de.lessvoid.nifty.render.batch.spi.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import de.lessvoid.nifty.render.batch.spi.GL;

/**
 * OpenGL Core Profile abstraction to make it easy for internal OpenGL-based rendering classes to make direct OpenGL
 * calls, rather than forcing external rendering implementations to reinvent the wheel. For an example of how this
 * abstraction may be useful, see {@link de.lessvoid.nifty.render.batch.core.BatchRenderBackendCoreProfileInternal}. This
 * interface is not intended (at this time) to provide a complete OpenGL abstraction, but only access to the methods
 * (and constants) currently in use by internal OpenGL-based rendering classes.
 *
 * Note: Requires OpenGL 3.2 or higher.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface CoreGL extends GL {
  
  /**
	 * Gl active texture.
	 *
	 * @return the int
	 */
  // OpenGL constants
  public int GL_ACTIVE_TEXTURE();
  
  /**
	 * Gl array buffer.
	 *
	 * @return the int
	 */
  public int GL_ARRAY_BUFFER();
  
  /**
	 * Gl bitmap.
	 *
	 * @return the int
	 */
  public int GL_BITMAP();
  
  /**
	 * Gl bgr.
	 *
	 * @return the int
	 */
  public int GL_BGR();
  
  /**
	 * Gl bgra.
	 *
	 * @return the int
	 */
  public int GL_BGRA();
  
  /**
	 * Gl blue.
	 *
	 * @return the int
	 */
  public int GL_BLUE();
  
  /**
	 * Gl color index.
	 *
	 * @return the int
	 */
  public int GL_COLOR_INDEX();
  
  /**
	 * Gl compile status.
	 *
	 * @return the int
	 */
  public int GL_COMPILE_STATUS();
  
  /**
	 * Gl compressed alpha.
	 *
	 * @return the int
	 */
  public int GL_COMPRESSED_ALPHA();
  
  /**
	 * Gl compressed luminance.
	 *
	 * @return the int
	 */
  public int GL_COMPRESSED_LUMINANCE();
  
  /**
	 * Gl compressed luminance alpha.
	 *
	 * @return the int
	 */
  public int GL_COMPRESSED_LUMINANCE_ALPHA();
  
  /**
	 * Gl compressed rgb.
	 *
	 * @return the int
	 */
  public int GL_COMPRESSED_RGB();
  
  /**
	 * Gl compressed rgba.
	 *
	 * @return the int
	 */
  public int GL_COMPRESSED_RGBA();
  
  /**
	 * Gl current program.
	 *
	 * @return the int
	 */
  public int GL_CURRENT_PROGRAM();
  
  /**
	 * Gl dynamic draw.
	 *
	 * @return the int
	 */
  public int GL_DYNAMIC_DRAW();
  
  /**
	 * Gl element array buffer.
	 *
	 * @return the int
	 */
  public int GL_ELEMENT_ARRAY_BUFFER();
  
  /**
	 * Gl fragment shader.
	 *
	 * @return the int
	 */
  public int GL_FRAGMENT_SHADER();
  
  /**
	 * Gl geometry shader.
	 *
	 * @return the int
	 */
  public int GL_GEOMETRY_SHADER();
  
  /**
	 * Gl green.
	 *
	 * @return the int
	 */
  public int GL_GREEN();
  
  /**
	 * Gl int.
	 *
	 * @return the int
	 */
  public int GL_INT();
  
  /**
	 * Gl link status.
	 *
	 * @return the int
	 */
  public int GL_LINK_STATUS();
  
  /**
	 * Gl primitive restart.
	 *
	 * @return the int
	 */
  public int GL_PRIMITIVE_RESTART();
  
  /**
	 * Gl primitive restart index.
	 *
	 * @return the int
	 */
  public int GL_PRIMITIVE_RESTART_INDEX();
  
  /**
	 * Gl red.
	 *
	 * @return the int
	 */
  public int GL_RED();
  
  /**
	 * Gl sampler binding.
	 *
	 * @return the int
	 */
  public int GL_SAMPLER_BINDING();
  
  /**
	 * Gl static draw.
	 *
	 * @return the int
	 */
  public int GL_STATIC_DRAW();
  
  /**
	 * Gl stream draw.
	 *
	 * @return the int
	 */
  public int GL_STREAM_DRAW();
  
  /**
	 * Gl texture0.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE0();
  
  /**
	 * Gl texture cube map negative x.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_X();
  
  /**
	 * Gl texture cube map negative y.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y();
  
  /**
	 * Gl texture cube map negative z.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z();
  
  /**
	 * Gl texture cube map positive x.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_X();
  
  /**
	 * Gl texture cube map positive y.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_Y();
  
  /**
	 * Gl texture cube map positive z.
	 *
	 * @return the int
	 */
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_Z();
  
  /**
	 * Gl unsigned byte 2 3 3 rev.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_BYTE_2_3_3_REV();
  
  /**
	 * Gl unsigned byte 3 3 2.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_BYTE_3_3_2();
  
  /**
	 * Gl unsigned int.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_INT();
  
  /**
	 * Gl unsigned int 10 10 10 2.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_INT_10_10_10_2();
  
  /**
	 * Gl unsigned int 2 10 10 10 rev.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_INT_2_10_10_10_REV();
  
  /**
	 * Gl unsigned int 8 8 8 8.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_INT_8_8_8_8();
  
  /**
	 * Gl unsigned int 8 8 8 8 rev.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_INT_8_8_8_8_REV();
  
  /**
	 * Gl unsigned short 5 6 5 rev.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_SHORT_5_6_5_REV();
  
  /**
	 * Gl unsigned short 4 4 4 4 rev.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_SHORT_4_4_4_4_REV();
  
  /**
	 * Gl unsigned short 1 5 5 5 rev.
	 *
	 * @return the int
	 */
  public int GL_UNSIGNED_SHORT_1_5_5_5_REV();
  
  /**
	 * Gl vertex shader.
	 *
	 * @return the int
	 */
  public int GL_VERTEX_SHADER();
  
  /**
	 * Gl write only.
	 *
	 * @return the int
	 */
  public int GL_WRITE_ONLY();

  /**
	 * Gl active texture.
	 *
	 * @param texture the texture
	 */
  // OpenGL methods
  public void glActiveTexture(int texture);
  
  /**
	 * Gl attach shader.
	 *
	 * @param program the program
	 * @param shader  the shader
	 */
  public void glAttachShader(int program, int shader);
  
  /**
	 * Gl bind attrib location.
	 *
	 * @param program the program
	 * @param index   the index
	 * @param name    the name
	 */
  public void glBindAttribLocation(int program, int index, String name);
  
  /**
	 * Gl bind buffer.
	 *
	 * @param target the target
	 * @param buffer the buffer
	 */
  public void glBindBuffer(int target, int buffer);
  
  /**
	 * Gl bind sampler.
	 *
	 * @param unit    the unit
	 * @param sampler the sampler
	 */
  public void glBindSampler(int unit, int sampler);
  
  /**
	 * Gl bind vertex array.
	 *
	 * @param array the array
	 */
  public void glBindVertexArray(int array);
  
  /**
	 * Gl buffer data.
	 *
	 * @param target the target
	 * @param data   the data
	 * @param usage  the usage
	 */
  public void glBufferData(int target, IntBuffer data, int usage);
  
  /**
	 * Gl buffer data.
	 *
	 * @param target the target
	 * @param data   the data
	 * @param usage  the usage
	 */
  public void glBufferData(int target, FloatBuffer data, int usage);
  
  /**
	 * Gl compile shader.
	 *
	 * @param shader the shader
	 */
  public void glCompileShader(int shader);
  
  /**
	 * Gl create program.
	 *
	 * @return the int
	 */
  public int glCreateProgram();
  
  /**
	 * Gl create shader.
	 *
	 * @param type the type
	 * @return the int
	 */
  public int glCreateShader(int type);
  
  /**
	 * Gl delete buffers.
	 *
	 * @param n       the n
	 * @param buffers the buffers
	 */
  public void glDeleteBuffers(int n, IntBuffer buffers);
  
  /**
	 * Gl delete vertex arrays.
	 *
	 * @param n      the n
	 * @param arrays the arrays
	 */
  public void glDeleteVertexArrays(int n, IntBuffer arrays);
  
  /**
	 * Gl draw arrays instanced.
	 *
	 * @param mode      the mode
	 * @param first     the first
	 * @param count     the count
	 * @param primcount the primcount
	 */
  public void glDrawArraysInstanced(int mode, int first, int count, int primcount);
  
  /**
	 * Gl enable vertex attrib array.
	 *
	 * @param index the index
	 */
  public void glEnableVertexAttribArray(int index);
  
  /**
	 * Gl gen buffers.
	 *
	 * @param n       the n
	 * @param buffers the buffers
	 */
  public void glGenBuffers(int n, IntBuffer buffers);
  
  /**
	 * Gl generate mipmap.
	 *
	 * @param target the target
	 */
  public void glGenerateMipmap(int target);
  
  /**
	 * Gl gen vertex arrays.
	 *
	 * @param n      the n
	 * @param arrays the arrays
	 */
  public void glGenVertexArrays(int n, IntBuffer arrays);
  
  /**
	 * Gl get attrib location.
	 *
	 * @param program the program
	 * @param name    the name
	 * @return the int
	 */
  public int glGetAttribLocation(int program, String name);
  
  /**
	 * Gl get programiv.
	 *
	 * @param program the program
	 * @param pname   the pname
	 * @param params  the params
	 */
  public void glGetProgramiv(int program, int pname, IntBuffer params);
  
  /**
	 * Gl get program info log.
	 *
	 * @param program the program
	 * @return the string
	 */
  public String glGetProgramInfoLog(int program);
  
  /**
	 * Gl get shaderiv.
	 *
	 * @param shader the shader
	 * @param pname  the pname
	 * @param params the params
	 */
  public void glGetShaderiv(int shader, int pname, IntBuffer params);
  
  /**
	 * Gl get shader info log.
	 *
	 * @param shader the shader
	 * @return the string
	 */
  public String glGetShaderInfoLog(int shader);
  
  /**
	 * Gl get uniform location.
	 *
	 * @param program the program
	 * @param name    the name
	 * @return the int
	 */
  public int glGetUniformLocation(int program, String name);
  
  /**
	 * Gl link program.
	 *
	 * @param program the program
	 */
  public void glLinkProgram(int program);
  
  /**
	 * Gl map buffer.
	 *
	 * @param target    the target
	 * @param access    the access
	 * @param length    the length
	 * @param oldBuffer the old buffer
	 * @return the byte buffer
	 */
  public ByteBuffer glMapBuffer(int target, int access, long length, ByteBuffer oldBuffer);
  
  /**
	 * Gl primitive restart index.
	 *
	 * @param index the index
	 */
  public void glPrimitiveRestartIndex(int index);
  
  /**
	 * Gl shader source.
	 *
	 * @param shader the shader
	 * @param string the string
	 */
  public void glShaderSource(int shader, String string);
  
  /**
	 * Gl uniform 1.
	 *
	 * @param location the location
	 * @param values   the values
	 */
  public void glUniform1(int location, FloatBuffer values);
  
  /**
	 * Gl uniform 1 f.
	 *
	 * @param location the location
	 * @param v0       the v 0
	 */
  public void glUniform1f(int location, float v0);
  
  /**
	 * Gl uniform 2 f.
	 *
	 * @param location the location
	 * @param v0       the v 0
	 * @param v1       the v 1
	 */
  public void glUniform2f(int location, float v0, float v1);
  
  /**
	 * Gl uniform 3 f.
	 *
	 * @param location the location
	 * @param v0       the v 0
	 * @param v1       the v 1
	 * @param v2       the v 2
	 */
  public void glUniform3f(int location, float v0, float v1, float v2);
  
  /**
	 * Gl uniform 4 f.
	 *
	 * @param location the location
	 * @param v0       the v 0
	 * @param v1       the v 1
	 * @param v2       the v 2
	 * @param v3       the v 3
	 */
  public void glUniform4f(int location, float v0, float v1, float v2, float v3);
  
  /**
	 * Gl uniform 1 i.
	 *
	 * @param location the location
	 * @param v0       the v 0
	 */
  public void glUniform1i(int location, int v0);
  
  /**
	 * Gl uniform 2 i.
	 *
	 * @param location the location
	 * @param v0       the v 0
	 * @param v1       the v 1
	 */
  public void glUniform2i(int location, int v0, int v1);
  
  /**
	 * Gl uniform 3 i.
	 *
	 * @param location the location
	 * @param v0       the v 0
	 * @param v1       the v 1
	 * @param v2       the v 2
	 */
  public void glUniform3i(int location, int v0, int v1, int v2);
  
  /**
	 * Gl uniform 4 i.
	 *
	 * @param location the location
	 * @param v0       the v 0
	 * @param v1       the v 1
	 * @param v2       the v 2
	 * @param v3       the v 3
	 */
  public void glUniform4i(int location, int v0, int v1, int v2, int v3);
  
  /**
	 * Gl uniform matrix 4.
	 *
	 * @param location  the location
	 * @param transpose the transpose
	 * @param matrices  the matrices
	 */
  public void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices);
  
  /**
	 * Gl unmap buffer.
	 *
	 * @param target the target
	 * @return true, if successful
	 */
  public boolean glUnmapBuffer(int target);
  
  /**
	 * Gl use program.
	 *
	 * @param program the program
	 */
  public void glUseProgram(int program);
  
  /**
	 * Gl vertex attrib pointer.
	 *
	 * @param index      the index
	 * @param size       the size
	 * @param type       the type
	 * @param normalized the normalized
	 * @param stride     the stride
	 * @param offset     the offset
	 */
  public void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long offset);
}
