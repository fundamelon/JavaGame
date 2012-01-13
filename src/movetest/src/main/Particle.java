package main;

import java.awt.Color;
import java.util.Random;

public class Particle {
	
	//Placeholder that only knows its own values and can change them according to a model.
	private float x, y, rate_x, rate_y, dec_x, dec_y, trace_x, trace_y, life, mouse_x, mouse_y, start_x, start_y, startalpha, curalpha;
	private float ang = 0, ang2=180;
	private long startTick, lastTick;
	private boolean alive, trace, grav;
	private Color curCol;
	private boolean[] modifiers = new boolean[10];
	
	
	/**
	 * Create a particle at the given coordinates, speed, and other attributes.
	 * @param x - start pos x
	 * @param y - start pos y
	 * @param rx - rate x
	 * @param ry - rate y
	 * @param dcx - rate decay x
	 * @param dcy - rate decay y
	 * @param st - start tick
	 * @param life - lifetime in ms
	 * @param gravity - toggle gravity effect
	 */
	public Particle(float x, float y, float rx, float ry, float dcx, float dcy, long st, float life, boolean gravity) {
		this(x, y, rx, ry, dcx, dcy, st, life, gravity, Color.black);
	}
	
	
	/**
	 * Create a particle at the given coordinates, speeds, life, and color.
	 * @param x
	 * @param y
	 * @param rx
	 * @param ry
	 * @param dcx
	 * @param dcy
	 * @param st
	 * @param life
	 * @param gravity
	 * @param newColor
	 */
	public Particle(float x, float y, float rx, float ry, float dcx, float dcy, long st, float life, boolean gravity, Color newColor) {
		this.x = x;
		this.y = y;
		this.rate_x = rx;
		this.rate_y = gravity ? -Math.abs(ry) : ry;
		this.dec_x = dcx;
		this.dec_y = dcy;
		this.startTick = st;
		this.lastTick = st;
		this.life = life;
		this.grav = gravity;
		this.alive = true;
		this.start_x = x;
		this.start_y = y;
		this.setColor(newColor);
		this.setAlpha(255);
	}
	
	
	
	/**
	 * Toggle the particle movement modifier on/off.
	 * @param n - index of modifier
	 */
	public void toggleModifier(int n) {
		modifiers[n] = !modifiers[n];
	}
	
	
	public void setX(float nx) {x = nx;}
	public void setY(float ny) {y = ny;}
	public void setRateX(float nrate_x) {rate_x = nrate_x;}
	public void setRateY(float nrate_y) {rate_y = nrate_y;}
	public void setDecX(float ndec_x) {dec_x = ndec_x;}
	public void setDecY(float ndec_y) {dec_y = ndec_y;}
	public void setLife(float nlife) {life = nlife;}
	public void setColor(Color nC) {curCol = nC;}
	
	public void setAlpha(float in) {
		int n = (int)in;
		curCol = new Color(curCol.getRed(), curCol.getGreen(), curCol.getBlue(), ControlManager.clamp(0, 255, n));
		startalpha = n;
	}
	
	public float getX() { return x; }
	public float getY() { return y; }
	public float getRateX() {return rate_x; }
	public float getRateY() {return rate_y; }
	public float getDecayX() {return dec_x; }
	public float getDecayY() {return dec_y; }
	public float getLife() {return life;}
	public boolean getStatus() {return alive;}
	public boolean getTrace() {return trace;}
	public long getStartTime() {return startTick;}
	
	public int getRed() {return curCol.getRed();}
	public int getBlue() {return curCol.getBlue();}
	public int getGreen() {return curCol.getGreen();}
	public int getAlpha() {return curCol.getAlpha();}
	
	public float getTraceX() { return trace_x;}
	public float getTraceY() {return trace_y;}
	
	public void kill() {alive = false;}
	
	
	
	/**
	 * Move the particle according to its movement rules.
	 * @param delay - frame delay
	 */
	public void move(long delay) {
		if(alive) {
			trace_x = x;
			trace_y = y;
			
			if(Math.abs(rate_x) > 0.1) rate_x *= dec_x * ControlManager.getLagComp(); else rate_x = 0;
			if(Math.abs(rate_y) > 0.1) rate_y *= dec_y * ControlManager.getLagComp(); else rate_y = 0;
			
			x += rate_x;
			y += rate_y;
			
			
		} else {
			//Stuff to do if its dead.
		}
	}
}
