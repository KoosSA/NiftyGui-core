package org.bushe.swing.event.annotation;

import java.lang.ref.WeakReference;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.Prioritized;
import org.bushe.swing.event.ProxySubscriber;

/**
 * Common base class for EventService Proxies.
 * <p>
 * Implementing Prioritized even when Priority is not used is always OK.  The default
 * value of 0 retains the FIFO order. 
 */
public abstract class AbstractProxySubscriber implements ProxySubscriber, Prioritized {
   
   /** The proxied subscriber. */
   private Object proxiedSubscriber;
   
   /** The subscription method. */
   private Method subscriptionMethod;
   
   /** The reference strength. */
   private ReferenceStrength referenceStrength;
   
   /** The event service. */
   private EventService eventService;
   
   /** The priority. */
   private int priority;
   
   /** The veto. */
   protected boolean veto;

   /**
	 * Instantiates a new abstract proxy subscriber.
	 *
	 * @param proxiedSubscriber  the proxied subscriber
	 * @param subscriptionMethod the subscription method
	 * @param referenceStrength  the reference strength
	 * @param es                 the es
	 * @param veto               the veto
	 */
   protected AbstractProxySubscriber(Object proxiedSubscriber, Method subscriptionMethod,
           ReferenceStrength referenceStrength, EventService es, boolean veto) {
      this(proxiedSubscriber, subscriptionMethod, referenceStrength, 0, es, veto);
   }

   /**
	 * Instantiates a new abstract proxy subscriber.
	 *
	 * @param proxiedSubscriber  the proxied subscriber
	 * @param subscriptionMethod the subscription method
	 * @param referenceStrength  the reference strength
	 * @param priority           the priority
	 * @param es                 the es
	 * @param veto               the veto
	 */
   protected AbstractProxySubscriber(Object proxiedSubscriber, Method subscriptionMethod,
           ReferenceStrength referenceStrength, int priority, EventService es, boolean veto) {
      this.referenceStrength = referenceStrength;
      this.priority = priority;
      eventService = es;
      this.veto = veto;
      if (proxiedSubscriber == null) {
         throw new IllegalArgumentException("The realSubscriber cannot be null when constructing a proxy subscriber.");
      }
      if (subscriptionMethod == null) {
         throw new IllegalArgumentException("The subscriptionMethod cannot be null when constructing a proxy subscriber.");
      }
      Class<?> returnType = subscriptionMethod.getReturnType();
      if (veto && returnType != Boolean.TYPE) {
         throw new IllegalArgumentException("The subscriptionMethod must have the two parameters, the first one must be a String and the second a non-primitive (Object or derivative).");
      }
      if (ReferenceStrength.WEAK.equals(referenceStrength)) {
         this.proxiedSubscriber = new WeakReference(proxiedSubscriber);
      } else {
         this.proxiedSubscriber = proxiedSubscriber;
      }
      this.subscriptionMethod = subscriptionMethod;
   }

   /**
	 * Gets the proxied subscriber.
	 *
	 * @return the object this proxy is subscribed on behalf of
	 */
   public Object getProxiedSubscriber() {
      if (proxiedSubscriber instanceof WeakReference) {
         return ((WeakReference)proxiedSubscriber).get();
      }
      return proxiedSubscriber;
   }
   
   /**
	 * Gets the subscription method.
	 *
	 * @return the subscriptionMethod passed in the constructor
	 */
   public Method getSubscriptionMethod() {
      return subscriptionMethod;
   }

   /**
	 * Gets the event service.
	 *
	 * @return the EventService passed in the constructor
	 */
   public EventService getEventService() {
      return eventService;
   }

   /**
	 * Gets the reference strength.
	 *
	 * @return the ReferenceStrength passed in the constructor
	 */
   public ReferenceStrength getReferenceStrength() {
      return referenceStrength;
   }

   /**
	 * Gets the priority.
	 *
	 * @return the priority, no effect if priority is 0 (the default value)
	 */
   public int getPriority() {
      return priority;
   }

   /**
    * Called by EventServices to inform the proxy that it is unsubscribed.  
    * The ProxySubscriber should perform any necessary cleanup.
    * <p>
    * <b>Overriding classes must call super.proxyUnsubscribed() or risk
    * things not being cleanup up properly.</b>
    */
   public void proxyUnsubscribed() {
      proxiedSubscriber = null;
   }

   /**
	 * Hash code.
	 *
	 * @return the int
	 */
   @Override
   public final int hashCode() {
      throw new RuntimeException("Proxy subscribers are not allowed in Hash " +
              "Maps, since the underlying values use Weak References that" +
              "may disappear, the calculations may not be the same in" +
              "successive calls as required by hashCode.");
   }

   /**
	 * Retry reflective call using accessible object.
	 *
	 * @param args               the args
	 * @param subscriptionMethod the subscription method
	 * @param obj                the obj
	 * @param e                  the e
	 * @param message            the message
	 * @return true, if successful
	 */
   protected boolean retryReflectiveCallUsingAccessibleObject(Object[] args, Method subscriptionMethod, Object obj,
           IllegalAccessException e, String message) {
      boolean accessibleTriedAndFailed = false;
      if (subscriptionMethod != null) {
         AccessibleObject[] accessibleMethod = {subscriptionMethod};
         try {
            AccessibleObject.setAccessible(accessibleMethod, true);
            Object returnValue = subscriptionMethod.invoke(obj, args);
            return Boolean.valueOf(returnValue+"");
         } catch (SecurityException ex) {
            accessibleTriedAndFailed = true;
         } catch (InvocationTargetException e1) {
            throw new RuntimeException(message, e);
         } catch (IllegalAccessException e1) {
            throw new RuntimeException(message, e);
         }
      }
      if (accessibleTriedAndFailed) {
         message = message + ".  An attempt was made to make the method accessible, but the SecurityManager denied the attempt.";
      }
      throw new RuntimeException(message, e);
   }

   /**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
   @Override
   public boolean equals(Object obj) {
      if (obj instanceof AbstractProxySubscriber) {
         AbstractProxySubscriber bps = (AbstractProxySubscriber) obj;
         if (referenceStrength != bps.referenceStrength) {
            return false;
         }
         if (subscriptionMethod != bps.subscriptionMethod) {
            return false;
         }
         if (ReferenceStrength.WEAK == referenceStrength) {
            if (((WeakReference)proxiedSubscriber).get() != ((WeakReference)bps.proxiedSubscriber).get()) {
               return false;
            }            
         } else {
            if (proxiedSubscriber != bps.proxiedSubscriber) {
               return false;
            }
         }
         if (veto != bps.veto) {
            return false;
         }
         if (eventService != bps.eventService) {
            return false;
         }
         return true;
      } else {
         return false;
      }
   }

   /**
	 * To string.
	 *
	 * @return the string
	 */
   @Override
   public String toString() {
      return "AbstractProxySubscriber{" +
              "realSubscriber=" + (proxiedSubscriber instanceof WeakReference?
                       ((WeakReference)proxiedSubscriber).get():proxiedSubscriber) +
              ", subscriptionMethod=" + subscriptionMethod +
              ", veto=" + veto +
              ", referenceStrength=" + referenceStrength +
              ", eventService=" + eventService +
              '}';
   }
}
