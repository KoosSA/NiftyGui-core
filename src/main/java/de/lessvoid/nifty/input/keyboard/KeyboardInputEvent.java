package de.lessvoid.nifty.input.keyboard;

/**
 * A Keyboard Input Event.
 *
 * @author void
 */
public class KeyboardInputEvent {

	public static int KEY_NONE = 0;

	public static int KEY_ESCAPE = 1;
	public static int KEY_TAB = 15;
	public static int KEY_RETURN = 28; /* Enter on main keyboard */
	public static int KEY_SPACE = 57;
	public static int KEY_F1 = 59;
	public static int KEY_UP = 200; /* UpArrow on arrow keypad */
	public static int KEY_LEFT = 203; /* LeftArrow on arrow keypad */
	public static int KEY_RIGHT = 205; /* RightArrow on arrow keypad */
	public static int KEY_DOWN = 208; /* DownArrow on arrow keypad */

	public static int KEY_DELETE;
	public static int KEY_BACK;
	public static int KEY_END;
	public static int KEY_HOME;
	public static int KEY_X;
	public static int KEY_RSHIFT;
	public static int KEY_LSHIFT;
	public static int KEY_C;
	public static int KEY_V;
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
	 * @return the key
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @return the character
	 */
	public char getCharacter() {
		return character;
	}

	/**
	 * @return the keyDown
	 */
	public boolean isKeyDown() {
		return keyDown;
	}

	/**
	 * @return the shiftDown
	 */
	public boolean isShiftDown() {
		return shiftDown;
	}

	/**
	 * @return the controlDown
	 */
	public boolean isControlDown() {
		return controlDown;
	}
}
