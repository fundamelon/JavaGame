package main;
import java.awt.Point;
import java.awt.event.*;
import java.util.Scanner;

//It seems we don't need this.  We'll see about that later.
//import javax.swing.Timer;

public class ControlManager {
	
	//declaration of characters to watch for 
	private static char KEY_MOVE_NORTH = "W".charAt(0);
	private static char KEY_MOVE_SOUTH = "S".charAt(0);
	private static char KEY_MOVE_EAST = "D".charAt(0);
	private static char KEY_MOVE_WEST = "A".charAt(0);
	private static float smoothTickX = 0, smoothTickY = 0;
	private static boolean anyKeysPressed = false;
	static boolean[] keys = new boolean[525];
	static boolean[] keyPressed =  new boolean[keys.length];
	
	private static GraphicsManager gameGraphics;
	public static long tick, lastTick, tickdiff;
	private static float mousePos_x, mousePos_y;
	private static int ticks;
	public static int shake_time = 0;
	
	public static int lag;
	
	public static Entity currentEntity;
	public static Entity_player player = GameBase.getPlayerEntity();
		
	

	/**
	 * Manually set mouse position
	 * @param nx - new pos x
	 * @param ny - new pos y
	 */
	public static void setMousePos(float nx, float ny) {
		mousePos_x = nx;
		mousePos_y = ny;
	}
	
	
	/** @return Mouse x position */
	public static float getMouseX() {
		return mousePos_x;
	}
	
	
	/** @return Mouse y position */
	public static float getMouseY() {
		return mousePos_y;
	}
	
	
	/**
	 * Clock that fires the move method when the timer is triggered.
	 * @param n - delay since last clock
	 */
	public static void clk(long n) {
		lastTick = tick;
		tick = n;
		movePlayerByAmt();	
		tryJump(player);
		updateUtilKeys();
		
		if(getKeyStatus("H".charAt(0)))
			GraphicsManager.showHelperText(true);
		else 
			GraphicsManager.showHelperText(false);
		
		if(getKeyStatus("8".charAt(0)))
			GraphicsManager.shake();
		if(shake_time != 0) {
			GraphicsManager.shake();
			shake_time -= 1;
		}
		
	}
	
	
	public static void setLag(int n) {
		lag = n;
	}
	
	public static int getLag() {
		return lag;
	}
	
	public static float getLagComp() {
		return lag / 1000.0f;
	}
	
	/**
	 * Function fired regardless of keys pressed; keys are additively combined
	 */
	public static void movePlayerByAmt() {		//Check for keys, send a message to the player.  Instantaneous.
		//dx and dy are distance x and y respectively - these are sent to the player.
		float dx=0, dy=0;

		//Get keys pressed.
		if(keys[KEY_MOVE_NORTH]){
			dy = dy - 1;
		}
		if(keys[KEY_MOVE_SOUTH]){
			dy = dy + 1;
		}
		if(keys[KEY_MOVE_EAST]){
			dx = dx + 1;
		}
		if(keys[KEY_MOVE_WEST]){
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
		
		//Slow the player down so that they dont go square root of 2 speed times normal diagonally.
		if(dx != 0 && dy != 0) {
			dx *= 0.7;
			dy *= 0.7;
		}
		player.move(dx * player.getMoveSpeed(), dy * player.getMoveSpeed());
	
	}
	
	
	/**
	 * Set Player's Z velocity upward to simulate a jump
	 */
	public static void tryJump(Entity ent) {
		if(keys[" ".charAt(0)] && !player.isJumping()) 
			ent.jump();
	}
	
	
	/**
	 * Hack method for 0 and 9 for the fade function only
	 */
	public static void updateUtilKeys() {
		if(keys["0".charAt(0)]) 
			GraphicsManager.setFade(false);
		
		if(keys["9".charAt(0)])
			GraphicsManager.setFade(true);
	}
	
	
	//Character array of keys pressed.
	
	/**
	 * Called when grid gets a key down event.
	 * @param kC - Char value of activated key
	 */
	public static void keyDown(KeyEvent e) {
		anyKeysPressed = true;
		int kC = e.getKeyCode();
		updateUtilKeys();
		
		if(!keys[kC]) {						//Check if they key hasn't been already registered - prevents glitches.
		//	System.out.println(kC);
			keys[kC] = true;				//Add the key to a boolean array.
			if(kC == 27) {
				if(GameLogic.inMainMenu()) GameLogic.closeMainMenu(); else GameLogic.openMainMenu();
				GraphicsManager.first_run = true;
			}
			
		}
	}
	
	
	/**
	 * Called when grid gets a key up event.
	 * @param kC - Char value of deactivated key
	 */
	public static void keyUp(KeyEvent e) {  
		// kC is ASCII char code.
		//Remove the key from the boolean array.
		int kC = e.getKeyCode();
		keys[kC] = false;		
		anyKeysPressed=false;
		for(int i=0; i<keys.length; i++) {
			if(keys[i]) {
				anyKeysPressed = true; 
				break;
			}
		}
	}
	
	//Some backup methods - just in case we need them.
	
	/**
	 * Get key status of selected key char value, 0-500.
	 * @param kC - char value of key in question
	 * @return boolean key status: true is down, false is up
	 */
	public static boolean getKeyStatus(int kC) {
		return keys[kC];
	}
	
	
	
	public static void setKeyStatus(int kC, boolean newState) {
		keys[kC] = newState;
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
	
	public static float clamp(float i, float high, float low) {
		return Math.max(high, Math.min(i, low));
	}
	
	public static int clamp(int i, int high, int low) {
		return Math.max(high, Math.min(i, low));
	}

	
	
	
}
