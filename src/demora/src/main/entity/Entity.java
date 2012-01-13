package main.entity;

public abstract class Entity {
	public String type = null, name = null;
	public String currentImage = "lib/img/girl_front_nobg.png";
	
	public int[] itemTable = new int[128];
	
	public float moveSpeed;
	
	public abstract void jump();
	
	public abstract void init();
	
	public abstract void move(double nx, double ny);
	
	
	public abstract void damage();

	public abstract void setAnimation();
	
	public String getType() {return type;}
	public String getName() {return name;}
	public int[] getItemTable() {return itemTable;}
	
	public void giveItem(int nID) {
		
	}
	
}
