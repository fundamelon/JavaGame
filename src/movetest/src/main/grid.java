package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class grid extends JPanel implements KeyListener, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6306113229343973266L;
	
	private ControlManager gameControl;
	private GraphicsManager gameGraphics;
	private int w, h;
	private PlayerEnt player = new PlayerEnt(5, 5, this);
	private Image dbImage;
	private Graphics dbg;
	
	private int fpsTick = 0, frames = 0, fps = 0;
	
	public void init() {}
	public void start() {
		//Get this process thread and assign it to th
		Thread th = new Thread(this);
		th.start();
	}
	
	
	//TODO: Make these live up to their names
	public void stop() {}
	public void destroy() {}
	
	//run() is called every time the thread executes.
	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		
		//Infinite loop; to stop it you call stop() or destroy().
		while(true) {
			fpsTick++;
			repaint();
			
			try
			{
				//Stop the infinite loop for 20 millis
				Thread.sleep(20);
			}
			catch(InterruptedException ex) {
				//just ignore it
			}
			
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}
	
	
	
	public grid(Color backColor, int width, int height) {
		setBackground(backColor);
		setPreferredSize(new Dimension(width, height));
		gameControl = new ControlManager(this, player);
		gameGraphics = new GraphicsManager(this, gameControl, player);
		System.out.println("Player initialized");
		w = width;
		h = height;
		
		player.setBlockSize(width/20);
		
		start();
	//	timer = new javax.swing.Timer(40, new MoveListener());
	//	timer.start();
	}
	
	public void update(Graphics g) //Called when repaint() is called, this runs before paintComponent().
	{
		//Capture the screen image
		if(dbImage == null) {
			dbImage = createImage(this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics();
		}
		
		//Paint the screen image after the screen is redrawn.  Don't touch unless you know what you're doing!
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
		dbg.setColor(getForeground());
		paint(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}
	
	
	public void paintComponent(Graphics g) {	//Called each time it's redrawn.  Send the gamegraphics a message to draw each component.
		super.paintComponent(g);
		drawGrid(g);
		gameGraphics.draw(g, player, this);
		frames++;
		if(fpsTick == 100) {
			fps = frames;
			fpsTick = 0;
			frames = 0;
		}
		print(g, "fps: "+fps, 100, 100);
	}
	
	public void drawGrid(Graphics g) {		//Simple grid drawing algorithm.
		
		int k=0;
		Color oldColor = g.getColor();
		g.setColor(new Color(200, 200, 200));
		int htOfRow = h / 15;
		for (k = 0; k <= 15; k++)
			g.drawLine(0, k * htOfRow , w, k * htOfRow );
		
		int wdOfRow = w / 20;
		for (k = 0; k <= 20; k++) 
			g.drawLine(k*wdOfRow , 0, k*wdOfRow , h);
		
		g.setColor(oldColor);
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
}
