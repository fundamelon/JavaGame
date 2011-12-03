package main;

import java.awt.*;
import java.util.Random;

public class ParticleEmitter {
	private String type = null;
	Random rand = new Random();
	private Particle[] particles;
	Color brown = new Color(92, 54, 25);
	Color spark_yellow = new Color(90, 0, 50);
	
	private boolean loop;
	private int ex, ey, life, size_x, size_y, trace_x, trace_y, number;
	private long tick;
	private double rate_x = 0, rate_y = 0, decay_x, decay_y, ang;
	private int particle_size = 1;
	
	private boolean rand_size_x, rand_size_y, rand_dir_x, rand_dir_y, rand_ang, grav, trails;
	
	public ParticleEmitter(int x, int y, int amt, int startTick, boolean repeat, boolean gravity) {
		this(x, y, amt, 0.1, 0,1, 0.1, 100, repeat, gravity, Color.yellow);
	}
	
	public ParticleEmitter(int x, int y, int amt, double rx, double ry, double dcx, double dcy, double life, boolean repeat, boolean gravity, Color newCol) {
		ex = x;
		ey = y;
		rate_x = rx;
		rate_y = ry;
		loop = repeat;
		grav = gravity;
		trails = false;
		
		particles = new Particle[amt];
		number = amt;
		initParticles(x, y, rx, ry, dcx, dcy, tick, life, gravity, newCol);
	}
	
	public void initParticles(int ox, int oy, double rx, double ry, double dcx, double dcy, long t, double life, boolean gravity, Color newCol) {
		for(int i = 0; i < particles.length; i++ ) {
			createParticle(i, ox, oy, rx, ry, dcx, dcy, t, life, gravity, newCol);
		}
	}
	
	public void createParticle(int i, int ox, int oy, double rx, double ry, double dcx, double dcy, long t, double life, boolean gravity, Color newColor) {
		int ang = rand.nextInt() * 360;
		double dist = ((rand.nextDouble() / 2) + 0.25);
		
		//Random direction from origin of emitter.
		rx = Math.cos(ang) * dist * rx;
		ry = Math.sin(ang) * dist * ry;
		particles[i] = new Particle(ox, oy, rx, ry, dcx, dcy, t, life, gravity, newColor);
	}
	
	public void updatePos(int i, long t) {
		particles[i].move(t);
	}
	
	public void toggleModifier(int n) {
		for(int i = 0; i < particles.length; i++) {
			if(particles[i] != null) {
				particles[i].toggleModifier(n);
			}
		}
	}
	
	public void setTrails(boolean n) {
		trails = n;
	}
	
	public int getX() {return ex;}
	public int getY() {return ey;}

	public void draw(Graphics2D g2, long tick) {
		
		Color oldColor;
		
		for(int i = 0; i < number; i++){
			//i corresponds to array index for ease of use.
			
			//skip empty
			if(particles[i] != null && particles[i].getStatus()) {
				
				//So much casting it ain't even funny.
				
				oldColor = g2.getColor();
				g2.setColor(new Color(particles[i].getRed(), particles[i].getGreen(), particles[i].getBlue(), particles[i].getAlpha()));
				
				//Update particle position
				updatePos(i, tick);
				
				//Draw a trace line
				if(trails)
					g2.drawLine((int)particles[i].getX(), (int)particles[i].getY(), (int)particles[i].getTraceX(), (int)particles[i].getTraceY());
				
				//Draw the particle
				g2.fillRect((int)particles[i].getX(), (int)particles[i].getY(), particle_size, particle_size);
				g2.setColor(oldColor);
			}
			
			//if it loops, create new ones where old ones die
			else if(loop) {
			//	createParticle(i, ex, ey, rate_x, rate_y, decay_x, decay_y, tick, life, grav);
			}
		}
	}
	
	public void setParticleSize(int nSize) {
		particle_size = nSize;
	}
	
	//Total up the particles this emitter has.
	public int getParticleCount() {
		int c = 0;
		for(int i = 0; i < particles.length; i++) {
			if(particles[i].getStatus())
				c++;
		}
		return c;
	}
}