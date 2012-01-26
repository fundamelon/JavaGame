package main;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import main.depreciated.ParticleEmitter;
import main.entity.Entity;
import main.entity.EntityManager;
import main.particles.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.particles.ParticleSystem;

public class GraphicsManager {
	
	static Image[] texture;
	static GameBase panel;
	public static ParticleEmitter emitters[] = new ParticleEmitter[1000];
	public static ParticleEmitter emitter_dump[] = new ParticleEmitter[emitters.length];
	public static org.newdawn.slick.particles.ParticleEmitter fire_effect;
	public static org.newdawn.slick.particles.ParticleSystem particle_system;
	private static int sparkct = 0, particle_count = 0;
	private static int fps_lag;
	public static boolean first_run = true;
	
	private static Color fadeCol = new Color(0, 0, 0);
	private static Color overlayCol = new Color(fadeCol.getRed(), fadeCol.getGreen(), fadeCol.getBlue(), 0);
	//Vars with preceding underscore are to be values for render options.  :O
	private static boolean fadeMode = true, helperText = false, shake = false;
	
	/**
	 * Initializes the object and loads images.
	 * @param transPanel - the instance of GameBase to be used
	 */
	public static void init() {
	//	mapRenderer = new OrthogonalRenderer(GameBase.getZone().getData());
		
		//Empty for now.  We'll see about it later.
		texture = new Image[10];
		
		try {
			particle_system = new ParticleSystem(new Image("lib/img/particle/flamelrg_02.tga"));
		//	particle_system = new ParticleSystem(new Image("src/org/newdawn/slick/data/particle.tga"));
		//	particle_system = new ParticleSystem(new Image("lib/img/Angry_Birds_promo_art.png"));
		} catch (SlickException e) {
			e.printStackTrace();
		}

		particle_system.setBlendingMode(1);
		
	}
	
	
	
	/**
	 * Main function that redraws all graphics on a GameBase.
	 * @param g - Graphics context
	 * @param panel - the GameBase to draw on
	 */
	public static void renderMain(Graphics g, int delta) {
		Color oldCol;
		oldCol = g.getColor();
		g.setColor(Color.white);
		
		int width = GameBase.getWidth();
		int height = GameBase.getHeight();

		Camera.followPlayer();
		
		renderMap(g);
		//Set the camera to follow the player with the players coordinates, then update the camera.

		Camera.update();
		Camera.moveToPos(EntityManager.getPlayer().getX(), EntityManager.getPlayer().getY(), 1); 
		
		g.translate(-Camera.getAnchorX(), -Camera.getAnchorY());
		
		
		renderEffects(g);
		renderEntities(g);
		
		particle_system.render();
		
		g.translate(Camera.getAnchorX(), Camera.getAnchorY());
		
		
		if(ControlManager.isMouseButtonDown(0)) {
			g.setColor(Color.white);
			float[] xArr = ControlManager.getMouseTraceXArr();
			float[] yArr = ControlManager.getMouseTraceYArr();
			for(int i = 0; i < ControlManager.getMouseTraceLength()-1; i++) {
				if(xArr[i+1] != 0)
					g.drawLine(xArr[i], GameBase.getHeight() - yArr[i], xArr[i+1], GameBase.getHeight() - yArr[i+1]);
			}
			g.setColor(Color.red);
			
			if(particle_system.getEmitterCount() == 0)
				for(int i = 0; i < 5; i++) 
					particle_system.addEmitter(new Emitter_FireLarge());
			
		}
		else if(particle_system.getEmitterCount() != 0)
				particle_system.removeAllEmitters();
		
		g.drawRect(ControlManager.getMouseX() - 8,  ControlManager.getMouseY() - 8, 16, 16);
		
		g.setColor(oldCol);
		
		for(int i = 0; i < particle_system.getEmitterCount(); i++)
			particle_system.getEmitter(i).setPos(toLocalX(ControlManager.getMouseX()), toLocalY(ControlManager.getMouseY()));
		
		particle_system.update(delta);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F2) && !Keyboard.isRepeatEvent()) {
			
			ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
			GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );
			
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			 
			for(int x = 0; x < width; x++)
				for(int y = 0; y < height; y++)
				{
					int i = (x + (width * y)) * 4;
					int r = buffer.get(i) & 0xFF;
					int g1 = buffer.get(i + 1) & 0xFF;
					int b = buffer.get(i + 2) & 0xFF;
					image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g1 << 8) | b);
				}
			
			System.out.println("Saved screenshot");
		}
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
		emitters[sparkct] = new ParticleEmitter(100, (int)toLocalX(x), (int)toLocalY(y));
		emitters[sparkct].setSize(5);
		
		
		if(sparkct == emitters.length-1) 
			sparkct = 0;
		else
			sparkct++;
	}
	
	
	public static void renderEntities(Graphics g) {
		for(int i = 0; i < EntityManager.getTableLength("mobile"); i++) {
			Entity curEnt = EntityManager.getByIndex(i, "mobile");
			g.draw(curEnt.getBounds());
			
			Image shadow = curEnt.getShadowCasterImg();		
			g.pushTransform();
			
			float x = curEnt.getX() - shadow.getWidth()/2, y = curEnt.getY() - shadow.getHeight()/2;
			
			g.scale(1, -0.4f);
			shadow.setAlpha(0.3f);
			shadow.draw(x-33, -y/0.4f - 370, shadow.getWidth(), shadow.getHeight(), 0, 40, Color.black);
			g.popTransform();
			shadow.setAlpha(1);
			g.drawImage(curEnt.getImg(), x, y);
		}
	}
	
	
	public static void renderMap(Graphics g) {
		GameBase.getZone().render(-(int)Camera.getAnchorX(), -(int)Camera.getAnchorY());
	}
	
	
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
				emitters[i].render(g, GameBase.getDelta());
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
	
	public static float toWorldX(float ox) {
		return ox - Camera.getAnchorX();
	}
	
	public static float toWorldY(float oy) {
		return oy - Camera.getAnchorY();
	}
}
