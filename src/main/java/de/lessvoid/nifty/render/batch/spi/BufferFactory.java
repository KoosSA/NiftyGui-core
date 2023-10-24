package de.lessvoid.nifty.render.batch.spi;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.annotation.Nonnull;

/**
 * A factory for creating Buffer objects.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface BufferFactory {
  
  /**
	 * Creates a new Buffer object.
	 *
	 * @param numBytes the num bytes
	 * @return the byte buffer
	 */
  @Nonnull
  public ByteBuffer createNativeOrderedByteBuffer(final int numBytes);

  /**
	 * Creates a new Buffer object.
	 *
	 * @param numFloats the num floats
	 * @return the float buffer
	 */
  @Nonnull
  public FloatBuffer createNativeOrderedFloatBuffer(final int numFloats);

  /**
	 * Creates a new Buffer object.
	 *
	 * @param numInts the num ints
	 * @return the int buffer
	 */
  @Nonnull
  public IntBuffer createNativeOrderedIntBuffer(final int numInts);
}
