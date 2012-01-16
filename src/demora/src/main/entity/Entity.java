package main.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public interface Entity {
	public void draw();

	public String getName();
	public String getType();
	
	public Image getImg();
	
	public void setImg();
	
	public float getX();
	public float getY();
	
	public void init();

	void init(int nx, int ny, boolean tilewise);
}
