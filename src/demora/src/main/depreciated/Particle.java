package main.depreciated;

import org.newdawn.slick.Color;
import java.util.Random;

public class Particle {
	private float x, y, rate, dir, fric, size;
	private boolean alive;
	
	public Particle(float nx, float ny, float nrate, float ndir, float nfric, float nsize) {
		x = nx;
		y = ny;
		rate = nrate;
		dir = ndir;
		fric = nfric;
		size = nsize;
		alive = true;
	}
	
	public void update(int delta) {
		x += rate * Math.cos(dir);
		y += rate * Math.sin(dir);
		if(rate > 0.1) 
			rate /= fric; 
		else 
			rate = 0;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getSize() {
		return size;
	}
	
	public boolean getStatus() {
		return alive;
	}
	
	public void setSize(float nsize) {
		size = nsize;
	}
}
