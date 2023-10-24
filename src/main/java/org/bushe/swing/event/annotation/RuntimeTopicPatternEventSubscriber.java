package org.bushe.swing.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.ThreadSafeEventService;

/**
 * The Interface RuntimeTopicPatternEventSubscriber.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RuntimeTopicPatternEventSubscriber {
    
    /**
	 * Method name.
	 *
	 * @return name of a method (which should return a String) and whose return
	 *         value will become the subscription topic.
	 */
    String methodName() default "getTopicPatternName";

    /**
	 * Reference strength.
	 *
	 * @return Whether to subscribe weakly or strongly.
	 */
    ReferenceStrength referenceStrength() default ReferenceStrength.WEAK;

    /**
	 * Event service name.
	 *
	 * @return event service to subscribe to, default to the
	 *         EventServiceLocator.SERVICE_NAME_EVENT_BUS.
	 */
    String eventServiceName() default EventServiceLocator.SERVICE_NAME_EVENT_BUS;

    /**
	 * Priority.
	 *
	 * @return Determines the order in which this subscriber is called, default is
	 *         FIFO.
	 */
    int priority() default 0;

    /**
	 * Exact.
	 *
	 * @return Whether or not to subscribe to the exact class or a class hierarchy,
	 *         defaults to class hierarchy (false).
	 */
    boolean exact() default false;

    /**
	 * Auto create event service class.
	 *
	 * @return Whether or not to autocreate the event service if it doesn't exist on
	 *         subscription, default is true. If the service needs to be created, it
	 *         must have a default constructor.
	 */
    Class<? extends EventService> autoCreateEventServiceClass() default ThreadSafeEventService.class;
}
