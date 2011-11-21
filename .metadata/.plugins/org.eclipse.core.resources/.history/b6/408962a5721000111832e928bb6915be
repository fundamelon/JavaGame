package main;
import javax.swing.*;
import java.awt.*;

public class window {	
	
	public static void main(String [] args){
		JFrame theGUI = new JFrame();
		theGUI.setTitle("Grid Movement Test");
		theGUI.setSize(900, 900);
		theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theGUI.setIconImage(new ImageIcon().getImage());
		grid panel = new grid(Color.white, 640, 480);
		theGUI.addKeyListener(panel);
		theGUI.addMouseListener(panel);
		Container pane = theGUI.getContentPane();
		pane.add(panel);
		theGUI.setIgnoreRepaint(true);

		theGUI.setResizable(false);
		theGUI.pack();	//Important function to keep interior dimensions even with the saiz of window.
		theGUI.setVisible(true);
		
	//	theGUI.createBufferStrategy(2);
	}
}
