package main.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public interface Entity {
	public void draw();

	public String getName();
	public String getType();
	
	public Rectangle getBounds();
	
	public Image getImg();
	public Image getShadowCasterImg();
	
	public void setImg();
	
	public float getX();
	public float getY();
	public float getAng();
	
	public void init();
	
	public void update();
	
	public float getImgOffsetX();
	public float getImgOffsetY();

	void init(int nx, int ny, boolean tilewise);
}
