package org.bushe.swing.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.ThreadSafeEventService;

/**
 * The Interface EventTopicPatternSubscriber.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventTopicPatternSubscriber {
   
   /**
	 * The Regular Expression to subscribe to.
	 *
	 * @return the string
	 */
   String topicPattern();

   /**
	 * Whether or not to subscribe to the exact class or a class hierarchy, defaults
	 * to class hierarchy (false).
	 *
	 * @return true, if successful
	 */
   boolean exact() default false;

   /**
	 * Whether to subscribe weakly or strongly.
	 *
	 * @return the reference strength
	 */
   ReferenceStrength referenceStrength() default ReferenceStrength.WEAK;

   /**
	 * The event service to subscribe to, default to the
	 * EventServiceLocator.SERVICE_NAME_EVENT_BUS.
	 *
	 * @return the string
	 */
   String eventServiceName() default EventServiceLocator.SERVICE_NAME_EVENT_BUS;

   /**
	 * Whether or not to autocreate the event service if it doesn't exist on
	 * subscription, default is true. If the service needs to be created, it must
	 * have a default constructor.
	 *
	 * @return the class<? extends event service>
	 */
   Class<? extends EventService> autoCreateEventServiceClass() default ThreadSafeEventService.class;

   /**
	 * Determines the order in which this subscriber is called, default is FIFO.
	 *
	 * @return the int
	 */
   int priority() default 0;
}
