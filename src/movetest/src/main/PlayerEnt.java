package main;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class PlayerEnt {
	private double x=0, y=0;
	private grid panel;
	private int blockSize;
	
	public PlayerEnt(int nx, int ny, grid oldGrid) {
		panel = oldGrid;
		this.x = nx * panel.getWidth();
		this.y = ny * panel.getHeight();
	}
	
	public PlayerEnt() {}
	
	public void move(double dx, double dy) {
	//	System.out.println("Player moved locally by "+ dx + ", "+dy);
		this.x += dx;
		this.y += dy;
		this.x = ControlManager.clamp(this.x, blockSize, 640 - blockSize * 2);	//Clamp outputs: it won't move out of bounds!
		this.y = ControlManager.clamp(this.y, blockSize, 480 - blockSize * 2);
	}
	
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	
	public void setBlockSize(int s) {
		blockSize = s;
	}
	
	
	public void draw(Graphics2D g2) {
		int w = panel.getWidth();
		int h = panel.getHeight();
		Color oldColor = g2.getColor();
		g2.setColor(Color.red);
		drawPlayer(g2, w, h);
		g2.setColor(oldColor);
	}
	
	private void drawPlayer(Graphics2D g2, int w, int h) {
		double posX = this.x;
		double posY = this.y;
		g2.drawRect((int)posX, (int)posY, ((w+1)/20), ((h+1)/15));
		
		//Draw player representation TODO: Animation
		double mX = (w/20)/2;	//Adjustments to get the center point.
		double mY = (h/15)/2;
		try {
			Image playerPic = ImageIO.read(new File("bin/img/dummy_idle.png"));
			g2.drawImage(playerPic, (int)((mX-32)+posX), (int)((mY-42)+posY), null);
			
			
		} catch (IOException e) {
			System.out.println("Failed to get player icon!");
		}
		
	//	g.setColor(Color.black);
	//	g.fillRect((int)((this.x*(w/20))+5), (int)((this.y*(h/15))-7), (((w)/20)-9), (((h)/15)));
	}
}
