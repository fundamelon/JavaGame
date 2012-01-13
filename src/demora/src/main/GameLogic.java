package main;

public class GameLogic {
	private static boolean
		playerDead,
		gameWon,
		inMainMenu = false,
		inGameView = true
	;
	public static void reportPlayerDeath() {
	}
	
	static int damage;
	//s is the weapon variable, when changed it changes the weapon and damage the player inflicts
	static boolean hit;
	static int s;
	static int hitdamage;
	
	public static void openMainMenu() {
		inMainMenu = true;
		inGameView = false;
	}
	
	public static void closeMainMenu() {
		inMainMenu = false;
		inGameView = true;
	}
	
	public static boolean inMainMenu() {
		return inMainMenu;
	}
	
	public static boolean inGameView() {
		return inGameView;
	}
	
	
	
	public static void hit(boolean hit){
		if(hit){
			hitdamage = damage;
		}
		else{
			hitdamage = 0;
		}
	}
	

	
	
	
	public static void sword(int s){
		if(s == 0){
			System.out.println("Player has no sword.");
		}
		else if(s == 1){
			System.out.println("Player has a sword");
		}
		else{
			System.out.println("Player has another weapon");
		}
	}

	public static int damage(){
		if(hit){
			if(s == 0){
				damage = 1;
				//player should have fists out
			}
			else if(s == 1){
				damage = 3;
				//player should have a sword
			}
			else{
				damage = 2;
				//player has an undecided weapon
			} 
		}
		return damage;

	}









}
