package main;


import main.entity.*;
import main.gui.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.Util;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.newdawn.slick.Graphics;

import Dario.*;

public class GameBase {
	

	private static Zone currentZone;

	/** Current game drawing mode
	 * 0: Menu
	 * 1: Game view
	 * 2: Console
	 */
	public static int viewMode = 1;
	
	public static final int VIEW_MENU = 0;
	public static final int VIEW_GAME = 1;
	public static final int VIEW_CONS = 2;
	
	public static boolean debug_keyboard = false;
	public static boolean debug_mouse = false;
	
	/** time at last frame */
	static long lastFrame;
	
	/** frames per second */
	static int fps;
	/** last fps time */
	static long lastFPS;
	
	static boolean vsync;
	public static boolean shading;
	
	private static Shader shader;

	public static boolean mapRendering = true;
	
	public static void main(String[] args) {
		GameBase.start();
	}
	
	public static void start() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		Graphics g = new Graphics();
		
		loadZone(new Zone("lib/map/test.tmx"));
		
		GraphicsManager.init();
		ControlManager.init();
		EntityManager.init();
		AIManager.init();

		initGL();
		
		AIManager.generateNodeMap(32, 64, 64);
		
	//	shading = true;
		GraphicsManager.setDebugMode(true);
		
	//	shader = new Shader("src/shader/screen.vert", "src/shader/screen.frag");
		
		
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer

		while (!Display.isCloseRequested()) {
			int delta = getDelta();

			
			update(delta);
			render(g, delta);

			Display.update();
			g.clear();
		//	Display.sync(60); // cap fps to 60fps
		}

		Display.destroy();
	}
	
	public static void update(int delta) {
		
		//Keyboard Event Handlers!!
		//TODO: Move to ControlManager
		
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_F) {
		        	setDisplayMode(800, 600, !Display.isFullscreen());
		        }
		        else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
		        	vsync = !vsync;
		        	Display.setVSyncEnabled(vsync);
		        }
		    }
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			System.exit(0);
			

		if(viewMode == VIEW_GAME) {
			ControlManager.update(delta);
			EntityManager.update();
		}
		
		updateFPS(); // update FPS Counter
	}
	
	public static void setDisplayMode(int width, int height, boolean fullscreen) {

	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
		(Display.isFullscreen() == fullscreen)) {
		    return;
	    }

	    try {
	        DisplayMode targetDisplayMode = null;
			
		if (fullscreen) {
		    DisplayMode[] modes = Display.getAvailableDisplayModes();
		    int freq = 0;
					
		    for (int i=0;i<modes.length;i++) {
		        DisplayMode current = modes[i];
						
			if ((current.getWidth() == width) && (current.getHeight() == height)) {
			    if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
			        if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
				    targetDisplayMode = current;
				    freq = targetDisplayMode.getFrequency();
	                        }
	                    }

			    // if we've found a match for bpp and frequence against the 
			    // original display mode then it's probably best to go for this one
			    // since it's most likely compatible with the monitor
			    if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
	                        (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
	                            targetDisplayMode = current;
	                            break;
	                    }
	                }
	            }
	        } else {
	            targetDisplayMode = new DisplayMode(width,height);
	        }

	        if (targetDisplayMode == null) {
	            System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
	            return;
	        }

	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
				
	    } catch (LWJGLException e) {
	        System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
	    }
	}
	
	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public static int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
	 
	    return delta;
	}
	
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public static long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public static void initGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 600, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		
		//Fix transparent pixels being black
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ZERO);
	}
	
	public static void render(Graphics g, int delta) {	
	//	shader.activate();
		switch(viewMode) {
		case VIEW_MENU:
			break;
		case VIEW_GAME:
	        g.setDrawMode(Graphics.MODE_NORMAL);
			g.drawLine(0.0f, 0f, 100f, 100f);
			GraphicsManager.renderGame(g, delta);
			break;
		case VIEW_CONS:
			break;
		}

	//	shader.deactivate();
	}
	
	public static int getWidth() {
		return Display.getWidth();
	}
	
	public static int getHeight() {
		return Display.getHeight();
	}
	
	public static void loadZone(Zone newZone) {
		currentZone = newZone;
		currentZone.init();
	}
	
	public static Zone getZone() {
		return currentZone;
	}

	public static void quit() {
		Display.destroy();
		
	}
}