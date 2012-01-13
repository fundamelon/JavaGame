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
	private float rate_x = 0, rate_y = 0, decay_x, decay_y, ang;
	private int particle_size = 1;
	
	private boolean rand_size_x, rand_size_y, rand_dir_x, rand_dir_y, rand_ang, grav, trails;
	
	
	/** Create a specified amount of particles at a position with default speeds, life, and colour.
	 * @param x - emitter origin x
	 * @param y - emitter origin y
	 * @param amt - number of particles to create
	 * @param startTick - time of creation
	 * @param repeat - continuously respawn particles
	 * @param gravity - gravity modifier
	 */
	public ParticleEmitter(int x, int y, int amt, int startTick, boolean repeat, boolean gravity) {
		this(x, y, amt, 0.1f, 0,1f, 0.1f, 100, repeat, gravity, Color.yellow);
	}
	
	
	/**
	 * Create a specified amount of particles at a position with specified speeds, life, and color.
	 * @param x - emitter origin x
	 * @param y - emitter origin y
	 * @param amt - number of particles to spawn
	 * @param rx - particle rate x
	 * @param ry - particle rate y
	 * @param dcx - rate decay x
	 * @param dcy - rate decay y
	 * @param life - particle life in ms
	 * @param repeat - continuously respawn particles
	 * @param gravity - gravity modifier
	 * @param newCol - particle color
	 */
	public ParticleEmitter(int x, int y, int amt, float rx, float ry, float dcx, float dcy, float life, boolean repeat, boolean gravity, Color newCol) {
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
	
	
	/**
	 * Arrange particles and spawn them.
	 * @param ox - emitter origin x
	 * @param oy - emitter origin y
	 * @param rx - rate x
	 * @param ry - rate y
	 * @param dcx - rate decay x
	 * @param dcy - rate decay y
	 * @param t - frame delay in ms
	 * @param life - particle life in ms
	 * @param gravity - gravity modifier
	 * @param newCol - particle color
	 */
	public void initParticles(int ox, int oy, float rx, float ry, float dcx, float dcy, long t, float life, boolean gravity, Color newCol) {
		for(int i = 0; i < particles.length; i++ ) {
			int ang = rand.nextInt() * 360;
			float dist = (float)((rand.nextDouble() / 2));
			
			//Random direction from origin of emitter.
			particles[i] = new Particle(ox, oy, (float)Math.cos(ang) * dist * rx, (float)Math.sin(ang) * dist * ry, dcx, dcy, t, life, gravity, newCol);
		}
	}
	
	
	/**
	 * Tells particle to update its position
	 * @param i - particle index
	 * @param t - frame delay
	 */
	public void updatePos(int i, long t) {
		particles[i].move(t);
	}
	
	
	/**
	 * Toggle particle movement modifier at local indes
	 * @param n - local particle index
	 */
	public void toggleModifier(int n) {
		for(int i = 0; i < particles.length; i++) {
			if(particles[i] != null) {
				particles[i].toggleModifier(n);
			}
		}
	}
	
	/** OBSOLETE */
	public void setTrails(boolean n) {
		trails = n;
	}
	
	public int getX() {return ex;}
	public int getY() {return ey;}

	
	/**
	 * Draw currently active particles.
	 * @param g2 - Graphics2D to draw with
	 * @param delay - frame delay
	 */
	public void draw(Graphics2D g2, long delay) {
		
		Color oldColor;
		
		for(int i = 0; i < number; i++){
			//i corresponds to array index for ease of use.
			
			//skip empty
			if(particles[i] != null && particles[i].getStatus()) {
				
				//So much casting it ain't even funny.
				
				oldColor = g2.getColor();
				g2.setColor(new Color(particles[i].getRed(), particles[i].getGreen(), particles[i].getBlue(), particles[i].getAlpha()));
				
				//Update particle position
				updatePos(i, delay);
				
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
	
	
	/**
	 * Set all particles' sizes
	 * @param nSize - size in pixels
	 */
	public void setParticleSize(int nSize) {
		particle_size = nSize;
	}
	
	
	
	/**
	 * Count the particles of this emitter
	 * @return Total active particles belonging to this emitter
	 */
	public int getParticleCount() {
		int c = 0;
		for(int i = 0; i < particles.length; i++) {
			if(particles[i].getStatus())
				c++;
		}
		return c;
	}
}