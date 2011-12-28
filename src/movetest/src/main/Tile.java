package main;

public class Tile {

	private int x, y, w, h, id;
	private String texture;

	public Tile(){}


	public int getX() {return x;}
	public int getY() {return y;}

		/*
	public void stop(){
		if(x1 == x && y1 == y){
			ControlManager.setplayerMovetAmt(0);
		}
	} */
	
	public String toString(){
		return getClass().getName() + " x: " + x + " y: " + y;
	}

	
}
