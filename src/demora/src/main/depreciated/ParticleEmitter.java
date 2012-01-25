package main.depreciated;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import java.util.Random;

public class ParticleEmitter {
	Random rand = new Random();
	private Particle[] particles;
	
	private float x, y, rate, rate_randMul, dir, dir_randMul, fric, size, size_randMul;
	private int amt;
	
	public ParticleEmitter(int namt, float nx, float ny, float nrate, float nrate_randMul, float ndir, float ndir_randMul, float nfric, float nsize, float nsize_randMul) {
		amt = namt;
		x = nx;
		y = ny;
		rate = nrate;
		rate_randMul = nrate_randMul;
		dir = ndir;
		dir_randMul = ndir_randMul;
		fric = nfric;
		size = nsize;
		size_randMul = nsize_randMul;
		
		particles = new Particle[amt];
		
		for(int i = 0; i < amt; i++) {
			rate *= rand.nextFloat() * rate_randMul;
			dir *= rand.nextFloat() * dir_randMul * 360;
			size *= rand.nextFloat() * size_randMul;
			particles[i] = new Particle(x, y, rate, dir, fric, size);
		}
	}
	
	public ParticleEmitter(int namt, float nx, float ny) {
		this(namt, nx, ny, 1, 0f, 0, 1, 1, 1, 0);
	}
	
	public void setSize(float nsize) {
		size = nsize;
	}
	
	/**
	 * Toggle particle movement modifier at local index
	 * @param n - local particle index
	 */
	public void toggleModifier(int n) {
		for(int i = 0; i < particles.length; i++) {
			if(particles[i] != null) {
			//	particles[i].toggleModifier(n);
			}
		}
	}
	
	public float getX() {return x;}
	public float getY() {return y;}

	
	/**
	 * Draw currently active particles.
	 * @param g2 - Graphics2D to draw with
	 * @param delay - frame delay
	 */
	public void render(Graphics g, int delta) {
		Color oldColor;
		for(int i = 0; i < this.getParticleCount(); i++){
			Particle particle = particles[i];
			oldColor = g.getColor();
			
			g.setColor(Color.white);
			
			particle.update(delta);
			
			g.fillRect(particle.getX(), particle.getY(), particle.getSize(), particle.getSize());
			g.setColor(oldColor);
			}
//		}
	}
	
	
	/**
	 * Set all particles' sizes
	 * @param nSize - size in pixels
	 */
	public void setParticleSize(int nSize) {
		for(int i = 0; i < this.getParticleCount(); i++){
			Particle particle = particles[i];
			particle.setSize(nSize);
		}
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