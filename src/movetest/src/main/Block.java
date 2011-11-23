package main;

public class Block {

	static double x;
	static double y;
	static double x1;
	static double y1;

	public Block(){
		x1 = 25;
		y1 = 25;
	}


	public static double getX() {return x;}
	public static double getY() {return y;}

		
	public void stop(){
		if(x1 == x && y1 == y){
			ControlManager.setplayerMovetAmt(0);
		}
	}
	
	public void location(){
		System.out.println(x + " " + y);  //should outprint location
	}

	
}
	
	


