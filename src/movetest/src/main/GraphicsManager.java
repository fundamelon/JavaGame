package main;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;


public class GraphicsManager {
	
	Image[] texture = new Image[10];
	grid panel;
	private Random rand = new Random();
	private int textureSeed = rand.nextInt(64);
	private ControlManager gameControls;
	
	public GraphicsManager(grid transPanel, ControlManager oldControls, PlayerEnt oldPlayer) {
		panel = transPanel;
		gameControls = oldControls;
	}
	
	public void draw(Graphics g, PlayerEnt player, grid panel) {		//The object is intended to tell other objects it's time to re-draw themselves.
		Graphics2D g2 = (Graphics2D) g;
		drawBackground(g2);
		player.draw(g2);
	}
	
	public void drawPlayer(Graphics g, PlayerEnt player) {
		
	}
	
	public void drawBackground(Graphics2D g2) {
		int wdOfRow = panel.getWidth() / 20;
		int htOfRow = panel.getHeight() / 15;
		try {
			texture[1] = ImageIO.read(new File("bin/img/grass1.png"));
			texture[2] = ImageIO.read(new File("bin/img/grass2.png"));
			texture[3] = ImageIO.read(new File("bin/img/grass3.png"));
			texture[4] = ImageIO.read(new File("bin/img/stone1.png"));
			texture[5] = ImageIO.read(new File("bin/img/stone2.png"));
			texture[6] = ImageIO.read(new File("bin/img/stone3.png"));
			texture[7] = ImageIO.read(new File("bin/img/grass_end1.png"));
			texture[8] = ImageIO.read(new File("bin/img/grass_end2.png"));
			texture[9] = ImageIO.read(new File("bin/img/grass_end3.png"));
			rand.setSeed(textureSeed);
			Image curTexture, overlay;
			for(int k = 0; k <= 19; k++) {
				for(int c = 0; c <= 14; c++) {
					if((k == 0)  || (k == 19) || (c == 0) || (c == 14)) { 

						g2.drawImage(texture[rand.nextInt(2)+4], k*wdOfRow, c*htOfRow, null);
						
						
						if(k==19) {
							if(c == 0) {
								g2.drawImage(texture[9], k*wdOfRow, c*htOfRow, null);
							}
							else {
								g2.drawImage(texture[rand.nextInt(2)+7], k*wdOfRow, c*htOfRow, null);
							}
						}
						
						if(k==0) {
								g2.drawImage(texture[rand.nextInt(2)+7], 
										k*wdOfRow, 
										c*htOfRow, 
										null);
						}
					}
					else {
						g2.drawImage(texture[rand.nextInt(3)+1], k * wdOfRow, c * htOfRow, null);
					}
					
				}
				if(gameControls.getKeyStatus("R".charAt(0))) {
					textureSeed++;
				}
			}
		} catch(IOException e) {
			System.out.println("Failed to load image!");
		}
	}
}
