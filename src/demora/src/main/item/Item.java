package main.item;

public abstract class Item {
	public final int ID;
	public Item(int nID) {
		ID = nID;
	}
	
	public int getID() {return ID;}
	
	public abstract void destroy();

}
