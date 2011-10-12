import java.awt.*;
import java.util.*;

public class PlayerEnt {
	private double x=0, y=0;
	private int ang;
	
	public PlayerEnt(int nx, int ny) {
		this.x = nx;
		this.y = ny;
	}
	
	public PlayerEnt() {}
	
	public void move(double dx, double dy) {
	//	System.out.println("Player moved locally by "+ dx + ", "+dy);
		this.x += dx;
		this.y += dy;
		this.x = PlayerEnt.clamp(this.x, 0, 19);	//Clamp outputs: it won't move out of bounds!
		this.y = PlayerEnt.clamp(this.y, 0, 14);
	}
	
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	
	private static double clamp(double i, int high, int low) {
		return Math.max (high, Math.min (i, low));
	}
	
	public void draw(Graphics g, grid panel) {
		int w = panel.getWidth();
		int h = panel.getHeight();
		Color oldColor = g.getColor();
		g.setColor(Color.red);
		drawPlayer(g, w, h);
		g.setColor(oldColor);
	}
	
	private void drawPlayer(Graphics g, int w, int h) {
		g.drawRect((int)(this.x*(w/20)), (int)(this.y*(h/15)), ((w+1)/20), ((h+1)/15));
		
		//Draw player representation TODO: Replace with sprite
		double mX = (w/20)/2;	//Adjustments to get the center point.
		double mY = (h/15)/2;
		
		g.setColor(Color.black);
		g.fillRect((int)((this.x*(w/20))+5), (int)((this.y*(h/15))-7), (((w)/20)-9), (((h)/15)));
	}
}
