package main.entity;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;


import main.*;

public class Entity_player extends Entity_mobile implements Entity {
	
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
	private float dist;
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
		
		float ndx = dx, ndy = dy;
		isColliding = false;
		
		Rectangle attempted_bounds = getBounds();
		attempted_bounds.setX(getBounds().getX() + dx);
		attempted_bounds.setY(getBounds().getY() + dy);
		Rectangle[] collisionArray = GameBase.getZone().getCollisionArray();
		
		if(PhysUtil.collisions) {
			for(int i = 0; i < collisionArray.length; i++) {
			//	Rectangle temp_obs = new Rectangle(100, 100, 100, 100);
				Rectangle temp_obs = collisionArray[i];
				
				int cushion; //Keep this value below 8 or it will cause problems
				if(GameBase.getZone().collisionType(i) == 2) {
					cushion = 8;
				} else {
					cushion = 8;
				}
				if(temp_obs != null && Point.distance(getBounds().getCenterX(), getBounds().getCenterY(), temp_obs.getCenterX(), temp_obs.getCenterY()) < bounds.getBoundingCircleRadius()*2-cushion) {
					if(PhysUtil.collision(attempted_bounds, temp_obs)) {
						isColliding = true;
						
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
						
						//If it's within 24 pixels of corner, x movement is blocked,
						//	and y movement is not zero, then nudge it over
						//Left edge
						if(		!GameBase.getZone().blocked(i-1) &&
								getBounds().getX() + getBounds().getWidth()-24 < temp_obs.getX() && 
								getBounds().getX() + getBounds().getWidth()    > temp_obs.getX() && 
								dy != 0 && ndx == 0 && dx <= 0) {
							ndx -= 0.05 * ControlManager.getDelta();
						}
						//Right edge
						if(		!GameBase.getZone().blocked(i+1) &&
								getBounds().getX() > temp_obs.getX() + temp_obs.getWidth()-24 && 
								getBounds().getX() <= temp_obs.getMaxX() + temp_obs.getWidth() && 
								dy != 0 && ndx == 0 && dx >= 0)	{
							ndx += 0.05 * ControlManager.getDelta();
						}
						
						//Top edge
						if(		!GameBase.getZone().blocked(i - GameBase.getZone().getWidth()) &&
								getBounds().getY() + getBounds().getHeight()-24 < temp_obs.getY() && 
								getBounds().getY() + getBounds().getHeight() >= temp_obs.getY()&& 
								dx != 0 && ndy == 0 && dy >= 0)	{
							ndy -= 0.05 * ControlManager.getDelta();
						}
						//Bottom edge
						if(		!GameBase.getZone().blocked(i + GameBase.getZone().getWidth()) &&
								getBounds().getY() > temp_obs.getY() + temp_obs.getHeight()-24 && 
								getBounds().getY() <= temp_obs.getMaxY() + temp_obs.getHeight() && 
								dx != 0 && ndy == 0 && dy >= 0)	{
							ndy += 0.05 * ControlManager.getDelta();
						}
					}
				}
			}
		}
		
		dist = Math.abs(ndx) + Math.abs(ndy);
		
		x += ndx;
		y += ndy;
		x = ControlManager.clamp(x, 18, GameBase.getZone().getWidth() * 32 + blockSize + 18);
		y = ControlManager.clamp(y, 18, GameBase.getZone().getHeight() * 32 + blockSize - 18);
		
		//TODO: Jump function goes here
		
		// direction controller
		if(dist == 0) 
			setAction("idle");
		else if(dx * dy == 0) {
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
