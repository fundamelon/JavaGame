package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.Robot;

public class GameBase extends JPanel implements KeyListener, MouseListener, Runnable {
	private static final long serialVersionUID = 6306113229343973266L;
	
	private boolean first = true;
	private BufferStrategy buffer;
	private static int scaleMul = 1;
	private Image dbImage;
	private Graphics dbg;
	private long startTime = System.currentTimeMillis(), curTime;
	private Point mousePos;
	private boolean mousedown = false, endgame = false;
	private Thread th;
	public Robot override;
	
	private long tick = 0;
	
	private static Zone currentZone;
	
	private static Entity_player ENT_Player;
	
	private int frame = 0, fps_frame = 0, fps = 20, fps_goal = 65, delay = 1000 / fps_goal, lag = 0;
	private long frameTime;
	
	public static Zone getZone() {
		return currentZone;
	}
	
	public static Entity_player getPlayerEntity() {
		return ENT_Player;
	}
	
	public static int getScaleMul() {
		return scaleMul;
	}
	
	/**OBSOLETE*/
	public void init() {
		//Compute millis needed for the fps.
		delay = (fps_goal > 0) ? (1000  / fps_goal) : 100;
	}
	
	
	/**
	 * Assign program to a thread and start it.
	 */
	public void start() {
		
		ENT_Player = new Entity_player();
		ENT_Player.setBlockSize(this.getWidth()/20);
		
		//init robot
		try {
			override = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		currentZone = new Zone();
		
		try {
			currentZone.readFromFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GraphicsManager.init();
		
		//Get this process thread and assign it to th
		th = new Thread(this);
		th.start();
	}
	
	
	/** Set this thread to null */
	public void stop() {
		endgame = true;
	}
	
	//TODO : Override
	public void destroy() {}
	
	/**
	 * Main process thread's instructions
	 */
	public void run() {
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		curTime = System.currentTimeMillis();
		
		//Call stop() to disable execution.
		while(!endgame) {
			tick++;
			
			if(first) {
				ENT_Player.init(5, 5, true);
			}
			
			try {
				// Add the mouse position to the container element position
				mousePos = new Point((MouseInfo.getPointerInfo().getLocation().x - this.getLocationOnScreen().x) / scaleMul, (MouseInfo.getPointerInfo().getLocation().y - this.getLocationOnScreen().y) / scaleMul);
				ControlManager.setMousePos(mousePos.x, mousePos.y);
			} catch(IllegalComponentStateException e) { /* breathing room */	}
			
			if(mousedown) GraphicsManager.createParticleShower();
			
			
			//Tell each manager to set its amount to frame_delay to compensate for lag!
			ControlManager.setLag(lag);
			GraphicsManager.setLag(lag);
			
			ControlManager.clk(tick);
			
			//Refresh graphics
			refresh();
			

			//Compute our elapsed time from last frame
			lag = (int)(System.currentTimeMillis() - curTime);
			curTime = System.currentTimeMillis();
			
			if(first) first = false;
			
		
			//Compute FPS
			if(System.currentTimeMillis() - frameTime > 1000) {
				frameTime = System.currentTimeMillis();
				fps = fps_frame;
				fps_frame = 0;
			}
			
			try {
				if(lag < 10)
				th.sleep(5);
			} catch (InterruptedException e) {
			}
		}
	}
	
	
	/**
	 * Initialize this grid
	 * @param backColor - Color of background
	 * @param width - in pixels
	 * @param height - in pixels
	 */
	public GameBase(Color backColor, int width, int height) {
		setBackground(backColor);
		setPreferredSize(new Dimension(width, height));
		
		start();
	//	timer = new javax.swing.Timer(40, new MoveListener());
	//	timer.start();
	}
	
	/**OBSOLETE*/
	public void update(Graphics g) //Called when repaint() is called, this runs before paintComponent().
	{
	}
	
	
	/**OBSOLETE*/
/*	public void paint(Graphics g) {
		super.paintComponent(g);
		gameGraphics.draw(g, this);
		frame++;
		fps_frame++;
		gameGraphics.print((Graphics2D)g, "fps: "+fps, 150.0, 100.0, true);
	} */
	
	
	/**
	 * Refreshes graphics through GraphicsManager and writes to a buffer, then copies buffer to Window
	 */
	public void refresh() {
		Graphics2D g = null;
		//Get the graphics of the buffer object for drawing on.
		g = (Graphics2D)Window.buffer.getDrawGraphics();
		if(this.getWidth() >= 1024 && this.getHeight() >= 768) 
			scaleMul = 2; 
		else 
			scaleMul = 1;
		g.scale(scaleMul, scaleMul);
		//Translate the buffer area to the panel area.
		g.translate(this.getLocationOnScreen().x - Window.getFrame().getLocationOnScreen().x, this.getLocationOnScreen().y - Window.getFrame().getLocationOnScreen().y);
		if(GameLogic.inMainMenu()) {
			GraphicsManager.drawMainMenu((Graphics2D)g);
		} else {
			GraphicsManager.drawGameView((Graphics2D)g, this);
			GraphicsManager.print((Graphics2D)g, "fps: "+fps, 150, 100, true);
			GraphicsManager.print((Graphics2D)g, "particles: " + GraphicsManager.getParticleCount(), 150, 115, true);
		}
		
		
		g.dispose();
		
		Window.buffer.show();
		
		frame++;
		fps_frame++;
	}

	/** Called when a key was just pressed */
	public void keyPressed(KeyEvent e) {
		ControlManager.keyDown(e);
	}
	
	
	/** Called when a key was just released */
	public void keyReleased(KeyEvent e) {
		ControlManager.keyUp(e);
	}
	
	
	/** Called when a key is pressed then released */
	public void keyTyped(KeyEvent e) {}
	
	
	/** Called when the mouse was pressed then released */
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

	/** Called when the mouse was just pressed */
	public void mousePressed(MouseEvent e) {
		mousedown = true;
		
	}

	/** Called when the mouse was just released */
	public void mouseReleased(MouseEvent e) {
		mousedown = false;
	}
}
