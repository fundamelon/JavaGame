package main.particles;

import java.util.ArrayList;
import org.newdawn.slick.particles.*;

public class Container {
	private ArrayList<ParticleEmitter> emitters;
	
	public Container(ParticleEmitter[] emitters) {
		for(ParticleEmitter e : emitters) 
			this.emitters.add(e);
	}
	
	public Container(ArrayList<ParticleEmitter> emitters) {
		this.emitters.addAll(emitters);
	}
	
	public void addEmitter(ParticleEmitter e) {
		this.emitters.add(e);
	}
	
	public void addEmitter(ParticleEmitter e, int count) {
		for(int i = 0; i < count; i++) {
			addEmitter(e);
		}
	}
	
	public ParticleEmitter getEmitterByIndex(int i) {
		return emitters.get(i);
	}
	
	public ParticleEmitter[] getEmittersArray() {
		return (ParticleEmitter[])emitters.toArray();
	}
	
	public ArrayList<ParticleEmitter> getEmitters() {
		return emitters;
	}
	
	public void clearEmitterType(Class<? extends ParticleEmitter> type) {
		for(ParticleEmitter e : emitters) 
			if(e.getClass() == type)
				emitters.remove(e);
	}
}
