package main;
import java.awt.event.*;

import javax.swing.Timer;

public class ControlManager {
	private char KEY_MOVE_NORTH = "W".charAt(0);
	private char KEY_MOVE_SOUTH = "S".charAt(0);
	private char KEY_MOVE_EAST = "D".charAt(0);
	private char KEY_MOVE_WEST = "A".charAt(0);
	
	private char KEY_RESET_RAND = "R".charAt(0);
	
	private double smoothTickX = 0, smoothTickY = 0, smoothAmt = 0.1;
	private boolean anyKeysPressed = false;
	public double distance;
	
	private PlayerEnt player;
	
	
	public ControlManager(grid panel, PlayerEnt oldPlayer) {
		player = oldPlayer;
	}
	
	
	private double playerMoveAmt = 1;
	
	private javax.swing.Timer playerMoveClk;  //how is the timer integerated? move speed?
	
	public double getPlayerMoveAmt() {
		return playerMoveAmt; //what does this code do?
	}
	
	private class playerMove implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			movePlayerByAmt();			//Fire the move function when the timer is triggered.
		}
	}
	
	public void movePlayerByAmt() {		//Check for keys, send a message to the player.  Instantaneous.
		double dx=0, dy=0;

		if(keys[KEY_MOVE_NORTH])
			dy = dy - playerMoveAmt;
		if(keys[KEY_MOVE_SOUTH])
			dy = dy + playerMoveAmt;
		if(keys[KEY_MOVE_EAST])
			dx = dx + playerMoveAmt;
		if(keys[KEY_MOVE_WEST])
			dx = dx - playerMoveAmt;

		if(dx == 0 && smoothTickX > 0)
			smoothTickX-=0.1;
		else if(dx != 0 && smoothTickX<1)
			smoothTickX+=0.1;
		
		if(dy == 0 && smoothTickY > 0) 
			smoothTickY-=0.1;
		else if(dy != 0 && smoothTickY<1)
			smoothTickY+=0.1;
		
		dx *= smoothTickX;
		dy *= smoothTickY;
		
		if(!anyKeysPressed) {
			playerMoveClk.stop();
			smoothTickX = 0;
			smoothTickY = 0;
		}
	//	DEBUG: System.out.println("Player asked to move by "+dx+", "+dy);
		
		if(dx != 0 && dy != 0) {
			dx *= 0.8;
			dy *= 0.8;
		}
		player.move(dx, dy);
	
	}
	
	//Keyboard manager.
	boolean[] keys = new boolean[525];
	
	public void keyDown(int kC) {
		anyKeysPressed = true;
		
		if(!keys[kC]) {						//Check if they key hasn't been already registered - prevents glitches.
		
			//	DEBUG: System.out.println("Keydown event. "+(char)kC+" is now active.");
			
			keys[kC] = true;				//Add the key to a boolean array.
			if(playerMoveClk == null) {		//Initialize a timer if there isn't one.
				playerMoveClk = new javax.swing.Timer(15, new playerMove());
				playerMoveClk.start();
			}
			else if(!playerMoveClk.isRunning())
				playerMoveClk.restart();	//If there is, restart it.
			
		}
	}
	public void keyUp(int kC) {
		keys[kC] = false;		//Remove the key from the boolean array.
		anyKeysPressed=false;
		for(int i=0; i<keys.length; i++) {
			if(keys[i]) {
				anyKeysPressed = true;
				break;
			}
		}
	}
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
	

	public static double clamp(double i, int high, int low) {
		return Math.max (high, Math.min (i, low));
	}
	
}
