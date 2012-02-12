package main;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import main.entity.Entity;
import main.entity.EntityManager;
import main.entity.Entity_player;
import main.particles.*;
import main.pathfinding.*;

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
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.particles.ParticleSystem;

public class GraphicsManager {
	
	static Image[] texture;
	static GameBase panel;
	public static org.newdawn.slick.particles.ParticleSystem particle_system_fire;
	public static org.newdawn.slick.particles.ParticleSystem particle_system_smoke;
	private static int sparkct = 0, particle_count = 0;
	private static int fps_lag;
	public static boolean first_run = true;
	
	private static Color fadeCol = new Color(0, 0, 0);
	private static Color overlayCol = new Color(fadeCol.getRed(), fadeCol.getGreen(), fadeCol.getBlue(), 0);
	//Vars with preceding underscore are to be values for render options.  :O
	private static boolean fadeMode = true, helperText = false, shake = false;
	
	private static boolean debug = false;
	
	/**
	 * Initialize state
	 */
	public static void init() {		
		try {
			particle_system_fire = new ParticleSystem(new Image("lib/img/particle/flamelrg_02.tga"));
			particle_system_smoke = new ParticleSystem(new Image("lib/img/particle/smoke_02.tga"));
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		particle_system_smoke.setBlendingMode(ParticleSystem.BLEND_COMBINE);
		particle_system_fire.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
	}
	
	
	
	/**
	 * Main function that redraws all graphics on a GameBase.
	 * @param g - graphics context
	 * @param delta - update delta
	 */
	public static void renderGame(Graphics g, int delta) {
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

		if(debug) {
			for(Rectangle r : GameBase.getZone().getCollisionArray()) {
				if(r != null) {
					g.draw(r);
				}
			}
			
		//	AIManager.renderNodeMap(g);
			new AStarHeuristic(AIManager.getNodeMap()).createPath().render(g);
		}
		
		renderEntities(g);
		
		particle_system_smoke.render();
		particle_system_fire.render();

		
		g.translate(Camera.getAnchorX(), Camera.getAnchorY());
		
		if(debug) {
			if(ControlManager.mouseButtonStatus(ControlManager.mousePrimary)) {
				
//				g.setColor(Color.white);
//				float[] xArr = ControlManager.getMouseTraceXArr();
//				float[] yArr = ControlManager.getMouseTraceYArr();
//				for(int i = 0; i < ControlManager.getMouseTraceLength()-1; i++) {
//					if(xArr[i+1] != 0)
//						g.drawLine(xArr[i], GameBase.getHeight() - yArr[i], xArr[i+1], GameBase.getHeight() - yArr[i+1]);
//				}
				g.setColor(Color.red);
				
	
				if(ControlManager.mouseButtonClicked(ControlManager.mousePrimary)) {
					for(int i = 0; i < 5; i++) {
						particle_system_smoke.addEmitter(new Emitter_SmokeMed());
						particle_system_smoke.getEmitter(particle_system_smoke.getEmitterCount()-1).setPos(toWorldX(ControlManager.getMouseX()), toWorldY(ControlManager.getMouseY()));
					}
					for(int i = 0; i < 5; i++) {
						particle_system_fire.addEmitter(new Emitter_FireMed());
						particle_system_fire.getEmitter(particle_system_fire.getEmitterCount()-1).setPos(toWorldX(ControlManager.getMouseX()), toWorldY(ControlManager.getMouseY()));
					}
				}
			//	for(int i = 0; i < particle_system_fire.getEmitterCount(); i++) {
			//		particle_system_fire.getEmitter(i).setPos(toWorldX(ControlManager.getMouseX()), toWorldY(ControlManager.getMouseY()));
			//	}
			}
			
	
			if(ControlManager.mouseButtonStatus(ControlManager.mouseSecondary)) {
				int tileX = GameBase.getZone().getTileAtX(toWorldX(ControlManager.getMouseX()));
				int tileY = GameBase.getZone().getTileAtY(toWorldY(ControlManager.getMouseY()));
				Color oldColor = g.getColor();
				g.setColor(Color.black);
				g.drawRect(toLocalX(tileX * 32), toLocalY(tileY * 32), 32, 32);
				g.setColor(oldColor);
				System.out.println("ID: "+(GameBase.getZone().getData().getTileId(tileX, tileY, 2)));
			}
			
			g.drawRect(ControlManager.getMouseX() - 8,  ControlManager.getMouseY() - 8, 16, 16);
		}
		
		g.setColor(oldCol);
		
		
		particle_system_fire.update(delta);
		particle_system_smoke.update(delta);
		
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
	
	public static void renderMainMenu(Graphics g) {	}
	
	
	public static ParticleSystem getParticleSystemFire() {
		return particle_system_fire;
	}
	public static void renderEntities(Graphics g) {
		for(int i = 0; i < EntityManager.getTableLength(); i++) {
			Entity curEnt = EntityManager.getByIndex(i);
			if(curEnt instanceof Entity_player) {
				curEnt = (Entity_player)curEnt;
				
				if(debug) 
					g.draw(curEnt.getBounds());
				
				Image shadow = curEnt.getShadowCasterImg();		
				g.pushTransform();
				
				float x = curEnt.getX() - shadow.getWidth()/2, y = curEnt.getY() - shadow.getHeight()/2;
				
				g.scale(1, -0.4f);
				shadow.setAlpha(0.3f);
				shadow.draw(x-33+curEnt.getImgOffsetX(), -y/0.4f - 370 + curEnt.getImgOffsetY(), shadow.getWidth(), shadow.getHeight(), 0, 40, Color.black);
				g.popTransform();
				shadow.setAlpha(1);
				g.drawImage(curEnt.getImg(), x + curEnt.getImgOffsetX(), y + curEnt.getImgOffsetY());
			}
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
	
	public static void setDebugMode(boolean mode) {
		debug = mode;
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
		return ox - Camera.getAnchorX();
	}
	/**
	 * Translate a global y value to a local y value
	 * @param oy - original y value
	 * @return localized y value
	 */
	public static float toLocalY(float oy) {
		return oy - Camera.getAnchorY();
	}
	
	public static float toWorldX(float ox) {
		return ox + Camera.getAnchorX();
	}
	
	public static float toWorldY(float oy) {
		return oy + Camera.getAnchorY();
	}
}
