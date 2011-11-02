package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;
import java.util.*;


public class GraphicsManager {
	
	BufferedImage[] texture = new BufferedImage[10];
	grid panel;
	private Random rand = new Random();
	private int textureSeed = rand.nextInt(64);
	private ControlManager gameControls;
	private ParticleEmitter sparks[] = new ParticleEmitter[50];
	private int sparkct = 0;
	private int ticks = 0;
	
	//Vars with preceding underscore are to be values for render options.  :O
	private boolean _dither = false;
	
	public GraphicsManager(grid transPanel, ControlManager oldControls, PlayerEnt oldPlayer) {
		panel = transPanel;
		gameControls = oldControls;
		
		try {
			texture[1] = ImageIO.read(new File("lib/img/grass1.png"));
			texture[2] = ImageIO.read(new File("lib/grass2.png"));
			texture[3] = ImageIO.read(new File("lib/grass3.png"));
			texture[4] = ImageIO.read(new File("lib/img/stone1.png"));
			texture[5] = ImageIO.read(new File("lib/img/stone2.png"));
			texture[6] = ImageIO.read(new File("lib/img/stone3.png"));
			texture[7] = ImageIO.read(new File("lib/img/grass_end1.png"));
			texture[8] = ImageIO.read(new File("lib/img/grass_end2.png"));
			texture[9] = ImageIO.read(new File("lib/img/grass_end3.png"));
		} catch(IOException e) {
			System.out.println("ERROR: Failed to load tile images");
		}
	}
	
	public void draw(Graphics g, PlayerEnt player, grid panel) {		//The object is intended to tell other objects it's time to re-draw themselves.
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
		
		//Dithering! Doesn't work at all lol.  I'm still working on it man, chill out bro
		if(gameControls.getKeyStatus("L".charAt(0)))
			_dither = !_dither;
		
		if(_dither) 
			g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		else
			g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
			
		drawBackground(g2);
		drawEffects(g2);
		player.draw(g2);
	}
	
	//Create a burst of sparks at dis location!
	public void createSparks(int x, int y) {
		// It goes X, Y, Amount, speed X/Y, drag X/Y (0.8 to 0.99 is good), fade rate, repeater (broked), float boolean, color (optional)
		if(sparkct == sparks.length-1) 
			sparkct = 0;
		else
			sparkct++;
		sparks[sparkct] = new ParticleEmitter(x, y, 5, 10, 10, 0.8, 0.8, 40, false, false, Color.yellow);
	}
	
	public void clk(int n) {
		ticks = n;
	}
	
	public void drawPlayer(Graphics g, PlayerEnt player) {
		
	}
	
	public void drawBackground(Graphics2D g2) {
		//Graphics2D g2 inherits all Graphics, but with more tools and function.
		
		
		//Read and load all the tile files into the array.
		//Set a random seed so random is the same every time it renders.
		rand.setSeed(textureSeed);
		int curPosX = 0, curPosY = 0;
		
		
		//Draw tiles.  TODO: Clean this shit up.  Algorithm is inverted :\
		for(int k = 0; k <= 19; k++) {
			for(int c = 0; c <= 14; c++) {
				curPosX = k * 32; 	//Current screen x position in pixels.  Increments by 32.
				curPosY = c * 32;	//Current screen y pos in pixels.  Increments of 32.
				
				if((k == 0)  || (k == 19) || (c == 0) || (c == 14)) { 
					
					
					//If it's on the edge first draw stone underlay
					g2.drawImage(texture[rand.nextInt(3)+4], curPosX, curPosY, curPosX + 32, curPosY+32, 0, 0, 32, 32, null);
					
					//Grass edge pictures
					//Left column
					if(k==19) {
						//Top left corner
						if(c == 0) {
							g2.drawImage(texture[9], curPosX, curPosY, null);
						}
						//In between
						else if(c != 14) {
							g2.drawImage(texture[rand.nextInt(2)+7], curPosX, curPosY, null);
						}
						//Bottom left corner
						else {
							g2.drawImage(texture[9], curPosX, curPosY + 32, curPosX + 32, curPosY, 0, 0, 32, 32, null);
						}
					}
					
					//Right column
					if(k==0) {
						//Top right corner
						if(c==0) {
							g2.drawImage(texture[9], curPosX + 32, curPosY, curPosX, curPosY+32, 0, 0, 32, 32, null);
						}
						//In between
						else if(c != 14) {
							g2.drawImage(texture[rand.nextInt(2)+7], curPosX + 32, curPosY + 32, curPosX, curPosY, 0, 0, 32, 32, null);
						}
						//Top left corner
						else {
							g2.drawImage(texture[9], curPosX + 32, curPosY + 32, curPosX, curPosY, 0, 0, 32, 32, null);
						}
					}
					
					//If it's not far left or right column
					if(k != 0 && k != 19) {
						//Top row
						g2.translate(curPosX + 16, curPosY + 16);
						if(c==0) {
							g2.rotate(-0.5 * Math.PI);
							g2.drawImage(texture[rand.nextInt(2)+7], -16, -16, null);
							g2.rotate(0.5 * Math.PI);
						}
						//Bottom row
						if(c==14) {
							g2.rotate(0.5 * Math.PI);
							g2.drawImage(texture[rand.nextInt(2)+7], -16, -16, null);
							g2.rotate(-0.5 * Math.PI);
						}
						g2.translate(-curPosX - 16, -curPosY - 16);
					}
				}
				else {
					//Just draw a grass tile if it's not a boundary
					g2.drawImage(texture[rand.nextInt(3)+1], curPosX, curPosY, null);
				}
			}
			if(gameControls.getKeyStatus("R".charAt(0))) {
				textureSeed++;
			}
		}
	}
	
	public void drawEffects(Graphics2D g2) {
		for(int i = 0; i < sparks.length; i++) {
			if(sparks[i] != null) 
				sparks[i].draw(g2, ticks);
		}
	}
}
