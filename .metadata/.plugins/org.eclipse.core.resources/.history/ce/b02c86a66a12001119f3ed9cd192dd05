package main;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Camera {
	public static Point pos = new Point(window.getPanelWidth()/2, window.getPanelHeight()/2);
	public static double pos_x = pos.x, pos_y = pos.y;
	public static Point origin = pos;
	public static double origin_x = pos.x, origin_y = pos.y;
	public static Point newPos = new Point(0, 0);
	public static boolean moving = false;
	public static int time, tick;
	public static double mul_x, mul_y;
	public static double moveDist, curDist;
	public static boolean lockToPlayer = true;
	
	public static void setX(double nx) {pos_x = nx;}
	public static void setY(double ny) {pos_y = ny;}
	public static void setPoint(Point npos) {pos_x = npos.x; pos_y = npos.y;}
	
	public static double getX() {return pos_x;}
	public static double getY() {return pos_y;}
	public static double getAnchorX() {return pos_x - window.getPanelWidth()/2;}
	public static double getAnchorY() {return pos_y - window.getPanelHeight()/2;}
	public static Point getPoint() {return pos;}
	public static String getString() {return "X: "+pos_x+" Y: "+pos_y;}
	public static void moveToPos(double x, double y, int ntime) {
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
	
	public static void moveToPos(Point npos, int ntime) {
		moveToPos(npos.x, npos.y, ntime);
	}
	
	public static void update() {
		if(true) {
			try {
				if(!lockToPlayer) {
					Camera.setX(Camera.getX() + mul_x);
					Camera.setY(Camera.getY() + mul_y);
					curDist = (newPos.y - pos_y) / (newPos.x - pos_x);
				}
				else {
					Camera.setX(ControlManager.clamp(Player.getX() + 16, window.getPanelWidth()/2, (Level.getWidth()+1) * 32 - window.getPanelWidth()/2));
					Camera.setY(ControlManager.clamp(Player.getY() + 16, window.getPanelHeight()/2, (Level.getHeight()+1) * 32 - window.getPanelHeight()/2));
				}
			} catch(ArithmeticException e){}
		}
	}
	
	public static void followPlayer() {
		lockToPlayer = true;
	}
}
