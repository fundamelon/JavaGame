package main;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Player {
	private static double x, y, z, vel_z;
	private static int blockSize, dustCount = 0;
	public static ParticleEmitter[] jump_dust = new ParticleEmitter[100];
	
	public static void init(int nx, int ny) {
		//Since main() is unavailable and it has no constructor, a function to initialize the static player is necessary.
		x = nx * window.getPanelWidth();
		y = ny * window.getPanelHeight();
	}
	
	public static void move(double dx, double dy) {
	//	System.out.println("Player moved locally by "+ dx + ", "+dy);
		x += dx;
		y += dy;
		x = ControlManager.clamp(x, blockSize, Level.getWidth() * 32 - blockSize);	//Clamp outputs: it won't move out of bounds!
		y = ControlManager.clamp(y, blockSize, Level.getHeight() * 32 - blockSize);
		if(z + vel_z <= 0) {
			if(isJumping()) {
				GraphicsManager.emitters[dustCount + 500] = new ParticleEmitter((int)x + 16, (int)y + 25, (int)-vel_z + 5, -vel_z * 0.7, -vel_z * 0.5, 0.9, 0.9, 1, false, true, new Color(139, 69, 19));
				if(-vel_z > 15) ControlManager.shake_time = (int)(-vel_z * 0.6);
				GraphicsManager.emitters[dustCount + 500].setParticleSize(3);
				if(dustCount < 100) dustCount++; else dustCount = 0;
			}
			z = 0;
			vel_z = 0;
		} else {
			z += vel_z * (ControlManager.tick / 50.0);
			vel_z -= (ControlManager.tick / 50.0);
		}
		
	}
	
	public static double getX() {
		return x;
	}
	public static double getY() {
		return y;
	}
	
	public static double getZ() {return z;}
	
	public static void setBlockSize(int s) {
		blockSize = s;
	}
	
	public static void setVelZ(double newVel) {
		vel_z = newVel;
	}
	
	public static boolean isJumping() {
		return z == 0 ? false : true;
	}
	
	public static void draw(Graphics2D g2) {
		int w = window.getPanelWidth();
		int h = window.getPanelHeight();
		Color oldColor = g2.getColor();
		g2.setColor(new Color(0, 0, 0, Math.max(0, 80 - (int)z)));
		g2.fillOval((int)x + 2, (int)y + 22, 30, 10);
		g2.setColor(Color.red);
		drawPlayer(g2, w, h);
		g2.setColor(oldColor);
	}
	
	private static void drawPlayer(Graphics2D g2, int w, int h) {
		double posX = x;
		double posY = y;
	//	g2.drawRect((int)posX, (int)posY, ((w+1)/20), ((h+1)/15));
		
		//Draw player representation TODO: Animation
		double mX = (w/20)/2;	//Adjustments to get the center point.
		double mY = (h/15)/2;
		try {
			Image playerPic = ImageIO.read(new File("lib/img/dummy_idle.png"));
			g2.drawImage(playerPic, (int)((mX-32)+posX), (int)((mY-42)+posY-z), null);
			
			
		} catch (IOException e) {
			System.out.println("Failed to get player icon!");
		}
		
	//	g.setColor(Color.black);
	//	g.fillRect((int)((this.x*(w/20))+5), (int)((this.y*(h/15))-7), (((w)/20)-9), (((h)/15)));
	}
}
