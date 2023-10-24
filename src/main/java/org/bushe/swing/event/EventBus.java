/**
 * Copyright 2005 Bushe Enterprises, Inc., Hopkinton, MA, USA, www.bushe.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bushe.swing.event;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The EventBus provides event publication and subscription services.  It is a simple static wrapper around a
 * global instance of an {@link EventService}, specifically a {@link SwingEventService} by default.
 * <p/>
 * For Swing Applications the EventBus is nearly all you need, besides some of your own Event classes (if so desired).
 * <p/>
 * The EventBus is really just a convenience class that provides a static wrapper around a global {@link
 * EventService} instance.  This class exists solely for simplicity.  Calling
 * <code>EventBus.subscribeXXX/publishXXX</code> is equivalent to
 * <code>EventServiceLocator.getEventBusService().subscribeXXX/publishXXX</code>,
 * it is just shorter to type.  See {@link org.bushe.swing.event.EventServiceLocator} for details on how to customize
 * the global EventService in place of the default SwingEventService. 
 *
 * @author Michael Bushe michael@bushe.com
 * @see EventService
 * @see SwingEventService
 * @see ThreadSafeEventService See package JavaDoc for more information
 */
public class EventBus {

  /**
    * The EventBus uses a global static EventService.  This method is not necessary in usual usage, use the other static
    * methods instead.  It is used to expose any other functionality and for framework classes (EventBusAction)
    *
    * @return the global static EventService
    */
   public static EventService getGlobalEventService() {
      return EventServiceLocator.getEventBusService();
   }

   /**
	 * Publish.
	 *
	 * @param event the event
	 * @see EventService#publish(Object)
	 */
   public static void publish(Object event) {
      if (event == null) {
         throw new IllegalArgumentException("Can't publish null.");
      }
      EventServiceLocator.getEventBusService().publish(event);
   }

   /**
	 * Publish.
	 *
	 * @param topic the topic
	 * @param o     the o
	 * @see EventService#publish(String,Object)
	 */
   public static void publish(String topic, Object o) {
      if (topic == null) {
         throw new IllegalArgumentException("Can't publish to null topic.");
      }
      EventServiceLocator.getEventBusService().publish(topic, o);
   }

   /**
	 * Publish.
	 *
	 * @param genericType the generic type
	 * @param o           the o
	 * @see EventService#publish(java.lang.reflect.Type, Object)
	 */
   public static void publish(Type genericType, Object o) {
      if (genericType == null) {
         throw new IllegalArgumentException("Can't publish to null type.");
      }
      EventServiceLocator.getEventBusService().publish(genericType, o);
   }


   /**
	 * Subscribe.
	 *
	 * @param eventClass the event class
	 * @param subscriber the subscriber
	 * @return true, if successful
	 * @see EventService#subscribe(Class,EventSubscriber)
	 */
   public static boolean subscribe(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribe(eventClass, subscriber);
   }

   /**
	 * Subscribe.
	 *
	 * @param genericType the generic type
	 * @param subscriber  the subscriber
	 * @return true, if successful
	 * @see EventService#subscribe(java.lang.reflect.Type, EventSubscriber)
	 */
   public static boolean subscribe(Type genericType, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribe(genericType, subscriber);
   }

   /**
	 * Subscribe exactly.
	 *
	 * @param eventClass the event class
	 * @param subscriber the subscriber
	 * @return true, if successful
	 * @see EventService#subscribeExactly(Class,EventSubscriber)
	 */
   public static boolean subscribeExactly(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribeExactly(eventClass, subscriber);
   }

   /**
	 * Subscribe.
	 *
	 * @param topic      the topic
	 * @param subscriber the subscriber
	 * @return true, if successful
	 * @see EventService#subscribe(String,EventTopicSubscriber)
	 */
   public static boolean subscribe(String topic, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribe(topic, subscriber);
   }

   /**
	 * Subscribe.
	 *
	 * @param topicPattern the topic pattern
	 * @param subscriber   the subscriber
	 * @return true, if successful
	 * @see EventService#subscribe(Pattern,EventTopicSubscriber)
	 */
   public static boolean subscribe(Pattern topicPattern, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribe(topicPattern, subscriber);
   }

   /**
	 * Subscribe strongly.
	 *
	 * @param eventClass the event class
	 * @param subscriber the subscriber
	 * @return true, if successful
	 * @see EventService#subscribeStrongly(Class,EventSubscriber)
	 */
   public static boolean subscribeStrongly(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribeStrongly(eventClass, subscriber);
   }

   /**
	 * Subscribe exactly strongly.
	 *
	 * @param eventClass the event class
	 * @param subscriber the subscriber
	 * @return true, if successful
	 * @see EventService#subscribeExactlyStrongly(Class,EventSubscriber)
	 */
   public static boolean subscribeExactlyStrongly(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribeExactlyStrongly(eventClass, subscriber);
   }

   /**
	 * Subscribe strongly.
	 *
	 * @param topic      the topic
	 * @param subscriber the subscriber
	 * @return true, if successful
	 * @see EventService#subscribeStrongly(String,EventTopicSubscriber)
	 */
   public static boolean subscribeStrongly(String topic, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribeStrongly(topic, subscriber);
   }

   /**
	 * Subscribe strongly.
	 *
	 * @param topicPattern the topic pattern
	 * @param subscriber   the subscriber
	 * @return true, if successful
	 * @see EventService#subscribeStrongly(Pattern,EventTopicSubscriber)
	 */
   public static boolean subscribeStrongly(Pattern topicPattern, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribeStrongly(topicPattern, subscriber);
   }

   /**
	 * Unsubscribe.
	 *
	 * @param eventClass the event class
	 * @param subscriber the subscriber
	 * @return true, if successful
	 * @see EventService#unsubscribe(Class,EventSubscriber)
	 */
   public static boolean unsubscribe(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribe(eventClass, subscriber);
   }

   /**
	 * Unsubscribe exactly.
	 *
	 * @param eventClass the event class
	 * @param subscriber the subscriber
	 * @return true, if successful
	 * @see EventService#unsubscribeExactly(Class,EventSubscriber)
	 */
   public static boolean unsubscribeExactly(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribeExactly(eventClass, subscriber);
   }

   /**
	 * Unsubscribe.
	 *
	 * @param topic      the topic
	 * @param subscriber the subscriber
	 * @return true, if successful
	 * @see EventService#unsubscribe(String,EventTopicSubscriber)
	 */
   public static boolean unsubscribe(String topic, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribe(topic, subscriber);
   }

   /**
	 * Unsubscribe.
	 *
	 * @param topicPattern the topic pattern
	 * @param subscriber   the subscriber
	 * @return true, if successful
	 * @see EventService#unsubscribe(Pattern,EventTopicSubscriber)
	 */
   public static boolean unsubscribe(Pattern topicPattern, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribe(topicPattern, subscriber);
   }

   /**
	 * For usage with annotations.
	 *
	 * @param eventClass the event class
	 * @param object     the object
	 * @return true, if successful
	 * @see EventService#unsubscribe(Class,Object)
	 */
   public static boolean unsubscribe(Class eventClass, Object object) {
      return EventServiceLocator.getEventBusService().unsubscribe(eventClass, object);
   }

   /**
	 * For usage with annotations.
	 *
	 * @param eventClass the event class
	 * @param subscriber the subscriber
	 * @return true, if successful
	 * @see EventService#unsubscribeExactly(Class,Object)
	 */
   public static boolean unsubscribeExactly(Class eventClass, Object subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribeExactly(eventClass, subscriber);
   }

   /**
	 * For usage with annotations.
	 *
	 * @param topic      the topic
	 * @param subscriber the subscriber
	 * @return true, if successful
	 * @see EventService#unsubscribe(String,Object)
	 */
   public static boolean unsubscribe(String topic, Object subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribe(topic, subscriber);
   }

   /**
	 * For usage with annotations.
	 *
	 * @param topicPattern the topic pattern
	 * @param subscriber   the subscriber
	 * @return true, if successful
	 * @see EventService#unsubscribe(Pattern,Object)
	 */
   public static boolean unsubscribe(Pattern topicPattern, Object subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribe(topicPattern, subscriber);
   }

   /**
	 * Subscribe veto listener.
	 *
	 * @param eventClass   the event class
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#subscribeVetoListener(Class,VetoEventListener)
	 */
   public static boolean subscribeVetoListener(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListener(eventClass, vetoListener);
   }

   /**
	 * Subscribe veto listener exactly.
	 *
	 * @param eventClass   the event class
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#subscribeVetoListener(Class,VetoEventListener)
	 */
   public static boolean subscribeVetoListenerExactly(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListenerExactly(eventClass, vetoListener);
   }


   /**
	 * Subscribe veto listener.
	 *
	 * @param topic        the topic
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#subscribeVetoListener(String,VetoTopicEventListener)
	 */
   public static boolean subscribeVetoListener(String topic, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListener(topic, vetoListener);
   }

   /**
	 * Subscribe veto listener.
	 *
	 * @param topicPattern the topic pattern
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#subscribeVetoListener(Pattern,VetoTopicEventListener)
	 */
   public static boolean subscribeVetoListener(Pattern topicPattern, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListener(topicPattern, vetoListener);
   }

   /**
	 * Subscribe veto listener strongly.
	 *
	 * @param eventClass   the event class
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#subscribeVetoListenerStrongly(Class,VetoEventListener)
	 */
   public static boolean subscribeVetoListenerStrongly(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListenerStrongly(eventClass, vetoListener);
   }

   /**
	 * Subscribe veto listener exactly strongly.
	 *
	 * @param eventClass   the event class
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#subscribeVetoListenerExactlyStrongly(Class,VetoEventListener)
	 */
   public static boolean subscribeVetoListenerExactlyStrongly(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListenerExactlyStrongly(eventClass, vetoListener);
   }

   /**
	 * Subscribe veto listener strongly.
	 *
	 * @param topic        the topic
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#subscribeVetoListenerStrongly(String,VetoTopicEventListener)
	 */
   public static boolean subscribeVetoListenerStrongly(String topic, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListenerStrongly(topic, vetoListener);
   }

   /**
	 * Subscribe veto listener strongly.
	 *
	 * @param topicPattern the topic pattern
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#subscribeVetoListener(String,VetoTopicEventListener)
	 */
   public static boolean subscribeVetoListenerStrongly(Pattern topicPattern, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListenerStrongly(topicPattern, vetoListener);
   }

   /**
	 * Unsubscribe veto listener.
	 *
	 * @param eventClass   the event class
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#unsubscribeVetoListener(Class,VetoEventListener)
	 */
   public static boolean unsubscribeVetoListener(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().unsubscribeVetoListener(eventClass, vetoListener);
   }

   /**
	 * Unsubscribe veto listener exactly.
	 *
	 * @param eventClass   the event class
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#unsubscribeVetoListenerExactly(Class,VetoEventListener)
	 */
   public static boolean unsubscribeVetoListenerExactly(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().unsubscribeVetoListenerExactly(eventClass, vetoListener);
   }

   /**
	 * Unsubscribe veto listener.
	 *
	 * @param topic        the topic
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#unsubscribeVetoListener(String,VetoTopicEventListener)
	 */
   public static boolean unsubscribeVetoListener(String topic, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().unsubscribeVetoListener(topic, vetoListener);
   }

   /**
	 * Unsubscribe veto listener.
	 *
	 * @param topicPattern the topic pattern
	 * @param vetoListener the veto listener
	 * @return true, if successful
	 * @see EventService#unsubscribeVetoListener(Pattern,VetoTopicEventListener)
	 */
   public static boolean unsubscribeVetoListener(Pattern topicPattern, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().unsubscribeVetoListener(topicPattern, vetoListener);
   }

   /**
	 * Gets the subscribers.
	 *
	 * @param <T>        the generic type
	 * @param eventClass the event class
	 * @return the subscribers
	 * @see EventService#getSubscribers(Class)
	 */
   public static <T> List<T> getSubscribers(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getSubscribers(eventClass);
   }   

   /**
	 * Gets the subscribers to class.
	 *
	 * @param <T>        the generic type
	 * @param eventClass the event class
	 * @return the subscribers to class
	 * @see EventService#getSubscribersToClass(Class)
	 */
   public static <T> List<T> getSubscribersToClass(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getSubscribersToClass(eventClass);
   }

   /**
	 * Gets the subscribers to exact class.
	 *
	 * @param <T>        the generic type
	 * @param eventClass the event class
	 * @return the subscribers to exact class
	 * @see EventService#getSubscribersToExactClass(Class)
	 */
   public static <T> List<T> getSubscribersToExactClass(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getSubscribersToExactClass(eventClass);
   }

   /**
	 * Gets the subscribers.
	 *
	 * @param <T>  the generic type
	 * @param type the type
	 * @return the subscribers
	 * @see EventService#getSubscribers(Type)
	 */
   public static <T> List<T> getSubscribers(Type type) {
      return EventServiceLocator.getEventBusService().getSubscribers(type);
   }

   /**
	 * Gets the subscribers.
	 *
	 * @param <T>   the generic type
	 * @param topic the topic
	 * @return the subscribers
	 * @see EventService#getSubscribers(String)
	 */
   public static <T> List<T> getSubscribers(String topic) {
      return EventServiceLocator.getEventBusService().getSubscribers(topic);
   }

   /**
	 * Gets the subscribers to topic.
	 *
	 * @param <T>   the generic type
	 * @param topic the topic
	 * @return the subscribers to topic
	 * @see EventService#getSubscribersToTopic(String)
	 */
   public static <T> List<T> getSubscribersToTopic(String topic) {
      return EventServiceLocator.getEventBusService().getSubscribersToTopic(topic);
   }

   /**
	 * Gets the subscribers.
	 *
	 * @param <T>     the generic type
	 * @param pattern the pattern
	 * @return the subscribers
	 * @see EventService#getSubscribers(Pattern)
	 */
   public static <T> List<T> getSubscribers(Pattern pattern) {
      return EventServiceLocator.getEventBusService().getSubscribers(pattern);
   }

   /**
	 * Gets the subscribers by pattern.
	 *
	 * @param <T>   the generic type
	 * @param topic the topic
	 * @return the subscribers by pattern
	 * @see EventService#getSubscribersByPattern(String)
	 */
   public static <T> List<T> getSubscribersByPattern(String topic) {
      return EventServiceLocator.getEventBusService().getSubscribersByPattern(topic);
   }

   /**
	 * Gets the veto subscribers.
	 *
	 * @param <T>        the generic type
	 * @param eventClass the event class
	 * @return the veto subscribers
	 * @see EventService#getSubscribers(Class)
	 */
   public static <T> List<T> getVetoSubscribers(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getVetoSubscribers(eventClass);
   }

   /**
	 * Gets the veto subscribers to class.
	 *
	 * @param <T>        the generic type
	 * @param eventClass the event class
	 * @return the veto subscribers to class
	 * @see EventService#getVetoSubscribersToClass(Class)
	 */
   public static <T> List<T> getVetoSubscribersToClass(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getVetoSubscribersToClass(eventClass);
   }

   /**
	 * Gets the veto subscribers to exact class.
	 *
	 * @param <T>        the generic type
	 * @param eventClass the event class
	 * @return the veto subscribers to exact class
	 * @see EventService#getVetoSubscribersToExactClass(Class)
	 */
   public static <T> List<T> getVetoSubscribersToExactClass(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getVetoSubscribersToExactClass(eventClass);
   }

   /**
	 * Gets the veto subscribers.
	 *
	 * @param <T>   the generic type
	 * @param topic the topic
	 * @return the veto subscribers
	 * @see EventService#getVetoSubscribers(Class)
	 * @deprecated use getVetoSubscribersToTopic instead for direct replacement, or
	 *             use getVetoEventListeners to get topic and pattern matchers. In
	 *             EventBus 2.0 this name will replace getVetoEventListeners() and
	 *             have it's union functionality
	 */
   public static <T> List<T> getVetoSubscribers(String topic) {
      return EventServiceLocator.getEventBusService().getVetoSubscribers(topic);
   }

   /**
	 * Gets the veto event listeners.
	 *
	 * @param <T>   the generic type
	 * @param topic the topic
	 * @return the veto event listeners
	 * @see EventService#getVetoEventListeners(String)
	 */
   public static <T> List<T> getVetoEventListeners(String topic) {
      return EventServiceLocator.getEventBusService().getVetoEventListeners(topic);
   }

   /**
	 * Gets the veto subscribers.
	 *
	 * @param <T>     the generic type
	 * @param pattern the pattern
	 * @return the veto subscribers
	 * @see EventService#getVetoSubscribers(Pattern)
	 */
   public static <T> List<T> getVetoSubscribers(Pattern pattern) {
      return EventServiceLocator.getEventBusService().getVetoSubscribers(pattern);
   }

   /**
	 * Gets the veto subscribers to topic.
	 *
	 * @param <T>   the generic type
	 * @param topic the topic
	 * @return the veto subscribers to topic
	 * @see EventService#getVetoSubscribersToTopic(String)
	 */
   public static <T> List<T> getVetoSubscribersToTopic(String topic) {
      return EventServiceLocator.getEventBusService().getVetoSubscribersToTopic(topic);
   }

   /**
	 * Gets the veto subscribers by pattern.
	 *
	 * @param <T>   the generic type
	 * @param topic the topic
	 * @return the veto subscribers by pattern
	 * @see EventService#getVetoSubscribersByPattern(String)
	 */
   public static <T> List<T> getVetoSubscribersByPattern(String topic) {
      return EventServiceLocator.getEventBusService().getVetoSubscribersByPattern(topic);
   }

   /**
	 * Unsubscribe veto.
	 *
	 * @param eventClass        the event class
	 * @param subscribedByProxy the subscribed by proxy
	 * @return true, if successful
	 * @see EventService#unsubscribeVeto(Class, Object)
	 */
   public static boolean unsubscribeVeto(Class eventClass, Object subscribedByProxy) {
      return EventServiceLocator.getEventBusService().unsubscribeVeto(eventClass, subscribedByProxy);
   }

   /**
	 * Unsubscribe veto exactly.
	 *
	 * @param eventClass        the event class
	 * @param subscribedByProxy the subscribed by proxy
	 * @return true, if successful
	 * @see EventService#unsubscribeVetoExactly(Class, Object)
	 */
   public static boolean unsubscribeVetoExactly(Class eventClass, Object subscribedByProxy) {
      return EventServiceLocator.getEventBusService().unsubscribeVetoExactly(eventClass, subscribedByProxy);
   }

   /**
	 * Unsubscribe veto.
	 *
	 * @param topic             the topic
	 * @param subscribedByProxy the subscribed by proxy
	 * @return true, if successful
	 * @see EventService#unsubscribeVeto(String, Object)
	 */
   public static boolean unsubscribeVeto(String topic, Object subscribedByProxy) {
      return EventServiceLocator.getEventBusService().unsubscribeVeto(topic, subscribedByProxy);
   }

   /**
	 * Unsubscribe veto.
	 *
	 * @param pattern           the pattern
	 * @param subscribedByProxy the subscribed by proxy
	 * @return true, if successful
	 * @see EventService#unsubscribeVeto(Pattern, Object)
	 */
   public static boolean unsubscribeVeto(Pattern pattern, Object subscribedByProxy) {
      return EventServiceLocator.getEventBusService().unsubscribeVeto(pattern, subscribedByProxy);
   }

   /**
	 * Clear all subscribers.
	 *
	 * @see EventService#clearAllSubscribers()
	 */
   public static void clearAllSubscribers() {
      EventServiceLocator.getEventBusService().clearAllSubscribers();
   }

   /**
	 * Sets the default cache size per class or topic.
	 *
	 * @param defaultCacheSizePerClassOrTopic the new default cache size per class
	 *                                        or topic
	 * @see EventService#setDefaultCacheSizePerClassOrTopic(int)
	 */
   public static void setDefaultCacheSizePerClassOrTopic(int defaultCacheSizePerClassOrTopic) {
      EventServiceLocator.getEventBusService().setDefaultCacheSizePerClassOrTopic(defaultCacheSizePerClassOrTopic);
   }

   /**
	 * Gets the default cache size per class or topic.
	 *
	 * @return the default cache size per class or topic
	 * @see org.bushe.swing.event.EventService#getDefaultCacheSizePerClassOrTopic()
	 */
   public static int getDefaultCacheSizePerClassOrTopic() {
      return EventServiceLocator.getEventBusService().getDefaultCacheSizePerClassOrTopic();
   }

   /**
	 * Sets the cache size for event class.
	 *
	 * @param eventClass the event class
	 * @param cacheSize  the cache size
	 * @see EventService#setCacheSizeForEventClass(Class,int)
	 */
   public static void setCacheSizeForEventClass(Class eventClass, int cacheSize) {
      EventServiceLocator.getEventBusService().setCacheSizeForEventClass(eventClass, cacheSize);
   }

   /**
	 * Gets the cache size for event class.
	 *
	 * @param eventClass the event class
	 * @return the cache size for event class
	 * @see EventService#getCacheSizeForEventClass(Class)
	 */
   public static int getCacheSizeForEventClass(Class eventClass) {
      return EventServiceLocator.getEventBusService().getCacheSizeForEventClass(eventClass);
   }

   /**
	 * Sets the cache size for topic.
	 *
	 * @param topicName the topic name
	 * @param cacheSize the cache size
	 * @see EventService#setCacheSizeForTopic(String,int)
	 */
   public static void setCacheSizeForTopic(String topicName, int cacheSize) {
      EventServiceLocator.getEventBusService().setCacheSizeForTopic(topicName, cacheSize);
   }

   /**
	 * Sets the cache size for topic.
	 *
	 * @param pattern   the pattern
	 * @param cacheSize the cache size
	 * @see EventService#setCacheSizeForTopic(java.util.regex.Pattern,int)
	 */
   public static void setCacheSizeForTopic(Pattern pattern, int cacheSize) {
      EventServiceLocator.getEventBusService().setCacheSizeForTopic(pattern, cacheSize);
   }

   /**
	 * Gets the cache size for topic.
	 *
	 * @param topic the topic
	 * @return the cache size for topic
	 * @see EventService#getCacheSizeForTopic(String)
	 */
   public static int getCacheSizeForTopic(String topic) {
      return EventServiceLocator.getEventBusService().getCacheSizeForTopic(topic);
   }

   /**
	 * Gets the last event.
	 *
	 * @param <T>        the generic type
	 * @param eventClass the event class
	 * @return the last event
	 * @see EventService#getLastEvent(Class)
	 */
   public static <T> T getLastEvent(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getLastEvent(eventClass);
   }

   /**
	 * Gets the cached events.
	 *
	 * @param <T>        the generic type
	 * @param eventClass the event class
	 * @return the cached events
	 * @see EventService#getCachedEvents(Class)
	 */
   public static <T> List<T> getCachedEvents(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getCachedEvents(eventClass);
   }

   /**
	 * Gets the last topic data.
	 *
	 * @param topic the topic
	 * @return the last topic data
	 * @see EventService#getLastTopicData(String)
	 */
   public static Object getLastTopicData(String topic) {
      return EventServiceLocator.getEventBusService().getLastTopicData(topic);
   }

   /**
	 * Gets the cached topic data.
	 *
	 * @param topic the topic
	 * @return the cached topic data
	 * @see EventService#getCachedTopicData(String)
	 */
   public static List getCachedTopicData(String topic) {
      return EventServiceLocator.getEventBusService().getCachedTopicData(topic);
   }

   /**
	 * Clear cache.
	 *
	 * @param eventClass the event class
	 * @see EventService#clearCache(Class)
	 */
   public static void clearCache(Class eventClass) {
      EventServiceLocator.getEventBusService().clearCache(eventClass);
   }

   /**
	 * Clear cache.
	 *
	 * @param topic the topic
	 * @see EventService#clearCache(String)
	 */
   public static void clearCache(String topic) {
      EventServiceLocator.getEventBusService().clearCache(topic);
   }

   /**
	 * Clear cache.
	 *
	 * @param pattern the pattern
	 * @see EventService#clearCache(java.util.regex.Pattern)
	 */
   public static void clearCache(Pattern pattern) {
      EventServiceLocator.getEventBusService().clearCache(pattern);
   }

   /**
	 * Clear cache.
	 *
	 * @see org.bushe.swing.event.EventService#clearCache()
	 */
   public static void clearCache() {
      EventServiceLocator.getEventBusService().clearCache();
   }
}
