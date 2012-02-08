package main.entity;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;


import main.*;

public class Entity_player extends Entity_mobile {
	
	private final Image TEX_PLR_FRONT;
	private final Image TEX_PLR_BACK;
	private final Image TEX_PLR_LEFT;
	private final Image TEX_PLR_RIGHT;
	
	private final Image[] TEX_PLR_RUN_FRONT;
	private final Image[] TEX_PLR_RUN_SIDE;
	
	private final Animation ANIM_PLR_RUN_FRONT;
	private final Animation ANIM_PLR_RUN_LEFT;
	private final Animation ANIM_PLR_RUN_RIGHT;
	
	private Image cur_img = null;
	private Animation cur_anim = null;
	
	private boolean flipCurImg = false;
	
	private float vel_z;
	private int blockSize, dustCount = 0;
	public float moveSpeed = 0.6f;
	
	private boolean isMoving = false;
	private boolean isColliding = false;
	
	private String action = "idle", direction = "front";
	
	private Rectangle bounds = new Rectangle(0, 0, 32, 32);
	
	public Entity_player() throws SlickException {
	
		TEX_PLR_FRONT = new Image("lib/img/char/girl_front_static.png");
		TEX_PLR_BACK =  new Image("lib/img/char/girl_back_static.png");
		TEX_PLR_LEFT =  new Image("lib/img/char/girl_left_static.png");
		TEX_PLR_RIGHT = new Image("lib/img/char/girl_right_static.png");
		
		TEX_PLR_RUN_FRONT = new Image[] {
			new Image("lib/img/char/girl_front_run02.png"),
			new Image("lib/img/char/girl_front_run01.png"),
			new Image("lib/img/char/girl_front_run03.png")
		};
		
		TEX_PLR_RUN_SIDE = new Image[] {
			new Image("lib/img/char/girl_side_run01.png"),
			new Image("lib/img/char/girl_side_run02.png"),
			new Image("lib/img/char/girl_side_run03.png"),
			new Image("lib/img/char/girl_side_run04.png")
		};
		
		ANIM_PLR_RUN_FRONT = new Animation(TEX_PLR_RUN_FRONT, new int[] {150, 70, 150}, true);
		ANIM_PLR_RUN_LEFT = new Animation(TEX_PLR_RUN_SIDE, new int[] {70, 150, 70, 150}, true);
		ANIM_PLR_RUN_RIGHT = new Animation(TEX_PLR_RUN_SIDE, new int[] {70, 150, 70, 150}, true);
		
		cur_img = TEX_PLR_FRONT;
	
		init();
	}
	
	/**
	 * Re-load the player at a position
	 * @param nx - start pos x
	 * @param ny - start pos y
	 * @param tilewise - True means tiles, false means pixels.
	 */
	@Override
	public void init(int nx, int ny, boolean tilewise) {
		
		x = nx * (tilewise ? 1 : GameBase.getWidth());
		y = ny * (tilewise ? 1 : GameBase.getHeight());
		updateBounds();
		
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
	public void move(float ndx, float ndy) {
		dx += ndx;
		dy += ndy;
	}
	
	public void update() {
		//skip if not moving
		if(dx == 0 && dy == 0) {
			isMoving = false; 
			setAction("idle");
			return;
		} else isMoving = true;
		
		Rectangle attempted_bounds = getBounds();
		attempted_bounds.setX(getBounds().getX() + dx);
		attempted_bounds.setY(getBounds().getY() + dy);
		Rectangle temp_obs = new Rectangle(100, 100, 100, 100);
		if(PhysUtil.collision(attempted_bounds, temp_obs)) {
			isColliding = true;
			float ndx = dx, ndy = dy;
			
			temp_obs.setY(temp_obs.getY() + dy);
			
			if(PhysUtil.collision(getBounds(), temp_obs)) {
				ndx = 0;
			}

			temp_obs.setX(temp_obs.getX() + dx);
			temp_obs.setY(temp_obs.getY() - dy);
			
			if(PhysUtil.collision(getBounds(), temp_obs)) {
				ndy = 0;
			}
			
			temp_obs.setX(temp_obs.getX() - dx);
			
			//If it's on the edge of the bounds between 2/5 player width and the edge, x movement is blocked, and y movement is not zero
			if(		getBounds().getX()+getBounds().getWidth()*0.6 < temp_obs.getX() && 
					getBounds().getX()+getBounds().getWidth() > temp_obs.getX() && dy != 0 && ndx == 0 && dx <= 0)
				ndx -= 0.1 * ControlManager.getDelta();

			if(		getBounds().getX() >= temp_obs.getMaxX()*0.6 && 
					getBounds().getX() <= temp_obs.getMaxX() && dy != 0 && ndx == 0 && dx >= 0)
				ndx += 0.1 * ControlManager.getDelta();

//			if(		getBounds().getX()+getBounds().getWidth()*0.6 < temp_obs.getX() && 
//					getBounds().getX()+getBounds().getWidth() > temp_obs.getX() && dy != 0 && ndx == 0 && dx <= 0)
//				ndx -= 0.1 * ControlManager.getDelta();
//
//			if(		getBounds().getX()+getBounds().getWidth()*0.6 < temp_obs.getX() && 
//					getBounds().getX()+getBounds().getWidth() > temp_obs.getX() && dy != 0 && ndx == 0 && dx <= 0)
//				ndx -= 0.1 * ControlManager.getDelta();
			
			
			dx = ndx;
			dy = ndy;
		} else isColliding = false;
		
	//	System.out.println("Player pos: "+ x + ", "+y);
		x += dx;
		y += dy;
		x = ControlManager.clamp(x, 18, GameBase.getZone().getWidth() * 32 + blockSize + 18);
		y = ControlManager.clamp(y, 18, GameBase.getZone().getHeight() * 32 + blockSize - 18);
		
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
		
		// direction controller
	//	if(dx == 0 && dy == 0) 
		//	setAction("idle");
	//	else 
		if(dx * dy == 0) {
			String newDirection;
			if(dx < 0)
				newDirection = "left";
			else if(dx > 0)
				newDirection = "right";
			else if(dy < 0)
				newDirection = "back";
			else
				newDirection = "front";
			
			setAction("move", newDirection);
		}
				
		updateBounds();
		
		dx = 0;
		dy = 0;
	}
	
	
	private void setAction(String newAction, String newDirection) {
		if(newAction == "move") {
			if(newDirection == "left") {
				if(cur_anim == null || !cur_anim.equals(ANIM_PLR_RUN_LEFT)) {
					cur_anim = ANIM_PLR_RUN_LEFT;
					cur_anim.setPingPong(false);
					cur_anim.start();
				}
				
				flipCurImg = false;
			}
			
			else if(newDirection == "right") {
				if(cur_anim == null || !cur_anim.equals(ANIM_PLR_RUN_RIGHT)) {
					cur_anim = ANIM_PLR_RUN_RIGHT;
					cur_anim.setPingPong(false);
					cur_anim.start();
				}
				
				flipCurImg = true;
			}
			
			else if(newDirection == "back") {
				cur_anim = null;
				cur_img = TEX_PLR_BACK;
				
				flipCurImg = false;
			}
			
			else if(newDirection == "front") {
				if(cur_anim == null || !cur_anim.equals(ANIM_PLR_RUN_FRONT)) {
					cur_anim = ANIM_PLR_RUN_FRONT;
					cur_anim.setPingPong(true);
					cur_anim.start();
				}
				
				flipCurImg = false;
			}
			
			else {
				System.out.println("Invalid direction: \""+newDirection+"\"");
				return;
			}
		}
		
		else if(newAction == "idle") {
			if(cur_anim != null && !cur_anim.isStopped()) cur_anim.stop();
			if(newDirection == "left") {
				cur_anim = null;
				cur_img = TEX_PLR_LEFT;
				
				flipCurImg = false;
			}
			
			else if(newDirection == "right") {
				cur_anim = null;
				cur_img = TEX_PLR_RIGHT;
				
				flipCurImg = false;
			}
			
			else if(newDirection == "back") {
				cur_anim = null;
				cur_img = TEX_PLR_BACK;
				
				flipCurImg = false;
			}
			
			else if(newDirection == "front") {
				cur_anim = null;
				cur_img = TEX_PLR_FRONT;
				
				flipCurImg = false;
			}
			
			else {
				System.out.println("Invalid direction: \""+newDirection+"\"");
				return;
			}
		}
		else
			System.out.println("Invalid action: \""+newAction+"\"");
		
		direction = newDirection;
	}
	
	private void setAction(String newAction) {
		setAction(newAction, direction);
	}

	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	
	private void updateBounds() {
		bounds.setX(x - getBounds().getWidth()/2);
		bounds.setY(y + getBounds().getHeight()/2);
	}
	
	public void resizeBounds(float newWidth, float newHeight) {
		bounds.setWidth(newWidth);
		bounds.setHeight(newHeight);
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
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImg() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Image getImg() {
		img_offset_x = 0;
		img_offset_y = 0;
		
		if(cur_anim != null) {
			cur_anim.update(ControlManager.getDelta());
			cur_img = cur_anim.getCurrentFrame();
			String[] offsetImgs = {
					"lib/img/char/girl_front_run01.png",
					"lib/img/char/girl_side_run01.png",
					"lib/img/char/girl_side_run03.png"
			};
			for(String s : offsetImgs)
				if(cur_img.getResourceReference().equals(s)) {
					img_offset_y -= 3;
					break;
				}
		}
		if(flipCurImg) {
			cur_img = cur_img.getFlippedCopy(true, false);
		}
		return cur_img;
	}
	
	public Image getShadowCasterImg() {
		if(cur_img.equals(TEX_PLR_FRONT))
			return TEX_PLR_BACK;
		
		else if(cur_img.equals(TEX_PLR_BACK))
			return TEX_PLR_FRONT;
		
		else return cur_img;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public float getAng() {
		// TODO Auto-generated method stub
		return 0;
	}
}
