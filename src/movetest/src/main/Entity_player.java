package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Entity_player extends Entity_human {

	private float x, y, z, vel_z;
	private int blockSize, dustCount = 0;
	public ParticleEmitter[] jump_dust = new ParticleEmitter[100];
	
	/**
	 * Initialize the player at a position
	 * @param nx - start pos x
	 * @param ny - start pos y
	 * @param tilewise - True means tiles, false means pixels.
	 */
	public void init(int nx, int ny, boolean tilewise) {
		super.moveSpeed = 120.0f;
		
		x = nx * (tilewise ? 1 : Window.getPanelWidth());
		y = ny * (tilewise ? 1 : Window.getPanelHeight());
	}
	
	public float getMoveSpeed() {
		return moveSpeed;
	}
	
	/**
	 * Move player by relative distance dx and dy
	 * @param dx - movement difference x
	 * @param dy - movement difference y
	 */
	public void move(float dx, float dy) {
	//	System.out.println("Player moved locally by "+ dx + ", "+dy);
		x += dx * ControlManager.getLagComp();
		y += dy * ControlManager.getLagComp();
		x = ControlManager.clamp(x, blockSize, GameBase.getZone().getWidth() * 32 - blockSize);
		y = ControlManager.clamp(y, blockSize, GameBase.getZone().getHeight() * 32 - blockSize);
		
		//If player is traveling downward and hits the original start pos then handle that accordingly
		if(z <= 0 && Math.signum(vel_z) == -1) {
			if(isJumping()) {
				GraphicsManager.emitters[dustCount + 500] = new ParticleEmitter((int)x + 16, (int)y + 25, (int)(-vel_z * 0.1) + 5, -vel_z * 7, -vel_z * 1.3f, 0.9f, 0.9f, 1, false, true, new Color(139, 69, 19));
				if(-vel_z > 1500) ControlManager.shake_time = (int)(-vel_z * 0.1);
				GraphicsManager.emitters[dustCount + 500].setParticleSize(3);
				if(dustCount < 100) dustCount++; else dustCount = 0;
			}
			
			z = 0;
			vel_z = 0;
		} else {
			z += vel_z * ControlManager.getLagComp();
			vel_z -= 1000 * ControlManager.getLagComp();
		}
		
	}
	
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	
	public float getZ() {return z;}
	
	
	public void setBlockSize(int s) {
		blockSize = s;
	}
	
	
	/**
	 * Set z (vertical axis) vel of entity
	 * @param newVel - z vel
	 */
	public void setVelZ(float newVel) {
		vel_z = newVel;
	}
	
	
	/**
	 * Is the entity jumping (is in the air)?
	 * @return true/false if player is or is not jumping respectively
	 */
	public boolean isJumping() {
		return z == 0 ? false : true;
	}
	
	
	/**
	 * Draw the entity basic dummy icon and an oval shadow.
	 * @param g2 - Graphics2D to draw with
	 */
	public void draw(Graphics2D g2) {
		int w = Window.getPanelWidth();
		int h = Window.getPanelHeight();
		Color oldColor = g2.getColor();
		g2.setColor(new Color(0, 0, 0, Math.max(0, 80 - (int)z)));
		g2.fillOval((int)x + 2, (int)y + 22, 30, 10);
		g2.setColor(Color.red);
		float mX = (w/20)/2;	//Adjustments to get the center point.
		float mY = (h/15)/2;
		try {
			Image playerPic = ImageIO.read(new File("lib/img/dummy_idle.png"));
			g2.drawImage(playerPic, (int)((mX-32)+x), (int)((mY-42)+y-z), 64, 64, null);
			
			
		} catch (IOException e) {
			System.out.println("Failed to get player icon!");
		}
		g2.setColor(oldColor);
	}

	@Override
	public void jump() {
		this.vel_z += 300;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move(double nx, double ny) {
		// TODO Auto-generated method stub
		
	}
}
