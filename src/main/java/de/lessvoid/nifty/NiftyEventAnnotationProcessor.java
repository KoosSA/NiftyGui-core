package de.lessvoid.nifty;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.EventTopicSubscriber;

/**
 * The Class NiftyEventAnnotationProcessor.
 */
public class NiftyEventAnnotationProcessor {
  
  /** The Constant log. */
  private static final Logger log = Logger.getLogger(NiftyEventAnnotationProcessor.class.getName());

  /**
	 * Process.
	 *
	 * @param obj the obj
	 */
  public static void process(final Object obj) {
    processOrUnprocess(obj, true);
  }

  /**
	 * Unprocess.
	 *
	 * @param obj the obj
	 */
  public static void unprocess(final Object obj) {
    processOrUnprocess(obj, false);
  }

  /**
	 * Process or unprocess.
	 *
	 * @param obj the obj
	 * @param add the add
	 */
  private static void processOrUnprocess(@Nullable final Object obj, final boolean add) {
    if (obj == null) {
      return;
    }
    Class<?> cl = obj.getClass();
    Method[] methods = cl.getMethods();
    if (log.isLoggable(Level.FINE)) {
      log.fine("Looking for EventBus annotations for class " + cl + ", methods:" + Arrays.toString(methods));
    }
    for (Method method : methods) {
      NiftyEventSubscriber niftyEventSubscriber = method.getAnnotation(NiftyEventSubscriber.class);
      if (niftyEventSubscriber != null) {
        if (log.isLoggable(Level.FINE)) {
          log.fine("Found NiftyEventSubscriber:" + niftyEventSubscriber + " on method:" + method);
        }
        process(niftyEventSubscriber, obj, method, add);
      }
    }
  }

  /**
	 * Process.
	 *
	 * @param annotation the annotation
	 * @param obj        the obj
	 * @param method     the method
	 * @param add        the add
	 */
  private static void process(@Nonnull final NiftyEventSubscriber annotation, final Object obj, @Nonnull final Method method, final boolean add) {
    String id = annotation.id();
    String pattern = annotation.pattern();
    ensureNotNull(id, pattern);
    ensureMethodParamCount(method.getParameterTypes());
    EventService eventService = getEventService();
    Class<?> eventClass = method.getParameterTypes()[1];
    if (isSet(id)) {
      idProcess(obj, method, add, id, eventClass, eventService);
    } else {
      patternProcess(obj, method, add, pattern, eventClass, eventService);
    }
  }

  /**
	 * Checks if is sets the.
	 *
	 * @param value the value
	 * @return true, if is sets the
	 */
  private static boolean isSet(@Nullable final String value) {
    return value != null && value.length() > 0;
  }

  /**
	 * Ensure not null.
	 *
	 * @param id      the id
	 * @param pattern the pattern
	 */
  private static void ensureNotNull(final String id, final String pattern) {
    if (!isSet(id) && !isSet(pattern)) {
      throw new IllegalArgumentException("id or pattern must have a value for NiftyEventSubscriber annotation");
    }
  }

  /**
	 * Ensure method param count.
	 *
	 * @param params the params
	 */
  private static void ensureMethodParamCount(@Nullable final Class<?>[] params) {
    if (params == null || params.length != 2 || !String.class.equals(params[0]) || params[1].isPrimitive()) {
       throw new IllegalArgumentException("The subscriptionMethod must have the two parameters, the first one must be a String and the second a non-primitive (Object or derivative).");
    }
  }

  /**
	 * Pattern process.
	 *
	 * @param obj          the obj
	 * @param method       the method
	 * @param add          the add
	 * @param topicPattern the topic pattern
	 * @param eventClass   the event class
	 * @param eventService the event service
	 */
  private static void patternProcess(final Object obj, final Method method, final boolean add, @Nonnull final String topicPattern, final Class<?> eventClass, @Nonnull final EventService eventService) {
    Pattern pattern = Pattern.compile(topicPattern);
    Subscriber subscriber = new Subscriber(obj, method, eventClass);
    StringBuilder sb = new StringBuilder(" [{0}] -> [{1}]");
    if (add) {      
      sb.insert(0, "-> subscribe");
      if(!eventService.subscribeStrongly(pattern, subscriber)){
          sb.insert(2, " failed to");
      }
      log.log(Level.FINE, sb.toString(), new Object[]{pattern, subscriber});
    } else {
      sb.insert(0, "<- unsubscribe");
      if(!eventService.unsubscribe(pattern, subscriber)){
          sb.insert(2, " failed to");
      }
      log.log(Level.FINE, sb.toString(), new Object[]{pattern, subscriber});
    }
  }

  /**
	 * Id process.
	 *
	 * @param obj          the obj
	 * @param method       the method
	 * @param add          the add
	 * @param id           the id
	 * @param eventClass   the event class
	 * @param eventService the event service
	 */
  private static void idProcess(final Object obj, final Method method, final boolean add, final String id, final Class<?> eventClass, @Nonnull final EventService eventService) {
    Subscriber subscriber = new Subscriber(obj, method, eventClass);
    StringBuilder sb = new StringBuilder(" [{0}] -> [{1}]");
    if (add) {      
      sb.insert(0, "-> subscribe");
      if(!eventService.subscribeStrongly(id, subscriber)){
          sb.insert(2, " failed to");
      }
      log.log(Level.FINE, sb.toString(), new Object[]{id, subscriber});
    } else {
      sb.insert(0, "<- unsubscribe");
      if(!eventService.unsubscribe(id, subscriber)){
          sb.insert(2, " failed to");
      }
      log.log(Level.FINE, sb.toString(), new Object[]{id, subscriber});
    }
  }

  /**
	 * Gets the event service.
	 *
	 * @return the event service
	 */
  private static EventService getEventService() {
    return EventServiceLocator.getEventService("NiftyEventBus");
  }

  /**
	 * The Class Subscriber.
	 */
  private static class Subscriber implements EventTopicSubscriber<Object> {
    
    /** The obj. */
    private final Object obj;
    
    /** The method. */
    private final Method method;
    
    /** The event class. */
    private final Class<?> eventClass;

    /**
	 * Instantiates a new subscriber.
	 *
	 * @param obj        the obj
	 * @param method     the method
	 * @param eventClass the event class
	 */
    private Subscriber(final Object obj, final Method method, final Class<?> eventClass) {
      this.obj = obj;
      this.method = method;
      this.eventClass = eventClass;
    }

    /**
	 * On event.
	 *
	 * @param topic the topic
	 * @param data  the data
	 */
    @Override
    public void onEvent(final String topic, final Object data) {
      if (eventClass.isInstance(data)) {
        try {
          method.invoke(obj, topic, eventClass.cast(data));
        } catch (Throwable e) {
          log.log(Level.WARNING, "failed to invoke method [" + method + "] with Exception [" + e.getMessage() + "][" + e.getCause() + "]", e);
        }
      }
    }

    /**
	 * Hash code.
	 *
	 * @return the int
	 */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((eventClass == null) ? 0 : eventClass.hashCode());
      result = prime * result + ((method == null) ? 0 : method.hashCode());
      result = prime * result + ((obj == null) ? 0 : obj.hashCode());
      return result;
    }

    /**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Subscriber other = (Subscriber) obj;
      if (eventClass == null) {
        if (other.eventClass != null)
          return false;
      } else if (!eventClass.equals(other.eventClass))
        return false;
      if (method == null) {
        if (other.method != null)
          return false;
      } else if (!method.equals(other.method))
        return false;
      if (this.obj == null) {
        if (other.obj != null)
          return false;
      } else if (!this.obj.equals(other.obj))
        return false;
      return true;
    }
  }
}
