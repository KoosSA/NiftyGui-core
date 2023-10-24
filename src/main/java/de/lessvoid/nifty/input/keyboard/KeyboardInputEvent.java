package de.lessvoid.nifty.input.keyboard;

/**
 * A Keyboard Input Event.
 *
 * @author void
 */
public class KeyboardInputEvent {

	/** The key none. */
	public static int KEY_NONE = 0;

	/** The key escape. */
	public static int KEY_ESCAPE = 1;
	
	/** The key tab. */
	public static int KEY_TAB = 15;
	
	/** The key return. */
	public static int KEY_RETURN = 28; /* Enter on main keyboard */
	
	/** The key space. */
	public static int KEY_SPACE = 57;
	
	/** The key f1. */
	public static int KEY_F1 = 59;
	
	/** The key up. */
	public static int KEY_UP = 200; /* UpArrow on arrow keypad */
	
	/** The key left. */
	public static int KEY_LEFT = 203; /* LeftArrow on arrow keypad */
	
	/** The key right. */
	public static int KEY_RIGHT = 205; /* RightArrow on arrow keypad */
	
	/** The key down. */
	public static int KEY_DOWN = 208; /* DownArrow on arrow keypad */

	/** The key delete. */
	public static int KEY_DELETE;
	
	/** The key back. */
	public static int KEY_BACK;
	
	/** The key end. */
	public static int KEY_END;
	
	/** The key home. */
	public static int KEY_HOME;
	
	/** The key x. */
	public static int KEY_X;
	
	/** The key rshift. */
	public static int KEY_RSHIFT;
	
	/** The key lshift. */
	public static int KEY_LSHIFT;
	
	/** The key c. */
	public static int KEY_C;
	
	/** The key v. */
	public static int KEY_V;
	
	/** The key a. */
	public static int KEY_A;

	/**
	 * key.
	 */
	private int key;

	/**
	 * character.
	 */
	private char character;

	/**
	 * keyDown.
	 */
	private boolean keyDown;

	/**
	 * shiftDown.
	 */
	private boolean shiftDown;

	/**
	 * controlDown.
	 */
	private boolean controlDown;

	/**
	 * Instantiates a new keyboard input event.
	 */
	public KeyboardInputEvent() {
	}

	/**
	 * create the event.
	 *
	 * @param newKey         key
	 * @param newCharacter   character
	 * @param newKeyDown     keyDown
	 * @param newShiftDown   shiftDown
	 * @param newControlDown controlDown
	 */
	public KeyboardInputEvent(int newKey, char newCharacter, boolean newKeyDown, boolean newShiftDown,
			boolean newControlDown) {
		setData(newKey, newCharacter, newKeyDown, newShiftDown, newControlDown);
	}

	/**
	 * Sets the data.
	 *
	 * @param newKey         the new key
	 * @param newCharacter   the new character
	 * @param newKeyDown     the new key down
	 * @param newShiftDown   the new shift down
	 * @param newControlDown the new control down
	 */
	public void setData(int newKey, char newCharacter, boolean newKeyDown, boolean newShiftDown,
			boolean newControlDown) {
		
		this.keyDown = newKeyDown;
		this.shiftDown = newShiftDown;
		this.controlDown = newControlDown;
		if (shiftDown && newKey != KeyboardInputEvent.KEY_SPACE) {
			this.key = newKey;
			this.character = newCharacter;
		} else {
			this.key = Character.toLowerCase(newKey);
			this.character = Character.toLowerCase(newCharacter);
		}
		
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public int getKey() {
		return key;
	}

	/**
	 * Gets the character.
	 *
	 * @return the character
	 */
	public char getCharacter() {
		return character;
	}

	/**
	 * Checks if is key down.
	 *
	 * @return the keyDown
	 */
	public boolean isKeyDown() {
		return keyDown;
	}

	/**
	 * Checks if is shift down.
	 *
	 * @return the shiftDown
	 */
	public boolean isShiftDown() {
		return shiftDown;
	}

	/**
	 * Checks if is control down.
	 *
	 * @return the controlDown
	 */
	public boolean isControlDown() {
		return controlDown;
	}
}
