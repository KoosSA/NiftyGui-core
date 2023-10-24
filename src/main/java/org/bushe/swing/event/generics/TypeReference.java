package org.bushe.swing.event.generics;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Courtesy of Neil Gafter's blog. Thanks to Curt Cox for the pointer.
 *
 * @param <T> the generic type
 */
public abstract class TypeReference<T> {

 /** The type. */
 private final Type type;
 
 /** The constructor. */
 private volatile Constructor<?> constructor;

 /**
	 * Instantiates a new type reference.
	 */
 protected TypeReference() {
     Type superclass = getClass().getGenericSuperclass();
     if (superclass instanceof Class) {
         throw new RuntimeException("Missing type parameter.");
     }
     this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
 }

 /**
	 * New instance.
	 *
	 * @return a new instance of {@code T} using the default, no-arg constructor.
	 * @throws NoSuchMethodException     there's not getRawType on the type
	 * @throws IllegalAccessException    on security reflection issues
	 * @throws InvocationTargetException the invocation target exception
	 * @throws InstantiationException    if the instance cannot be instantiated
	 */
 @SuppressWarnings("unchecked")
 public T newInstance()
         throws NoSuchMethodException, IllegalAccessException,
         InvocationTargetException, InstantiationException {
     if (constructor == null) {
         Class<?> rawType = type instanceof Class<?>
             ? (Class<?>) type
             : (Class<?>) ((ParameterizedType) type).getRawType();
         constructor = rawType.getConstructor();
     }
     return (T) constructor.newInstance();
 }

 /**
	 * Gets the type.
	 *
	 * @return the referenced type.
	 */
 public Type getType() {
     return this.type;
 }
}
