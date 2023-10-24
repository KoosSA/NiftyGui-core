package de.lessvoid.nifty;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * You can register an instance of this interface with Nifty and Nifty will notify you for every event it receives
 * and if Nifty actually handled it. This is probably only useful for debugging purpose tho.
 * 
 * @author void
 */
public interface NiftyInputConsumerNotify {
  
  /**
	 * Processed mouse event.
	 *
	 * @param mouseX     the mouse X
	 * @param mouseY     the mouse Y
	 * @param mouseWheel the mouse wheel
	 * @param button     the button
	 * @param buttonDown the button down
	 * @param processed  the processed
	 */
  void processedMouseEvent(int mouseX, int mouseY, int mouseWheel, int button, boolean buttonDown, boolean processed);
  
  /**
	 * Process keyboard event.
	 *
	 * @param keyEvent  the key event
	 * @param processed the processed
	 */
  void processKeyboardEvent(KeyboardInputEvent keyEvent, boolean processed);
}
