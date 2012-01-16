package main.item;
import main.entity.Entity;

public abstract class Item implements Entity{
	public final int ID;
	public Item(int nID) {
		ID = nID;
	}
	
	public int getID() {return ID;}
	
	public abstract void destroy();

}
