package main.entity;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;


public abstract class Entity_mobile implements Entity{
	public float moveSpeed, x, y, z, ang;
	public String type = null, name = null;
	public String currentImage = "lib/img/girl_front_nobg.png";
	private Rectangle bounds;
	private Animation[] anims;
	
	public void setAnimation() {}
	
	public float imgX, imgY;
	
	public int[] itemTable = new int[128];
	
	public abstract void init();
	
	public abstract void move(double nx, double ny);

	public void damage() {}

	
	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	
	public int getImgX() {
		return (int)imgX;
	}
	
	public int getImgY() {
		return (int)imgY;
	}
	
	public Image getImg() {
		try {
			return new Image(currentImage);
		} catch (SlickException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setImg(String newPath) {
		currentImage = newPath;
	}

}
