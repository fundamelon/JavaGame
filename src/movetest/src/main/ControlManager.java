package main;
import java.awt.event.*;

//It seems we don't need this.  We'll see about that later.
//import javax.swing.Timer;

public class ControlManager {
	
	//declaration of characters to watch for 
	private char KEY_MOVE_NORTH = "W".charAt(0);
	private char KEY_MOVE_SOUTH = "S".charAt(0);
	private char KEY_MOVE_EAST = "D".charAt(0);
	private char KEY_MOVE_WEST = "A".charAt(0);
	@SuppressWarnings("unused")
	private char KEY_RESET_RAND = "R".charAt(0);
	private double smoothTickX = 0, smoothTickY = 0;
	private boolean anyKeysPressed = false;
	private int ticks;
	
	private PlayerEnt player;
	
	
	public ControlManager(grid panel, PlayerEnt oldPlayer) {
		player = oldPlayer;
	}
	
	/*  What happened here? FIX IT >:(
<<<<<<< HEAD
	private double playerMoveAmt = 1.5;
=======
	private double playerMoveAmt = 20;
>>>>>>> 746b475b3700c7468a53c4a3185f17fc7585cd58
	
	*/
	
	//Player move amount and move timer varibabelz
	private double playerMoveAmt = 2.3;
//	private javax.swing.Timer playerMoveClk;  
	
	
	//Just an accessor in case we need it
	public double getPlayerMoveAmt() {
		return playerMoveAmt; 
	}
	
	//Fire the move function when the timer is triggered.
	public void clk(int n) {
		ticks = n;
		movePlayerByAmt();	
	}
	
	public void movePlayerByAmt() {		//Check for keys, send a message to the player.  Instantaneous.
		//dx and dy are distance x and y respectively - these are sent to the player.
		double dx=0, dy=0;

		//Get keys pressed.
		if(keys[KEY_MOVE_NORTH]){
			dy = dy - playerMoveAmt;
			distancepy++;
		}
		if(keys[KEY_MOVE_SOUTH]){
			dy = dy + playerMoveAmt;
			distanceny++;
		}
		if(keys[KEY_MOVE_EAST]){
			dx = dx + playerMoveAmt;
			distancepx++;
		}
		if(keys[KEY_MOVE_WEST]){
			dx = dx - playerMoveAmt;
			distancenx++;
		}

		//movement and movement speed controls.
		if(dx == 0 && smoothTickX > 0)
			smoothTickX-=0.1;
		else if(dx != 0 && smoothTickX<1)
			smoothTickX+=0.1;
		
		if(dy == 0 && smoothTickY > 0) 
			smoothTickY-=0.1;
		else if(dy != 0 && smoothTickY<1)
			smoothTickY+=0.1;
		
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
		player.move(dx, dy);
	
	}
	
	//Character array of keys pressed.
	boolean[] keys = new boolean[525];
	
	//Called when grid gets a key down event.
	public void keyDown(int kC) {
		anyKeysPressed = true;
		
		if(!keys[kC]) {						//Check if they key hasn't been already registered - prevents glitches.
		
			//	DEBUG: System.out.println("Keydown event. "+(char)kC+" is now active.");
			
			keys[kC] = true;				//Add the key to a boolean array.
	//		if(playerMoveClk == null) {		//Initialize a timer if there isn't one.
	//			playerMoveClk = new javax.swing.Timer(10, new playerMove());
	//			playerMoveClk.start();
	//		}
	//		else if(!playerMoveClk.isRunning())
	//			playerMoveClk.restart();	//If there is, restart it.
			
		}
	}
	public void keyUp(int kC) {  
		// kC is ASCII char code.
		//Remove the key from the boolean array.
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
	public boolean getKeyStatus(int kC) {
		return keys[kC];
	}
	
	public boolean getKeyStatus(char kC) {
		return keys[kC];
	}
	
	public void setKeyStatus(int kC, boolean newState) {
		keys[kC] = newState;
	}
	
	public void setKeyStatus(char kC, boolean newState) {
		keys[kC] = newState;
	}
	
	public char getKeyN() {return KEY_MOVE_NORTH;}
	public char getKeyS() {return KEY_MOVE_SOUTH;}
	public char getKeyE() {return KEY_MOVE_EAST;}
	public char getKeyW() {return KEY_MOVE_WEST;}
	
	//Function will return the number i clamped between high and low.  Used for now for boundary clamping
	//TODO: Rewrite collision algorithm!  This is just a hack.
	public static double clamp(double i, int high, int low) {
		return Math.max (high, Math.min (i, low));
	}
	//backwards clamp win?
	//public static double stop(double i, int high, int low) {
	//	return Math.min (high, Math.max (i, low))
	//}
	
	
	
}
