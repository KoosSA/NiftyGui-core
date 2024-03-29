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

/**
 * This event is published internally to report timing for subscribers on an EventService.  Applications may subscribe to
 * this event to do handle subscribers that take too long.
 *
 * @author Michael Bushe michael@bushe.com
 * @see ThreadSafeEventService
 */
public class SubscriberTimingEvent extends AbstractEventServiceEvent {
   
   /** The start. */
   private Long start;
   
   /** The end. */
   private Long end;
   
   /** The time limit milliseconds. */
   private Long timeLimitMilliseconds;
   
   /** The event. */
   private Object event;
   
   /** The subscriber. */
   private EventSubscriber subscriber;
   
   /** The veto event listener. */
   private VetoEventListener vetoEventListener;
   
   /** The stringified. */
   private String stringified;

   /**
	 * Create a timing event.
	 *
	 * @param source                event source
	 * @param start                 system time at start of the notification of
	 *                              listener
	 * @param end                   system time at end of the notification of
	 *                              listener
	 * @param timeLimitMilliseconds expected maximum time
	 * @param event                 the published event
	 * @param subscriber            the event subscriber that went over the time
	 *                              limit, can be null if vetoEventListener is not
	 *                              null
	 * @param vetoEventListener     the vetoEventListener that took too long, can be
	 *                              null if the eventListener is not null
	 */
   public SubscriberTimingEvent(Object source, Long start, Long end, Long timeLimitMilliseconds,
           Object event, EventSubscriber subscriber, VetoEventListener vetoEventListener) {
      super(source);
      this.start = start;
      this.end = end;
      this.timeLimitMilliseconds = timeLimitMilliseconds;
      this.event = event;
      this.subscriber = subscriber;
      this.vetoEventListener = vetoEventListener;
      String type = "EventServiceSubscriber";
      String thing = ", EventServiceSubscriber:" + subscriber;
      if (vetoEventListener != null) {
         type = "VetoEventListener";
         thing = ", VetoEventListener" + vetoEventListener;
      }
      try {
         stringified = "Time limit exceeded for " + type + ". Handling time=" + (end.longValue() - start.longValue()) +
                 ", Time limit=" + timeLimitMilliseconds + ", event:" + event
                 + thing + ", start:" + start + ", end:" + end;
      } catch (Exception ex) {
         stringified = "Time limit exceeded for event, toString threw and exception.";
      }
   }

   /**
	 * Gets the start.
	 *
	 * @return system time at start of the notification of listener
	 */
   public Long getStart() {
      return start;
   }

   /**
	 * Gets the end.
	 *
	 * @return system time at end of the notification of listener
	 */
   public Long getEnd() {
      return end;
   }

   /**
	 * Gets the time limit milliseconds.
	 *
	 * @return expected maximum time
	 */
   public Long getTimeLimitMilliseconds() {
      return timeLimitMilliseconds;
   }

   /**
	 * Gets the event.
	 *
	 * @return the published event
	 */
   public Object getEvent() {
      return event;
   }

   /**
	 * Gets the subscriber.
	 *
	 * @return subscriber the event subscriber that went over the time limit, can be
	 *         null if vetoEventListener is not null
	 */
   public EventSubscriber getSubscriber() {
      return subscriber;
   }

   /**
	 * Gets the veto event listener.
	 *
	 * @return the vetoEventListener that took too long, can be null if the
	 *         eventListener is not null
	 */
   public VetoEventListener getVetoEventListener() {
      return vetoEventListener;
   }

   /**
	 * Checks if is veto exceeded.
	 *
	 * @return true if a veto listener took too long, false if an EventSubscriber
	 *         took took long
	 */
   public boolean isVetoExceeded() {
      return vetoEventListener != null;
   }

   /**
	 * Checks if is event handling exceeded.
	 *
	 * @return true if an EventSubscriber took too long, false if a veto listener
	 *         took took long
	 */
   public boolean isEventHandlingExceeded() {
      return subscriber == null;
   }

   /**
	 * To string.
	 *
	 * @return the string
	 */
   public String toString() {
      return stringified;
   }
}
