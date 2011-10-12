import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class grid extends JPanel implements KeyListener {
	private javax.swing.Timer timer;
	private int tick=0;
	private ControlManager gameControl;
	private GraphicsManager gameGraphics;
	private char keyChar = "w".charAt(0);
	private int w, h;
	private PlayerEnt player = new PlayerEnt(0, 0);
	
	public grid(Color backColor, int width, int height) {
		setBackground(backColor);
		setPreferredSize(new Dimension(width, height));
		gameControl = new ControlManager(player);
		gameGraphics = new GraphicsManager(this, gameControl, player);
		System.out.println("Player initialized");
		timer = new javax.swing.Timer(10, new MoveListener());
		timer.start();
		w = width;
		h = height;
	}
	
	public void drawGrid(Graphics g) {		//Simple grid drawing algorithm.
		int k=0;
		Color oldColor = g.getColor();
		g.setColor(Color.black);
		int htOfRow = getHeight() / 15;
		for (k = 0; k <= 15; k++)
			g.drawLine(0, k * htOfRow , getWidth(), k * htOfRow );
		
		int wdOfRow = getWidth() / 20;
		for (k = 0; k <= 20; k++) 
			g.drawLine(k*wdOfRow , 0, k*wdOfRow , getHeight());
		g.setColor(oldColor);
	}
	
	public void print(Graphics g, String text) {
		g.drawString(text, 100, 100);
	}
	
	
	public void paintComponent(Graphics g) {	//Called each time it's redrawn.  Send the gamegraphics a message to draw each component.
		super.paintComponent(g);
		drawGrid(g);
		gameGraphics.draw(g, player, this);
	}
	
	private class MoveListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			repaint();
		}
	}

	public void keyPressed(KeyEvent e) {
		gameControl.keyDown(e.getKeyCode());
	}
	
	public void keyReleased(KeyEvent e) {
		gameControl.keyUp(e.getKeyCode());
		
	}

	public void keyTyped(KeyEvent e) {}
}
