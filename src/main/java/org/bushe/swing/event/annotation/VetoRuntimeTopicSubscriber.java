package org.bushe.swing.event.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.ThreadSafeEventService;

/**
 * A veto subscriber to a topic that is determined at runtime.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface VetoRuntimeTopicSubscriber {
    
    /**
	 * Method name.
	 *
	 * @return name of a method (that must return a String) and whose return value
	 *         will become the subscription topic.
	 */
    public abstract String methodName() default "getTopicName";

    /**
	 * Reference strength.
	 *
	 * @return Whether to subscribe weakly or strongly.
	 */
    public abstract ReferenceStrength referenceStrength() default ReferenceStrength.WEAK;

    /**
	 * Event service name.
	 *
	 * @return event service to subscribe to, default to the
	 *         EventServiceLocator.SERVICE_NAME_EVENT_BUS.
	 */
    public abstract String eventServiceName() default EventServiceLocator.SERVICE_NAME_EVENT_BUS;

    /**
	 * Priority.
	 *
	 * @return Determines the order in which this subscriber is called, default is
	 *         FIFO.
	 */
    public abstract int priority() default 0;

    /**
	 * Auto create event service class.
	 *
	 * @return Whether or not to autocreate the event service if it doesn't exist on
	 *         subscription, default is true. If the service needs to be created, it
	 *         must have a default constructor.
	 */
    public abstract Class<? extends EventService> autoCreateEventServiceClass() default ThreadSafeEventService.class;
}
