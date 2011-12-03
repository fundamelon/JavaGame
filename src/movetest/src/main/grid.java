package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class grid extends JPanel implements KeyListener, MouseListener, Runnable {
	private static final long serialVersionUID = 6306113229343973266L;
	
	private boolean first = true;
	private ControlManager gameControl;
	private GraphicsManager gameGraphics;
	private BufferStrategy buffer;
	private int w, h;
	private Image dbImage;
	private Graphics dbg;
	private long startTime = System.currentTimeMillis(), curTime;
	private Point mousePos;
	private boolean mousedown = false;
	private Thread th;
	
	private int frame = 0, fps_frame = 0, fps = 20, fps_goal = 45, delay = 1000 / fps_goal, fps_lag = 0;
	private long frameTime;
	
	public void init() {
		//Compute millis needed for the fps.
		delay = (fps_goal > 0) ? (1000  / fps_goal) : 100;
		System.out.print(delay);
	}
	public void start() {
		//Get this process thread and assign it to th
		th = new Thread(this);
		th.start();
	}
	
	
	public void stop() {
		th = null;
	}
	
	//TODO : Override
	public void destroy() {}
	
	//run() is called every time the thread executes.
	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		curTime = System.currentTimeMillis();
		
		//Call stop() to stoppit.
		while(Thread.currentThread() == th) {
			if(first) 
				Player.init(5, 5);
			try {
				// Add the mouse position to the container element position! Yay.
				mousePos = new Point((MouseInfo.getPointerInfo().getLocation().x - this.getLocationOnScreen().x), (MouseInfo.getPointerInfo().getLocation().y - this.getLocationOnScreen().y));
			} catch(IllegalComponentStateException e) { /* This only exists cause it'll throw an error first time around. */	}
			ControlManager.setMousePos(mousePos.x, mousePos.y);
			
			if(mousedown) 
				GraphicsManager.createParticleShower();
			
		//	System.out.println(curTime);
			
			//Tell each to set its amount to fps_lag to compensate for lag!
			ControlManager.clk(fps_lag);
			GraphicsManager.clk(fps_lag);
			refresh();
			
			try {
				//Compute our elapsed time from last frame
				fps_lag = (int)(System.currentTimeMillis() - curTime);
				//Add delay to see how far we've got
				curTime = System.currentTimeMillis();
				
				//Sleep it for the time specified to keep 
				if(first) first = false;
				Thread.sleep(Math.max(0, 20));
			}
			catch(InterruptedException ex) {
				System.out.println("I say! Someone seems to have interrupted me!");
				break;
			}
			
			//advance fraem counters
			
			//Compute FPS (broken)
			if(System.currentTimeMillis() - frameTime > 1000) {
				frameTime = System.currentTimeMillis();
				fps = fps_frame;
				fps_frame = 0;
			}
			
			//Max priority for slower comps
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}
	
	
	
	public grid(Color backColor, int width, int height) {
		setBackground(backColor);
		setPreferredSize(new Dimension(width, height));
		GraphicsManager.init(this);
		System.out.println("Player initialized");
		w = width;
		h = height;
		
		Player.setBlockSize(width/20);
		
		start();
	//	timer = new javax.swing.Timer(40, new MoveListener());
	//	timer.start();
	}
	
	public void update(Graphics g) //Called when repaint() is called, this runs before paintComponent().
	{
	}
	
	//OBSOLETE
/*	public void paint(Graphics g) {
		super.paintComponent(g);
		gameGraphics.draw(g, this);
		frame++;
		fps_frame++;
		gameGraphics.print((Graphics2D)g, "fps: "+fps, 150.0, 100.0, true);
	} */
	
	//Alternative for paint(g)
	public void refresh() {
		Graphics2D g = null;
		//Get the graphics of the buffer object for drawing on.
		g = (Graphics2D)window.buffer.getDrawGraphics();
		//Translate the buffer area to the panel area.
		g.translate(this.getLocationOnScreen().x - window.getFrame().getLocationOnScreen().x, this.getLocationOnScreen().y - window.getFrame().getLocationOnScreen().y);
		GraphicsManager.draw((Graphics2D)g, this);
		GraphicsManager.print((Graphics2D)g, "fps: "+fps, 150.0, 100.0, true);
		GraphicsManager.print((Graphics2D)g, "particles: " + gameGraphics.getParticleCount(), 150.0, 115.0, true);
		
		//You don't need g anymore, it'd just gobble up memory
		g.dispose();
		
		//Copy buffer to the window area.
		window.buffer.show();
		
		//Increment frame counter.
		frame++;
		fps_frame++;
	}
	
	
	public void print(Graphics g, String text, int x, int y) {
		g.drawString(text, x, y);
	}

	public void keyPressed(KeyEvent e) {
		gameControl.keyDown(e.getKeyCode());
	}
	
	public void keyReleased(KeyEvent e) {
		gameControl.keyUp(e.getKeyCode());
		
	}
	
	public void keyTyped(KeyEvent e) {}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		mousedown = true;
		
	}

	public void mouseReleased(MouseEvent arg0) {
		mousedown = false;
	}
}
