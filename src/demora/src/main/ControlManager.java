package main;

import main.entity.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


public class ControlManager {
	
	//declaration of characters to watch for 
	private static char KEY_MOVE_NORTH = "W".charAt(0);
	private static char KEY_MOVE_SOUTH = "S".charAt(0);
	private static char KEY_MOVE_EAST  = "D".charAt(0);
	private static char KEY_MOVE_WEST  = "A".charAt(0);
	private static float smoothTickX = 0, smoothTickY = 0;
	private static boolean anyKeysPressed = false;
	
	static boolean[] keyStatus = new boolean[Keyboard.getKeyCount()];
	static boolean[] keyToggle = new boolean[keyStatus.length];
	static boolean[] keyPressed =  new boolean[keyStatus.length];
	static boolean[] keyReleased = new boolean[keyStatus.length];
	
	private static float[] mouseTraceX = new float[64];
	private static float[] mouseTraceY = new float[64];
	private static int traceCount;
	
	private static int mousePrevX = 0, mousePrevY = 0, mouseDX, mouseDY;
	
	private static boolean[] mouseButtonStatus = new boolean[16];
	private static boolean[] mouseButtonClicked = new boolean[16];
	private static boolean[] mouseButtonReleased = new boolean[16];
	
	
	public static int keyToggleMapRendering = Keyboard.KEY_M;
	public static int keyToggleMeGusta = Keyboard.KEY_N;
	
	
	public static int mousePrimary = 0;
	public static int mouseSecondary = 1;
	
	public static int shake_time = 0;
	
	public static int delta;
	
	public static Entity currentEntity;
		
	/**Initialize to preconditional status*/
	public static void init() {
		traceCount = 0;
		
		setKeyToggle(keyToggleMapRendering);
		setKeyToggle(keyToggleMeGusta);
	}
	
	/**Run through input devices and update status*/
	public static void update(int delta) {
		ControlManager.delta = delta;
		
		updatePlayerCtrls();
		updateMouseButtons();
		updateKeys();
		
//		if(keyStatus(keyToggleMapRendering)) {
//			System.out.println("Map rendering: ON! :D");
//		} else {
//			System.out.println("Map rendering: OFF! :C");
//		}
		
		mouseTraceX[traceCount] = Mouse.getX();
		mouseTraceY[traceCount] = Mouse.getY();
		traceCount++;
		traceCount %= mouseTraceX.length;
		
		mouseDX = Mouse.getDX();
		mouseDY = Mouse.getDY();
		
	}
	
	public static float[] getMouseTraceXArr() {
		return mouseTraceX;
	}
	public static float[] getMouseTraceYArr() {
		return mouseTraceY;
	}
	public static int getMouseTraceLength() {
		return traceCount;
	}
	
	public static int getDelta() {
		return delta;
	}

	/**
	 * Manually set mouse position
	 * @param nx - new pos x
	 * @param ny - new pos y
	 */
	
	/** @return Mouse x position */
	public static int getMouseX() {
		return Mouse.getX();
	}
	
	
	/** @return Mouse y position */
	public static int getMouseY() {
		return GameBase.getHeight() - Mouse.getY();
	}
	
	public static int getMouseDX() {
		return mouseDX;
	}
	
	public static int getMouseDY() {
		return mouseDY;
	}
	
	/**
	 * Function fired regardless of keys pressed; keys are additively combined
	 */
	public static void updatePlayerCtrls() {
		//dx and dy are distance x and y respectively - these are sent to the player.
		float dx=0, dy=0;

		//Get keys pressed.
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			dy = dy - 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			dy = dy + 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			dx = dx + 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			dx = dx - 1;
		}

		//movement and movement speed controls.
		if(dx == 0 && smoothTickX > 0)
			smoothTickX-=0.2;
		else if(dx != 0 && smoothTickX<1)
			smoothTickX+=0.2;
		
		if(dy == 0 && smoothTickY > 0) 
			smoothTickY-=0.2;
		else if(dy != 0 && smoothTickY<1)
			smoothTickY+=0.2;
		
		//Reduce dx and dy by smoothing factors.
		dx *= smoothTickX;
		dy *= smoothTickY;
		
		//Reset smoothing if you're at a standstill.
		if(!anyKeysPressed) {
	//		playerMoveClk.stop();
			smoothTickX = 0;
			smoothTickY = 0;
		}
	//	DEBUG: System.out.println("Player asked to move by "+dx+", "+dy);
		
		//Slow the player down if diagonal
		if(dx != 0 && dy != 0) {
			dx *= 0.7;
			dy *= 0.7;
		}
		EntityManager.getPlayer().move(dx * EntityManager.getPlayer().getMoveSpeed() * delta, dy * EntityManager.getPlayer().getMoveSpeed() * delta);
	
	}
	
	
	
	
	public static void updateKeys() {
		for(int i = 0; i < Keyboard.getKeyCount(); i++) {
			if(Keyboard.isKeyDown(i)) {
				if(keyStatus[i]) {
					keyPressed[i] = false;
				} else {
					keyPressed[i] = true;
					
					if(GameBase.debug_keyboard) {
						System.out.println("Key "+Keyboard.getKeyName(i)+" pressed");
					}
				}
				
				if(!keyToggle[i]) {
					keyStatus[i] = true;
				} else if(Keyboard.isKeyDown(i) != keyStatus[i]) {
					keyStatus[i] = !keyStatus[i];
				}
			} else {
				if(!keyStatus[i] || keyReleased[i]) {
					keyReleased[i] = false;
				} else {
					keyReleased[i] = true;
					
					if(GameBase.debug_keyboard) {
						System.out.println("Key "+Keyboard.getKeyName(i)+" released");
					}
				}
				
				if(!keyToggle[i]) {
					keyStatus[i] = false;
				}
			}
		}
	}
	
	public static void updateMouseButtons() {
		for(int i = 0; i < 16; i++) {
			if(Mouse.isButtonDown(i)) {
				if(mouseButtonStatus[i]) {
					mouseButtonClicked[i] = false;
				} else {
					mouseButtonClicked[i] = true;
					
					if(GameBase.debug_mouse){
						System.out.println("Mouse "+Mouse.getButtonName(i)+" pressed");
					}
				}
				
				mouseButtonStatus[i] = true;
			} else {
				if(!mouseButtonStatus[i] || mouseButtonReleased[i]) {
					mouseButtonReleased[i] = false;
				} else {
					mouseButtonReleased[i] = true;

					if(GameBase.debug_mouse) {
						System.out.println("Mouse "+Mouse.getButtonName(i)+" released");
					}
				}
				
				mouseButtonStatus[i] = false;
			}
		}
	}
	
	/**Status of a key on the keyboard*/
	public static boolean keyStatus(int i) {
		return keyStatus[i];
	}
	
	/**If key was just pressed*/
	public static boolean keyPressed(int i) {
		return keyPressed[i];
	}
	
	/**If key was just released*/
	public static boolean keyReleased(int i) {
		return keyReleased[i];
	}
	
	/**If the key is in toggle mode*/
	public static boolean keyToggleable(int i) {
		return keyToggle[i];
	}
	
	/**Set a key to toggle mode*/
	public static void setKeyToggle(int i) {
		keyToggle[i] = true;
	}
	
	/**Remove key toggle mode*/
	public static void resetKeyToggle(int i) {
		keyToggle[i] = false;
	}
	
	/**Status of a mouse button*/
	public static boolean mouseButtonStatus(int i) {
		return mouseButtonStatus[i];
	}
	
	/**If mouse button was just clicked*/
	public static boolean mouseButtonClicked(int i) {
		return mouseButtonClicked[i];
	}
	
	/**If mouse button was just released*/
	public static boolean mouseButtonReleased(int i) {
		return mouseButtonReleased[i];
	}
	
	public static char getKeyN() {return KEY_MOVE_NORTH;}
	public static char getKeyS() {return KEY_MOVE_SOUTH;}
	public static char getKeyE() {return KEY_MOVE_EAST;}
	public static char getKeyW() {return KEY_MOVE_WEST;}
	
	/**
	 * Clamp a number i between high and low limits
	 * @param i - number to clamp
	 * @param high - upper limit
	 * @param low - lower limit
	 * @return clamped value
	 */
	public static double clamp(double i, double high, double low) {
		return Math.max(high, Math.min(i, low));
	}
	
	/**Identical to {@link main.ControlManager#clamp(double, double, double) clamp()}*/
	public static float clamp(float i, float high, float low) {
		return Math.max(high, Math.min(i, low));
	}
	
	/**Identical to {@link main.ControlManager#clamp(double, double, double) clamp()}*/
	public static int clamp(int i, int high, int low) {
		return Math.max(high, Math.min(i, low));
	}
}
