package de.lessvoid.nifty;

import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A factory for creating ParameterizedObject objects.
 *
 * @param <T> the generic type
 */
public class ParameterizedObjectFactory<T extends Parameterizable> {
  
  /** The Constant log. */
  @Nonnull
	private static final Logger log = Logger.getLogger(ParameterizedObjectFactory.class.getName());

  /** The m object name to class mapping. */
  @Nonnull
	private final Map<String, Class<? extends T>> m_objectNameToClassMapping;
  
  /** The m fallback object name. */
  @Nonnull
	private final String m_fallbackObjectName;

	/**
	 * Instantiates a new parameterized object factory.
	 *
	 * @param objectNameToClassMapping the object name to class mapping
	 * @param fallbackObjectName       the fallback object name
	 */
	public ParameterizedObjectFactory(
      @Nonnull final Map<String, Class<? extends T>> objectNameToClassMapping,
			@Nonnull final String fallbackObjectName) {
		m_objectNameToClassMapping = objectNameToClassMapping;
		m_fallbackObjectName = fallbackObjectName;
	}

  /**
	 * Creates the.
	 *
	 * @param objectDescription the object description
	 * @return the t
	 */
  @Nonnull
	public T create(@Nullable String objectDescription) {
		T object;
		try {
			object = createInternal(objectDescription);
		} catch (IllegalArgumentException e) {
			log.warning(e.getMessage() + " -> Falling back to default " + m_fallbackObjectName + ".");
			object = createInternal(m_fallbackObjectName);
		}

		return object;
	}

  /**
	 * Creates a new ParameterizedObject object.
	 *
	 * @param objectDescription the object description
	 * @return the t
	 */
  @Nonnull
	private T createInternal(@Nullable String objectDescription) {
		T object = instanciateObject(objectDescription);
		initializeObject(object, objectDescription);

		return object;
	}

  /**
	 * Instanciate object.
	 *
	 * @param objectDescription the object description
	 * @return the t
	 */
  @Nonnull
	private T instanciateObject(@Nullable String objectDescription) {
		String objectName = m_fallbackObjectName;
		if (objectDescription != null) {
			objectName = objectDescription.split(":")[0];
		}

		Class<? extends T> objectClass = m_objectNameToClassMapping.get(objectName);
		if (objectClass == null) {
			throw new IllegalArgumentException("No class found for [" + objectName + "].");
		}

		try {
			return objectClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to instantiate class [" + objectClass.getName() + "].", e);
		}
	}

	/**
	 * Initialize object.
	 *
	 * @param object            the object
	 * @param objectDescription the object description
	 */
	private void initializeObject(@Nonnull T object, @Nullable String objectDescription) {
		String objectParameters = null;
		if (objectDescription != null) {
			String[] tokens = objectDescription.split(":");
			if (tokens.length > 1) {
				objectParameters = tokens[1];
			}
		}

		object.setParameters(objectParameters);
	}
}
