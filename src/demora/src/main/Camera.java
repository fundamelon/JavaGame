package main;

import java.awt.Point;
import java.awt.geom.Point2D;

import main.entity.EntityManager;

public class Camera {
	public static Point pos = new Point(GameBase.getWidth()/2, GameBase.getHeight()/2);
	public static float pos_x = pos.x, pos_y = pos.y;
	public static Point origin = pos;
	public static float origin_x = pos.x, origin_y = pos.y;
	public static Point newPos = new Point(0, 0);
	public static boolean moving = false;
	public static int time, tick;
	public static float mul_x, mul_y;
	public static float moveDist, curDist;
	public static boolean lockToPlayer = true;
	
	public static void setX(float nx) {pos_x = nx;}
	public static void setY(float ny) {pos_y = ny;}
	public static void setPoint(Point npos) {pos_x = npos.x; pos_y = npos.y;}
	
	public static float getX() {return pos_x;}
	public static float getY() {return pos_y;}
	public static float getAnchorX() {return (pos_x - GameBase.getWidth()/2);}
	public static float getAnchorY() {return (pos_y - GameBase.getHeight()/2);}
	public static Point getPoint() {return pos;}
	public static String getString() {return "X: "+pos_x+" Y: "+pos_y;}
	
	/**
	 * Move the camera to certain position over specified interval
	 * @param x - new pos x
	 * @param y - new pos y
	 * @param ntime - movement interval
	 */
	public static void moveToPos(float x, float y, int ntime) {
		moving = true;
		origin = pos;
		origin_x = origin.x;
		origin_y = origin.y;
		newPos = new Point((int)x, (int)y);
		time = ntime;
	//	mul_x = pos_x < newPos.x ? 1 : -1;
		mul_x = (newPos.x - pos_x) / time;

	//	mul_y = pos_y < newPos.y ? 1 : -1;
		mul_y = (newPos.y - pos_y) / time;
		
		moveDist = (newPos.y - pos_y) / (newPos.x - pos_x);
		
	}
	
	/**
	 * Move the camera to certain position over specified interval
	 * @param npos - new Point
	 * @param ntime - movement interval
	 */
	public static void moveToPos(Point npos, int ntime) {
		moveToPos(npos.x, npos.y, ntime);
	}
	
	
	/**
	 * Move the camera if it's not at the desired coordinates yet
	 */
	public static void update() {
		if(true) {
			try {
				if(!lockToPlayer) {
					Camera.setX(Camera.getX() + mul_x);
					Camera.setY(Camera.getY() + mul_y);
					curDist = (newPos.y - pos_y) / (newPos.x - pos_x);
				}
				else {
					Camera.setX(ControlManager.clamp(EntityManager.getPlayer().getX() + 16, GameBase.getWidth()/2, (GameBase.getZone().getWidth()+1) * 32 - GameBase.getWidth()/2));
					Camera.setY(ControlManager.clamp(EntityManager.getPlayer().getY() + 16, GameBase.getHeight()/2, (GameBase.getZone().getHeight()+1) * 32 - GameBase.getHeight()/2));
				}
			} catch(ArithmeticException e){}
		}
	}
	
	public static void followPlayer() {
		lockToPlayer = true;
	}
}
