package main;

public class GameLogic {
	public static boolean
		playerDead,
		gameWon
	;
	public static void reportPlayerDeath() {
	}
	
	static int damage;
	//s is the weapon variable, when changed it changes the weapon and damage the player inflicts
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

	public static void damage(boolean hit, int s){
		if(hit){
			if(s == 0){
				damage = 1;
				return damage;
				//player should have fists out
			}
			else if(s == 1){
				damage = 3;
				return damage;
				//player should have a sword
			}
			else{
				damage = 2;
				return damage;
				//player has an undecided weapon
			}
		}

	}









}
