package main;

import java.io.File;

public abstract class Entity {
	public String currentImage = "lib/img/dummy_idle.png";
	
	public float moveSpeed;
	
	public abstract void jump();
	
	public abstract void init();
	
	public abstract void move(double nx, double ny);
	
	
	public abstract void damage();

	public abstract void setAnimation();
	
	
}
