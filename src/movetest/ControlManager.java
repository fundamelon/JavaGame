import java.awt.event.*;

import javax.swing.Timer;

public class ControlManager {
	private char KEY_MOVE_NORTH = "W".charAt(0);
	private char KEY_MOVE_SOUTH = "S".charAt(0);
	private char KEY_MOVE_EAST = "D".charAt(0);
	private char KEY_MOVE_WEST = "A".charAt(0);
	
	private PlayerEnt player;
	
	public ControlManager(PlayerEnt oldPlayer) {
		player = oldPlayer;
	}
	
	
	private double playerMoveAmt = 0.25;
	
	private javax.swing.Timer playerMoveClk;
	
	public double getPlayerMoveAmt() {
		return playerMoveAmt;
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
		
	//	DEBUG: System.out.println("Player asked to move by "+dx+", "+dy);
		player.move(dx, dy);
		
	}
	
	//Keyboard manager.
	boolean[] keys = new boolean[525];
	
	public void keyDown(int kC) {
		if(!keys[kC]) {						//Check if they key hasn't been already registered - prevents glitches.
		
			//	DEBUG: System.out.println("Keydown event. "+(char)kC+" is now active.");
			
			keys[kC] = true;				//Add the key to a boolean array.
			movePlayerByAmt();			//Comment this out to remove instantaneous movement.  TODO: causes bugs in timer.
			if(playerMoveClk == null) {		//Initialize a timer if there isn't one.
				playerMoveClk = new javax.swing.Timer(60, new playerMove());
				playerMoveClk.start();
			}
			else
				playerMoveClk.restart();	//If there is, restart it.
			
		}
	}
	public void keyUp(int kC) {
		keys[kC] = false;		//Remove the key from the boolean array.
	}
	public boolean keyStatus(int kC) {
		return keys[kC];
	}
	
	public char getKeyN() {return KEY_MOVE_NORTH;}
	public char getKeyS() {return KEY_MOVE_SOUTH;}
	public char getKeyE() {return KEY_MOVE_EAST;}
	public char getKeyW() {return KEY_MOVE_WEST;}
	
}
