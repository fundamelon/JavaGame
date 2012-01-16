package main;


import javax.imageio.*;
import java.io.*;
import java.util.*;

import tiled.core.Map;
import tiled.core.TileLayer;

import main.entity.Entity;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

public class GraphicsManager {
	
	static Image[] texture;
	static GameBase panel;
	private static Random rand = new Random();
	public static ParticleEmitter emitters[] = new ParticleEmitter[1000];
	public static ParticleEmitter emitter_dump[] = new ParticleEmitter[emitters.length];
	private static int sparkct = 0, particle_count = 0;
	private static long ticks = 0;
	private static Image camIcon, dbImage;
	private static int fps_lag;
	public static boolean first_run = true;
	
	private static Color fadeCol = new Color(0, 0, 0);
	private static Color overlayCol = new Color(fadeCol.getRed(), fadeCol.getGreen(), fadeCol.getBlue(), 0);
	//Vars with preceding underscore are to be values for render options.  :O
	private static boolean _dither = false, fadeMode = true, helperText = false, shake = false;
	
	private static int lag;
	
	private static Image bg_blurred;
	
	/**
	 * Initializes the object and loads images.
	 * @param transPanel - the instance of GameBase to be used
	 */
	public static void init() {
	//	mapRenderer = new OrthogonalRenderer(GameBase.getZone().getData());
		
		//Empty for now.  We'll see about it later.
		texture = new Image[10];
		camIcon = texture[0];
		
		
	}
	
	public static void setLag(int n) {
		lag = n;
	}
	
	
	
	/**
	 * Main function that redraws all graphics on a GameBase.
	 * @param g - Graphics context
	 * @param panel - the GameBase to draw on
	 */
	public static void renderMain(Graphics g, int delta) {
		Color oldCol;
		

		renderMap(g);
		//Set the camera to follow the player with the players coordinates, then update the camera.
		Camera.followPlayer();
		Camera.moveToPos(EntityManager.getPlayer().getX() + 16, EntityManager.getPlayer().getY() + 16, 1); 
		Camera.update();

		g.translate(-Camera.getAnchorX(), -Camera.getAnchorY());
		
//		
//		//DEBUG: Next draw a square representing the tile the player is currently on.
		oldCol = g.getColor();
		g.setColor(new Color(0, 0, 255, 50));
	//	g2.fillRect((int)Math.round(Player.getX() / 32) * 32, (int)Math.round(Player.getY() / 32) * 32, 32, 32);
		g.setColor(oldCol);
//		
//		
//		
//		//Effects are drawn last along with the camera center point icon.
//		renderEffects(g);
		renderEntities(g);
//		renderFrontEffects(g);
//	//	g2.drawImage(camIcon, (int)Camera.getX() - 7, (int)Camera.getY() - 3, null);
//
//		//This will update the fade amount then apply a dark box to the entire viewport based on the fade value.
//		oldCol = g.getColor();
//		fade();
//		g.setColor(overlayCol);
//		g.fillRect((int)toLocalX(0), (int)toLocalY(0), (int)toLocalX(GameBase.getWidth()), (int)toLocalY(GameBase.getHeight()));
//		g.setColor(oldCol);
//		
//		//Draw helper text menu if it's called upon.
//		if(helperText) {
//			//TODO: Find a better way to do the line breaks.
//			g.drawString("TEST CONTROLS", 50, 50);
//			g.drawString("R: Randomize texture seed", 50, 65);
//			g.drawString("0: Fade out", 50, 80);
//			g.drawString("9: Fade in", 50, 95);
//			g.drawString("8: Shake camera", 50, 110);
//		}
//		first_run = false;
		g.translate(Camera.getAnchorX(), Camera.getAnchorY());
	}
	
	public static void renderMainMenu(Graphics g) {
//		Color oldColor = g.getColor();
//		if(first_run) {
//			boolean done = false;
//			while(!done)
//				try {
//					ImageOp op = null;				
//					float[] matrix = {
//					        0.111f, 0.111f, 0.111f, 
//					        0.111f, 0.111f, 0.111f, 
//					        0.111f, 0.111f, 0.111f, 
//					    };
//					op = new ConvolveOp( new Kernel(3, 3, matrix), ConvolveOp.EDGE_NO_OP, null );
//					bg_blurred = panel.override.createScreenCapture(new Rectangle(Window.getPanel().getLocationOnScreen().x, Window.getPanel().getLocationOnScreen().y, Window.getPanelWidth(), Window.getPanelHeight()));
//					Image trans = null;
//					bg_blurred = op.filter(bg_blurred, trans);
//					g2.drawImage(bg_blurred, op, 0, 0);
//				//	bg_blurred = new Robot().createScreenCapture(new Rectangle(Window.getPanel().getLocationOnScreen().x, Window.getPanel().getLocationOnScreen().y, Window.getPanelWidth(), Window.getPanelHeight()));
//					done = true;
//				} catch (Exception e) {e.printStackTrace(); done=true;}
//		}
//		first_run = false;
//		g.drawImage(bg_blurred, 0, 0, null);
//		g.setColor(new Color(0, 0, 0, 15));
//		.fillRect(0, 0, Window.getPanelWidth(), Window.getPanelHeight());
//		g2.setColor(new Color(0, 0, 0, 120));
//		g2.fillRect(panel.getWidth() / 2 - panel.getWidth() / 6, 200, panel.getWidth() / 3, 100);
//		g2.setColor(Color.white);
//		g2.drawString("le menue", panel.getWidth() / 2 - 50, 300);
//		g2.setColor(oldColor);
	}
	
	/**
	 * Create a burst of particles at this location!
	 */
	public static void createParticleShower() {
		int x = (int)ControlManager.getMouseX();
		int y = (int)ControlManager.getMouseY();
		// It goes X, Y, Amount, speed X/Y, drag X/Y (0.8 to 0.99 is good), fade rate, repeater (broked), float boolean, color (optional)if(sparkct == emitters.length-1) 
		if(sparkct == emitters.length-1) 
			sparkct = 0;
		else
			sparkct++;
		emitters[sparkct] = new ParticleEmitter((int)toLocalX(x), (int)toLocalY(y), 100, 100, 100, 0.05f, 0.05f, 2, false, false, Color.yellow);
		emitters[sparkct].setParticleSize(0);
		emitters[sparkct].setTrails(true);
	//	emitters[sparkct].toggleModifier(0);
	//	emitters[sparkct].toggleModifier(3);
		if(sparkct == emitters.length-1) 
			sparkct = 0;
		else
			sparkct++;
		emitters[sparkct] = new ParticleEmitter((int)toLocalX(x), (int)toLocalY(y), 0, 5, 5, 0.9f, 0.9f, 0.1f, false, false, Color.gray);
	}
	
	
	public static void renderEntities(Graphics g) {
		for(int i = 0; i < EntityManager.getTableLength(); i++) {
			Entity curEnt = EntityManager.getEntByIndex(i);
			g.drawImage(curEnt.getImg(), curEnt.getX(), curEnt.getY());
		}
	}
	
	
	public static void renderMap(Graphics g) {
		GameBase.getZone().render(-Camera.getAnchorX(), -Camera.getAnchorY());
	}
	
	
	
	/**
	 * Draw the tile textures used on the background. OBSOLETE + REMOVED
	 * @param g2 - Graphics2D context
	 */
	public static void renderBackground(Graphics g) {}
	
	
	/**
	 * Simple method to display strings.
	 * @param g2 - Graphics2D context
	 * @param text - String in question
	 * @param x - Pos in pixels
	 * @param y - Pos in pizels
	 * @param local - True means it's local to the camera, false means it's static on the map surface.
	 */
	public static void print(Graphics g, String text, float x, float y, boolean local) {
		if(local) 
			g.drawString(text, (int)toLocalX(x), (int)toLocalY(y));
		else
			g.drawString(text, (int)x, (int)y);
	}
	
	/**
	 * Update particles; if particles are in front throw them on different list
	 * @param g2 - Graphics2D context
	 */
	public static void renderEffects(Graphics g) {
		for(int i = 0; i < emitters.length; i++) {
			if(emitters[i] != null) 
				if(emitters[i].getY() < EntityManager.getPlayer().getY() + EntityManager.getPlayer().getZ() + 32)
					emitters[i].render(g, ticks);
				else
					emitter_dump[i] = emitters[i];
		}
	}
	
	/**
	 * Update particles on the second render list; rendered after player
	 * @param g2 - Graphics2D context
	 */
	public static void renderFrontEffects(Graphics g) {
		for(int i = 0; i < emitter_dump.length; i++) {
			if(emitter_dump[i] != null) 
				emitter_dump[i].render(g, ticks);
			emitter_dump[i] = null;
		}
	}
	
	/**
	 * Simple grid drawing algorithm
	 * @param g - Graphics context
	 */
	public static void renderGrid(Graphics g) {
		
		int k=0;
		Color oldColor = g.getColor();
		g.setColor(new Color(200, 200, 200));
		int htOfRow = GameBase.getHeight() / 15;
		for (k = 0; k <= 15; k++)
			g.drawLine(0, k * htOfRow , GameBase.getWidth(), k * htOfRow );
		
		int wdOfRow = GameBase.getWidth() / 20;
		for (k = 0; k <= 20; k++) 
			g.drawLine(k*wdOfRow , 0, k*wdOfRow , GameBase.getHeight());
		
		g.setColor(oldColor);
	}
	
	/**
	 * Mutator to update boolean fade variable.
	 * @param mode - true/false to toggle if its fading in or out respectively
	 */
	public static void setFade(boolean mode) {
		fadeMode = mode;
	}
	
	/**
	 * Update overlay alpha based on time and boolean fade variable.
	 */
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
	
	/**
	 * Ask each emitter to total its particles, then total that to get number of all particles.
	 * @return Total particle count
	 */
	public static int getParticleCount() {
		particle_count = 0;
		for(int i = 0; i < emitters.length; i++) {
			if(emitters[i] != null)
				particle_count += emitters[i].getParticleCount();
		}
		return particle_count;
	}
	
	/**
	 * Updated every tick to show helper text (will reset after each tick)
	 * @param mode - true/false to show/hide helper text respectively
	 */
	public static void showHelperText(boolean mode) {
		helperText = mode;
	}
	
	
	
	/**
	 * Set camera shaking to true (is reset to false after each tick)
	 */
	public static void shake() {
		shake = true;
	}
	
	
	
	//Important functions: they add the camera's x and y to the given GLOBAL coordinates
	//resulting in LOCAL coordinates relative to the screen itself.
	
	/**
	 * Translate a global x value to a local x value
	 * @param ox - original x value
	 * @return localized x value
	 */
	public static float toLocalX(float ox) {
		return ox + Camera.getAnchorX();
	}
	/**
	 * Translate a global y value to a local y value
	 * @param oy - original y value
	 * @return localized y value
	 */
	public static float toLocalY(float oy) {
		return oy + Camera.getAnchorY();
	}
}
