package main;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Player {
	private static double x, y, z, vel_z;
	private static int blockSize, dustCount = 0;
	public static ParticleEmitter[] jump_dust = new ParticleEmitter[100];
	
	/**
	 * Initialize the player at a position
	 * @param nx - start pos x
	 * @param ny - start pos y
	 */
	public static void init(int nx, int ny) {
		//Since main() is unavailable and it has no constructor, a function to initialize the static player is necessary.
		x = nx * window.getPanelWidth();
		y = ny * window.getPanelHeight();
	}
	
	
	/**
	 * Move player by relative distance dx and dy
	 * @param dx - movement difference x
	 * @param dy - movement difference y
	 */
	public static void move(double dx, double dy) {
	//	System.out.println("Player moved locally by "+ dx + ", "+dy);
		x += dx;
		y += dy;
		x = ControlManager.clamp(x, blockSize, Level.getWidth() * 32 - blockSize);	//Clamp outputs: it won't move out of bounds!
		y = ControlManager.clamp(y, blockSize, Level.getHeight() * 32 - blockSize);
		if(z + vel_z <= 0) {
			if(isJumping()) {
				GraphicsManager.emitters[dustCount + 500] = new ParticleEmitter((int)x + 16, (int)y + 25, (int)-vel_z + 5, -vel_z * 0.7, -vel_z * 1.3, 0.9, 0.9, 1, false, true, new Color(139, 69, 19));
				if(-vel_z > 15) ControlManager.shake_time = (int)(-vel_z * 0.6);
				GraphicsManager.emitters[dustCount + 500].setParticleSize(3);
				if(dustCount < 100) dustCount++; else dustCount = 0;
			}
			z = 0;
			vel_z = 0;
		} else {
			z += vel_z * (ControlManager.tick / 50.0);
			vel_z -= (ControlManager.tick / 50.0);
		}
		
	}
	
	
	public static double getX() {
		return x;
	}
	public static double getY() {
		return y;
	}
	
	public static double getZ() {return z;}
	
	
	public static void setBlockSize(int s) {
		blockSize = s;
	}
	
	
	/**
	 * Set z (vertical axis) velocity of player
	 * @param newVel - z axis velocity
	 */
	public static void setVelZ(double newVel) {
		vel_z = newVel;
	}
	
	
	/**
	 * Is the player jumping (is in the air)?
	 * @return true/false if player is or is not jumping respectively
	 */
	public static boolean isJumping() {
		return z == 0 ? false : true;
	}
	
	
	/**
	 * Draw the player's basic dummy icon and an oval shadow.
	 * @param g2 - Graphics2D to draw with
	 */
	public static void draw(Graphics2D g2) {
		int w = window.getPanelWidth();
		int h = window.getPanelHeight();
		Color oldColor = g2.getColor();
		g2.setColor(new Color(0, 0, 0, Math.max(0, 80 - (int)z)));
		g2.fillOval((int)x + 2, (int)y + 22, 30, 10);
		g2.setColor(Color.red);
		double mX = (w/20)/2;	//Adjustments to get the center point.
		double mY = (h/15)/2;
		try {
			Image playerPic = ImageIO.read(new File("lib/img/dummy_idle.png"));
			g2.drawImage(playerPic, (int)((mX-32)+x), (int)((mY-42)+y-z), null);
			
			
		} catch (IOException e) {
			System.out.println("Failed to get player icon!");
		}
		g2.setColor(oldColor);
	}
}
