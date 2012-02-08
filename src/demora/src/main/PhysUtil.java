package main;

import org.newdawn.slick.geom.Rectangle;

public class PhysUtil {
	
	public static boolean collision(Rectangle a, Rectangle b) {
		return !(	(a.getX() + a.getWidth() < b.getX()) ||
				(a.getX() > b.getX() + b.getWidth()) ||
				(a.getY() + a.getHeight() < b.getY()) ||
				(a.getY() > b.getY() + b.getHeight()));
	}
	
	public static boolean collidedFromLeft(Rectangle a, Rectangle b) {
		System.out.println("Left");
		return a.getX() + a.getWidth() > b.getX() && collision(a, b);
	}

	public static boolean collidedFromRight(Rectangle a, Rectangle b) {
		System.out.println("Right");
		return a.getX() < b.getX() + b.getHeight() && collision(a, b);
	}

	public static boolean collidedFromTop(Rectangle a, Rectangle b) {
		System.out.println("Top");
		return a.getY() + a.getHeight() > b.getY() && collision(a, b);
	}

	public static boolean collidedFromBottom(Rectangle a, Rectangle b) {
		System.out.println("Bottom");
		return a.getY() < b.getY() + b.getHeight() && collision(a, b);
	}
}
