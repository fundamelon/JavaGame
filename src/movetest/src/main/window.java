package main;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class window {	
	private static grid panel;
	private static JFrame theGUI;
	public static BufferStrategy buffer;
	public static void main(String [] args){
		theGUI = new JFrame();
		theGUI.setTitle("Grid Movement Test");
		theGUI.setSize(900, 900);
		theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theGUI.setIconImage(new ImageIcon().getImage());
		theGUI.setVisible(true);
		theGUI.createBufferStrategy(2); //Double buffering initialized.
		buffer = theGUI.getBufferStrategy(); //Get this frame's buffer object.
		panel = new grid(Color.white, 640, 480);
		theGUI.addKeyListener(panel);
		theGUI.addMouseListener(panel);
		Container pane = theGUI.getContentPane();
		pane.add(panel);
		theGUI.setIgnoreRepaint(true);

		theGUI.setResizable(true);
		theGUI.pack();	//Important function to keep interior dimensions even with the saiz of window.
		
	//	theGUI.createBufferStrategy(2);
	}
	
	public static JFrame getFrame() {
		return theGUI;
	}
	
	public static int getPanelWidth() {
		return panel.getWidth();
	}
	
	public static int getPanelHeight() {
		return panel.getHeight();
	}
	
	public static void close() {
		theGUI.dispose();
	}
}
