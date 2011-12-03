package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;
import java.util.*;


public class GraphicsManager {
	
	static BufferedImage[] texture = new BufferedImage[10];
	static grid panel;
	private static Random seed_rand = new Random(), rand = new Random();
	private static int textureSeed = seed_rand.nextInt(64);
	private static ControlManager gameControls;
	public static ParticleEmitter emitters[] = new ParticleEmitter[1000];
	public static ParticleEmitter emitter_dump[] = new ParticleEmitter[emitters.length];
	private static int sparkct = 0, particle_count = 0;
	private static long ticks = 0;
	private static Graphics temp_g;
	private static Image camIcon, dbImage;
	private static int fps_lag;
	
	private static Color fadeCol = new Color(0, 0, 0);
	private static Color overlayCol = new Color(fadeCol.getRed(), fadeCol.getGreen(), fadeCol.getBlue(), 0);
	//Vars with preceding underscore are to be values for render options.  :O
	private static boolean _dither = false, fadeMode = true, helperText = false, shake = false;
	
	public static void init(grid transPanel) {
		panel = transPanel;
		
		//Try and load the textures.
		try {
			texture[1] = ImageIO.read(new File("lib/img/grass1.png"));
			texture[2] = ImageIO.read(new File("lib/img/grass2.png"));
			texture[3] = ImageIO.read(new File("lib/img/grass3.png"));
			texture[4] = ImageIO.read(new File("lib/img/stone1.png"));
			texture[5] = ImageIO.read(new File("lib/img/stone2.png"));
			texture[6] = ImageIO.read(new File("lib/img/stone3.png"));
			texture[7] = ImageIO.read(new File("lib/img/grass_end1.png"));
			texture[8] = ImageIO.read(new File("lib/img/grass_end2.png"));
			texture[9] = ImageIO.read(new File("lib/img/grass_end3.png"));
			
			camIcon = ImageIO.read(new File("lib/img/camicon.png"));
		} catch(IOException e) {
			System.out.println("ERROR: Failed to load tile images");
		}
	}
	
	public static void draw(Graphics g, grid panel) {
		Graphics2D g2 = (Graphics2D) g;
		Color oldCol;
		
		//Various rendering options to speed it up.
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                       RenderingHints.VALUE_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                       RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                       RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                       RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                       RenderingHints.VALUE_COLOR_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING,
                       RenderingHints.VALUE_DITHER_DISABLE);
                       
		//Set the camera to follow the player with the players coordinates, then update the camera.
		Camera.followPlayer();
		Camera.moveToPos(Player.getX() + 16, Player.getY() + 16, 1); 
		Camera.update();
		
		//Random offsets "shake" the camera.
		if(shake) 
			g2.translate(-Camera.getAnchorX() + (rand.nextInt(10) - 5), -Camera.getAnchorY() + (rand.nextInt(10) - 5));
		else
			g2.translate(-Camera.getAnchorX(), -Camera.getAnchorY());
		shake = false;
		
		//Draw background and grid first.
		drawGrid(g2);
		drawBackground(g2);
		
		//Next draw a square representing the tile the player is currently on.
		oldCol = g2.getColor();
		g2.setColor(new Color(0, 0, 255, 50));
	//	g2.fillRect((int)Math.round(Player.getX() / 32) * 32, (int)Math.round(Player.getY() / 32) * 32, 32, 32);
		g2.setColor(oldCol);
		
		
		//This will update the fade amount then apply a dark box to the entire viewport based on the fade value.
		oldCol = g2.getColor();
		fade();
		g2.setColor(overlayCol);
		g2.fillRect((int)toLocalX(0), (int)toLocalY(0), (int)toLocalX(window.getPanelWidth()), (int)toLocalY(window.getPanelHeight()));
		g2.setColor(oldCol);
		
		//Effects are drawn last along with the camera center point icon.
		drawEffects(g2);
		Player.draw(g2);
		drawFrontEffects(g2);
		g2.drawImage(camIcon, (int)Camera.getX() - 7, (int)Camera.getY() - 3, null);
		
		//Draw helper text menu if it's called upon.
		if(helperText) {
			//TODO: Find a better way to do the line breaks.
			g2.drawString("TEST CONTROLS", 50, 50);
			g2.drawString("R: Randomize texture seed", 50, 65);
			g2.drawString("0: Fade out", 50, 80);
			g2.drawString("9: Fade in", 50, 95);
			g2.drawString("8: Shake camera", 50, 110);
		}
	}
	
	//Create a burst of sparks at this location!
	public static void createParticleShower() {
		int x = (int)ControlManager.getMouseX();
		int y = (int)ControlManager.getMouseY();
		// It goes X, Y, Amount, speed X/Y, drag X/Y (0.8 to 0.99 is good), fade rate, repeater (broked), float boolean, color (optional)if(sparkct == emitters.length-1) 
		if(sparkct == emitters.length-1) 
			sparkct = 0;
		else
			sparkct++;
		emitters[sparkct] = new ParticleEmitter((int)toLocalX(x), (int)toLocalY(y), 200, 20, 40, 0.8, 0.8, 1, false, true, Color.blue);
		emitters[sparkct].setParticleSize(4);
		emitters[sparkct].setTrails(false);
		emitters[sparkct].toggleModifier(2);
		emitters[sparkct].toggleModifier(3);
		if(sparkct == emitters.length-1) 
			sparkct = 0;
		else
			sparkct++;
		emitters[sparkct] = new ParticleEmitter((int)toLocalX(x), (int)toLocalY(y), 0, 5, 5, 0.9, 0.9, 0.1, false, false, Color.gray);
	
	}
	
	//Update the fps lag.
	public static void clk(int n) {
		fps_lag = n;
	}
	
	//unused
	public static void drawPlayer(Graphics g) {
		
	}
	
	//Draw the tile textures used on the background.
	public static void drawBackground(Graphics2D g2) {		
		//Read and load all the tile files into the array.
		//Set a random seed so random is the same every time it renders.
		seed_rand.setSeed(textureSeed);
		int curPosX = 0, curPosY = 0;
		
		
		//Draw tiles.  TODO: Clean this shit up.  Algorithm is inverted :\
		for(int k = 0; k <= Level.getWidth(); k++) {
			for(int c = 0; c <= Level.getHeight(); c++) {
				curPosX = k * 32; 	//Current screen x position in pixels.  Increments by 32.
				curPosY = c * 32;	//Current screen y pos in pixels.  Increments of 32.
				
				if((k == 0)  || (k == Level.getWidth()) || (c == 0) || (c == Level.getHeight())) { 
					
					
					//If it's on the edge first draw stone underlay
					g2.drawImage(texture[seed_rand.nextInt(3)+4], curPosX, curPosY, curPosX + 32, curPosY+32, 0, 0, 32, 32, null);
					
					//Grass edge pictures
					//Left column
					if(k==Level.getWidth()) {
						//Top left corner
						if(c == 0) {
							g2.drawImage(texture[9], curPosX, curPosY, null);
						}
						//In between
						else if(c != Level.getHeight()) {
							g2.drawImage(texture[seed_rand.nextInt(2)+7], curPosX, curPosY, null);
						}
						//Bottom left corner
						else {
							g2.drawImage(texture[9], curPosX, curPosY + 32, curPosX + 32, curPosY, 0, 0, 32, 32, null);
						}
					}
					
					//Right column
					if(k==0) {
						//Top right corner
						if(c==0) {
							g2.drawImage(texture[9], curPosX + 32, curPosY, curPosX, curPosY+32, 0, 0, 32, 32, null);
						}
						//In between
						else if(c != Level.getHeight()) {
							g2.drawImage(texture[seed_rand.nextInt(2)+7], curPosX + 32, curPosY + 32, curPosX, curPosY, 0, 0, 32, 32, null);
						}
						//Top left corner
						else {
							g2.drawImage(texture[9], curPosX + 32, curPosY + 32, curPosX, curPosY, 0, 0, 32, 32, null);
						}
					}
					
					//If it's not far left or right column
					if(k != 0 && k != Level.getWidth()) {
						//Top row
						
						//Funky way of doing it.  The entire window is translated then rotated, the image drawn at origin, then the window position reset.
						g2.translate(curPosX + 16, curPosY + 16);
						if(c==0) {
							g2.rotate(-0.5 * Math.PI);
							g2.drawImage(texture[seed_rand.nextInt(2)+7], -16, -16, null);
							g2.rotate(0.5 * Math.PI);
						}
						//Bottom row
						if(c==Level.getHeight()) {
							g2.rotate(0.5 * Math.PI);
							g2.drawImage(texture[seed_rand.nextInt(2)+7], -16, -16, null);
							g2.rotate(-0.5 * Math.PI);
						}
						g2.translate(-curPosX - 16, -curPosY - 16);
					}
				}
				else {
					//Just draw a grass tile if it's not a boundary
					g2.translate(curPosX + 16, curPosY + 16);
					double rot = ((seed_rand.nextInt(8) / 2.0) - 2);
					g2.rotate(rot * Math.PI);
					g2.drawImage(texture[seed_rand.nextInt(3)+1], -16, -16, null);
					g2.rotate(rot * -Math.PI);
					g2.translate(-curPosX - 16, -curPosY - 16);
				}
			}
			if(ControlManager.getKeyStatus("R".charAt(0))) {
				textureSeed++;
			}
		}
		
	}
	
	//Display strings.
	public static void print(Graphics2D g2, String text, double x, double y, boolean local) {
		if(local) 
			g2.drawString(text, (int)toLocalX(x), (int)toLocalY(y));
		else
			g2.drawString(text, (int)x, (int)y);
	}
	
	//Just update every spark if it's not yet dead.
	public static void drawEffects(Graphics2D g2) {
		for(int i = 0; i < emitters.length; i++) {
			if(emitters[i] != null) 
				if(emitters[i].getY() < Player.getY() + Player.getZ() + 32)
					emitters[i].draw(g2, ticks);
				else
					emitter_dump[i] = emitters[i];
		}
	}
	
	public static void drawFrontEffects(Graphics2D g2) {
		for(int i = 0; i < emitter_dump.length; i++) {
			if(emitter_dump[i] != null) 
				emitter_dump[i].draw(g2, ticks);
			emitter_dump[i] = null;
		}
	}
	
	public static void drawGrid(Graphics g) {		//Simple grid drawing algorithm.
		
		int k=0;
		Color oldColor = g.getColor();
		g.setColor(new Color(200, 200, 200));
		int htOfRow = panel.getHeight() / 15;
		for (k = 0; k <= 15; k++)
			g.drawLine(0, k * htOfRow , panel.getWidth(), k * htOfRow );
		
		int wdOfRow = panel.getWidth() / 20;
		for (k = 0; k <= 20; k++) 
			g.drawLine(k*wdOfRow , 0, k*wdOfRow , panel.getHeight());
		
		g.setColor(oldColor);
	}
	
	//Mutator to update boolean fade variable.
	public static void setFade(boolean mode) {
		fadeMode = mode;
	}
	
	//Update overlay alpha based on time and boolean fade variable.
	public static void fade() {
		if(fadeMode) {
			if(overlayCol.getAlpha() - fps_lag/10 > 0)
				overlayCol = new Color(overlayCol.getRed(), overlayCol.getGreen(), overlayCol.getBlue(), overlayCol.getAlpha() - fps_lag / 10);
			else
				overlayCol = new Color(overlayCol.getRed(), overlayCol.getGreen(), overlayCol.getBlue(), 0);
				
		}
		else {
			if(overlayCol.getAlpha() + fps_lag/10 < 255)
				overlayCol = new Color(overlayCol.getRed(), overlayCol.getGreen(), overlayCol.getBlue(), overlayCol.getAlpha() + fps_lag / 10);
			else
				overlayCol = new Color(overlayCol.getRed(), overlayCol.getGreen(), overlayCol.getBlue(), 255);
		}
	}
	
	//Ask each emitter to total its particles, then total that to get number of all particles.
	public static int getParticleCount() {
		particle_count = 0;
		for(int i = 0; i < emitters.length; i++) {
			if(emitters[i] != null)
				particle_count += emitters[i].getParticleCount();
		}
		return particle_count;
	}
	
	public static void showHelperText(boolean mode) {
		helperText = mode;
	}
	
	//Make shake true just for this one execution (it's set to false every execution)
	public static void shake() {
		shake = true;
	}
	
	//Important functions: they add the camera's x and y to the given GLOBAL coordinates
	//resulting in LOCAL coordinates relative to the screen itself.
	public static double toLocalX(double ox) {
		return ox + Camera.getAnchorX();
	}
	public static double toLocalY(double oy) {
		return oy + Camera.getAnchorY();
	}
}
