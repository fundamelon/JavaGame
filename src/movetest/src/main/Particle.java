package main;

import java.awt.Color;
import java.util.Random;

public class Particle {
	
	//Placeholder that only knows its own values and can change them according to a model.
	private double x, y, rate_x, rate_y, dec_x, dec_y, trace_x, trace_y, life, mouse_x, mouse_y, start_x, start_y;
	private double ang = 0, ang2=180;
	private long startTick, lastTick;
	private boolean alive, trace, grav;
	private Color curCol;
	private boolean[] modifiers = new boolean[10];
	
	public Particle(double x, double y, double rx, double ry, double dcx, double dcy, long st, double life, boolean gravity) {
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
		this.curCol = Color.black;
		this.alive = true;
	}
	
	public Particle(double x, double y, double rx, double ry, double dcx, double dcy, long st, double life, boolean gravity, Color newColor) {
		this(x, y, rx, ry, dcx, dcy, st, life, gravity);
		this.curCol = newColor;
		this.setAlpha(100);
		this.start_x = x;
		this.start_y = y;
	}
	
	public void toggleModifier(int n) {
		modifiers[n] = !modifiers[n];
	}
	
	public void setX(double nx) {x = nx;}
	public void setY(double ny) {y = ny;}
	public void setRateX(double nrate_x) {rate_x = nrate_x;}
	public void setRateY(double nrate_y) {rate_y = nrate_y;}
	public void setDecX(double ndec_x) {dec_x = ndec_x;}
	public void setDecY(double ndec_y) {dec_y = ndec_y;}
	public void setLife(double nlife) {life = nlife;}
	public void setColor(Color nC) {curCol = nC;}
	
	public void setAlpha(int n) {curCol = new Color(curCol.getRed(), curCol.getGreen(), curCol.getBlue(), n);}
	
	public double getX() { return x; }
	public double getY() { return y; }
	public double getRateX() {return rate_x; }
	public double getRateY() {return rate_y; }
	public double getDecayX() {return dec_x; }
	public double getDecayY() {return dec_y; }
	public double getLife() {return life;}
	public boolean getStatus() {return alive;}
	public boolean getTrace() {return trace;}
	public long getStartTime() {return startTick;}
	
	public int getRed() {return curCol.getRed();}
	public int getBlue() {return curCol.getBlue();}
	public int getGreen() {return curCol.getGreen();}
	public int getAlpha() {return curCol.getAlpha();}
	
	public double getTraceX() { return trace_x;}
	public double getTraceY() {return trace_y;}
	
	public void kill() {alive = false;}
	
	
	public void move(long tick) {
		if(alive) {
			mouse_x = ControlManager.getMouseX();
			mouse_y = ControlManager.getMouseY();
			//Trace line origin is the previous X and Y.
			trace_x = x;
			trace_y = y;
			
			//Move the particles.
			this.x += rate_x;
			this.y += rate_y;
			
			//Decrease the rates via the decay variables.
			if(Math.abs(rate_x) > 0.1) 
				rate_x *= dec_x;
			else if(!grav) rate_x = 0;
			if(Math.abs(rate_y) > 0.1)
				rate_y *= dec_y;
			else if(!grav) rate_y = 0;
			
			//Make a random angle, then add its sin and cos to the particle.
			Random rand = new Random();
			//if(ang<360) 
				ang+=1;// else ang = 0;
				
				ang2+= 2 * (rand.nextDouble() - 0.5);
			
			//Various thingies you can use to modify each particles random movement.

			if(modifiers[0]) {
				this.rate_x += (mouse_x - this.x) * 0.01;
				this.rate_y += (mouse_y - this.y) * 0.01;
			}
			
			if(modifiers[1]) {	
				this.rate_x += Math.cos(ang) * 3 + Math.cos(ang2) * 2;
				this.rate_y += Math.sin(ang) * 3 + Math.sin(ang2) * 2;
			}
				
			if(modifiers[2]) {
				this.rate_x += rand.nextDouble() * 2 - 1;
				this.rate_y += rand.nextDouble() * 2 - 1;
			}
			
			if(modifiers[3]) {
				this.rate_y = -Math.abs(this.rate_y);
			}
		//	this.rate_y += 1;
			
			
			//Just makes the particles fan out on top and float upwards.
			if(grav){
				if(this.y - rate_y >= start_y) {
					rate_y = 0;
					this.y = start_y;
					this.setLife(Math.max(0, this.getLife() + 12));
				} else {
					rate_y += 1;
				}
			}
			this.setColor(new Color(this.getRed(), this.getGreen(), this.getBlue(), (int)Math.max(this.getAlpha() - this.getLife(), 0)));
			if(this.getAlpha() == 0) {this.kill();}
			
		//	System.out.println(tick - lastTick);

			
		} else {
			//Stuff to do if its dead.
		}
	}
}
