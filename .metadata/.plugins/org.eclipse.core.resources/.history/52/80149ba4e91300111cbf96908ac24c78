package main;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Player {
	private static double x, y;
	private static int blockSize;
	
	public static void init(int nx, int ny) {
		x = nx * window.getPanelWidth();
		y = ny * window.getPanelHeight();
	}
	
	public static void move(double dx, double dy) {
	//	System.out.println("Player moved locally by "+ dx + ", "+dy);
		x += dx;
		y += dy;
		x = ControlManager.clamp(x, blockSize, Level.getWidth() * 32 - blockSize);	//Clamp outputs: it won't move out of bounds!
		y = ControlManager.clamp(y, blockSize, Level.getHeight() * 32 - blockSize);
	}
	
	public static double getX() {
		return x;
	}
	public static double getY() {
		return y;
	}
	
	public static void setBlockSize(int s) {
		blockSize = s;
	}
	
	
	public static void draw(Graphics2D g2) {
		int w = window.getPanelWidth();
		int h = window.getPanelHeight();
		Color oldColor = g2.getColor();
		g2.setColor(Color.red);
		drawPlayer(g2, w, h);
		g2.setColor(oldColor);
	}
	
	private static void drawPlayer(Graphics2D g2, int w, int h) {
		double posX = x;
		double posY = y;
		g2.drawRect((int)posX, (int)posY, ((w+1)/20), ((h+1)/15));
		
		//Draw player representation TODO: Animation
		double mX = (w/20)/2;	//Adjustments to get the center point.
		double mY = (h/15)/2;
		try {
			Image playerPic = ImageIO.read(new File("lib/img/dummy_idle.png"));
			g2.drawImage(playerPic, (int)((mX-32)+posX), (int)((mY-42)+posY), null);
			
			
		} catch (IOException e) {
			System.out.println("Failed to get player icon!");
		}
		
	//	g.setColor(Color.black);
	//	g.fillRect((int)((this.x*(w/20))+5), (int)((this.y*(h/15))-7), (((w)/20)-9), (((h)/15)));
	}
}
