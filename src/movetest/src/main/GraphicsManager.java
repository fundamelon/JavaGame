package main;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;


public class GraphicsManager {
	
	Image[] texture = new Image[4];
	grid panel;
	
	public GraphicsManager(grid transPanel, ControlManager oldControls, PlayerEnt oldPlayer) {
		panel = transPanel;
	}
	
	public void draw(Graphics g, PlayerEnt player, grid panel) {		//The object is intended to tell other objects it's time to re-draw themselves.
		player.draw(g, panel);
	}
	
	public void drawPlayer(Graphics g, PlayerEnt player) {
		
	}
	
	public void drawBackground(Graphics g) {
		int wdOfRow = panel.getWidth() / 20;
		int htOfRow = panel.getHeight() / 15;
		try {
			Random rand = new Random();
			texture[1] = ImageIO.read(new File("bin/img/grass1.png"));
			texture[2] = ImageIO.read(new File("bin/img/grass2.png"));
			texture[3] = ImageIO.read(new File("bin/img/grass3.png"));
			for(int k = 0; k <= 20; k++) {
				for(int c = 0; c <= 15; c++) {
					rand.setSeed(k*c);
					g.drawImage(texture[rand.nextInt(3)+1], k * wdOfRow, c * htOfRow, null);
				}
			}
		} catch(IOException e) {
			System.out.println("Failed to load image!");
		}
	}
}
