package main.particles;
import main.ControlManager;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.*;

public class Emitter_Magic_AbsorbMed implements ParticleEmitter {
	/** The x coordinate of the center of the fire effect */
	private int x;
	/** The y coordinate of the center of the fire effect */
	private int y;
	
	/** The particle emission rate */
	private int interval = 20;
	/** Time til the next particle */
	private int timer;
	/** The size of the initial particles */
	private float size = 35;
	
	/** Timer controlling particle variation */
	private int variation_timer = 0;
	
	/**
	 * Create a default fire effect at 0,0
	 */
	public Emitter_Magic_AbsorbMed() {
	}

	/**
	 * Create a default fire effect at x,y
	 * 
	 * @param x The x coordinate of the fire effect
	 * @param y The y coordinate of the fire effect
	 */
	public Emitter_Magic_AbsorbMed(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Create a default fire effect at x,y
	 * 
	 * @param x The x coordinate of the fire effect
	 * @param y The y coordinate of the fire effect
	 * @param size The size of the particle being pumped out
	 */
	public Emitter_Magic_AbsorbMed(int x, int y, float size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#update(org.newdawn.slick.particles.ParticleSystem, int)
	 */
	public void update(ParticleSystem system, int delta) {
		timer -= delta;
		variation_timer += delta;
		if (timer <= 0 ){//&& variation_timer <= 200) {
			for(int i = 0; i < 2; i++) {
				timer = interval;
				Particle p = system.getNewParticle(this, 1000);
				p.setColor(1, 1, 1, (float)Math.abs(Math.cos(variation_timer * 0.0002 + Math.sin(variation_timer*0.001))) * 0.04f);
				

				float ang = (float) Math.random() * 360;
//				float vx = (float) (-0.5f + Math.random())*0.1f;
//				float vy = (float) (-0.5f + Math.random())*0.1f;
				
				float nx = (float)Math.cos(ang) * 100.5f;
				float ny = (float)Math.sin(ang) * 50.5f;
				
				p.setPosition(x + (float)(Math.random() * 8) + nx, y + ny);
				p.setSize(size * (float)(Math.random()*0.9f + 0.5f));
				
				p.setVelocity(-nx*0.001f, -ny*0.001f, 1.1f);
			}
		}
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#updateParticle(org.newdawn.slick.particles.Particle, int)
	 */
	public void updateParticle(Particle particle, int delta) {
		if (particle.getLife() > 600) {
			particle.adjustSize((float)(0.1f*Math.random()) * delta);
		} else {
			particle.adjustSize(-0.6f * delta * (size / 40.0f));
		}
		float c = 0.002f * delta;
		particle.adjustColor(-c/0.2f,-c/0.2f,-c/5f,c/14);
		
	//	particle.adjustVelocity(0,  c*-0.019f);
	}
	
	public void setPos(float nx, float ny) {
		x = (int)nx;
		y = (int)ny;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#completed()
	 */
	public boolean completed() {
		return false;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#useAdditive()
	 */
	public boolean useAdditive() {
		return false;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#usePoints(org.newdawn.slick.particles.ParticleSystem)
	 */
	public boolean usePoints(ParticleSystem system) {
		return false;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#isOriented()
	 */
	public boolean isOriented() {
		return false;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#wrapUp()
	 */
	public void wrapUp() {
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#resetState()
	 */
	public void resetState() {
	}

	@Override
	public float getBrightness() {
		// TODO Auto-generated method stub
		return 0;
	}
}
