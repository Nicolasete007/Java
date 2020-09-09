package mainStuff;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class IsKeyPressed {
	private static volatile boolean spacePressed = false;
	private static volatile boolean shiftPressed = false;
	private static volatile boolean wPressed  = false;
	private static volatile boolean sPressed  = false;
	private static volatile boolean aPressed  = false;
	private static volatile boolean dPressed  = false;
	private static volatile boolean upPressed = false;
	private static volatile boolean doPressed = false;
	private static volatile boolean lePressed = false;
	private static volatile boolean riPressed = false;

	public static boolean isSpacePressed() {
		synchronized (IsKeyPressed.class) {
			return spacePressed;
		}
	}
	
	public static boolean isShiftPressed() {
		synchronized (IsKeyPressed.class) {
			return shiftPressed;
		}
	}
	
	public static boolean isWPressed() {
		synchronized (IsKeyPressed.class) {
			return wPressed;
		}
	}

	public static boolean isSPressed() {
		synchronized (IsKeyPressed.class) {
			return sPressed;
		}
	}

	public static boolean isAPressed() {
		synchronized (IsKeyPressed.class) {
			return aPressed;
		}
	}

	public static boolean isDPressed() {
		synchronized (IsKeyPressed.class) {
			return dPressed;
		}
	}
	
	public static boolean isUpPressed() {
		synchronized (IsKeyPressed.class) {
			return upPressed;
		}
	}

	public static boolean isDoPressed() {
		synchronized (IsKeyPressed.class) {
			return doPressed;
		}
	}

	public static boolean isLePressed() {
		synchronized (IsKeyPressed.class) {
			return lePressed;
		}
	}

	public static boolean isRiPressed() {
		synchronized (IsKeyPressed.class) {
			return riPressed;
		}
	}

	public static void initialize() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent ke) {
				synchronized (IsKeyPressed.class) {
					switch (ke.getID()) {
					case KeyEvent.KEY_PRESSED:
						if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
							spacePressed = true;
						}
						if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
							shiftPressed = true;
						}
						if (ke.getKeyCode() == KeyEvent.VK_W) {
							wPressed = true;
						}
						if (ke.getKeyCode() == KeyEvent.VK_S) {
							sPressed = true;
						}
						if (ke.getKeyCode() == KeyEvent.VK_A) {
							aPressed = true;
						}
						if (ke.getKeyCode() == KeyEvent.VK_D) {
							dPressed = true;
						}
						if (ke.getKeyCode() == KeyEvent.VK_UP) {
							upPressed = true;
						}
						if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
							doPressed = true;
						}
						if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
							lePressed = true;
						}
						if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
							riPressed = true;
						}
						break;

					case KeyEvent.KEY_RELEASED:
						if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
							spacePressed = false;
						}
						if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
							shiftPressed = false;
						}
						if (ke.getKeyCode() == KeyEvent.VK_W) {
							wPressed = false;
						}
						if (ke.getKeyCode() == KeyEvent.VK_S) {
							sPressed = false;
						}
						if (ke.getKeyCode() == KeyEvent.VK_A) {
							aPressed = false;
						}
						if (ke.getKeyCode() == KeyEvent.VK_D) {
							dPressed = false;
						}
						if (ke.getKeyCode() == KeyEvent.VK_UP) {
							upPressed = false;
						}
						if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
							doPressed = false;
						}
						if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
							lePressed = false;
						}
						if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
							riPressed = false;
						}
						break;
					}
					return false;
				}
			}
		});
	}
}