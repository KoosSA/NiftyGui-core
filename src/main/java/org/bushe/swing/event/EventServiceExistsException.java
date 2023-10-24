package org.bushe.swing.event;

/** Exception thrown by the EventServiceLocator when an EventService already is registered for a name. */
public class EventServiceExistsException extends Exception {
   
   /**
	 * Instantiates a new event service exists exception.
	 *
	 * @param msg the msg
	 */
   public EventServiceExistsException(String msg) {
      super(msg);
   }
}
