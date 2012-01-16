package main.entity;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import main.ControlManager;
import main.GameBase;
import main.GraphicsManager;
import main.ParticleEmitter;

public class Entity_player extends Entity_mobile {
	
	private Image PLR_FRONT = null;
	private Image PLR_BACK = null;
	private Image PLR_LEFT = null;
	private Image PLR_RIGHT = null;
	
	private Image CUR_IMG = PLR_FRONT;
	
	private float vel_z;
	private int blockSize, dustCount = 0;
	public ParticleEmitter[] jump_dust = new ParticleEmitter[100];
	public float moveSpeed = 0.6f;
	
	/**
	 * Initialize the player at a position
	 * @param nx - start pos x
	 * @param ny - start pos y
	 * @param tilewise - True means tiles, false means pixels.
	 */
	@Override
	public void init(int nx, int ny, boolean tilewise) {
		
		x = nx * (tilewise ? 1 : GameBase.getWidth());
		y = ny * (tilewise ? 1 : GameBase.getHeight());
		
		try {
			PLR_FRONT = new Image("lib/img/char/girl_front_static.jpeg");
			PLR_BACK =  new Image("lib/img/char/girl_back_static.jpeg");
			PLR_LEFT =  new Image("lib/img/char/girl_left_static.jpeg");
			PLR_RIGHT = new Image("lib/img/char/girl_right_static.jpeg");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Failed to get player images");
		}
		
		System.out.print("player initialized");
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
		//skip if you're not gonna move
		if(dx == 0 && dy == 0) return;
		
		System.out.println("Player pos: "+ x + ", "+y);
		x += dx;
		y += dy;
		x = ControlManager.clamp(x, blockSize, GameBase.getZone().getWidth() * 32 - blockSize);
		y = ControlManager.clamp(y, blockSize, GameBase.getZone().getHeight() * 32 - blockSize);
		
		//If player is traveling downward and hits the original start pos then handle that accordingly
		if(z <= 0 && Math.signum(vel_z) == -1) {
			if(isJumping()) {
			//	GraphicsManager.emitters[dustCount + 500] = new ParticleEmitter((int)x + 16, (int)y + 25, (int)(-vel_z * 0.1) + 5, -vel_z * 7, -vel_z * 1.3f, 0.9f, 0.9f, 1, false, true, new Color(139, 69, 19));
				if(-vel_z > 1500) ControlManager.shake_time = (int)(-vel_z * 0.1);
				GraphicsManager.emitters[dustCount + 500].setParticleSize(3);
				if(dustCount < 100) dustCount++; else dustCount = 0;
			}
			
			z = 0;
			vel_z = 0;
		} else {
	//		z += vel_z * ControlManager.getLagComp();
	//		vel_z -= 1000 * ControlManager.getLagComp();
		}
		
		//image handler
		if(dx * dy == 0 && (dx != 0 || dy != 0))
			if(dx < 0)
				CUR_IMG = PLR_LEFT;
			else if(dx > 0)
				CUR_IMG = PLR_RIGHT;
			else if(dy < 0)
				CUR_IMG = PLR_BACK;
			else
				CUR_IMG = PLR_FRONT;
				
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
	 */
	public boolean isJumping() {
		return z == 0 ? false : true;
	}
	

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

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImg() {
		// TODO Auto-generated method stub
		
	}
}
